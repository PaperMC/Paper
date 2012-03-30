package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WhitelistCommand extends VanillaCommand {
    public WhitelistCommand() {
        super("whitelist");
        this.description = "Prevents the specified player from using this server";
        this.usageMessage = "/whitelist (add|remove) <player>\n/whitelist (on|off|list|reload)";
        this.setPermission("bukkit.command.whitelist.reload;bukkit.command.whitelist.enable;bukkit.command.whitelist.disable;bukkit.command.whitelist.list;bukkit.command.whitelist.add;bukkit.command.whitelist.remove");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (badPerm(sender, "reload")) return true;

                Bukkit.reloadWhitelist();
                Command.broadcastCommandMessage(sender, "Reloaded white-list from file");
                return true;
            } else if (args[0].equalsIgnoreCase("on")) {
                if (badPerm(sender, "enable")) return true;

                Bukkit.setWhitelist(true);
                Command.broadcastCommandMessage(sender, "Turned on white-listing");
                return true;
            } else if (args[0].equalsIgnoreCase("off")) {
                if (badPerm(sender, "disable")) return true;

                Bukkit.setWhitelist(false);
                Command.broadcastCommandMessage(sender, "Turned off white-listing");
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                if (badPerm(sender, "list")) return true;

                StringBuilder result = new StringBuilder();

                for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
                    if (result.length() > 0) {
                        result.append(", ");
                    }

                    result.append(player.getName());
                }

                sender.sendMessage("White-listed players: " + result.toString());
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (badPerm(sender, "add")) return true;

                Bukkit.getOfflinePlayer(args[1]).setWhitelisted(true);

                Command.broadcastCommandMessage(sender, "Added " + args[1] + " to white-list");
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (badPerm(sender, "remove")) return true;

                Bukkit.getOfflinePlayer(args[1]).setWhitelisted(false);

                Command.broadcastCommandMessage(sender, "Removed " + args[1] + " from white-list");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Correct command usage:\n" + usageMessage);
        return false;
    }

    private boolean badPerm(CommandSender sender, String perm) {
        if (!sender.hasPermission("bukkit.command.whitelist." + perm)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
            return true;
        }

        return false;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("whitelist");
    }
}
