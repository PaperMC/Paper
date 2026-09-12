package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

public final class WorldPresets {

    // Start generate - WorldPresets
    public static final WorldPreset AMPLIFIED = getPreset("amplified");

    public static final WorldPreset DEBUG_ALL_BLOCK_STATES = getPreset("debug_all_block_states");

    public static final WorldPreset FLAT = getPreset("flat");

    public static final WorldPreset LARGE_BIOMES = getPreset("large_biomes");

    public static final WorldPreset NORMAL = getPreset("normal");

    public static final WorldPreset SINGLE_BIOME_SURFACE = getPreset("single_biome_surface");
    // End generate - WorldPresets

    private static WorldPreset getPreset(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.WORLD_PRESET).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
