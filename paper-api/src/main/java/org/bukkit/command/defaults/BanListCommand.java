package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class BanListCommand extends VanillaCommand {
    public BanListCommand() {
        super("banlist");
        this.description = "View all players banned from this server";
        this.usageMessage = "/banlist";
        this.setPermission("bukkit.command.ban.list");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        StringBuilder message = new StringBuilder().append(ChatColor.GRAY).append("Ban list: ");

        int count = 0;
        for (OfflinePlayer p : Bukkit.getServer().getBannedPlayers()) {
            if (count++ > 0) {
                message.append(", ");
            }
            message.append(p.getName());
        }
        sender.sendMessage(message.toString());
        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("banlist");
    }
}
