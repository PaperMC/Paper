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
     * Collide only with water.
     */
    WATER,
    /**
     * Collide with all fluids.
     */
    ALWAYS;
}
