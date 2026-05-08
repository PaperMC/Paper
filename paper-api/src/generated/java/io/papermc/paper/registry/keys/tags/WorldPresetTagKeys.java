package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.world.WorldPreset;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#WORLD_PRESET}.
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
@ApiStatus.Experimental
public final class WorldPresetTagKeys {
    /**
     * {@code #minecraft:extended}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<WorldPreset> EXTENDED = create(key("extended"));

    /**
     * {@code #minecraft:normal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<WorldPreset> NORMAL = create(key("normal"));

    private WorldPresetTagKeys() {
    }

    /**
     * Creates a tag key for {@link WorldPreset} in the registry {@code minecraft:worldgen/world_preset}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<WorldPreset> create(final Key key) {
        return TagKey.create(RegistryKey.WORLD_PRESET, key);
    }
}
