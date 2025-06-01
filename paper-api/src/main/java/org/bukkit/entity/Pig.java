package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

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

        // Start generate - PigVariant
        // @GeneratedFrom 1.21.6-pre1
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - PigVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.PIG_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
