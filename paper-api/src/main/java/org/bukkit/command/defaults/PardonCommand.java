package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PardonCommand extends VanillaCommand {
    public PardonCommand() {
        super("pardon");
        this.description = "Allows the specified player to use this server";
        this.usageMessage = "/pardon <player>";
        this.setPermission("bukkit.command.unban.player");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Bukkit.getOfflinePlayer(args[0]).setBanned(false);
        Command.broadcastCommandMessage(sender, "Pardoned " + args[0]);
        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("pardon");
    }
}
