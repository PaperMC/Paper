package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StopCommand extends VanillaCommand {
    public StopCommand() {
        super("stop");
        this.description = "Stops the server";
        this.usageMessage = "/stop";
        this.setPermission("bukkit.command.stop");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, "Stopping the server..");
        Bukkit.shutdown();

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("stop");
    }
}
