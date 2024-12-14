package org.bukkit.entity;

/**
 * Represents a Bat
 *
 * @since 1.4.5 R1.0
 */
public interface Bat extends Ambient {

    /**
     * Checks the current waking state of this bat.
     * <p>
     * This does not imply any persistence of state past the method call.
     *
     * @return true if the bat is awake or false if it is currently hanging
     *     from a block
     * @since 1.7.10
     */
    boolean isAwake();

    /**
     * This method modifies the current waking state of this bat.
     * <p>
     * This does not prevent a bat from spontaneously awaking itself, or from
     * reattaching itself to a block.
     *
     * @param state the new state
     * @since 1.7.10
     */
    void setAwake(boolean state);

    // Paper start
    /**
     * Gets the location that this bat is currently trying to move towards.
     *
     * @return target location, or null if it's going to find a new location
     * @since 1.19.2
     */
    @org.jetbrains.annotations.Nullable
    org.bukkit.Location getTargetLocation();

    /**
     * Sets the location that this bat is currently trying to move towards.
     * <p>
     * This can be set to null to cause the bat to recalculate its target location
     *
     * @param location location to move towards (world is ignored, will always use the entity's world)
     * @since 1.19.2
     */
    void setTargetLocation(@org.jetbrains.annotations.Nullable org.bukkit.Location location);
    // Paper end
}
