package org.bukkit.entity;

import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Frog.
 */
public interface Frog extends Animals {

    /**
     * Gets the tongue target of this frog.
     *
     * @return tongue target or null if not set
     */
    @Nullable
    Entity getTongueTarget();

    /**
     * Sets the tongue target of this frog.
     *
     * @param target tongue target or null to clear
     */
    void setTongueTarget(@Nullable Entity target);

    /**
     * Get the variant of this frog.
     *
     * @return frog variant
     */
    @NotNull
    Variant getVariant();

    /**
     * Set the variant of this frog.
     *
     * @param variant frog variant
     */
    void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a frog - ie its color.
     */
    public enum Variant implements Keyed {

        /**
         * Temperate (brown-orange) frog.
         */
        TEMPERATE,
        /**
         * Warm (gray) frog.
         */
        WARM,
        /**
         * Cold (green) frog.
         */
        COLD;
        private final NamespacedKey key;

        private Variant() {
            this.key = NamespacedKey.minecraft(name().toLowerCase(Locale.ROOT));
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return key;
        }
    }
}
