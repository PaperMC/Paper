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
        this.usageMessage = "/xp <amount> [player]";
        this.setPermission("bukkit.command.xp");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        int exp = getInteger(sender, args[0], 0, 5000);
        Player player = null;

        if (args.length > 1) {
            player = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null) {
            player.giveExp(exp);
            Command.broadcastCommandMessage(sender, "Given " + exp + " exp to " + player.getName());
        } else {
            sender.sendMessage("Can't find user, was one provided?\n" + ChatColor.RED + "Usage: " + usageMessage);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("xp");
    }
}
