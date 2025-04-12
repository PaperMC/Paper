package org.bukkit;

import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.InlinedRegistryBuilderProvider;
import io.papermc.paper.registry.data.InstrumentRegistryEntry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.kyori.adventure.translation.Translatable;

public abstract class MusicInstrument implements Keyed, Translatable {

    public static final MusicInstrument PONDER_GOAT_HORN = getInstrument("ponder_goat_horn");
    public static final MusicInstrument SING_GOAT_HORN = getInstrument("sing_goat_horn");
    public static final MusicInstrument SEEK_GOAT_HORN = getInstrument("seek_goat_horn");
    public static final MusicInstrument FEEL_GOAT_HORN = getInstrument("feel_goat_horn");
    public static final MusicInstrument ADMIRE_GOAT_HORN = getInstrument("admire_goat_horn");
    public static final MusicInstrument CALL_GOAT_HORN = getInstrument("call_goat_horn");
    public static final MusicInstrument YEARN_GOAT_HORN = getInstrument("yearn_goat_horn");
    public static final MusicInstrument DREAM_GOAT_HORN = getInstrument("dream_goat_horn");

    /**
     * Create an inlined music instrument.
     *
     * @param value a consumer for the builder factory
     * @return the created instrument
     */
    @ApiStatus.Experimental
    static @NotNull MusicInstrument create(final @NotNull Consumer<RegistryBuilderFactory<MusicInstrument, ? extends InstrumentRegistryEntry.Builder>> value) {
        return InlinedRegistryBuilderProvider.instance().createInstrument(value);
    }

    /**
     * Returns a {@link MusicInstrument} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the key
     * @return the event or null
     * @deprecated Use {@link Registry#get(NamespacedKey)} instead.
     */
    @Nullable
    @Deprecated(since = "1.20.1")
    public static MusicInstrument getByKey(@NotNull NamespacedKey namespacedKey) {
        return Registry.INSTRUMENT.get(namespacedKey);
    }

    /**
     * Returns all known MusicInstruments.
     *
     * @return the memoryKeys
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.20.1")
    public static Collection<MusicInstrument> values() {
        return Collections.unmodifiableCollection(Lists.newArrayList(Registry.INSTRUMENT));
    }

    @NotNull
    private static MusicInstrument getInstrument(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.INSTRUMENT).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Gets for how long the use duration is for the instrument.
     *
     * @return the duration.
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
    public abstract @NotNull Component description();

    /**
     * Gets the sound/sound-event for this instrument.
     *
     * @return a sound
     */
    public abstract @NotNull Sound getSoundEvent();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#INSTRUMENT}. MusicInstruments can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.5")
    @Override
    public abstract @NotNull NamespacedKey getKey();

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
    public abstract @NotNull String translationKey();
}
