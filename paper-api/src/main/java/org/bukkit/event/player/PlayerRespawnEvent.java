package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerRespawnEvent extends PlayerEvent {
    private Location respawnLocation;
    private boolean isBedSpawn;

    public PlayerRespawnEvent(Player respawnPlayer, Location respawnLocation, boolean isBedSpawn) {
        super(Type.PLAYER_RESPAWN, respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
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

    /**
     * Gets whether the respawn location is the players bed.
     *
     * @return true if the respawn location is the players bed.
     */
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }
}
