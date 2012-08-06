package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportCommand extends VanillaCommand {
    public TeleportCommand() {
        super("tp");
        this.description = "Teleports the given player to another player";
        this.usageMessage = "/tp [player] <target>\n/tp [player] <x> <y> <z>";
        this.setPermission("bukkit.command.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1 || args.length > 4) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Player player;

        if (args.length == 1 || args.length == 3) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("Please provide a player!");
                return true;
            }
        } else {
            player = Bukkit.getPlayerExact(args[0]);
        }

        if (player == null) {
            sender.sendMessage("Player not found: " + args[0]);
            return true;
        }

        if (args.length < 3) {
            Player target = Bukkit.getPlayerExact(args[args.length - 1]);
            if (target == null) {
                sender.sendMessage("Can't find user " + args[args.length - 1] + ". No tp.");
                return true;
            }
            player.teleport(target, TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, "Teleported " + player.getName() + " to " + target.getName());
        } else if (player.getWorld() != null) {
            int x = getInteger(sender, args[args.length - 3], -30000000, 30000000);
            int y = getInteger(sender, args[args.length - 2], 0, 256);
            int z = getInteger(sender, args[args.length - 1], -30000000, 30000000);

            Location location = new Location(player.getWorld(), x, y, z);
            player.teleport(location);
            Command.broadcastCommandMessage(sender, "Teleported " + player.getName() + " to " + + x + "," + y + "," + z);
        }
        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("tp");
    }
}
