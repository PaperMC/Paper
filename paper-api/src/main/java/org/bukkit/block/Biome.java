package org.bukkit.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.FeatureFlag;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Holds all accepted Biomes in the server.
 * <p>
 * The Biomes listed in this interface are present in the default server
 * or can be enabled via a {@link FeatureFlag}.
 * There may be additional biomes present in the server, for example from a {@link io.papermc.paper.datapack.Datapack}
 * which can be accessed via {@link io.papermc.paper.registry.RegistryAccess#getRegistry(RegistryKey)} and {@link RegistryKey#BIOME}.
 */
public interface Biome extends OldEnum<Biome>, Keyed, net.kyori.adventure.translation.Translatable { // Paper - Adventure translations

    // Start generate - Biome
    // @GeneratedFrom 1.21.6-pre1
    Biome BADLANDS = getBiome("badlands");

    Biome BAMBOO_JUNGLE = getBiome("bamboo_jungle");

    Biome BASALT_DELTAS = getBiome("basalt_deltas");

    Biome BEACH = getBiome("beach");

    Biome BIRCH_FOREST = getBiome("birch_forest");

    Biome CHERRY_GROVE = getBiome("cherry_grove");

    Biome COLD_OCEAN = getBiome("cold_ocean");

    Biome CRIMSON_FOREST = getBiome("crimson_forest");

    Biome DARK_FOREST = getBiome("dark_forest");

    Biome DEEP_COLD_OCEAN = getBiome("deep_cold_ocean");

    Biome DEEP_DARK = getBiome("deep_dark");

    Biome DEEP_FROZEN_OCEAN = getBiome("deep_frozen_ocean");

    Biome DEEP_LUKEWARM_OCEAN = getBiome("deep_lukewarm_ocean");

    Biome DEEP_OCEAN = getBiome("deep_ocean");

    Biome DESERT = getBiome("desert");

    Biome DRIPSTONE_CAVES = getBiome("dripstone_caves");

    Biome END_BARRENS = getBiome("end_barrens");

    Biome END_HIGHLANDS = getBiome("end_highlands");

    Biome END_MIDLANDS = getBiome("end_midlands");

    Biome ERODED_BADLANDS = getBiome("eroded_badlands");

    Biome FLOWER_FOREST = getBiome("flower_forest");

    Biome FOREST = getBiome("forest");

    Biome FROZEN_OCEAN = getBiome("frozen_ocean");

    Biome FROZEN_PEAKS = getBiome("frozen_peaks");

    Biome FROZEN_RIVER = getBiome("frozen_river");

    Biome GROVE = getBiome("grove");

    Biome ICE_SPIKES = getBiome("ice_spikes");

    Biome JAGGED_PEAKS = getBiome("jagged_peaks");

    Biome JUNGLE = getBiome("jungle");

    Biome LUKEWARM_OCEAN = getBiome("lukewarm_ocean");

    Biome LUSH_CAVES = getBiome("lush_caves");

    Biome MANGROVE_SWAMP = getBiome("mangrove_swamp");

    Biome MEADOW = getBiome("meadow");

    Biome MUSHROOM_FIELDS = getBiome("mushroom_fields");

    Biome NETHER_WASTES = getBiome("nether_wastes");

    Biome OCEAN = getBiome("ocean");

    Biome OLD_GROWTH_BIRCH_FOREST = getBiome("old_growth_birch_forest");

    Biome OLD_GROWTH_PINE_TAIGA = getBiome("old_growth_pine_taiga");

    Biome OLD_GROWTH_SPRUCE_TAIGA = getBiome("old_growth_spruce_taiga");

    Biome PALE_GARDEN = getBiome("pale_garden");

    Biome PLAINS = getBiome("plains");

    Biome RIVER = getBiome("river");

    Biome SAVANNA = getBiome("savanna");

    Biome SAVANNA_PLATEAU = getBiome("savanna_plateau");

    Biome SMALL_END_ISLANDS = getBiome("small_end_islands");

    Biome SNOWY_BEACH = getBiome("snowy_beach");

    Biome SNOWY_PLAINS = getBiome("snowy_plains");

    Biome SNOWY_SLOPES = getBiome("snowy_slopes");

    Biome SNOWY_TAIGA = getBiome("snowy_taiga");

    Biome SOUL_SAND_VALLEY = getBiome("soul_sand_valley");

    Biome SPARSE_JUNGLE = getBiome("sparse_jungle");

    Biome STONY_PEAKS = getBiome("stony_peaks");

    Biome STONY_SHORE = getBiome("stony_shore");

    Biome SUNFLOWER_PLAINS = getBiome("sunflower_plains");

    Biome SWAMP = getBiome("swamp");

    Biome TAIGA = getBiome("taiga");

    Biome THE_END = getBiome("the_end");

    Biome THE_VOID = getBiome("the_void");

    Biome WARM_OCEAN = getBiome("warm_ocean");

    Biome WARPED_FOREST = getBiome("warped_forest");

    Biome WINDSWEPT_FOREST = getBiome("windswept_forest");

    Biome WINDSWEPT_GRAVELLY_HILLS = getBiome("windswept_gravelly_hills");

    Biome WINDSWEPT_HILLS = getBiome("windswept_hills");

    Biome WINDSWEPT_SAVANNA = getBiome("windswept_savanna");

    Biome WOODED_BADLANDS = getBiome("wooded_badlands");
    // End generate - Biome

    /**
     * Represents a custom Biome
     *
     * @deprecated Biome is no longer an enum, custom biomes will have their own biome instance.
     */
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    Biome CUSTOM = InternalAPIBridge.get().constructLegacyCustomBiome();

    @NotNull
    private static Biome getBiome(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * @param name of the biome.
     * @return the biome with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Biome valueOf(@NotNull String name) {
        if ("CUSTOM".equals(name)) {
            return Biome.CUSTOM;
        }

        Biome biome = Bukkit.getUnsafe().get(RegistryKey.BIOME, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(biome != null, "No biome found with the name %s", name);
        return biome;
    }

    /**
     * @return an array of all known biomes.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Biome[] values() {
        return Lists.newArrayList(Registry.BIOME).toArray(new Biome[0]);
    }

    // Paper start
    @Override
    default @NotNull String translationKey() {
        return "biome.minecraft." + this.getKey().getKey();
    }
    // Paper end
}
