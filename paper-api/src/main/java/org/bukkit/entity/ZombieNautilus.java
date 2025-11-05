package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ZombieNautilus extends AbstractNautilus {

    /**
     * Gets the variant of this zombie nautilus.
     *
     * @return the variant
     */
    Variant getVariant();

    /**
     * Sets the variant of this zombie nautilus.
     *
     * @param variant the variant
     */
    void setVariant(Variant variant);

    /**
     * Represents the variant of a Zombie Nautilus.
     */
    interface Variant extends Keyed {

        // Start generate - ZombieNautilusVariant
        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - ZombieNautilusVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ZOMBIE_NAUTILUS_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
