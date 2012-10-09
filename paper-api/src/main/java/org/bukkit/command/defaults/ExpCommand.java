package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

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

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
