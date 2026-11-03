package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

import java.util.function.Predicate;
import java.util.function.Supplier;

/*
Basically this converts the argument to a different type when parsing.
 */
public class WrappedArgumentCommandNode<NMS, API> extends ArgumentCommandNode<CommandSourceStack, NMS> {

    // Signature checks must parse the same argument types the client received.
    private static final ThreadLocal<Boolean> PARSE_NATIVE_ARGUMENTS = ThreadLocal.withInitial(() -> false);
    private final ArgumentType<API> pureArgumentType;

    public static <T> T parseNativeArguments(final Supplier<T> action) {
        boolean previous = PARSE_NATIVE_ARGUMENTS.get();
        PARSE_NATIVE_ARGUMENTS.set(true);
        try {
            return action.get();
        } finally {
            if (previous) {
                PARSE_NATIVE_ARGUMENTS.set(true);
            } else {
                PARSE_NATIVE_ARGUMENTS.remove();
            }
        }
    }

    public WrappedArgumentCommandNode(
        final String name,
        final ArgumentType<API> pureArgumentType,
        final ArgumentType<NMS> nmsNativeType,
        final Command<CommandSourceStack> command,
        final Predicate<CommandSourceStack> requirement,
        final CommandNode<CommandSourceStack> redirect,
        final RedirectModifier<CommandSourceStack> modifier,
        final boolean forks,
        final SuggestionProvider<CommandSourceStack> customSuggestions
    ) {
        super(name, nmsNativeType, command, requirement, redirect, modifier, forks, customSuggestions);
        if (!ArgumentTypeInfos.isClassRecognized(nmsNativeType.getClass())) {
            // Is this argument an NMS argument?
            throw new IllegalArgumentException("Unexpected argument type was passed: " + nmsNativeType.getClass() + ". This should be an NMS type!");
        }

        this.pureArgumentType = pureArgumentType;
    }

    // See ArgumentCommandNode#parse
    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<CommandSourceStack> contextBuilder) throws CommandSyntaxException {
        if (PARSE_NATIVE_ARGUMENTS.get()) {
            super.parse(reader, contextBuilder);
            return;
        }

        final int start = reader.getCursor();
        final API result = this.pureArgumentType.parse(reader, contextBuilder.getSource()); // Use the api argument parser
        final ParsedArgument<CommandSourceStack, API> parsed = new ParsedArgument<>(start, reader.getCursor(), result); // Return an API parsed argument instead.

        contextBuilder.withArgument(this.getName(), parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }
}
