package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.common.collect.ImmutableList;

public class TeleportCommand extends VanillaCommand {
    public TeleportCommand() {
        super("tp");
        this.description = "Teleports the given player to another player or location";
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
            double x = getCoordinate(sender, player.getLocation().getX(), args[args.length - 3]);
            double y = getCoordinate(sender,player.getLocation().getY(), args[args.length - 2], 0, 0);
            double z = getCoordinate(sender, player.getLocation().getZ(), args[args.length - 1]);

            if (x == -30000001 || y == -30000001 || z == -30000001) {
                sender.sendMessage("Please provide a valid location!");
                return true;
            }

            Location location = new Location(player.getWorld(), x, y, z);
            player.teleport(location);
            Command.broadcastCommandMessage(sender, "Teleported " + player.getName() + " to " + + x + "," + y + "," + z);
        }
        return true;
    }

    private double getCoordinate(CommandSender sender, double current, String input) {
        return getCoordinate(sender, current, input, -30000000, 30000000);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max) {
        boolean relative = input.startsWith("~");
        double result = relative ? current : 0;

        if (!relative || input.length() > 1) {
            boolean exact = input.contains(".");
            if (relative) input = input.substring(1);

            double testResult = getDouble(sender, input);
            if (testResult == -30000001) {
                return -30000001;
            }
            result += testResult;

            if (!exact && !relative) result += 0.5f;
        }
        if (min != 0 || max != 0) {
            if (result < min) {
                result = -30000001;
            }

            if (result > max) {
                result = -30000001;
            }
        }

        return result;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("tp");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1 || args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
