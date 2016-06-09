package org.bukkit.entity;

/**
 * Represents a Skeleton.
 */
public interface Skeleton extends Monster {

    /**
     * Gets the current type of this skeleton.
     *
     * @return Current type
     */
    public SkeletonType getSkeletonType();

    /**
     * Sets the new type of this skeleton.
     *
     * @param type New type
     */
    public void setSkeletonType(SkeletonType type);

    /*
     * Represents the various different Skeleton types.
     */
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
        STRAY;
    }
}
