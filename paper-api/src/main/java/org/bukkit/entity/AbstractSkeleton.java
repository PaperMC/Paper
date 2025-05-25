package org.bukkit.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This interface defines or represents the abstract concept of skeleton-like
 * entities on the server. The interface is hence not a direct representation
 * of an entity but rather serves as a parent to interfaces/entity types like
 * {@link Skeleton}, {@link WitherSkeleton} or {@link Stray}.
 *
 * To compute what specific type of skeleton is present in a variable/field
 * of this type, instanceOf checks against the specific subtypes listed prior
 * are recommended.
 */
public interface AbstractSkeleton extends Monster, com.destroystokyo.paper.entity.RangedEntity { // Paper

    /**
     * Gets the current type of this skeleton.
     *
     * @return Current type
     * @deprecated should check what class instance this is.
     */
    @Deprecated(since = "1.17", forRemoval = true)
    @NotNull
    public Skeleton.SkeletonType getSkeletonType();

    /**
     * @param type type
     * @deprecated Must spawn a new subtype variant
     */
    @Deprecated(since = "1.17", forRemoval = true)
    @Contract("_ -> fail")
    public void setSkeletonType(Skeleton.SkeletonType type);

    // Paper start
    /**
     * Check if this skeleton will burn in the sunlight. This
     * does not take into account an entity's natural fire
     * immunity.
     *
     * @return True if skeleton will burn in sunlight
     */
    boolean shouldBurnInDay();

    /**
     * Set if this skeleton should burn in the sunlight. This
     * will not override an entity's natural fire
     * immunity.
     *
     * @param shouldBurnInDay True to burn in sunlight
     */
    void setShouldBurnInDay(boolean shouldBurnInDay);
    // Paper end
}
