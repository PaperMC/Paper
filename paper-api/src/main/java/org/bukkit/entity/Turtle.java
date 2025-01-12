package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a turtle.
 */
public interface Turtle extends Animals {

    /**
     * Gets whether the turtle has an egg
     *
     * @return Whether the turtle has an egg
     */
    boolean hasEgg();

    /**
     * Gets whether the turtle is laying an egg
     *
     * @return Whether the turtle is laying an egg
     */
    boolean isLayingEgg();

    /**
     * Get the turtle's home location
     *
     * @return Home location
     */
    @NotNull
    Location getHome();

    /**
     * Set the turtle's home location
     *
     * @param location Home location
     */
    void setHome(@NotNull Location location);

    /**
     * Check if turtle is currently pathfinding to it's home
     *
     * @return True if going home
     */
    boolean isGoingHome();

    /**
     * Get if turtle is digging to lay eggs
     *
     * @return True if digging
     * @deprecated in favor of {@link #isLayingEgg()}
     */
    @Deprecated(since = "1.21.4")
    default boolean isDigging() {
        return this.isLayingEgg();
    }

    /**
     * Set if turtle is carrying egg
     *
     * @param hasEgg True if carrying egg
     */
    void setHasEgg(boolean hasEgg);
}
