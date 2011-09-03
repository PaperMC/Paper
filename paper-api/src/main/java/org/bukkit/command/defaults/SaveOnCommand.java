package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveOnCommand extends VanillaCommand {
    public SaveOnCommand() {
        super("save-on");
        this.description = "Enables server autosaving";
        this.usageMessage = "/save-on";
        this.setPermission("bukkit.command.save.enable");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, "Enabling level saving..");

        for (World world : Bukkit.getWorlds()) {
            world.setAutoSave(true);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("save-on");
    }
}
