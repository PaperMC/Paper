package org.bukkit;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MusicInstrument implements Keyed {

    private static final Map<NamespacedKey, MusicInstrument> INSTRUMENTS = new HashMap<>();
    //
    public static final MusicInstrument PONDER = getInstrument("ponder_goat_horn");
    public static final MusicInstrument SING = getInstrument("sing_goat_horn");
    public static final MusicInstrument SEEK = getInstrument("seek_goat_horn");
    public static final MusicInstrument FEEL = getInstrument("feel_goat_horn");
    public static final MusicInstrument ADMIRE = getInstrument("admire_goat_horn");
    public static final MusicInstrument CALL = getInstrument("call_goat_horn");
    public static final MusicInstrument YEARN = getInstrument("yearn_goat_horn");
    public static final MusicInstrument DREAM = getInstrument("dream_goat_horn");
    //
    private final NamespacedKey key;

    private MusicInstrument(NamespacedKey key) {
        this.key = key;

        INSTRUMENTS.put(key, this);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Returns a {@link MusicInstrument} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the key
     * @return the event or null
     */
    @Nullable
    public static MusicInstrument getByKey(@NotNull NamespacedKey namespacedKey) {
        Preconditions.checkArgument(namespacedKey != null, "NamespacedKey cannot be null");

        return INSTRUMENTS.get(namespacedKey);
    }

    /**
     * Returns all known MusicInstruments.
     *
     * @return the memoryKeys
     */
    @NotNull
    public static Collection<MusicInstrument> values() {
        return Collections.unmodifiableCollection(INSTRUMENTS.values());
    }

    private static MusicInstrument getInstrument(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Instrument name cannot be null");

        return new MusicInstrument(NamespacedKey.minecraft(name));
    }
}
