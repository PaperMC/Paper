package io.papermc.paper.command.brigadier;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.bukkit.BukkitCommandNode;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.commands.CommandBuildContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@NullMarked
public class PaperCommands implements Commands, PaperRegistrar<LifecycleEventOwner> {

    public static final PaperCommands INSTANCE = new PaperCommands();

    private @Nullable LifecycleEventOwner currentContext;
    private @Nullable CommandDispatcher<CommandSourceStack> dispatcher;
    private @Nullable CommandBuildContext buildContext;
    private boolean invalid = false;

    @Override
    public void setCurrentContext(final @Nullable LifecycleEventOwner context) {
        this.currentContext = context;
    }

    public void setDispatcher(final net.minecraft.commands.Commands commands, final CommandBuildContext commandBuildContext) {
        this.invalid = false;
        this.dispatcher = new CommandDispatcher<>(new ApiMirrorRootNode() {
            @Override
            public CommandDispatcher<net.minecraft.commands.CommandSourceStack> getDispatcher() {
                return commands.getDispatcher();
            }
        });
        this.buildContext = commandBuildContext;
    }

    public void setValid() {
        this.invalid = false;
    }

    @Override
    public void invalidate() {
        this.invalid = true;
    }

    // use this method internally as it bypasses the valid check
    public CommandDispatcher<CommandSourceStack> getDispatcherInternal() {
        Preconditions.checkState(this.dispatcher != null, "the dispatcher hasn't been set yet");
        return this.dispatcher;
    }

    public CommandBuildContext getBuildContext() {
        Preconditions.checkState(this.buildContext != null, "the build context hasn't been set yet");
        return this.buildContext;
    }

    @Override
    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        Preconditions.checkState(!this.invalid && this.dispatcher != null, "cannot access the dispatcher in this context");
        return this.dispatcher;
    }

    @Override
    public @Unmodifiable Set<String> register(final LiteralCommandNode<CommandSourceStack> node, final @Nullable String description, final Collection<String> aliases) {
        return this.register(requireNonNull(this.currentContext, "No lifecycle owner context is set").getPluginMeta(), node, description, aliases);
    }

    @Override
    public @Unmodifiable Set<String> register(final PluginMeta pluginMeta, final LiteralCommandNode<CommandSourceStack> node, final @Nullable String description, final Collection<String> aliases) {
        return this.registerWithFlags(pluginMeta, node, description, aliases, Set.of());
    }

    @Override
    public @Unmodifiable Set<String> registerWithFlags(final PluginMeta pluginMeta, final LiteralCommandNode<CommandSourceStack> node, final @Nullable String description, final Collection<String> aliases, final Set<CommandRegistrationFlag> flags) {
        return this.registerWithFlagsInternal(pluginMeta, pluginMeta.namespace(), null, node, description, aliases, flags);
    }

    public @Unmodifiable Set<String> registerWithFlagsInternal(final @Nullable PluginMeta pluginMeta, final String namespace, final @Nullable String helpNamespaceOverride, final LiteralCommandNode<CommandSourceStack> node, final @Nullable String description, final Collection<String> aliases, final Set<CommandRegistrationFlag> flags) {
        final APICommandMeta meta = new APICommandMeta(pluginMeta, description, List.of(), helpNamespaceOverride);
        final String literal = node.getLiteral();
        final LiteralCommandNode<CommandSourceStack> pluginLiteral = PaperBrigadier.copyLiteral(namespace + ":" + literal, node);

        final Set<String> registeredLabels = new HashSet<>(aliases.size() * 2 + 2);

        if (this.registerIntoDispatcher(pluginLiteral, true)) {
            registeredLabels.add(pluginLiteral.getLiteral());
        }
        if (this.registerIntoDispatcher(node, true)) { // Plugin commands should override vanilla commands
            registeredLabels.add(literal);
        }

        // Add aliases
        final List<String> registeredAliases = new ArrayList<>(aliases.size() * 2);
        for (final String alias : aliases) {
            if (this.registerCopy(alias, pluginLiteral, meta)) {
                registeredAliases.add(alias);
            }
            if (this.registerCopy(namespace + ":" + alias, pluginLiteral, meta)) {
                registeredAliases.add(namespace + ":" + alias);
            }
        }

        pluginLiteral.apiCommandMeta = meta.withAliases(registeredAliases);
        node.apiCommandMeta = pluginLiteral.apiCommandMeta;

        registeredLabels.addAll(registeredAliases);
        return registeredLabels.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(registeredLabels);
    }

    private boolean registerCopy(final String aliasLiteral, final LiteralCommandNode<CommandSourceStack> redirectTo, final APICommandMeta meta) {
        final LiteralCommandNode<CommandSourceStack> node = PaperBrigadier.copyLiteral(aliasLiteral, redirectTo);
        node.apiCommandMeta = meta;
        return this.registerIntoDispatcher(node, false);
    }

    private boolean registerIntoDispatcher(final LiteralCommandNode<CommandSourceStack> node, boolean override) {
        final CommandNode<CommandSourceStack> existingChild = this.getDispatcher().getRoot().getChild(node.getLiteral());
        if (existingChild != null && existingChild.apiCommandMeta == null && !(existingChild instanceof BukkitCommandNode)) {
            override = true; // override vanilla commands
        }
        if (existingChild == null || override) { // Avoid merging behavior. Maybe something to look into in the future
            if (override) {
                this.getDispatcher().getRoot().removeCommand(node.getLiteral());
            }
            this.getDispatcher().getRoot().addChild(node);
            return true;
        }

        return false;
    }

    @Override
    public @Unmodifiable Set<String> register(final String label, final @Nullable String description, final Collection<String> aliases, final BasicCommand basicCommand) {
        return this.register(requireNonNull(this.currentContext, "No lifecycle owner context is set").getPluginMeta(), label, description, aliases, basicCommand);
    }

    @Override
    public @Unmodifiable Set<String> register(final PluginMeta pluginMeta, final String label, final @Nullable String description, final Collection<String> aliases, final BasicCommand basicCommand) {
        final LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(label)
            .requires(stack -> basicCommand.canUse(stack.getSender()))
            .then(
                Commands.argument("args", StringArgumentType.greedyString())
                    .suggests((context, suggestionsBuilder) -> {
                        String[] args = StringUtils.split(suggestionsBuilder.getRemaining());
                        if (suggestionsBuilder.getRemaining().endsWith(" ")) {
                            // if there is trailing whitespace, we should add an empty argument to signify
                            // that there may be more, but no characters have been typed yet
                            args = ArrayUtils.add(args, "");
                        }
                        final SuggestionsBuilder offsetSuggestionsBuilder = suggestionsBuilder.createOffset(suggestionsBuilder.getInput().lastIndexOf(' ') + 1);

                        final Collection<String> suggestions = basicCommand.suggest(context.getSource(), args);
                        suggestions.forEach(offsetSuggestionsBuilder::suggest);
                        return offsetSuggestionsBuilder.buildFuture();
                    })
                    .executes((stack) -> {
                        basicCommand.execute(stack.getSource(), StringUtils.split(stack.getArgument("args", String.class), ' '));
                        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                    })
            )
            .executes((stack) -> {
                basicCommand.execute(stack.getSource(), new String[0]);
                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
            });

        return this.register(pluginMeta, builder.build(), description, aliases);
    }
}
