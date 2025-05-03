package io.papermc.paper.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandRegistrationFlag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

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
        // registerInternalCommand(
        //     LiteralArgumentBuilder.<CommandSourceStack>literal("cool-paper").executes(commandContext -> {
        //         commandContext.getSource().getSender().sendMessage("Hi");
        //         return 1;
        //     }).build(),
        //     "Paper command to say paper is cool",
        //     List.of(),
        //     Set.of()
        // );
        // registerInternalCommand(
        //     LiteralArgumentBuilder.<CommandSourceStack>literal("paper-callback-2.0").executes(commandContext -> {
        //         commandContext.getSource().getSender().sendMessage("Hi");
        //         return 1;
        //     }).build(),
        //     "Server sided only command, like for callback command",
        //     List.of(),
        //     Set.of(CommandRegistrationFlag.SERVER_ONLY)
        // );
    }

    private static void registerInternalCommand(final LiteralCommandNode<CommandSourceStack> node, final String description, final List<String> aliases, final Set<CommandRegistrationFlag> flags) {
        io.papermc.paper.command.brigadier.PaperCommands.INSTANCE.registerWithFlagsInternal(
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
