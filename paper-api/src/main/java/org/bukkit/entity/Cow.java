package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

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

        // Start generate - CowVariant
        // @GeneratedFrom 1.21.6-pre1
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - CowVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.COW_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
