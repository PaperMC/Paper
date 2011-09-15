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
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length != 2)  {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. Correct usage:\n" + usageMessage);
            return false;
        }

        int value = 0;

        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Unable to convert time value, " + args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (!sender.hasPermission("bukkit.command.time.add")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to add to the time");
            } else {
                for (World world : Bukkit.getWorlds()) {
                    world.setFullTime(world.getFullTime() + value);
                }

                Command.broadcastCommandMessage(sender, "Added " + value + " to time");
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("bukkit.command.time.set")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to set the time");
            } else {
                for (World world : Bukkit.getWorlds()) {
                    world.setTime(value);
                }

                Command.broadcastCommandMessage(sender, "Set time to " + value);
            }
        } else {
            sender.sendMessage("Unknown method, use either \"add\" or \"set\"");
            return true;
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("time ") || input.equalsIgnoreCase("time");
    }
}
