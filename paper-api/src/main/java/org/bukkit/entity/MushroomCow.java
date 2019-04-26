package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a mushroom {@link Cow}
 */
public interface MushroomCow extends Cow {

    /**
     * Get the variant of this cow.
     *
     * @return cow variant
     */
    @NotNull
    public Variant getVariant();

    /**
     * Set the variant of this cow.
     *
     * @param variant cow variant
     */
    public void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a cow - ie its color.
     */
    public enum Variant {
        /**
         * Red mushroom cow.
         */
        RED,
        /**
         * Brown mushroom cow.
         */
        BROWN;
    }
}
