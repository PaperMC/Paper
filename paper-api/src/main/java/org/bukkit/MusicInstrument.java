package org.bukkit;

import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MusicInstrument implements Keyed, net.kyori.adventure.translation.Translatable { // Paper - translation keys

    // Start generate - MusicInstrument
    // @GeneratedFrom 1.21.4
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

    // Paper start - deprecate getKey
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

    // Paper end - deprecate getKey

    // Paper start - mark translation key as deprecated
    /**
     * @deprecated this method assumes that the instrument description
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    public abstract @NotNull String translationKey();
    // Paper end - mark translation key as deprecated
}
