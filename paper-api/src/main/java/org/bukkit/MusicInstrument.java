package org.bukkit;

import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Collection;
import java.util.Collections;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class MusicInstrument implements Keyed, net.kyori.adventure.translation.Translatable {

    // Start generate - MusicInstrument
    // @GeneratedFrom 1.21.6-pre1
    public static final MusicInstrument ADMIRE_GOAT_HORN = getInstrument("admire_goat_horn");

    public static final MusicInstrument CALL_GOAT_HORN = getInstrument("call_goat_horn");

    public static final MusicInstrument DREAM_GOAT_HORN = getInstrument("dream_goat_horn");

    public static final MusicInstrument FEEL_GOAT_HORN = getInstrument("feel_goat_horn");

    public static final MusicInstrument PONDER_GOAT_HORN = getInstrument("ponder_goat_horn");

    public static final MusicInstrument SEEK_GOAT_HORN = getInstrument("seek_goat_horn");

    public static final MusicInstrument SING_GOAT_HORN = getInstrument("sing_goat_horn");

    public static final MusicInstrument YEARN_GOAT_HORN = getInstrument("yearn_goat_horn");
    // End generate - MusicInstrument

    /**
     * Returns a {@link MusicInstrument} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the key
     * @return the event or null
     * @deprecated Use {@link Registry#get(NamespacedKey)} instead.
     */
    @Nullable
    @Deprecated(since = "1.20.1")
    public static MusicInstrument getByKey(final NamespacedKey namespacedKey) {
        return Registry.INSTRUMENT.get(namespacedKey);
    }

    /**
     * Returns all known MusicInstruments.
     *
     * @return the memoryKeys
     * @deprecated use {@link Registry#iterator()}.
     */
    @Deprecated(since = "1.20.1")
    public static Collection<MusicInstrument> values() {
        return Collections.unmodifiableCollection(Lists.newArrayList(Registry.INSTRUMENT));
    }

    private static MusicInstrument getInstrument(final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.INSTRUMENT).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Gets the use duration of this music instrument.
     *
     * @return the duration expressed in seconds.
     */
    public abstract float getDuration();

    /**
     * Gets the range of the sound.
     *
     * @return the range of the sound.
     */
    public abstract float getRange();

    /**
     * Provides the description of this instrument as displayed to the client.
     *
     * @return the description component.
     */
    public abstract Component description();

    /**
     * Gets the sound for this instrument.
     *
     * @return the sound
     */
    public abstract Sound getSound();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#INSTRUMENT}. MusicInstruments can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.5")
    @Override
    public abstract NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#INSTRUMENT}. MusicInstruments can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.5")
    @Override
    public net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return Keyed.super.key();
    }

    /**
     * @deprecated this method assumes that the instrument description
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    public abstract String translationKey();
}
