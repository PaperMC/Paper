package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BanIpCommand extends VanillaCommand {
    public BanIpCommand() {
        super("ban-ip");
        this.description = "Prevents the specified IP address from using this server";
        this.usageMessage = "/ban-ip <address>";
        this.setPermission("bukkit.command.ban.ip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Bukkit.banIP(args[0]);
        Command.broadcastCommandMessage(sender, "Banning ip " + args[0]);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("ban-ip ") || input.equalsIgnoreCase("ban-ip");
    }
}
