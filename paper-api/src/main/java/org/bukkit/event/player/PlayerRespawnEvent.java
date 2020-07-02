package org.bukkit.event.player;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player respawns.
 */
public class PlayerRespawnEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location respawnLocation;
    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;

    @Deprecated
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn) {
        this(respawnPlayer, respawnLocation, isBedSpawn, false);
    }

    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
        this.isAnchorSpawn = isAnchorSpawn;
    }

    /**
     * Gets the current respawn location
     *
     * @return Location current respawn location
     */
    @NotNull
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    /**
     * Sets the new respawn location
     *
     * @param respawnLocation new location for the respawn
     */
    public void setRespawnLocation(@NotNull Location respawnLocation) {
        Validate.notNull(respawnLocation, "Respawn location can not be null");
        Validate.notNull(respawnLocation.getWorld(), "Respawn world can not be null");

        this.respawnLocation = respawnLocation;
    }

    /**
     * Gets whether the respawn location is the player's bed.
     *
     * @return true if the respawn location is the player's bed.
     */
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    /**
     * Gets whether the respawn location is the player's respawn anchor.
     *
     * @return true if the respawn location is the player's respawn anchor.
     */
    public boolean isAnchorSpawn() {
        return isAnchorSpawn;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
