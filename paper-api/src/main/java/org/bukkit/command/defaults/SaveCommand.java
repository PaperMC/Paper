package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveCommand extends VanillaCommand {
    public SaveCommand() {
        super("save-all");
        this.description = "Saves the server to disk";
        this.usageMessage = "/save-all";
        this.setPermission("bukkit.command.save.perform");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, "Forcing save..");

        Bukkit.savePlayers();

        for (World world : Bukkit.getWorlds()) {
            world.save();
        }

        Command.broadcastCommandMessage(sender, "Save complete.");

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("save-all");
    }
}
