package org.bukkit.entity;

import org.jetbrains.annotations.Nullable;

/**
 * @since 1.9.4
 */
public interface ShulkerBullet extends Projectile {

    /**
     * Retrieve the target of this bullet.
     *
     * @return the targeted entity
     */
    @Nullable
    Entity getTarget();

    /**
     * Sets the target of this bullet
     *
     * @param target the entity to target
     */
    void setTarget(@Nullable Entity target);
    // Paper start
    /**
     * Gets the relative offset that this shulker bullet should move towards,
     * note that this will change each tick as the skulker bullet approaches the target.
     *
     * @return target delta offset
     * @since 1.19.2
     */
    @org.jetbrains.annotations.NotNull
    org.bukkit.util.Vector getTargetDelta();


    /**
     * Sets the relative offset that this shulker bullet should move towards,
     * note that this will change each tick as the skulker bullet approaches the target.
     * This is usually relative towards their target.
     *
     * @param vector target
     * @since 1.19.2
     */
    void setTargetDelta(@org.jetbrains.annotations.NotNull org.bukkit.util.Vector vector);

    /**
     * Gets the current movement direction.
     * This is used to determine the next movement direction to ensure
     * that the bullet does not move in the same direction.
     *
     * @return null or their current direction
     * @since 1.19.2
     */
    @Nullable
    org.bukkit.block.BlockFace getCurrentMovementDirection();

    /**
     * Set the current movement direction.
     * This is used to determine the next movement direction to ensure
     * that the bullet does not move in the same direction.
     *
     * Set to null to simply pick a random direction.
     *
     * @param movementDirection null or a direction
     * @since 1.19.2
     */
    void setCurrentMovementDirection(@Nullable org.bukkit.block.BlockFace movementDirection);

    /**
     * Gets how many ticks this shulker bullet
     * will attempt to move in its current direction.
     *
     * @return number of steps
     * @since 1.19.2
     */
    int getFlightSteps();

    /**
     * Sets how many ticks this shulker bullet
     * will attempt to move in its current direction.
     *
     * @param steps number of steps
     * @since 1.19.2
     */
    void setFlightSteps(int steps);
    // Paper end
}
