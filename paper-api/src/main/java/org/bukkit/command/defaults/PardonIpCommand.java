package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PardonIpCommand extends VanillaCommand {
    public PardonIpCommand() {
        super("pardon-ip");
        this.description = "Allows the specified IP address to use this server";
        this.usageMessage = "/pardon-ip <address>";
        this.setPermission("bukkit.command.unban.ip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (BanIpCommand.ipValidity.matcher(args[0]).matches()) {
            Bukkit.unbanIP(args[0]);
            Command.broadcastCommandMessage(sender, "Pardoned ip " + args[0]);
        } else {
            sender.sendMessage("Invalid ip");
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("pardon-ip");
    }
}
