package org.bukkit.block;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of an end gateway.
 */
public interface EndGateway extends TileState {

    /**
     * Gets the location that entities are teleported to when
     * entering the gateway portal.
     * <p>
     * If this block state is not placed the location's world will be null.
     *
     * @return the gateway exit location
     */
    @Nullable
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
    void setExitLocation(@Nullable Location location);

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

    /**
     * Gets the age in ticks of the gateway.
     * <br>
     * If the age is less than 200 ticks a magenta beam will be emitted, whilst
     * if it is a multiple of 2400 ticks a purple beam will be emitted.
     *
     * @return age in ticks
     */
    long getAge();

    /**
     * Sets the age in ticks of the gateway.
     * <br>
     * If the age is less than 200 ticks a magenta beam will be emitted, whilst
     * if it is a multiple of 2400 ticks a purple beam will be emitted.
     *
     * @param age new age in ticks
     */
    void setAge(long age);
}
