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
    @NotNull
    public Vector getVelocity();

    /**
     * Sets the vehicle's velocity.
     *
     * @param vel velocity vector
     */
    public void setVelocity(@NotNull Vector vel);
}
