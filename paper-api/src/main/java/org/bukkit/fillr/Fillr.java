package org.bukkit.fillr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.*;

public class Fillr extends JavaPlugin {
    public static final String NAME = "Fillr";
    public static final String VERSION = "1.0";
    public static final String DIRECTORY = "plugins";

    public void onDisable() {
    }

    public void onEnable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("check")) {
            new Checker().check(sender);
            return true;
        } else if (commandLabel.equalsIgnoreCase("updateAll")) {
            new Updater(getServer()).updateAll(sender);
            return true;
        } else if (commandLabel.equalsIgnoreCase("update")) {
            if (args.length == 0) {
                sender.sendMessage("Usage is /update <name>");
            } else {
                new Updater(getServer()).update(args[0], sender);
            }

            return true;
        } else if (commandLabel.equalsIgnoreCase("get")) {
            if (args.length == 0) {
                sender.sendMessage("Usage is /get <name>");
            } else {
                try {
                    new Getter(getServer()).get(args[0], sender);
                } catch (Exception e) {
                    sender.sendMessage("There was an error downloading " + args[0]);
                }
            }

            return true;
        }

        return false;
    }
}
