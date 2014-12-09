package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class PlaySoundCommand extends VanillaCommand {
    public PlaySoundCommand() {
        super("playsound");
        this.description = "Plays a sound to a given player";
        this.usageMessage = "/playsound <sound> <player> [x] [y] [z] [volume] [pitch] [minimumVolume]";
        this.setPermission("bukkit.command.playsound");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        final String soundArg = args[0];
        final String playerArg = args[1];

        final Player player = Bukkit.getPlayerExact(playerArg);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Can't find player " + playerArg);
            return false;
        }

        final Location location = player.getLocation();

        double x = Math.floor(location.getX());
        double y = Math.floor(location.getY() + 0.5D);
        double z = Math.floor(location.getZ());
        double volume = 1.0D;
        double pitch = 1.0D;
        double minimumVolume = 0.0D;

        switch (args.length) {
        default:
        case 8:
            minimumVolume = getDouble(sender, args[7], 0.0D, 1.0D);
        case 7:
            pitch = getDouble(sender, args[6], 0.0D, 2.0D);
        case 6:
            volume = getDouble(sender, args[5], 0.0D, Float.MAX_VALUE);
        case 5:
            z = getRelativeDouble(z, sender, args[4]);
        case 4:
            y = getRelativeDouble(y, sender, args[3]);
        case 3:
            x = getRelativeDouble(x, sender, args[2]);
        case 2:
            // Noop
        }

        final double fixedVolume = volume > 1.0D ? volume * 16.0D : 16.0D;
        final Location soundLocation = new Location(player.getWorld(), x, y, z);
        if (location.distanceSquared(soundLocation) > fixedVolume * fixedVolume) {
            if (minimumVolume <= 0.0D) {
                sender.sendMessage(ChatColor.RED + playerArg + " is too far away to hear the sound");
                return false;
            }

            final double deltaX = x - location.getX();
            final double deltaY = y - location.getY();
            final double deltaZ = z - location.getZ();
            final double delta = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / 2.0D;

            if (delta > 0.0D) {
                location.add(deltaX / delta, deltaY / delta, deltaZ / delta);
            }

            player.playSound(location, soundArg, (float) minimumVolume, (float) pitch);
        } else {
            player.playSound(soundLocation, soundArg, (float) volume, (float) pitch);
        }
        sender.sendMessage(String.format("Played '%s' to %s", soundArg, playerArg));
        return true;
    }
}
