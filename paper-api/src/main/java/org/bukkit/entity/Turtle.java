package org.bukkit.entity;

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
}
