package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Parrot.
 */
public interface Parrot extends Tameable, Sittable {

    /**
     * Get the variant of this parrot.
     *
     * @return parrot variant
     */
    @NotNull
    public Variant getVariant();

    /**
     * Set the variant of this parrot.
     *
     * @param variant parrot variant
     */
    public void setVariant(@NotNull Variant variant);

    /**
     * Gets whether a parrot is dancing
     *
     * @return Whether the parrot is dancing
     */
    public boolean isDancing();

    /**
     * Represents the variant of a parrot - ie its color.
     */
    public enum Variant {
        /**
         * Classic parrot - red with colored wingtips.
         */
        RED,
        /**
         * Royal blue colored parrot.
         */
        BLUE,
        /**
         * Green colored parrot.
         */
        GREEN,
        /**
         * Cyan colored parrot.
         */
        CYAN,
        /**
         * Gray colored parrot.
         */
        GRAY;
    }
}
