package org.bukkit.entity;

/**
 * Represents a Skeleton.
 * <p>
 * This interface only represents the normal skeleton type on the server.
 * Other skeleton-like entities, such as the {@link WitherSkeleton} or the
 * {@link Stray} are not related to this type.
 */
public interface Skeleton extends AbstractSkeleton {

    /**
     * Computes whether or not this skeleton is currently in the process of
     * converting to a {@link Stray} due to it being frozen by powdered snow.
     *
     * @return whether or not the skeleton is converting to a stray.
     */
    boolean isConverting();

    /**
     * Gets the amount of ticks until this entity will be converted to a stray
     * as a result of being frozen by a powdered snow block.
     * <p>
     * When this reaches 0, the entity will be converted.
     *
     * @return the conversion time left represented in ticks.
     *
     * @throws IllegalStateException if {@link #isConverting()} is false.
     */
    int getConversionTime();

    /**
     * Sets the amount of ticks until this entity will be converted to a stray
     * as a result of being frozen by a powdered snow block.
     * <p>
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time the new conversion time left before the conversion in ticks.
     */
    void setConversionTime(int time);

    /**
     * A legacy enum that defines the different variances of skeleton-like
     * entities on the server.
     *
     * @deprecated classes are different types. This interface only remains in
     *     the Skeleton interface to preserve backwards compatibility.
     */
    @Deprecated
    public enum SkeletonType {

        /**
         * Standard skeleton type.
         */
        NORMAL,
        /**
         * Wither skeleton. Generally found in Nether fortresses.
         */
        WITHER,
        /**
         * Stray skeleton. Generally found in ice biomes. Shoots tipped arrows.
         */
        STRAY,
        /**
         * Bogged skeleton.
         */
        BOGGED;
    }
}
