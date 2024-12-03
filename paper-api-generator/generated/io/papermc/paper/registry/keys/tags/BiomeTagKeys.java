package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#BIOME}.
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
public final class BiomeTagKeys {
    /**
     * {@code #minecraft:allows_surface_slime_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> ALLOWS_SURFACE_SLIME_SPAWNS = create(key("allows_surface_slime_spawns"));

    /**
     * {@code #minecraft:allows_tropical_fish_spawns_at_any_height}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT = create(key("allows_tropical_fish_spawns_at_any_height"));

    /**
     * {@code #minecraft:has_closer_water_fog}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_CLOSER_WATER_FOG = create(key("has_closer_water_fog"));

    /**
     * {@code #minecraft:has_structure/ancient_city}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_ANCIENT_CITY = create(key("has_structure/ancient_city"));

    /**
     * {@code #minecraft:has_structure/bastion_remnant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_BASTION_REMNANT = create(key("has_structure/bastion_remnant"));

    /**
     * {@code #minecraft:has_structure/buried_treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_BURIED_TREASURE = create(key("has_structure/buried_treasure"));

    /**
     * {@code #minecraft:has_structure/desert_pyramid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_DESERT_PYRAMID = create(key("has_structure/desert_pyramid"));

    /**
     * {@code #minecraft:has_structure/end_city}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_END_CITY = create(key("has_structure/end_city"));

    /**
     * {@code #minecraft:has_structure/igloo}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_IGLOO = create(key("has_structure/igloo"));

    /**
     * {@code #minecraft:has_structure/jungle_temple}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_JUNGLE_TEMPLE = create(key("has_structure/jungle_temple"));

    /**
     * {@code #minecraft:has_structure/mineshaft}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_MINESHAFT = create(key("has_structure/mineshaft"));

    /**
     * {@code #minecraft:has_structure/mineshaft_mesa}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_MINESHAFT_MESA = create(key("has_structure/mineshaft_mesa"));

    /**
     * {@code #minecraft:has_structure/nether_fortress}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_NETHER_FORTRESS = create(key("has_structure/nether_fortress"));

    /**
     * {@code #minecraft:has_structure/nether_fossil}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_NETHER_FOSSIL = create(key("has_structure/nether_fossil"));

    /**
     * {@code #minecraft:has_structure/ocean_monument}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_OCEAN_MONUMENT = create(key("has_structure/ocean_monument"));

    /**
     * {@code #minecraft:has_structure/ocean_ruin_cold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_OCEAN_RUIN_COLD = create(key("has_structure/ocean_ruin_cold"));

    /**
     * {@code #minecraft:has_structure/ocean_ruin_warm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_OCEAN_RUIN_WARM = create(key("has_structure/ocean_ruin_warm"));

    /**
     * {@code #minecraft:has_structure/pillager_outpost}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_PILLAGER_OUTPOST = create(key("has_structure/pillager_outpost"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_desert}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_DESERT = create(key("has_structure/ruined_portal_desert"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_jungle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_JUNGLE = create(key("has_structure/ruined_portal_jungle"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_mountain}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_MOUNTAIN = create(key("has_structure/ruined_portal_mountain"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_NETHER = create(key("has_structure/ruined_portal_nether"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_OCEAN = create(key("has_structure/ruined_portal_ocean"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_standard}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_STANDARD = create(key("has_structure/ruined_portal_standard"));

    /**
     * {@code #minecraft:has_structure/ruined_portal_swamp}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_SWAMP = create(key("has_structure/ruined_portal_swamp"));

    /**
     * {@code #minecraft:has_structure/shipwreck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_SHIPWRECK = create(key("has_structure/shipwreck"));

    /**
     * {@code #minecraft:has_structure/shipwreck_beached}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_SHIPWRECK_BEACHED = create(key("has_structure/shipwreck_beached"));

    /**
     * {@code #minecraft:has_structure/stronghold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_STRONGHOLD = create(key("has_structure/stronghold"));

    /**
     * {@code #minecraft:has_structure/swamp_hut}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_SWAMP_HUT = create(key("has_structure/swamp_hut"));

    /**
     * {@code #minecraft:has_structure/trail_ruins}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_TRAIL_RUINS = create(key("has_structure/trail_ruins"));

    /**
     * {@code #minecraft:has_structure/trial_chambers}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_TRIAL_CHAMBERS = create(key("has_structure/trial_chambers"));

    /**
     * {@code #minecraft:has_structure/village_desert}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_VILLAGE_DESERT = create(key("has_structure/village_desert"));

    /**
     * {@code #minecraft:has_structure/village_plains}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_VILLAGE_PLAINS = create(key("has_structure/village_plains"));

    /**
     * {@code #minecraft:has_structure/village_savanna}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_VILLAGE_SAVANNA = create(key("has_structure/village_savanna"));

    /**
     * {@code #minecraft:has_structure/village_snowy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_VILLAGE_SNOWY = create(key("has_structure/village_snowy"));

    /**
     * {@code #minecraft:has_structure/village_taiga}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_VILLAGE_TAIGA = create(key("has_structure/village_taiga"));

    /**
     * {@code #minecraft:has_structure/woodland_mansion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> HAS_STRUCTURE_WOODLAND_MANSION = create(key("has_structure/woodland_mansion"));

    /**
     * {@code #minecraft:increased_fire_burnout}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> INCREASED_FIRE_BURNOUT = create(key("increased_fire_burnout"));

    /**
     * {@code #minecraft:is_badlands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_BADLANDS = create(key("is_badlands"));

    /**
     * {@code #minecraft:is_beach}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_BEACH = create(key("is_beach"));

    /**
     * {@code #minecraft:is_deep_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_DEEP_OCEAN = create(key("is_deep_ocean"));

    /**
     * {@code #minecraft:is_end}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_END = create(key("is_end"));

    /**
     * {@code #minecraft:is_forest}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_FOREST = create(key("is_forest"));

    /**
     * {@code #minecraft:is_hill}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_HILL = create(key("is_hill"));

    /**
     * {@code #minecraft:is_jungle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_JUNGLE = create(key("is_jungle"));

    /**
     * {@code #minecraft:is_mountain}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_MOUNTAIN = create(key("is_mountain"));

    /**
     * {@code #minecraft:is_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_NETHER = create(key("is_nether"));

    /**
     * {@code #minecraft:is_ocean}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_OCEAN = create(key("is_ocean"));

    /**
     * {@code #minecraft:is_overworld}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_OVERWORLD = create(key("is_overworld"));

    /**
     * {@code #minecraft:is_river}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_RIVER = create(key("is_river"));

    /**
     * {@code #minecraft:is_savanna}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_SAVANNA = create(key("is_savanna"));

    /**
     * {@code #minecraft:is_taiga}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> IS_TAIGA = create(key("is_taiga"));

    /**
     * {@code #minecraft:mineshaft_blocking}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> MINESHAFT_BLOCKING = create(key("mineshaft_blocking"));

    /**
     * {@code #minecraft:more_frequent_drowned_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> MORE_FREQUENT_DROWNED_SPAWNS = create(key("more_frequent_drowned_spawns"));

    /**
     * {@code #minecraft:plays_underwater_music}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> PLAYS_UNDERWATER_MUSIC = create(key("plays_underwater_music"));

    /**
     * {@code #minecraft:polar_bears_spawn_on_alternate_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS = create(key("polar_bears_spawn_on_alternate_blocks"));

    /**
     * {@code #minecraft:produces_corals_from_bonemeal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> PRODUCES_CORALS_FROM_BONEMEAL = create(key("produces_corals_from_bonemeal"));

    /**
     * {@code #minecraft:reduce_water_ambient_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> REDUCE_WATER_AMBIENT_SPAWNS = create(key("reduce_water_ambient_spawns"));

    /**
     * {@code #minecraft:required_ocean_monument_surrounding}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> REQUIRED_OCEAN_MONUMENT_SURROUNDING = create(key("required_ocean_monument_surrounding"));

    /**
     * {@code #minecraft:snow_golem_melts}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SNOW_GOLEM_MELTS = create(key("snow_golem_melts"));

    /**
     * {@code #minecraft:spawns_cold_variant_frogs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FROGS = create(key("spawns_cold_variant_frogs"));

    /**
     * {@code #minecraft:spawns_gold_rabbits}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SPAWNS_GOLD_RABBITS = create(key("spawns_gold_rabbits"));

    /**
     * {@code #minecraft:spawns_snow_foxes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SPAWNS_SNOW_FOXES = create(key("spawns_snow_foxes"));

    /**
     * {@code #minecraft:spawns_warm_variant_frogs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FROGS = create(key("spawns_warm_variant_frogs"));

    /**
     * {@code #minecraft:spawns_white_rabbits}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> SPAWNS_WHITE_RABBITS = create(key("spawns_white_rabbits"));

    /**
     * {@code #minecraft:stronghold_biased_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> STRONGHOLD_BIASED_TO = create(key("stronghold_biased_to"));

    /**
     * {@code #minecraft:water_on_map_outlines}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> WATER_ON_MAP_OUTLINES = create(key("water_on_map_outlines"));

    /**
     * {@code #minecraft:without_patrol_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> WITHOUT_PATROL_SPAWNS = create(key("without_patrol_spawns"));

    /**
     * {@code #minecraft:without_wandering_trader_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> WITHOUT_WANDERING_TRADER_SPAWNS = create(key("without_wandering_trader_spawns"));

    /**
     * {@code #minecraft:without_zombie_sieges}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Biome> WITHOUT_ZOMBIE_SIEGES = create(key("without_zombie_sieges"));

    private BiomeTagKeys() {
    }

    /**
     * Creates a tag key for {@link Biome} in the registry {@code minecraft:worldgen/biome}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Biome> create(final Key key) {
        return TagKey.create(RegistryKey.BIOME, key);
    }
}
