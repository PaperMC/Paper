package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.WorldPreset;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#WORLD_PRESET}.
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
public final class WorldPresetKeys {
    /**
     * {@code minecraft:amplified}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> AMPLIFIED = create(key("amplified"));

    /**
     * {@code minecraft:debug_all_block_states}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> DEBUG_ALL_BLOCK_STATES = create(key("debug_all_block_states"));

    /**
     * {@code minecraft:flat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> FLAT = create(key("flat"));

    /**
     * {@code minecraft:large_biomes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> LARGE_BIOMES = create(key("large_biomes"));

    /**
     * {@code minecraft:normal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> NORMAL = create(key("normal"));

    /**
     * {@code minecraft:single_biome_surface}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldPreset> SINGLE_BIOME_SURFACE = create(key("single_biome_surface"));

    private WorldPresetKeys() {
    }

    /**
     * Creates a typed key for {@link WorldPreset} in the registry {@code minecraft:worldgen/world_preset}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<WorldPreset> create(final Key key) {
        return TypedKey.create(RegistryKey.WORLD_PRESET, key);
    }
}
