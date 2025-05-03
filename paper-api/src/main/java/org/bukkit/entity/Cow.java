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
 * Represents a Cow.
 */
@NullMarked
public interface Cow extends AbstractCow {

    /**
     * Gets the variant of this cow.
     *
     * @return the cow variant
     */
    Variant getVariant();

    /**
     * Sets the variant of this cow.
     *
     * @param variant the cow variant
     */
    void setVariant(Variant variant);

    /**
     * Represents the variant of a cow.
     */
    interface Variant extends Keyed {

        enum Model {
            COLD,
            NORMAL,
            WARM
        }

        // Start generate - CowVariant
        // @GeneratedFrom 1.21.5
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - CowVariant

        /**
         * Get the cow variant's asset id
         *
         * @return the asset id
         */
        @NotNull Key assetId();

        /**
         * Get the cow variant's {@link Cow.Variant.Model}. Defaults to {@link Cow.Variant.Model#NORMAL}.
         *
         * @return the model
         */
        @Nullable Model getModel();

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.COW_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
