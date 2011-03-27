package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Holds information for player teleport events
 */
public class PlayerTeleportEvent extends PlayerMoveEvent {
    public PlayerTeleportEvent(Player player, Location from, Location to) {
        super(Type.PLAYER_TELEPORT, player, from, to);
    }
}
