package org.bukkit.block;

import org.bukkit.Location;

/**
 * Represents an end gateway.
 */
public interface EndGateway extends BlockState {

    /**
     * Gets the location that entities are teleported to when 
     * entering the gateway portal.
     * 
     * @return the gateway exit location
     */
    Location getExitLocation();

    /**
     * Sets the exit location that entities are teleported to when
     * they enter the gateway portal.
     * 
     * @param location the new exit location
     * @throws IllegalArgumentException for differing worlds
     */
    void setExitLocation(Location location);
}
