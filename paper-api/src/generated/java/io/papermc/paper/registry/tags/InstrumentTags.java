package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.InstrumentTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.MusicInstrument;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#INSTRUMENT}.
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
@GeneratedFrom("1.21.6")
public final class InstrumentTags {
    /**
     * {@code #minecraft:goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<MusicInstrument> GOAT_HORNS = fetch(InstrumentTagKeys.GOAT_HORNS);

    /**
     * {@code #minecraft:regular_goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<MusicInstrument> REGULAR_GOAT_HORNS = fetch(InstrumentTagKeys.REGULAR_GOAT_HORNS);

    /**
     * {@code #minecraft:screaming_goat_horns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<MusicInstrument> SCREAMING_GOAT_HORNS = fetch(InstrumentTagKeys.SCREAMING_GOAT_HORNS);

    private InstrumentTags() {
    }

    private static Tag<MusicInstrument> fetch(final TagKey<MusicInstrument> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.INSTRUMENT).getTag(tagKey);
    }
}
