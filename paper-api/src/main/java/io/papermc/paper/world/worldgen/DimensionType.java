package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;

public interface DimensionType extends Keyed {

    DimensionType OVERWORLD = get("overworld");
    DimensionType NETHER = get("the_nether");
    DimensionType END = get("the_end");
    DimensionType OVERWORLD_CAVES = get("overworld_caves");

    private static DimensionType get(@KeyPattern final String name) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DIMENSION_TYPE).getOrThrow(Key.key(name));
    }
}
