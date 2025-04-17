package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a Pig.
 */
@NullMarked
public interface Pig extends Steerable, Vehicle {

    /**
     * Gets the variant of this pig.
     *
     * @return the pig variant
     */
    Variant getVariant();

    /**
     * Sets the variant of this pig.
     *
     * @param variant the pig variant
     */
    void setVariant(Variant variant);

    /**
     * Represents the variant of a pig.
     */
    interface Variant extends Keyed {

        enum Model {
            COLD,
            NORMAL
        }

        // Start generate - PigVariant
        // @GeneratedFrom 1.21.5
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - PigVariant

        /**
         * Get the pig variant's asset id
         *
         * @return the asset id
         */
        @NotNull Key assetId();

        /**
         * Get the pig variant's {@link Pig.Variant.Model}. Defaults to {@link Pig.Variant.Model#NORMAL}.
         *
         * @return the model
         */
        @Nullable Model getModel();

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.PIG_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
