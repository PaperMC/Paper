package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.MusicInstrument;
import org.jetbrains.annotations.ApiStatus;
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
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class InstrumentTagKeys {
    /**
     * {@code #minecraft:goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<MusicInstrument> GOAT_HORNS = create(key("goat_horns"));

    /**
     * {@code #minecraft:regular_goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<MusicInstrument> REGULAR_GOAT_HORNS = create(key("regular_goat_horns"));

    /**
     * {@code #minecraft:screaming_goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<MusicInstrument> SCREAMING_GOAT_HORNS = create(key("screaming_goat_horns"));

    private InstrumentTagKeys() {
    }

    /**
     * Creates a tag key for {@link MusicInstrument} in the registry {@code minecraft:instrument}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<MusicInstrument> create(final Key key) {
        return TagKey.create(RegistryKey.INSTRUMENT, key);
    }
}
