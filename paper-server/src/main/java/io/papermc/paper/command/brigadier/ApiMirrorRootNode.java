package io.papermc.paper.command.brigadier;

import com.google.common.collect.Collections2;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.VanillaArgumentProviderImpl;
import io.papermc.paper.command.brigadier.argument.WrappedArgumentCommandNode;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This root command node is responsible for wrapping around vanilla's dispatcher.
 * <p>
 * The reason for this is conversion is we do NOT want there to be NMS types
 * in the api. This allows us to reconstruct the nodes to be more api friendly, while
 * we can then unwrap it when needed and convert them to NMS types.
 * <p>
 * Command nodes such as vanilla (those without a proper "api node")
 * will be assigned a {@link ShadowBrigNode}.
 * This prevents certain parts of it (children) from being accessed by the api.
 */
public abstract class ApiMirrorRootNode extends RootCommandNode<CommandSourceStack> {

    /**
     * Represents argument types that are allowed to exist in the api.
     * These typically represent primitives that don't need to be wrapped
     * by NMS.
     */
    private static final Set<Class<? extends ArgumentType<?>>> ARGUMENT_WHITELIST = Set.of(
        BoolArgumentType.class,
        DoubleArgumentType.class,
        FloatArgumentType.class,
        IntegerArgumentType.class,
        LongArgumentType.class,
        StringArgumentType.class
    );

    public static void validatePrimitiveType(ArgumentType<?> type) {
        if (ARGUMENT_WHITELIST.contains(type.getClass())) {
            if (!ArgumentTypeInfos.isClassRecognized(type.getClass())) {
                throw new IllegalArgumentException("This whitelisted primitive argument type is not recognized by the server!");
            }
        } else if (!(type instanceof VanillaArgumentProviderImpl.NativeWrapperArgumentType<?,?> nativeWrapperArgumentType) || !ArgumentTypeInfos.isClassRecognized(nativeWrapperArgumentType.nativeNmsArgumentType().getClass())) {
            throw new IllegalArgumentException("Custom argument type was passed, this was not a recognized type to send to the client! You must only pass vanilla arguments or primitive brig args in the wrapper!");
        }
    }

    public abstract CommandDispatcher<net.minecraft.commands.CommandSourceStack> getDispatcher();

    /**
     * This logic is responsible for unwrapping an API node to be supported by NMS.
     * See the method implementation for detailed steps.
     *
     * @param maybeWrappedNode api provided node / node to be "wrapped"
     * @return wrapped node
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private @NotNull CommandNode<CommandSourceStack> unwrapNode(final CommandNode<CommandSourceStack> maybeWrappedNode) {
        /*
        If the type is a shadow node we can assume that the type that it represents is an already supported NMS node.
        This is because these are typically minecraft command nodes.
         */
        if (maybeWrappedNode instanceof final ShadowBrigNode shadowBrigNode) {
            return (CommandNode) shadowBrigNode.getHandle();
        }

        /*
        This node already has had an unwrapped node created, so we can assume that it's safe to reuse that cached copy.
         */
        if (maybeWrappedNode.unwrappedCached != null) {
            return maybeWrappedNode.unwrappedCached;
        }

