package io.papermc.paper.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class PaperCommands {

    private PaperCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put("paper", new PaperCommand("paper"));
        COMMANDS.put("callback", new CallbackCommand("callback"));
        COMMANDS.put("mspt", new MSPTCommand("mspt"));
    }

    public static void registerCommands(final MinecraftServer server) {
        COMMANDS.forEach((s, command) -> {
            server.server.getCommandMap().register(s, "Paper", command);
        });
        server.server.getCommandMap().register("bukkit", new PaperPluginsCommand());
    }
}
