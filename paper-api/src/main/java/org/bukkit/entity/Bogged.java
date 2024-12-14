package org.bukkit.entity;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Bogged Skeleton.
 *
 * @since 1.20.6
 */
public interface Bogged extends AbstractSkeleton, Shearable, io.papermc.paper.entity.Shearable { // Paper - Shear API

    /**
     * Gets whether the bogged is in its sheared state.
     *
     * @return Whether the bogged is sheared.
     * @since 1.21.4
     */
    boolean isSheared();

    /**
     * Sets whether the bogged is in its sheared state.
     *
     * @param flag Whether to shear the bogged
     * @since 1.21.4
     */
    void setSheared(boolean flag);
}
