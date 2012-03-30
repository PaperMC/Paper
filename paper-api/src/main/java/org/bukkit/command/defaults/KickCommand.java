package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends VanillaCommand {
    public KickCommand() {
        super("kick");
        this.description = "Removes the specified player from the server";
        this.usageMessage = "/kick <player>";
        this.setPermission("bukkit.command.kick");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            Command.broadcastCommandMessage(sender, "Kicking " + player.getName());
            player.kickPlayer("Kicked by admin");
        } else {
            sender.sendMessage("Can't find user " + args[0] + ". No kick.");
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("kick");
    }
}
