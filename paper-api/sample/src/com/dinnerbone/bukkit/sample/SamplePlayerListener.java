
package com.dinnerbone.bukkit.sample;

import org.bukkit.Location;
import org.bukkit.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * @author Dinnerbone
 */
public class SamplePlayerListener extends PlayerListener {
    private final SamplePlugin plugin;

    public SamplePlayerListener(SamplePlugin instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerEvent event) {
        System.out.println(event.getPlayer().getName() + " joined the server! :D");
    }

    @Override
    public void onPlayerQuit(PlayerEvent event) {
        System.out.println(event.getPlayer().getName() + " left the server! :'(");
    }

    @Override
    public void onPlayerCommand(PlayerChatEvent event) {
        String[] split = event.getMessage().split(" ");
        Player player = event.getPlayer();

        if (split[0].equalsIgnoreCase("/pos")) {
            if (split.length == 1) {
                Location location = player.getLocation();
                player.sendMessage("You are currently at " + location.getX() +"," + location.getY() + "," + location.getZ() +
                        " with " + location.getYaw() + " yaw and " + location.getPitch() + " pitch");
            } else if (split.length == 4) {
                try {
                    double x = Double.parseDouble(split[1]);
                    double y = Double.parseDouble(split[2]);
                    double z = Double.parseDouble(split[3]);

                    player.teleportTo(new Location(player.getWorld(), x, y, z));
                } catch (NumberFormatException ex) {
                    player.sendMessage("Given location is invalid");
                }
            } else {
                player.sendMessage("Usage: '/pos' to get current position, or '/pos x y z' to teleport to x,y,z");
            }

            event.setCancelled(true);
        }
    }
}
