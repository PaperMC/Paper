package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnpointCommand extends VanillaCommand {

    public SpawnpointCommand() {
        super("spawnpoint");
        this.description = "Sets a player's spawn point";
        this.usageMessage = "/spawnpoint OR /spawnpoint <player> OR /spawnpoint <player> <x> <y> <z>";
        this.setPermission("bukkit.command.spawnpoint");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Player player;

        if (args.length == 0) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("Please provide a player!");
                return true;
            }
        } else {
            player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage("Can't find user " + args[0]);
                return true;
            }
        }

        World world = player.getWorld();

        if (args.length == 4) {
            if (world != null) {
                int pos = 1;
                int maxPos = 30000000;
                int x = getInteger(sender, args[pos++], -maxPos, maxPos);
                int y = getInteger(sender, args[pos++], 0, world.getMaxHeight());
                int z = getInteger(sender, args[pos], -maxPos, maxPos);

                player.setBedSpawnLocation(new Location(world, x, y, z), true);
                sender.sendMessage("Set " + player.getDisplayName() + "'s spawnpoint to " + x + ", " + y + ", " + z);
            }
        } else if (args.length <= 1) {
            Location location = player.getLocation();
            player.setBedSpawnLocation(location, true);
            sender.sendMessage("Set " + player.getDisplayName() + "'s spawnpoint to " + location.getX() + ", " + location.getY() + ", " + location.getZ());
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("spawnpoint");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }

        return ImmutableList.of();
    }
}
