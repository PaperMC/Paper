package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a turtle.
 *
 * @since 1.13
 */
public interface Turtle extends Animals {

    /**
     * Gets whether the turtle has an egg
     *
     * @return Whether the turtle has an egg
     * @since 1.13.1
     */
    boolean hasEgg();

    /**
     * Gets whether the turtle is laying an egg
     *
     * @return Whether the turtle is laying an egg
     * @since 1.18.2
     */
    boolean isLayingEgg();

    // Paper start
    /**
     * Get the turtle's home location
     *
     * @return Home location
     * @since 1.13.1
     */
    @NotNull
    Location getHome();

    /**
     * Set the turtle's home location
     *
     * @param location Home location
     * @since 1.13.1
     */
    void setHome(@NotNull Location location);

    /**
     * Check if turtle is currently pathfinding to it's home
     *
     * @return True if going home
     * @since 1.13.1
     */
    boolean isGoingHome();

    /**
     * Get if turtle is digging to lay eggs
     *
     * @return True if digging
     * @since 1.13.1
     */
    boolean isDigging();

    /**
     * Set if turtle is carrying egg
     *
     * @param hasEgg True if carrying egg
     * @since 1.13.1
     */
    void setHasEgg(boolean hasEgg);
    // Paper end
}
