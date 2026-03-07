package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
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
     * Get the sound variant of this cow.
     *
     * @return cow sound variant
     */
    SoundVariant getSoundVariant();

    /**
     * Set the sound variant of this cow.
     *
     * @param variant cow sound variant
     */
    void setSoundVariant(SoundVariant variant);

    /**
     * Represents the variant of a cow.
     */
    interface Variant extends Keyed {

        // Start generate - CowVariant
        Variant COLD = getVariant("cold");

        Variant TEMPERATE = getVariant("temperate");

        Variant WARM = getVariant("warm");
        // End generate - CowVariant

        private static Variant getVariant(String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.COW_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }

    /**
     * Represents the sound variant of a cow.
     */
    interface SoundVariant extends Keyed {

        // Start generate - CowSoundVariant
        SoundVariant CLASSIC = getSoundVariant("classic");

        SoundVariant MOODY = getSoundVariant("moody");
        // End generate - CowSoundVariant

        private static SoundVariant getSoundVariant(final @KeyPattern.Value String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.COW_SOUND_VARIANT).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
        }
    }
}
