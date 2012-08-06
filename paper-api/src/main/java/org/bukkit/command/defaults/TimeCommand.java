package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TimeCommand extends VanillaCommand {
    public TimeCommand() {
        super("time");
        this.description = "Changes the time on each world";
        this.usageMessage = "/time set <value>\n/time add <value>";
        this.setPermission("bukkit.command.time.add;bukkit.command.time.set");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. Correct usage:\n" + usageMessage);
            return false;
        }

        int value;

        if (args[0].equals("set")) {
            if (!sender.hasPermission("bukkit.command.time.set")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to set the time");
                return true;
            }

            if (args[1].equals("day")) {
                value = 0;
            } else if (args[1].equals("night")) {
                value = 12500;
            } else {
                value = getInteger(sender, args[1], 0);
            }

            for (World world : Bukkit.getWorlds()) {
                world.setTime(value);
            }

            Command.broadcastCommandMessage(sender, "Set time to " + value);
        } else if (args[0].equals("add")) {
            if (!sender.hasPermission("bukkit.command.time.add")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to set the time");
                return true;
            }

            value = getInteger(sender, args[1], 0);

            for (World world : Bukkit.getWorlds()) {
                world.setFullTime(world.getFullTime() + value);
            }

            Command.broadcastCommandMessage(sender, "Added " + value + " to time");
        } else {
            sender.sendMessage("Unknown method. Usage: " + usageMessage);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("time");
    }
}
