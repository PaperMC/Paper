package org.bukkit.entity;

/**
 * Represents a Skeleton.
 */
public interface Skeleton extends Monster {

    /**
     * Gets the current type of this skeleton.
     *
     * @return Current type
     * @deprecated should check what class instance this is
     */
    @Deprecated
    public SkeletonType getSkeletonType();

    /**
     * @deprecated Must spawn a new subtype variant
     */
    @Deprecated
    public void setSkeletonType(SkeletonType type);

    /*
     * @deprecated classes are different types
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
        STRAY;
    }
}
