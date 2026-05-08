package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

public final class DimensionTypes {

    // Start generate - DimensionTypes
    public static final DimensionType OVERWORLD = getType("overworld");

    public static final DimensionType OVERWORLD_CAVES = getType("overworld_caves");

    public static final DimensionType THE_END = getType("the_end");

    public static final DimensionType THE_NETHER = getType("the_nether");
    // End generate - DimensionTypes

    private static DimensionType getType(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DIMENSION_TYPE).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
