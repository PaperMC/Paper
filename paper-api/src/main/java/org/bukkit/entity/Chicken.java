package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a Chicken.
 */
@NullMarked
public interface Chicken extends Animals {

    /**
     * Gets the variant of this chicken.
     *
     * @return the chicken variant
     */
    Variant getVariant();

    /**
     * Sets the variant of this chicken.
     *
     * @param variant the chicken variant
     */
    void setVariant(Variant variant);

    /**
     * Gets if this chicken was spawned as a chicken jockey.
     *
     * @return is chicken jockey
     */
    boolean isChickenJockey();

    /**
     * Sets if this chicken was spawned as a chicken jockey.
     *
     * @param isChickenJockey is chicken jockey
     */
    void setIsChickenJockey(boolean isChickenJockey);

    /**
     * Gets the number of ticks till this chicken lays an egg.
     *
     * @return ticks till the chicken lays an egg
     */
    int getEggLayTime();

    /**
     * Sets the number of ticks till this chicken lays an egg.
     *
     * @param eggLayTime ticks till the chicken lays an egg
     */
    void setEggLayTime(int eggLayTime);

    /**
     * Represents the variant of a chicken.
     */
    interface Variant extends Keyed {

        // Start generate - ChickenVariant
        // @GeneratedFrom 1.21.6-pre1
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - ChickenVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.CHICKEN_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
