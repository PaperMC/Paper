package io.papermc.paper.raytrace;

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
    VISUAL,
    /**
     * Use the shape of a full block, but only consider blocks tagged with {@link org.bukkit.Tag#FALL_DAMAGE_RESETTING}.
     */
    FALL_DAMAGE_RESETTING

}
