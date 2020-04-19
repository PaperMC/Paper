package org.bukkit.craftbukkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.server.CommandListenerWrapper;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.CraftServer;

public class BukkitCommandWrapper implements com.mojang.brigadier.Command<CommandListenerWrapper>, Predicate<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper>, com.destroystokyo.paper.brigadier.BukkitBrigadierCommand<CommandListenerWrapper> { // Paper

    private final CraftServer server;
    private final Command command;

    public BukkitCommandWrapper(CraftServer server, Command command) {
        this.server = server;
        this.command = command;
    }

    public LiteralCommandNode<CommandListenerWrapper> register(CommandDispatcher<CommandListenerWrapper> dispatcher, String label) {
        // Paper start - Expose Brigadier to Paper-MojangAPI
        com.mojang.brigadier.tree.RootCommandNode<CommandListenerWrapper> root = dispatcher.getRoot();
        LiteralCommandNode<CommandListenerWrapper> literal = LiteralArgumentBuilder.<CommandListenerWrapper>literal(label).requires(this).executes(this).build();
        com.mojang.brigadier.tree.ArgumentCommandNode<CommandListenerWrapper, String> defaultArgs = RequiredArgumentBuilder.<CommandListenerWrapper, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this).build();
        literal.addChild(defaultArgs);
        com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent<CommandListenerWrapper> event = new com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent<>(label, this, this.command, root, literal, defaultArgs);
        if (!event.callEvent()) {
            return null;
        }
        literal = event.getLiteral();
        root.addChild(literal);
        return literal;
        // Paper end
    }

    @Override
    public boolean test(CommandListenerWrapper wrapper) {
        return command.testPermissionSilent(wrapper.getBukkitSender());
    }

    @Override
    public int run(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
        return server.dispatchCommand(context.getSource().getBukkitSender(), context.getInput()) ? 1 : 0;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        List<String> results = server.tabComplete(context.getSource().getBukkitSender(), builder.getInput(), context.getSource().getWorld(), context.getSource().getPosition(), true);

        // Defaults to sub nodes, but we have just one giant args node, so offset accordingly
        builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

        for (String s : results) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }
}
