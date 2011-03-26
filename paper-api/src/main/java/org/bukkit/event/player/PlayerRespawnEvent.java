package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerRespawnEvent extends PlayerEvent {
    private Location respawnLocation;

    public PlayerRespawnEvent(Player respawnPlayer, Location respawnLocation) {
        super(Type.PLAYER_RESPAWN, respawnPlayer);
        this.respawnLocation = respawnLocation;
    }

    /**
     * Gets the current respawn location
     *
     * @return Location current respawn location
     */
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    /**
     * Sets the new respawn location
     *
     * @param respawnLocation new location for the respawn
     */
    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }
}
