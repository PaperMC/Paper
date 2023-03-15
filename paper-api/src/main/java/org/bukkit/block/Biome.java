package org.bukkit.block;

import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Holds all accepted Biomes in the default server
 */
public enum Biome implements Keyed {
    OCEAN,
    PLAINS,
    DESERT,
    WINDSWEPT_HILLS,
    FOREST,
    TAIGA,
    SWAMP,
    MANGROVE_SWAMP,
    RIVER,
    NETHER_WASTES,
    THE_END,
    FROZEN_OCEAN,
    FROZEN_RIVER,
    SNOWY_PLAINS,
    MUSHROOM_FIELDS,
    BEACH,
    JUNGLE,
    SPARSE_JUNGLE,
    DEEP_OCEAN,
    STONY_SHORE,
    SNOWY_BEACH,
    BIRCH_FOREST,
    DARK_FOREST,
    SNOWY_TAIGA,
    OLD_GROWTH_PINE_TAIGA,
    WINDSWEPT_FOREST,
    SAVANNA,
    SAVANNA_PLATEAU,
    BADLANDS,
    WOODED_BADLANDS,
    SMALL_END_ISLANDS,
    END_MIDLANDS,
    END_HIGHLANDS,
    END_BARRENS,
    WARM_OCEAN,
    LUKEWARM_OCEAN,
    COLD_OCEAN,
    DEEP_LUKEWARM_OCEAN,
    DEEP_COLD_OCEAN,
    DEEP_FROZEN_OCEAN,
    THE_VOID,
    SUNFLOWER_PLAINS,
    WINDSWEPT_GRAVELLY_HILLS,
    FLOWER_FOREST,
    ICE_SPIKES,
    OLD_GROWTH_BIRCH_FOREST,
    OLD_GROWTH_SPRUCE_TAIGA,
    WINDSWEPT_SAVANNA,
    ERODED_BADLANDS,
    BAMBOO_JUNGLE,
    SOUL_SAND_VALLEY,
    CRIMSON_FOREST,
    WARPED_FOREST,
    BASALT_DELTAS,
    DRIPSTONE_CAVES,
    LUSH_CAVES,
    DEEP_DARK,
    MEADOW,
    GROVE,
    SNOWY_SLOPES,
    FROZEN_PEAKS,
    JAGGED_PEAKS,
    STONY_PEAKS,
    CHERRY_GROVE,
    /**
     * Represents a custom Biome
     */
    CUSTOM;

    private final NamespacedKey key;

    private Biome() {
        this.key = NamespacedKey.minecraft(name().toLowerCase(Locale.ROOT));
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
