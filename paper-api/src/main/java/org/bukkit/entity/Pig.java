package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
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
     * Get the sound variant of this pig.
     *
     * @return pig sound variant
     */
    SoundVariant getSoundVariant();

    /**
     * Set the sound variant of this pig.
     *
     * @param variant pig sound variant
     */
    void setSoundVariant(SoundVariant variant);

    /**
     * Represents the variant of a pig.
     */
    interface Variant extends Keyed {

        // Start generate - PigVariant
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - PigVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.PIG_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }

    /**
     * Represents the sound variant of a pig.
     */
    interface SoundVariant extends Keyed {

        // Start generate - PigSoundVariant
        SoundVariant BIG = getSoundVariant("big");

        SoundVariant CLASSIC = getSoundVariant("classic");

        SoundVariant MINI = getSoundVariant("mini");
        // End generate - PigSoundVariant

        private static SoundVariant getSoundVariant(final @KeyPattern.Value String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.PIG_SOUND_VARIANT).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
        }
    }
}
