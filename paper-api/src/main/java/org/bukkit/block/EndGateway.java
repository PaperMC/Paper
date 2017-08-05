package org.bukkit.block;

import org.bukkit.Location;

/**
 * Represents a captured state of an end gateway.
 */
public interface EndGateway extends BlockState {

    /**
     * Gets the location that entities are teleported to when 
     * entering the gateway portal.
     * <p>
     * If this block state is not placed the location's world will be null.
     * 
     * @return the gateway exit location
     */
    Location getExitLocation();

    /**
     * Sets the exit location that entities are teleported to when
     * they enter the gateway portal.
     * <p>
     * If this block state is not placed the location's world has to be null.
     * 
     * @param location the new exit location
     * @throws IllegalArgumentException for differing worlds
     */
    void setExitLocation(Location location);

    /**
     * Gets whether this gateway will teleport entities directly to
     * the exit location instead of finding a nearby location.
     * 
     * @return true if the gateway is teleporting to the exact location
     */
    boolean isExactTeleport();

    /**
     * Sets whether this gateway will teleport entities directly to
     * the exit location instead of finding a nearby location.
     * 
     * @param exact whether to teleport to the exact location
     */
    void setExactTeleport(boolean exact);
}
