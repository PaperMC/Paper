package io.papermc.paper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandRegistrationFlag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

@DefaultQualifier(NonNull.class)
public final class PaperCommands {

    private PaperCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public static void registerCommands(final MinecraftServer server) {
        COMMANDS.put("paper", new PaperCommand("paper"));
        COMMANDS.put("callback", new CallbackCommand("callback"));
        COMMANDS.put("mspt", new MSPTCommand("mspt"));

        COMMANDS.forEach((s, command) -> {
            server.server.getCommandMap().register(s, "Paper", command);
        });
        server.server.getCommandMap().register("bukkit", new PaperPluginsCommand());
    }

    public static void registerCommands() {
        registerInternalCommand(
            LiteralArgumentBuilder.<CommandSourceStack>literal("cool-paper").executes(commandContext -> {
                commandContext.getSource().getSender().sendMessage("Hi");
                return 1;
            }).build(),
            "Paper command to say paper is cool",
            List.of(),
            Set.of()
        );
        registerInternalCommand(
            LiteralArgumentBuilder.<CommandSourceStack>literal("paper-callback-2.0").executes(commandContext -> {
                commandContext.getSource().getSender().sendMessage("Hi");
                return 1;
            }).build(),
            "Server sided only command, like for callback command",
            List.of(),
            Set.of(CommandRegistrationFlag.SERVER_ONLY)
        );
    }

    private static void registerInternalCommand(final LiteralCommandNode<CommandSourceStack> node, final String description, final List<String> aliases, final Set<CommandRegistrationFlag> flags) {
        io.papermc.paper.command.brigadier.PaperCommands.INSTANCE.registerWithFlagInternal(
            null,
            "paper",
            "Paper",
            node,
            description,
            aliases,
            flags
        );
    }
}
