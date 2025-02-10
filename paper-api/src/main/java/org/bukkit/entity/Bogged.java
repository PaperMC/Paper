package org.bukkit.entity;

/**
 * Represents a Bogged Skeleton.
 */
public interface Bogged extends AbstractSkeleton, Shearable, io.papermc.paper.entity.Shearable { // Paper - Shear API

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
