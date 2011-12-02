package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExpCommand extends VanillaCommand {
    public ExpCommand() {
        super("xp");
        this.description = "Gives the specified player a certain amount of experience";
        this.usageMessage = "/xp <player> <amount>";
        this.setPermission("bukkit.command.xp");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            try {
                int exp = Integer.parseInt(args[1]);

                Command.broadcastCommandMessage(sender, "Giving " + exp + " exp to " +  player.getName());

                player.giveExp(exp);
            } catch (NumberFormatException ex) {
                sender.sendMessage("Invalid exp count: " + args[1]);
            }
        } else {
            sender.sendMessage("Can't find user " + args[0]);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("xp ") || input.equalsIgnoreCase("xp");
    }
}
