package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveOffCommand extends VanillaCommand {
    public SaveOffCommand() {
        super("save-off");
        this.description = "Disables server autosaving";
        this.usageMessage = "/save-off";
        this.setPermission("bukkit.command.save.disable");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        for (World world : Bukkit.getWorlds()) {
            world.setAutoSave(false);
        }

        Command.broadcastCommandMessage(sender, "Disabled level saving..");
        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("save-off");
    }
}
