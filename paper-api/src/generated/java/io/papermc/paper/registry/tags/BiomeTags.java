package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.BiomeTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.block.Biome;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#BIOME}.
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
public final class BiomeTags {
    /**
     * {@code #minecraft:allows_surface_slime_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> ALLOWS_SURFACE_SLIME_SPAWNS = fetch(BiomeTagKeys.ALLOWS_SURFACE_SLIME_SPAWNS);

    /**
     * {@code #minecraft:allows_tropical_fish_spawns_at_any_height}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT = fetch(BiomeTagKeys.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT);

    /**
     * {@code #minecraft:has_closer_water_fog}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_CLOSER_WATER_FOG = fetch(BiomeTagKeys.HAS_CLOSER_WATER_FOG);

    /**
     * {@code #minecraft:has_structure/ancient_city}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_ANCIENT_CITY = fetch(BiomeTagKeys.HAS_STRUCTURE_ANCIENT_CITY);

    /**
     * {@code #minecraft:has_structure/bastion_remnant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_BASTION_REMNANT = fetch(BiomeTagKeys.HAS_STRUCTURE_BASTION_REMNANT);

    /**
     * {@code #minecraft:has_structure/buried_treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_BURIED_TREASURE = fetch(BiomeTagKeys.HAS_STRUCTURE_BURIED_TREASURE);

    /**
     * {@code #minecraft:has_structure/desert_pyramid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_DESERT_PYRAMID = fetch(BiomeTagKeys.HAS_STRUCTURE_DESERT_PYRAMID);

    /**
     * {@code #minecraft:has_structure/end_city}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_END_CITY = fetch(BiomeTagKeys.HAS_STRUCTURE_END_CITY);

    /**
     * {@code #minecraft:has_structure/igloo}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_IGLOO = fetch(BiomeTagKeys.HAS_STRUCTURE_IGLOO);

    /**
     * {@code #minecraft:has_structure/jungle_temple}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_JUNGLE_TEMPLE = fetch(BiomeTagKeys.HAS_STRUCTURE_JUNGLE_TEMPLE);

    /**
     * {@code #minecraft:has_structure/mineshaft}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_MINESHAFT = fetch(BiomeTagKeys.HAS_STRUCTURE_MINESHAFT);

    /**
     * {@code #minecraft:has_structure/mineshaft_mesa}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_MINESHAFT_MESA = fetch(BiomeTagKeys.HAS_STRUCTURE_MINESHAFT_MESA);

    /**
     * {@code #minecraft:has_structure/nether_fortress}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_NETHER_FORTRESS = fetch(BiomeTagKeys.HAS_STRUCTURE_NETHER_FORTRESS);

    /**
     * {@code #minecraft:has_structure/nether_fossil}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_NETHER_FOSSIL = fetch(BiomeTagKeys.HAS_STRUCTURE_NETHER_FOSSIL);

    /**
     * {@code #minecraft:has_structure/ocean_monument}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_OCEAN_MONUMENT = fetch(BiomeTagKeys.HAS_STRUCTURE_OCEAN_MONUMENT);

    /**
     * {@code #minecraft:has_structure/ocean_ruin_cold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_OCEAN_RUIN_COLD = fetch(BiomeTagKeys.HAS_STRUCTURE_OCEAN_RUIN_COLD);

    /**
     * {@code #minecraft:has_structure/ocean_ruin_warm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_OCEAN_RUIN_WARM = fetch(BiomeTagKeys.HAS_STRUCTURE_OCEAN_RUIN_WARM);

    /**
     * {@code #minecraft:has_structure/pillager_outpost}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_PILLAGER_OUTPOST = fetch(BiomeTagKeys.HAS_STRUCTURE_PILLAGER_OUTPOST);

    /**
     * {@code #minecraft:has_structure/ruined_portal_desert}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_DESERT = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_DESERT);

    /**
     * {@code #minecraft:has_structure/ruined_portal_jungle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_JUNGLE = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_JUNGLE);

    /**
     * {@code #minecraft:has_structure/ruined_portal_mountain}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_MOUNTAIN = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_MOUNTAIN);

    /**
     * {@code #minecraft:has_structure/ruined_portal_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_NETHER = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_NETHER);

    /**
     * {@code #minecraft:has_structure/ruined_portal_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_OCEAN = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_OCEAN);

    /**
     * {@code #minecraft:has_structure/ruined_portal_standard}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_STANDARD = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_STANDARD);

    /**
     * {@code #minecraft:has_structure/ruined_portal_swamp}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_RUINED_PORTAL_SWAMP = fetch(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_SWAMP);

    /**
     * {@code #minecraft:has_structure/shipwreck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_SHIPWRECK = fetch(BiomeTagKeys.HAS_STRUCTURE_SHIPWRECK);

    /**
     * {@code #minecraft:has_structure/shipwreck_beached}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_SHIPWRECK_BEACHED = fetch(BiomeTagKeys.HAS_STRUCTURE_SHIPWRECK_BEACHED);

    /**
     * {@code #minecraft:has_structure/stronghold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_STRONGHOLD = fetch(BiomeTagKeys.HAS_STRUCTURE_STRONGHOLD);

    /**
     * {@code #minecraft:has_structure/swamp_hut}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_SWAMP_HUT = fetch(BiomeTagKeys.HAS_STRUCTURE_SWAMP_HUT);

    /**
     * {@code #minecraft:has_structure/trail_ruins}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_TRAIL_RUINS = fetch(BiomeTagKeys.HAS_STRUCTURE_TRAIL_RUINS);

    /**
     * {@code #minecraft:has_structure/trial_chambers}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_TRIAL_CHAMBERS = fetch(BiomeTagKeys.HAS_STRUCTURE_TRIAL_CHAMBERS);

    /**
     * {@code #minecraft:has_structure/village_desert}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_VILLAGE_DESERT = fetch(BiomeTagKeys.HAS_STRUCTURE_VILLAGE_DESERT);

    /**
     * {@code #minecraft:has_structure/village_plains}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_VILLAGE_PLAINS = fetch(BiomeTagKeys.HAS_STRUCTURE_VILLAGE_PLAINS);

    /**
     * {@code #minecraft:has_structure/village_savanna}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_VILLAGE_SAVANNA = fetch(BiomeTagKeys.HAS_STRUCTURE_VILLAGE_SAVANNA);

    /**
     * {@code #minecraft:has_structure/village_snowy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_VILLAGE_SNOWY = fetch(BiomeTagKeys.HAS_STRUCTURE_VILLAGE_SNOWY);

    /**
     * {@code #minecraft:has_structure/village_taiga}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_VILLAGE_TAIGA = fetch(BiomeTagKeys.HAS_STRUCTURE_VILLAGE_TAIGA);

    /**
     * {@code #minecraft:has_structure/woodland_mansion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> HAS_STRUCTURE_WOODLAND_MANSION = fetch(BiomeTagKeys.HAS_STRUCTURE_WOODLAND_MANSION);

    /**
     * {@code #minecraft:increased_fire_burnout}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> INCREASED_FIRE_BURNOUT = fetch(BiomeTagKeys.INCREASED_FIRE_BURNOUT);

    /**
     * {@code #minecraft:is_badlands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_BADLANDS = fetch(BiomeTagKeys.IS_BADLANDS);

    /**
     * {@code #minecraft:is_beach}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_BEACH = fetch(BiomeTagKeys.IS_BEACH);

    /**
     * {@code #minecraft:is_deep_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_DEEP_OCEAN = fetch(BiomeTagKeys.IS_DEEP_OCEAN);

    /**
     * {@code #minecraft:is_end}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_END = fetch(BiomeTagKeys.IS_END);

    /**
     * {@code #minecraft:is_forest}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_FOREST = fetch(BiomeTagKeys.IS_FOREST);

    /**
     * {@code #minecraft:is_hill}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_HILL = fetch(BiomeTagKeys.IS_HILL);

    /**
     * {@code #minecraft:is_jungle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_JUNGLE = fetch(BiomeTagKeys.IS_JUNGLE);

    /**
     * {@code #minecraft:is_mountain}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_MOUNTAIN = fetch(BiomeTagKeys.IS_MOUNTAIN);

    /**
     * {@code #minecraft:is_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_NETHER = fetch(BiomeTagKeys.IS_NETHER);

    /**
     * {@code #minecraft:is_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_OCEAN = fetch(BiomeTagKeys.IS_OCEAN);

    /**
     * {@code #minecraft:is_overworld}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_OVERWORLD = fetch(BiomeTagKeys.IS_OVERWORLD);

    /**
     * {@code #minecraft:is_river}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_RIVER = fetch(BiomeTagKeys.IS_RIVER);

    /**
     * {@code #minecraft:is_savanna}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_SAVANNA = fetch(BiomeTagKeys.IS_SAVANNA);

    /**
     * {@code #minecraft:is_taiga}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> IS_TAIGA = fetch(BiomeTagKeys.IS_TAIGA);

    /**
     * {@code #minecraft:mineshaft_blocking}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> MINESHAFT_BLOCKING = fetch(BiomeTagKeys.MINESHAFT_BLOCKING);

    /**
     * {@code #minecraft:more_frequent_drowned_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> MORE_FREQUENT_DROWNED_SPAWNS = fetch(BiomeTagKeys.MORE_FREQUENT_DROWNED_SPAWNS);

    /**
     * {@code #minecraft:plays_underwater_music}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> PLAYS_UNDERWATER_MUSIC = fetch(BiomeTagKeys.PLAYS_UNDERWATER_MUSIC);

    /**
     * {@code #minecraft:polar_bears_spawn_on_alternate_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS = fetch(BiomeTagKeys.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS);

    /**
     * {@code #minecraft:produces_corals_from_bonemeal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> PRODUCES_CORALS_FROM_BONEMEAL = fetch(BiomeTagKeys.PRODUCES_CORALS_FROM_BONEMEAL);

    /**
     * {@code #minecraft:reduce_water_ambient_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> REDUCE_WATER_AMBIENT_SPAWNS = fetch(BiomeTagKeys.REDUCE_WATER_AMBIENT_SPAWNS);

    /**
     * {@code #minecraft:required_ocean_monument_surrounding}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> REQUIRED_OCEAN_MONUMENT_SURROUNDING = fetch(BiomeTagKeys.REQUIRED_OCEAN_MONUMENT_SURROUNDING);

    /**
     * {@code #minecraft:snow_golem_melts}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SNOW_GOLEM_MELTS = fetch(BiomeTagKeys.SNOW_GOLEM_MELTS);

    /**
     * {@code #minecraft:spawns_cold_variant_farm_animals}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = fetch(BiomeTagKeys.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    /**
     * {@code #minecraft:spawns_cold_variant_frogs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_COLD_VARIANT_FROGS = fetch(BiomeTagKeys.SPAWNS_COLD_VARIANT_FROGS);

    /**
     * {@code #minecraft:spawns_gold_rabbits}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_GOLD_RABBITS = fetch(BiomeTagKeys.SPAWNS_GOLD_RABBITS);

    /**
     * {@code #minecraft:spawns_snow_foxes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_SNOW_FOXES = fetch(BiomeTagKeys.SPAWNS_SNOW_FOXES);

    /**
     * {@code #minecraft:spawns_warm_variant_farm_animals}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = fetch(BiomeTagKeys.SPAWNS_WARM_VARIANT_FARM_ANIMALS);

    /**
     * {@code #minecraft:spawns_warm_variant_frogs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_WARM_VARIANT_FROGS = fetch(BiomeTagKeys.SPAWNS_WARM_VARIANT_FROGS);

    /**
     * {@code #minecraft:spawns_white_rabbits}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> SPAWNS_WHITE_RABBITS = fetch(BiomeTagKeys.SPAWNS_WHITE_RABBITS);

    /**
     * {@code #minecraft:stronghold_biased_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> STRONGHOLD_BIASED_TO = fetch(BiomeTagKeys.STRONGHOLD_BIASED_TO);

    /**
     * {@code #minecraft:water_on_map_outlines}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> WATER_ON_MAP_OUTLINES = fetch(BiomeTagKeys.WATER_ON_MAP_OUTLINES);

    /**
     * {@code #minecraft:without_patrol_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> WITHOUT_PATROL_SPAWNS = fetch(BiomeTagKeys.WITHOUT_PATROL_SPAWNS);

    /**
     * {@code #minecraft:without_wandering_trader_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> WITHOUT_WANDERING_TRADER_SPAWNS = fetch(BiomeTagKeys.WITHOUT_WANDERING_TRADER_SPAWNS);

    /**
     * {@code #minecraft:without_zombie_sieges}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Biome> WITHOUT_ZOMBIE_SIEGES = fetch(BiomeTagKeys.WITHOUT_ZOMBIE_SIEGES);

    private BiomeTags() {
    }

    private static Tag<Biome> fetch(final TagKey<Biome> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).getTag(tagKey);
    }
}
