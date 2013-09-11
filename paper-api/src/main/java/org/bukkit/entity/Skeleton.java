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
        NORMAL(0),
        WITHER(1);

        private static final SkeletonType[] types = new SkeletonType[SkeletonType.values().length];
        private final int id;

        static {
            for (SkeletonType type : values()) {
                types[type.getId()] = type;
            }
        }

        private SkeletonType(int id) {
            this.id = id;
        }

        /**
         * Gets the ID of this skeleton type.
         *
         * @return Skeleton type ID
         * @deprecated Magic value
         */
        @Deprecated
        public int getId() {
            return id;
        }

        /**
         * Gets a skeleton type by its ID.
         *
         * @param id ID of the skeleton type to get.
         * @return Resulting skeleton type, or null if not found.
         * @deprecated Magic value
         */
        @Deprecated
        public static SkeletonType getType(int id) {
            return (id >= types.length) ? null : types[id];
        }
    }
}
