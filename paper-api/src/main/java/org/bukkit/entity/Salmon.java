package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a salmon fish.
 */
public interface Salmon extends io.papermc.paper.entity.SchoolableFish { // Paper - Schooling Fish API

    /**
     * Get the variant of this salmon.
     *
     * @return salmon variant
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

        // Start generate - SalmonVariant
        // @GeneratedFrom 1.21.6-pre1
        SMALL,
        MEDIUM,
        LARGE;
        // End generate - SalmonVariant
    }
}
