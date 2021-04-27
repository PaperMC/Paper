package org.bukkit.entity;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a vehicle entity.
 */
public interface Vehicle extends Entity {

    /**
     * Gets the vehicle's velocity.
     *
     * @return velocity vector
     */
    @Override
    @NotNull
    public Vector getVelocity();

    /**
     * Sets the vehicle's velocity in meters per tick.
     *
     * @param vel velocity vector
     */
    @Override
    public void setVelocity(@NotNull Vector vel);
}
