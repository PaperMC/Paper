package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.MusicInstrument;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#INSTRUMENT}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedFrom("1.21.6-pre1")
public final class InstrumentKeys {
    /**
     * {@code minecraft:admire_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> ADMIRE_GOAT_HORN = create(key("admire_goat_horn"));

    /**
     * {@code minecraft:call_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> CALL_GOAT_HORN = create(key("call_goat_horn"));

    /**
     * {@code minecraft:dream_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> DREAM_GOAT_HORN = create(key("dream_goat_horn"));

    /**
     * {@code minecraft:feel_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> FEEL_GOAT_HORN = create(key("feel_goat_horn"));

    /**
     * {@code minecraft:ponder_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> PONDER_GOAT_HORN = create(key("ponder_goat_horn"));

    /**
     * {@code minecraft:seek_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> SEEK_GOAT_HORN = create(key("seek_goat_horn"));

    /**
     * {@code minecraft:sing_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> SING_GOAT_HORN = create(key("sing_goat_horn"));

    /**
     * {@code minecraft:yearn_goat_horn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MusicInstrument> YEARN_GOAT_HORN = create(key("yearn_goat_horn"));

    private InstrumentKeys() {
    }

    /**
     * Creates a typed key for {@link MusicInstrument} in the registry {@code minecraft:instrument}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<MusicInstrument> create(final Key key) {
        return TypedKey.create(RegistryKey.INSTRUMENT, key);
    }
}
