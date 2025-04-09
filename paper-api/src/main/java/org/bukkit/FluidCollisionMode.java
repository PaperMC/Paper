package org.bukkit;

/**
 * Determines the collision behavior when fluids get hit during ray tracing.
 */
public enum FluidCollisionMode {

    /**
     * Ignore fluids.
     */
    NEVER,
    /**
     * Only collide with source fluid blocks.
     */
    SOURCE_ONLY,
    /**
     * Collide with all fluids.
     */
    ALWAYS,
    /**
     * Only collide with lava fluid blocks.
     */
    LAVA,
    /**
     * Only collide with water fluid blocks.
     */
    WATER
}
