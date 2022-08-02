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
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import static java.util.Objects.requireNonNull;

@DefaultQualifier(NonNull.class)
public class PaperCommands implements Commands, PaperRegistrar<LifecycleEventOwner> {

    public static final PaperCommands INSTANCE = new PaperCommands();

    private @Nullable LifecycleEventOwner currentContext;
    private @MonotonicNonNull CommandDispatcher<CommandSourceStack> dispatcher;
    private @MonotonicNonNull CommandBuildContext buildContext;
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
    public @Unmodifiable Set<String> registerWithFlags(@NotNull final PluginMeta pluginMeta, @NotNull final LiteralCommandNode<CommandSourceStack> node, @org.jetbrains.annotations.Nullable final String description, @NotNull final Collection<String> aliases, @NotNull final Set<CommandRegistrationFlag> flags) {
        final boolean hasFlattenRedirectFlag = flags.contains(CommandRegistrationFlag.FLATTEN_ALIASES);
        final String identifier = pluginMeta.getName().toLowerCase(Locale.ROOT);
        final String literal = node.getLiteral();
        final PluginCommandNode pluginLiteral = new PluginCommandNode(identifier + ":" + literal, pluginMeta, node, description);  // Treat the keyed version of the command as the root

        final Set<String> registeredLabels = new HashSet<>(aliases.size() * 2 + 2);

        if (this.registerIntoDispatcher(pluginLiteral, true)) {
            registeredLabels.add(pluginLiteral.getLiteral());
        }
        if (this.registerRedirect(literal, pluginMeta, pluginLiteral, description, true, hasFlattenRedirectFlag)) { // Plugin commands should override vanilla commands
            registeredLabels.add(literal);
        }

        // Add aliases
        final List<String> registeredAliases = new ArrayList<>(aliases.size() * 2);
        for (final String alias : aliases) {
            if (this.registerRedirect(alias, pluginMeta, pluginLiteral, description, false, hasFlattenRedirectFlag)) {
                registeredAliases.add(alias);
            }
            if (this.registerRedirect(identifier + ":" + alias, pluginMeta, pluginLiteral, description, false, hasFlattenRedirectFlag)) {
                registeredAliases.add(identifier + ":" + alias);
            }
        }

        if (!registeredAliases.isEmpty()) {
            pluginLiteral.setAliases(registeredAliases);
        }

        registeredLabels.addAll(registeredAliases);
        return registeredLabels.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(registeredLabels);
    }

    private boolean registerRedirect(final String aliasLiteral, final PluginMeta plugin, final PluginCommandNode redirectTo, final @Nullable String description, final boolean override, boolean hasFlattenRedirectFlag) {
        final LiteralCommandNode<CommandSourceStack> redirect;
        if (redirectTo.getChildren().isEmpty() || hasFlattenRedirectFlag) {
            redirect = Commands.literal(aliasLiteral)
                .executes(redirectTo.getCommand())
                .requires(redirectTo.getRequirement())
                .build();

            for (final CommandNode<CommandSourceStack> child : redirectTo.getChildren()) {
                redirect.addChild(child);
            }
        } else {
            redirect = Commands.literal(aliasLiteral)
                .executes(redirectTo.getCommand())
                .redirect(redirectTo)
                .requires(redirectTo.getRequirement())
                .build();
        }

        return this.registerIntoDispatcher(new PluginCommandNode(aliasLiteral, plugin, redirect, description), override);
    }

    private boolean registerIntoDispatcher(final PluginCommandNode node, boolean override) {
        final @Nullable CommandNode<CommandSourceStack> existingChild = this.getDispatcher().getRoot().getChild(node.getLiteral());
        if (existingChild != null && !(existingChild instanceof PluginCommandNode) && !(existingChild instanceof BukkitCommandNode)) {
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