        // convert the pure brig node into one compatible with the nms dispatcher
        return this.convertFromPureBrigNode(maybeWrappedNode);
    }

    private @NotNull CommandNode<CommandSourceStack> convertFromPureBrigNode(final CommandNode<CommandSourceStack> pureNode) {
        /*
        Logic for converting a node.
         */
        final CommandNode<CommandSourceStack> converted;
        if (pureNode instanceof final LiteralCommandNode<CommandSourceStack> node) {
            /*
            Remap the literal node, we only have to account
            for the redirect in this case.
             */
            converted = this.simpleUnwrap(node);
        } else if (pureNode instanceof final ArgumentCommandNode<CommandSourceStack, ?> pureArgumentNode) {
            final ArgumentType<?> pureArgumentType = pureArgumentNode.getType();
            /*
            Check to see if this argument type is a wrapped type, if so we know that
            we can unwrap the node to get an NMS type.
             */
            if (pureArgumentType instanceof final CustomArgumentType<?, ?> customArgumentType) {
                final SuggestionProvider<?> suggestionProvider;
                try {
                    final Method listSuggestions = customArgumentType.getClass().getMethod("listSuggestions", CommandContext.class, SuggestionsBuilder.class);
                    if (listSuggestions.getDeclaringClass() != CustomArgumentType.class) {
                        suggestionProvider = customArgumentType::listSuggestions;
                    } else {
                        suggestionProvider = null;
                    }
                } catch (final NoSuchMethodException ex) {
                    throw new IllegalStateException("Could not determine if the custom argument type " + customArgumentType + " overrides listSuggestions", ex);
                }

                converted = this.unwrapArgumentWrapper(pureArgumentNode, customArgumentType, customArgumentType.getNativeType(), suggestionProvider);
            } else if (pureArgumentType instanceof final VanillaArgumentProviderImpl.NativeWrapperArgumentType<?, ?> nativeWrapperArgumentType) {
                converted = this.unwrapArgumentWrapper(pureArgumentNode, nativeWrapperArgumentType, nativeWrapperArgumentType, null); // "null" for suggestion provider so it uses the argument type's suggestion provider

            /*
            If it's not a wrapped type, it either has to be a primitive or an already
            defined NMS type.
            This method allows us to check if this is recognized by vanilla.
             */
            } else if (ArgumentTypeInfos.isClassRecognized(pureArgumentType.getClass())) {
                // Allow any type of argument, as long as it's recognized by the client (but in most cases, this should be API only types)
                // Previously we only allowed whitelisted types.
                converted = this.simpleUnwrap(pureArgumentNode);
            } else {
                // Unknown argument type was passed
                throw new IllegalArgumentException("Custom unknown argument type was passed, should be wrapped inside an CustomArgumentType.");
            }
        } else if (pureNode == this) {
            return (CommandNode) this.getDispatcher().getRoot();
        } else {
            throw new IllegalArgumentException("Unknown command node passed. Don't know how to unwrap this.");
        }

        // Store unwrapped node before unwrapping children to avoid infinite recursion in cyclic redirects.
        converted.wrappedCached = pureNode;
        pureNode.unwrappedCached = converted;

        /*
        Add the children to the node, unwrapping each child in the process.
         */
        for (final CommandNode<CommandSourceStack> child : pureNode.getChildren()) {
            converted.addChild(this.unwrapNode(child));
        }

        return converted;
    }

    /**
     * This logic is responsible for rewrapping a node.
     * If a node was unwrapped in the past, it should have a wrapped type
     * stored in its cache.
     * <p>
     * However, if it doesn't seem to have a wrapped version we will return
     * a {@link ShadowBrigNode} instead. This supports being unwrapped/wrapped while
     * preventing the API from accessing it unsafely.
     *
     * @param unwrapped argument node
     * @return wrapped node
     */
    private @Nullable CommandNode<CommandSourceStack> wrapNode(@Nullable final CommandNode<net.minecraft.commands.CommandSourceStack> unwrapped) {
        if (unwrapped == null) {
            return null;
        }

        /*
        This was most likely created by API and has a wrapped variant,
        so we can return this safely.
         */
        if (unwrapped.wrappedCached != null) {
            return unwrapped.wrappedCached;
        }

        /*
        We don't know the type of this, or where this came from.
        Return a shadow, where we will allow the api to handle this but have
        restrictive access.
         */
        CommandNode<CommandSourceStack> shadow = new ShadowBrigNode(unwrapped);
        unwrapped.wrappedCached = shadow;
        return shadow;
    }

    /**
     * Nodes added to this dispatcher must be unwrapped
     * in order to be added to the NMS dispatcher.
     *
     * @param node node to add
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addChild(CommandNode<CommandSourceStack> node) {
        CommandNode convertedNode = this.unwrapNode(node);
        this.getDispatcher().getRoot().addChild(convertedNode);
    }

    /**
     * Gets the children for the vanilla dispatcher,
     * ensuring that all are wrapped.
     *
     * @return wrapped children
     */
    @Override
    public Collection<CommandNode<CommandSourceStack>> getChildren() {
        return Collections2.transform(this.getDispatcher().getRoot().getChildren(), this::wrapNode);
    }

    @Override
    public CommandNode<CommandSourceStack> getChild(String name) {
        return this.wrapNode(this.getDispatcher().getRoot().getChild(name));
    }

    // These are needed for bukkit... we should NOT allow this
    @Override
    public void removeCommand(String name) {
        this.getDispatcher().getRoot().removeCommand(name);
    }

    @Override
    public void clearAll() {
        this.getDispatcher().getRoot().clearAll();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private CommandNode<CommandSourceStack> unwrapArgumentWrapper(final ArgumentCommandNode pureNode, final ArgumentType pureArgumentType, final ArgumentType possiblyWrappedNativeArgumentType, @Nullable SuggestionProvider argumentTypeSuggestionProvider) {
        validatePrimitiveType(possiblyWrappedNativeArgumentType);
        final CommandNode redirectNode = pureNode.getRedirect() == null ? null : this.unwrapNode(pureNode.getRedirect());
        // If there is already a custom suggestion provider, ignore the suggestion provider from the argument type
        final SuggestionProvider suggestionProvider = pureNode.getCustomSuggestions() != null ? pureNode.getCustomSuggestions() : argumentTypeSuggestionProvider;

        final ArgumentType nativeArgumentType = possiblyWrappedNativeArgumentType instanceof final VanillaArgumentProviderImpl.NativeWrapperArgumentType<?,?> nativeWrapperArgumentType ? nativeWrapperArgumentType.nativeNmsArgumentType() : possiblyWrappedNativeArgumentType;
        return new WrappedArgumentCommandNode<>(pureNode.getName(), pureArgumentType, nativeArgumentType, pureNode.getCommand(), pureNode.getRequirement(), redirectNode, pureNode.getRedirectModifier(), pureNode.isFork(), suggestionProvider);
    }

    private CommandNode<CommandSourceStack> simpleUnwrap(final CommandNode<CommandSourceStack> node) {
        return node.createBuilder()
            .forward(node.getRedirect() == null ? null : this.unwrapNode(node.getRedirect()), node.getRedirectModifier(), node.isFork())
            .build();
    }

}
