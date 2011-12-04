package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportCommand extends VanillaCommand {
    public TeleportCommand() {
        super("tp");
        this.description = "Teleports the given player to another player";
        this.usageMessage = "/tp <player> <target>";
        this.setPermission("bukkit.command.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 2)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Player victim = Bukkit.getPlayerExact(args[0]);
        Player target = Bukkit.getPlayerExact(args[1]);

        if (victim == null) {
            sender.sendMessage("Can't find user " + args[0] + ". No tp.");
        } else if (target == null) {
            sender.sendMessage("Can't find user " + args[1] + ". No tp.");
        } else {
            Command.broadcastCommandMessage(sender, "Teleporting " + victim.getName() + " to " + target.getName());
            victim.teleport(target, TeleportCause.COMMAND);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("tp ") || input.equalsIgnoreCase("tp");
    }
}
