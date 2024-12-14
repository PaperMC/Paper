package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a salmon fish.
 *
 * @since 1.13
 */
public interface Salmon extends io.papermc.paper.entity.SchoolableFish { // Paper - Schooling Fish API

    /**
     * Get the variant of this salmon.
     *
     * @return salmon variant
     * @since 1.21.3
     */
    @NotNull
    public Variant getVariant();

    /**
     * Set the variant of this salmon.
     *
     * @param variant salmon variant
     */
    public void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a salmon - ie its size.
     */
    public enum Variant {

        /**
         * Small salmon.
         */
        SMALL,
        /**
         * Default salmon.
         */
        MEDIUM,
        /**
         * Large salmon.
         */
        LARGE;
    }
}
