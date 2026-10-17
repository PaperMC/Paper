package io.papermc.paper.raytracing;

/**
 * Determines the collision behavior when blocks get hit during ray tracing.
 */
public enum BlockCollisionMode {

    /**
     * Use the collision shape.
     */
    COLLIDER,
    /**
     * Use the outline shape.
     */
    OUTLINE,
    /**
     * Use the visual shape.
     */
    VISUAL
}
