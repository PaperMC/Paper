package org.bukkit.entity;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Bogged Skeleton.
 */
@ApiStatus.Experimental
public interface Bogged extends AbstractSkeleton, Shearable {

    /**
     * Gets whether the bogged is in its sheared state.
     *
     * @return Whether the bogged is sheared.
     */
    boolean isSheared();

    /**
     * Sets whether the bogged is in its sheared state.
     *
     * @param flag Whether to shear the bogged
     */
    void setSheared(boolean flag);
}
