package org.bukkit;

import java.util.Set;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a tag that may be defined by the server or a resource pack to
 * group like things together.
 *
 * Note that whilst all tags defined within this interface must be present in
 * implementations, their existence is not guaranteed across future versions.
 *
 * <p>Custom tags defined by Paper are not present (as constants) in this class.
 * To access them please refer to {@link com.destroystokyo.paper.MaterialTags}
 * and {@link io.papermc.paper.tag.EntityTags}.</p>
 *
 * @param <T> the type of things grouped by this tag
 */
public interface Tag<T extends Keyed> extends Keyed {

    // Start generate - Tag
    // @GeneratedFrom 1.21.6-pre1
    String REGISTRY_BLOCKS = "blocks";

    Tag<Material> ACACIA_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("acacia_logs"), Material.class);

    Tag<Material> AIR = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("air"), Material.class);

    Tag<Material> ALL_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("all_hanging_signs"), Material.class);

    Tag<Material> ALL_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("all_signs"), Material.class);

    Tag<Material> ANCIENT_CITY_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ancient_city_replaceable"), Material.class);

    Tag<Material> ANIMALS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("animals_spawnable_on"), Material.class);

    Tag<Material> ANVIL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("anvil"), Material.class);

    Tag<Material> ARMADILLO_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("armadillo_spawnable_on"), Material.class);

    Tag<Material> AXOLOTLS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("axolotls_spawnable_on"), Material.class);

    Tag<Material> AZALEA_GROWS_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("azalea_grows_on"), Material.class);

    Tag<Material> AZALEA_ROOT_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("azalea_root_replaceable"), Material.class);

    Tag<Material> BADLANDS_TERRACOTTA = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("badlands_terracotta"), Material.class);

    Tag<Material> BAMBOO_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bamboo_blocks"), Material.class);

    Tag<Material> BAMBOO_PLANTABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bamboo_plantable_on"), Material.class);

    Tag<Material> BANNERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("banners"), Material.class);

    Tag<Material> BASE_STONE_NETHER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("base_stone_nether"), Material.class);

    Tag<Material> BASE_STONE_OVERWORLD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("base_stone_overworld"), Material.class);

    Tag<Material> BATS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bats_spawnable_on"), Material.class);

    Tag<Material> BEACON_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beacon_base_blocks"), Material.class);

    Tag<Material> BEDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beds"), Material.class);

    Tag<Material> BEE_ATTRACTIVE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bee_attractive"), Material.class);

    Tag<Material> BEE_GROWABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bee_growables"), Material.class);

    Tag<Material> BEEHIVES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beehives"), Material.class);

    Tag<Material> BIG_DRIPLEAF_PLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("big_dripleaf_placeable"), Material.class);

    Tag<Material> BIRCH_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("birch_logs"), Material.class);

    Tag<Material> BLOCKS_WIND_CHARGE_EXPLOSIONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("blocks_wind_charge_explosions"), Material.class);

    Tag<Material> BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("buttons"), Material.class);

    Tag<Material> CAMEL_SAND_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("camel_sand_step_sound_blocks"), Material.class);

    Tag<Material> CAMELS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("camels_spawnable_on"), Material.class);

    Tag<Material> CAMPFIRES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("campfires"), Material.class);

    Tag<Material> CANDLE_CAKES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("candle_cakes"), Material.class);

    Tag<Material> CANDLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("candles"), Material.class);

    Tag<Material> CAULDRONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cauldrons"), Material.class);

    Tag<Material> CAVE_VINES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cave_vines"), Material.class);

    Tag<Material> CEILING_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ceiling_hanging_signs"), Material.class);

    Tag<Material> CHERRY_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cherry_logs"), Material.class);

    Tag<Material> CLIMBABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("climbable"), Material.class);

    Tag<Material> COAL_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coal_ores"), Material.class);

    Tag<Material> COMBINATION_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("combination_step_sound_blocks"), Material.class);

    Tag<Material> COMPLETES_FIND_TREE_TUTORIAL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("completes_find_tree_tutorial"), Material.class);

    Tag<Material> CONCRETE_POWDER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("concrete_powder"), Material.class);

    Tag<Material> CONVERTABLE_TO_MUD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("convertable_to_mud"), Material.class);

    Tag<Material> COPPER_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("copper_ores"), Material.class);

    Tag<Material> CORAL_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_blocks"), Material.class);

    Tag<Material> CORAL_PLANTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_plants"), Material.class);

    Tag<Material> CORALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("corals"), Material.class);

    Tag<Material> CRIMSON_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crimson_stems"), Material.class);

    Tag<Material> CROPS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crops"), Material.class);

    Tag<Material> CRYSTAL_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crystal_sound_blocks"), Material.class);

    Tag<Material> DAMPENS_VIBRATIONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dampens_vibrations"), Material.class);

    Tag<Material> DARK_OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dark_oak_logs"), Material.class);

    Tag<Material> DEEPSLATE_ORE_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("deepslate_ore_replaceables"), Material.class);

    Tag<Material> DIAMOND_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("diamond_ores"), Material.class);

    Tag<Material> DIRT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dirt"), Material.class);

    Tag<Material> DOES_NOT_BLOCK_HOPPERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("does_not_block_hoppers"), Material.class);

    Tag<Material> DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("doors"), Material.class);

    Tag<Material> DRAGON_IMMUNE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dragon_immune"), Material.class);

    Tag<Material> DRAGON_TRANSPARENT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dragon_transparent"), Material.class);

    Tag<Material> DRIPSTONE_REPLACEABLE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dripstone_replaceable_blocks"), Material.class);

    Tag<Material> DRY_VEGETATION_MAY_PLACE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dry_vegetation_may_place_on"), Material.class);

    Tag<Material> EDIBLE_FOR_SHEEP = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("edible_for_sheep"), Material.class);

    Tag<Material> EMERALD_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("emerald_ores"), Material.class);

    Tag<Material> ENCHANTMENT_POWER_PROVIDER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enchantment_power_provider"), Material.class);

    Tag<Material> ENCHANTMENT_POWER_TRANSMITTER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enchantment_power_transmitter"), Material.class);

    Tag<Material> ENDERMAN_HOLDABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enderman_holdable"), Material.class);

    Tag<Material> FALL_DAMAGE_RESETTING = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fall_damage_resetting"), Material.class);

    Tag<Material> FEATURES_CANNOT_REPLACE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("features_cannot_replace"), Material.class);

    Tag<Material> FENCE_GATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fence_gates"), Material.class);

    Tag<Material> FENCES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fences"), Material.class);

    Tag<Material> FIRE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fire"), Material.class);

    Tag<Material> FLOWER_POTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flower_pots"), Material.class);

    Tag<Material> FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flowers"), Material.class);

    Tag<Material> FOXES_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("foxes_spawnable_on"), Material.class);

    Tag<Material> FROG_PREFER_JUMP_TO = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("frog_prefer_jump_to"), Material.class);

    Tag<Material> FROGS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("frogs_spawnable_on"), Material.class);

    Tag<Material> GEODE_INVALID_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("geode_invalid_blocks"), Material.class);

    Tag<Material> GOATS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("goats_spawnable_on"), Material.class);

    Tag<Material> GOLD_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("gold_ores"), Material.class);

    Tag<Material> GUARDED_BY_PIGLINS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("guarded_by_piglins"), Material.class);

    Tag<Material> HAPPY_GHAST_AVOIDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("happy_ghast_avoids"), Material.class);

    Tag<Material> HOGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("hoglin_repellents"), Material.class);

    Tag<Material> ICE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ice"), Material.class);

    Tag<Material> IMPERMEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("impermeable"), Material.class);

    Tag<Material> INCORRECT_FOR_DIAMOND_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_diamond_tool"), Material.class);

    Tag<Material> INCORRECT_FOR_GOLD_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_gold_tool"), Material.class);

    Tag<Material> INCORRECT_FOR_IRON_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_iron_tool"), Material.class);

    Tag<Material> INCORRECT_FOR_NETHERITE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_netherite_tool"), Material.class);

    Tag<Material> INCORRECT_FOR_STONE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_stone_tool"), Material.class);

    Tag<Material> INCORRECT_FOR_WOODEN_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_wooden_tool"), Material.class);

    Tag<Material> INFINIBURN_END = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_end"), Material.class);

    Tag<Material> INFINIBURN_NETHER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_nether"), Material.class);

    Tag<Material> INFINIBURN_OVERWORLD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_overworld"), Material.class);

    Tag<Material> INSIDE_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("inside_step_sound_blocks"), Material.class);

    Tag<Material> INVALID_SPAWN_INSIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("invalid_spawn_inside"), Material.class);

    Tag<Material> IRON_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("iron_ores"), Material.class);

    Tag<Material> JUNGLE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("jungle_logs"), Material.class);

    Tag<Material> LAPIS_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lapis_ores"), Material.class);

    Tag<Material> LAVA_POOL_STONE_CANNOT_REPLACE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lava_pool_stone_cannot_replace"), Material.class);

    Tag<Material> LEAVES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("leaves"), Material.class);

    Tag<Material> LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("logs"), Material.class);

    Tag<Material> LOGS_THAT_BURN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("logs_that_burn"), Material.class);

    Tag<Material> LUSH_GROUND_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lush_ground_replaceable"), Material.class);

    Tag<Material> MAINTAINS_FARMLAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("maintains_farmland"), Material.class);

    Tag<Material> MANGROVE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_logs"), Material.class);

    Tag<Material> MANGROVE_LOGS_CAN_GROW_THROUGH = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_logs_can_grow_through"), Material.class);

    Tag<Material> MANGROVE_ROOTS_CAN_GROW_THROUGH = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_roots_can_grow_through"), Material.class);

    Tag<Material> MINEABLE_AXE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/axe"), Material.class);

    Tag<Material> MINEABLE_HOE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/hoe"), Material.class);

    Tag<Material> MINEABLE_PICKAXE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/pickaxe"), Material.class);

    Tag<Material> MINEABLE_SHOVEL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/shovel"), Material.class);

    Tag<Material> MOB_INTERACTABLE_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mob_interactable_doors"), Material.class);

    Tag<Material> MOOSHROOMS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mooshrooms_spawnable_on"), Material.class);

    Tag<Material> MOSS_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("moss_replaceable"), Material.class);

    Tag<Material> MUSHROOM_GROW_BLOCK = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mushroom_grow_block"), Material.class);

    Tag<Material> NEEDS_DIAMOND_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_diamond_tool"), Material.class);

    Tag<Material> NEEDS_IRON_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_iron_tool"), Material.class);

    Tag<Material> NEEDS_STONE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_stone_tool"), Material.class);

    Tag<Material> NETHER_CARVER_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("nether_carver_replaceables"), Material.class);

    Tag<Material> NYLIUM = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("nylium"), Material.class);

    Tag<Material> OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("oak_logs"), Material.class);

    Tag<Material> OCCLUDES_VIBRATION_SIGNALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("occludes_vibration_signals"), Material.class);

    Tag<Material> OVERWORLD_CARVER_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("overworld_carver_replaceables"), Material.class);

    Tag<Material> OVERWORLD_NATURAL_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("overworld_natural_logs"), Material.class);

    Tag<Material> PALE_OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("pale_oak_logs"), Material.class);

    Tag<Material> PARROTS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("parrots_spawnable_on"), Material.class);

    Tag<Material> PIGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("piglin_repellents"), Material.class);

    Tag<Material> PLANKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("planks"), Material.class);

    Tag<Material> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("polar_bears_spawnable_on_alternate"), Material.class);

    Tag<Material> PORTALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("portals"), Material.class);

    Tag<Material> PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("pressure_plates"), Material.class);

    Tag<Material> PREVENT_MOB_SPAWNING_INSIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("prevent_mob_spawning_inside"), Material.class);

    Tag<Material> RABBITS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("rabbits_spawnable_on"), Material.class);

    Tag<Material> RAILS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("rails"), Material.class);

    Tag<Material> REDSTONE_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("redstone_ores"), Material.class);

    Tag<Material> REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("replaceable"), Material.class);

    Tag<Material> REPLACEABLE_BY_MUSHROOMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("replaceable_by_mushrooms"), Material.class);

    Tag<Material> REPLACEABLE_BY_TREES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("replaceable_by_trees"), Material.class);

    Tag<Material> SAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sand"), Material.class);

    Tag<Material> SAPLINGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("saplings"), Material.class);

    Tag<Material> SCULK_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sculk_replaceable"), Material.class);

    Tag<Material> SCULK_REPLACEABLE_WORLD_GEN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sculk_replaceable_world_gen"), Material.class);

    Tag<Material> SHULKER_BOXES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("shulker_boxes"), Material.class);

    Tag<Material> SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("signs"), Material.class);

    Tag<Material> SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("slabs"), Material.class);

    Tag<Material> SMALL_DRIPLEAF_PLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("small_dripleaf_placeable"), Material.class);

    Tag<Material> SMALL_FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("small_flowers"), Material.class);

    Tag<Material> SMELTS_TO_GLASS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("smelts_to_glass"), Material.class);

    Tag<Material> SNAPS_GOAT_HORN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snaps_goat_horn"), Material.class);

    Tag<Material> SNIFFER_DIGGABLE_BLOCK = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sniffer_diggable_block"), Material.class);

    Tag<Material> SNIFFER_EGG_HATCH_BOOST = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sniffer_egg_hatch_boost"), Material.class);

    Tag<Material> SNOW = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow"), Material.class);

    Tag<Material> SNOW_LAYER_CAN_SURVIVE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow_layer_can_survive_on"), Material.class);

    Tag<Material> SNOW_LAYER_CANNOT_SURVIVE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow_layer_cannot_survive_on"), Material.class);

    Tag<Material> SOUL_FIRE_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("soul_fire_base_blocks"), Material.class);

    Tag<Material> SOUL_SPEED_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("soul_speed_blocks"), Material.class);

    Tag<Material> SPRUCE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("spruce_logs"), Material.class);

    Tag<Material> STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stairs"), Material.class);

    Tag<Material> STANDING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("standing_signs"), Material.class);

    Tag<Material> STONE_BRICKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_bricks"), Material.class);

    Tag<Material> STONE_BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_buttons"), Material.class);

    Tag<Material> STONE_ORE_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_ore_replaceables"), Material.class);

    Tag<Material> STONE_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_pressure_plates"), Material.class);

    Tag<Material> STRIDER_WARM_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("strider_warm_blocks"), Material.class);

    Tag<Material> SWORD_EFFICIENT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sword_efficient"), Material.class);

    Tag<Material> SWORD_INSTANTLY_MINES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sword_instantly_mines"), Material.class);

    Tag<Material> TERRACOTTA = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("terracotta"), Material.class);

    Tag<Material> TRAIL_RUINS_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("trail_ruins_replaceable"), Material.class);

    Tag<Material> TRAPDOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("trapdoors"), Material.class);

    Tag<Material> TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("triggers_ambient_desert_dry_vegetation_block_sounds"), Material.class);

    Tag<Material> TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("triggers_ambient_desert_sand_block_sounds"), Material.class);

    Tag<Material> TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("triggers_ambient_dried_ghast_block_sounds"), Material.class);

    Tag<Material> UNDERWATER_BONEMEALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("underwater_bonemeals"), Material.class);

    Tag<Material> UNSTABLE_BOTTOM_CENTER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("unstable_bottom_center"), Material.class);

    Tag<Material> VALID_SPAWN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("valid_spawn"), Material.class);

    Tag<Material> VIBRATION_RESONATORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("vibration_resonators"), Material.class);

    Tag<Material> WALL_CORALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_corals"), Material.class);

    Tag<Material> WALL_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_hanging_signs"), Material.class);

    Tag<Material> WALL_POST_OVERRIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_post_override"), Material.class);

    Tag<Material> WALL_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_signs"), Material.class);

    Tag<Material> WALLS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("walls"), Material.class);

    Tag<Material> WARPED_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("warped_stems"), Material.class);

    Tag<Material> WART_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wart_blocks"), Material.class);

    Tag<Material> WITHER_IMMUNE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wither_immune"), Material.class);

    Tag<Material> WITHER_SUMMON_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wither_summon_base_blocks"), Material.class);

    Tag<Material> WOLVES_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wolves_spawnable_on"), Material.class);

    Tag<Material> WOODEN_BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_buttons"), Material.class);

    Tag<Material> WOODEN_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_doors"), Material.class);

    Tag<Material> WOODEN_FENCES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_fences"), Material.class);

    Tag<Material> WOODEN_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_pressure_plates"), Material.class);

    Tag<Material> WOODEN_SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_slabs"), Material.class);

    Tag<Material> WOODEN_STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_stairs"), Material.class);

    Tag<Material> WOODEN_TRAPDOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_trapdoors"), Material.class);

    Tag<Material> WOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wool"), Material.class);

    Tag<Material> WOOL_CARPETS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wool_carpets"), Material.class);

    String REGISTRY_ITEMS = "items";

    Tag<Material> ITEMS_ACACIA_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("acacia_logs"), Material.class);

    Tag<Material> ITEMS_ANVIL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("anvil"), Material.class);

    Tag<Material> ITEMS_ARMADILLO_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("armadillo_food"), Material.class);

    Tag<Material> ITEMS_ARROWS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("arrows"), Material.class);

    Tag<Material> ITEMS_AXES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("axes"), Material.class);

    Tag<Material> ITEMS_AXOLOTL_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("axolotl_food"), Material.class);

    Tag<Material> ITEMS_BAMBOO_BLOCKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bamboo_blocks"), Material.class);

    Tag<Material> ITEMS_BANNERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("banners"), Material.class);

    Tag<Material> ITEMS_BEACON_PAYMENT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("beacon_payment_items"), Material.class);

    Tag<Material> ITEMS_BEDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("beds"), Material.class);

    Tag<Material> ITEMS_BEE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bee_food"), Material.class);

    Tag<Material> ITEMS_BIRCH_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("birch_logs"), Material.class);

    Tag<Material> ITEMS_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("boats"), Material.class);

    Tag<Material> ITEMS_BOOK_CLONING_TARGET = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("book_cloning_target"), Material.class);

    Tag<Material> ITEMS_BOOKSHELF_BOOKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bookshelf_books"), Material.class);

    Tag<Material> ITEMS_BREAKS_DECORATED_POTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("breaks_decorated_pots"), Material.class);

    Tag<Material> ITEMS_BREWING_FUEL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("brewing_fuel"), Material.class);

    Tag<Material> ITEMS_BUNDLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bundles"), Material.class);

    Tag<Material> ITEMS_BUTTONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("buttons"), Material.class);

    Tag<Material> ITEMS_CAMEL_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("camel_food"), Material.class);

    Tag<Material> ITEMS_CANDLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("candles"), Material.class);

    Tag<Material> ITEMS_CAT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cat_food"), Material.class);

    Tag<Material> ITEMS_CHERRY_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cherry_logs"), Material.class);

    Tag<Material> ITEMS_CHEST_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chest_armor"), Material.class);

    Tag<Material> ITEMS_CHEST_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chest_boats"), Material.class);

    Tag<Material> ITEMS_CHICKEN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chicken_food"), Material.class);

    Tag<Material> ITEMS_CLUSTER_MAX_HARVESTABLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cluster_max_harvestables"), Material.class);

    Tag<Material> ITEMS_COAL_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("coal_ores"), Material.class);

    Tag<Material> ITEMS_COALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("coals"), Material.class);

    Tag<Material> ITEMS_COMPASSES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("compasses"), Material.class);

    Tag<Material> ITEMS_COMPLETES_FIND_TREE_TUTORIAL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("completes_find_tree_tutorial"), Material.class);

    Tag<Material> ITEMS_COPPER_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("copper_ores"), Material.class);

    Tag<Material> ITEMS_COW_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cow_food"), Material.class);

    Tag<Material> ITEMS_CREEPER_DROP_MUSIC_DISCS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("creeper_drop_music_discs"), Material.class);

    Tag<Material> ITEMS_CREEPER_IGNITERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("creeper_igniters"), Material.class);

    Tag<Material> ITEMS_CRIMSON_STEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("crimson_stems"), Material.class);

    Tag<Material> ITEMS_DAMPENS_VIBRATIONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("dampens_vibrations"), Material.class);

    Tag<Material> ITEMS_DARK_OAK_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("dark_oak_logs"), Material.class);

    Tag<Material> ITEMS_DECORATED_POT_INGREDIENTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("decorated_pot_ingredients"), Material.class);

    Tag<Material> ITEMS_DECORATED_POT_SHERDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("decorated_pot_sherds"), Material.class);

    Tag<Material> ITEMS_DIAMOND_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("diamond_ores"), Material.class);

    Tag<Material> ITEMS_DIAMOND_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("diamond_tool_materials"), Material.class);

    Tag<Material> ITEMS_DIRT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("dirt"), Material.class);

    Tag<Material> ITEMS_DOORS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("doors"), Material.class);

    Tag<Material> ITEMS_DROWNED_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("drowned_preferred_weapons"), Material.class);

    Tag<Material> ITEMS_DUPLICATES_ALLAYS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("duplicates_allays"), Material.class);

    Tag<Material> ITEMS_DYEABLE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("dyeable"), Material.class);

    Tag<Material> ITEMS_EGGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("eggs"), Material.class);

    Tag<Material> ITEMS_EMERALD_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("emerald_ores"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/armor"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_BOW = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/bow"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_CHEST_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/chest_armor"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_CROSSBOW = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/crossbow"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_DURABILITY = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/durability"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_EQUIPPABLE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/equippable"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_FIRE_ASPECT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/fire_aspect"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_FISHING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/fishing"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_FOOT_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/foot_armor"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_HEAD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/head_armor"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_LEG_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/leg_armor"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_MACE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mace"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_MINING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mining"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_MINING_LOOT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mining_loot"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_SHARP_WEAPON = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/sharp_weapon"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_SWORD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/sword"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_TRIDENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/trident"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_VANISHING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/vanishing"), Material.class);

    Tag<Material> ITEMS_ENCHANTABLE_WEAPON = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/weapon"), Material.class);

    Tag<Material> ITEMS_FENCE_GATES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fence_gates"), Material.class);

    Tag<Material> ITEMS_FENCES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fences"), Material.class);

    Tag<Material> ITEMS_FISHES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fishes"), Material.class);

    Tag<Material> ITEMS_FLOWERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("flowers"), Material.class);

    Tag<Material> ITEMS_FOOT_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("foot_armor"), Material.class);

    Tag<Material> ITEMS_FOX_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fox_food"), Material.class);

    Tag<Material> ITEMS_FREEZE_IMMUNE_WEARABLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("freeze_immune_wearables"), Material.class);

    Tag<Material> ITEMS_FROG_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("frog_food"), Material.class);

    Tag<Material> ITEMS_FURNACE_MINECART_FUEL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("furnace_minecart_fuel"), Material.class);

    Tag<Material> ITEMS_GAZE_DISGUISE_EQUIPMENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("gaze_disguise_equipment"), Material.class);

    Tag<Material> ITEMS_GOAT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("goat_food"), Material.class);

    Tag<Material> ITEMS_GOLD_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("gold_ores"), Material.class);

    Tag<Material> ITEMS_GOLD_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("gold_tool_materials"), Material.class);

    Tag<Material> ITEMS_HANGING_SIGNS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hanging_signs"), Material.class);

    Tag<Material> ITEMS_HAPPY_GHAST_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("happy_ghast_food"), Material.class);

    Tag<Material> ITEMS_HAPPY_GHAST_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("happy_ghast_tempt_items"), Material.class);

    Tag<Material> ITEMS_HARNESSES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("harnesses"), Material.class);

    Tag<Material> ITEMS_HEAD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("head_armor"), Material.class);

    Tag<Material> ITEMS_HOES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hoes"), Material.class);

    Tag<Material> ITEMS_HOGLIN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hoglin_food"), Material.class);

    Tag<Material> ITEMS_HORSE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("horse_food"), Material.class);

    Tag<Material> ITEMS_HORSE_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("horse_tempt_items"), Material.class);

    Tag<Material> ITEMS_IGNORED_BY_PIGLIN_BABIES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("ignored_by_piglin_babies"), Material.class);

    Tag<Material> ITEMS_IRON_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("iron_ores"), Material.class);

    Tag<Material> ITEMS_IRON_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("iron_tool_materials"), Material.class);

    Tag<Material> ITEMS_JUNGLE_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("jungle_logs"), Material.class);

    Tag<Material> ITEMS_LAPIS_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("lapis_ores"), Material.class);

    Tag<Material> ITEMS_LEAVES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("leaves"), Material.class);

    Tag<Material> ITEMS_LECTERN_BOOKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("lectern_books"), Material.class);

    Tag<Material> ITEMS_LEG_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("leg_armor"), Material.class);

    Tag<Material> ITEMS_LLAMA_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("llama_food"), Material.class);

    Tag<Material> ITEMS_LLAMA_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("llama_tempt_items"), Material.class);

    Tag<Material> ITEMS_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("logs"), Material.class);

    Tag<Material> ITEMS_LOGS_THAT_BURN = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("logs_that_burn"), Material.class);

    Tag<Material> ITEMS_MANGROVE_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("mangrove_logs"), Material.class);

    Tag<Material> ITEMS_MAP_INVISIBILITY_EQUIPMENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("map_invisibility_equipment"), Material.class);

    Tag<Material> ITEMS_MEAT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("meat"), Material.class);

    Tag<Material> ITEMS_NETHERITE_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("netherite_tool_materials"), Material.class);

    Tag<Material> ITEMS_NON_FLAMMABLE_WOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("non_flammable_wood"), Material.class);

    Tag<Material> ITEMS_NOTEBLOCK_TOP_INSTRUMENTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("noteblock_top_instruments"), Material.class);

    Tag<Material> ITEMS_OAK_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("oak_logs"), Material.class);

    Tag<Material> ITEMS_OCELOT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("ocelot_food"), Material.class);

    Tag<Material> ITEMS_PALE_OAK_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pale_oak_logs"), Material.class);

    Tag<Material> ITEMS_PANDA_EATS_FROM_GROUND = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("panda_eats_from_ground"), Material.class);

    Tag<Material> ITEMS_PANDA_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("panda_food"), Material.class);

    Tag<Material> ITEMS_PARROT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("parrot_food"), Material.class);

    Tag<Material> ITEMS_PARROT_POISONOUS_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("parrot_poisonous_food"), Material.class);

    Tag<Material> ITEMS_PICKAXES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pickaxes"), Material.class);

    Tag<Material> ITEMS_PIG_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pig_food"), Material.class);

    Tag<Material> ITEMS_PIGLIN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_food"), Material.class);

    Tag<Material> ITEMS_PIGLIN_LOVED = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_loved"), Material.class);

    Tag<Material> ITEMS_PIGLIN_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_preferred_weapons"), Material.class);

    Tag<Material> ITEMS_PIGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_repellents"), Material.class);

    Tag<Material> ITEMS_PIGLIN_SAFE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_safe_armor"), Material.class);

    Tag<Material> ITEMS_PILLAGER_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pillager_preferred_weapons"), Material.class);

    Tag<Material> ITEMS_PLANKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("planks"), Material.class);

    Tag<Material> ITEMS_RABBIT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("rabbit_food"), Material.class);

    Tag<Material> ITEMS_RAILS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("rails"), Material.class);

    Tag<Material> ITEMS_REDSTONE_ORES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("redstone_ores"), Material.class);

    Tag<Material> ITEMS_REPAIRS_CHAIN_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_chain_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_DIAMOND_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_diamond_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_GOLD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_gold_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_IRON_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_iron_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_LEATHER_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_leather_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_NETHERITE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_netherite_armor"), Material.class);

    Tag<Material> ITEMS_REPAIRS_TURTLE_HELMET = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_turtle_helmet"), Material.class);

    Tag<Material> ITEMS_REPAIRS_WOLF_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_wolf_armor"), Material.class);

    Tag<Material> ITEMS_SAND = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("sand"), Material.class);

    Tag<Material> ITEMS_SAPLINGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("saplings"), Material.class);

    Tag<Material> ITEMS_SHEEP_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("sheep_food"), Material.class);

    Tag<Material> ITEMS_SHOVELS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("shovels"), Material.class);

    Tag<Material> ITEMS_SHULKER_BOXES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("shulker_boxes"), Material.class);

    Tag<Material> ITEMS_SIGNS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("signs"), Material.class);

    Tag<Material> ITEMS_SKELETON_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("skeleton_preferred_weapons"), Material.class);

    Tag<Material> ITEMS_SKULLS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("skulls"), Material.class);

    Tag<Material> ITEMS_SLABS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("slabs"), Material.class);

    Tag<Material> ITEMS_SMALL_FLOWERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("small_flowers"), Material.class);

    Tag<Material> ITEMS_SMELTS_TO_GLASS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("smelts_to_glass"), Material.class);

    Tag<Material> ITEMS_SNIFFER_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("sniffer_food"), Material.class);

    Tag<Material> ITEMS_SOUL_FIRE_BASE_BLOCKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("soul_fire_base_blocks"), Material.class);

    Tag<Material> ITEMS_SPRUCE_LOGS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("spruce_logs"), Material.class);

    Tag<Material> ITEMS_STAIRS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stairs"), Material.class);

    Tag<Material> ITEMS_STONE_BRICKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_bricks"), Material.class);

    Tag<Material> ITEMS_STONE_BUTTONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_buttons"), Material.class);

    Tag<Material> ITEMS_STONE_CRAFTING_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_crafting_materials"), Material.class);

    Tag<Material> ITEMS_STONE_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_tool_materials"), Material.class);

    Tag<Material> ITEMS_STRIDER_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("strider_food"), Material.class);

    Tag<Material> ITEMS_STRIDER_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("strider_tempt_items"), Material.class);

    Tag<Material> ITEMS_SWORDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("swords"), Material.class);

    Tag<Material> ITEMS_TERRACOTTA = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("terracotta"), Material.class);

    Tag<Material> ITEMS_TRAPDOORS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("trapdoors"), Material.class);

    Tag<Material> ITEMS_TRIM_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("trim_materials"), Material.class);

    Tag<Material> ITEMS_TRIMMABLE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("trimmable_armor"), Material.class);

    Tag<Material> ITEMS_TURTLE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("turtle_food"), Material.class);

    Tag<Material> ITEMS_VILLAGER_PICKS_UP = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("villager_picks_up"), Material.class);

    Tag<Material> ITEMS_VILLAGER_PLANTABLE_SEEDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("villager_plantable_seeds"), Material.class);

    Tag<Material> ITEMS_WALLS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("walls"), Material.class);

    Tag<Material> ITEMS_WARPED_STEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("warped_stems"), Material.class);

    Tag<Material> ITEMS_WART_BLOCKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wart_blocks"), Material.class);

    Tag<Material> ITEMS_WITHER_SKELETON_DISLIKED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wither_skeleton_disliked_weapons"), Material.class);

    Tag<Material> ITEMS_WOLF_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wolf_food"), Material.class);

    Tag<Material> ITEMS_WOODEN_BUTTONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_buttons"), Material.class);

    Tag<Material> ITEMS_WOODEN_DOORS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_doors"), Material.class);

    Tag<Material> ITEMS_WOODEN_FENCES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_fences"), Material.class);

    Tag<Material> ITEMS_WOODEN_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_pressure_plates"), Material.class);

    Tag<Material> ITEMS_WOODEN_SLABS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_slabs"), Material.class);

    Tag<Material> ITEMS_WOODEN_STAIRS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_stairs"), Material.class);

    Tag<Material> ITEMS_WOODEN_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_tool_materials"), Material.class);

    Tag<Material> ITEMS_WOODEN_TRAPDOORS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_trapdoors"), Material.class);

    Tag<Material> ITEMS_WOOL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wool"), Material.class);

    Tag<Material> ITEMS_WOOL_CARPETS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wool_carpets"), Material.class);

    String REGISTRY_FLUIDS = "fluids";

    Tag<Fluid> FLUIDS_LAVA = Bukkit.getTag(REGISTRY_FLUIDS, NamespacedKey.minecraft("lava"), Fluid.class);

    Tag<Fluid> FLUIDS_WATER = Bukkit.getTag(REGISTRY_FLUIDS, NamespacedKey.minecraft("water"), Fluid.class);

    String REGISTRY_ENTITY_TYPES = "entity_types";

    Tag<EntityType> ENTITY_TYPES_AQUATIC = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("aquatic"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_ARROWS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("arrows"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_ARTHROPOD = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("arthropod"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_AXOLOTL_ALWAYS_HOSTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("axolotl_always_hostiles"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_AXOLOTL_HUNT_TARGETS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("axolotl_hunt_targets"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_BEEHIVE_INHABITORS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("beehive_inhabitors"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_BOAT = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("boat"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_CAN_BREATHE_UNDER_WATER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_breathe_under_water"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_CAN_EQUIP_HARNESS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_equip_harness"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_CAN_EQUIP_SADDLE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_equip_saddle"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_CAN_TURN_IN_BOATS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_turn_in_boats"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_CAN_WEAR_HORSE_ARMOR = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_wear_horse_armor"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_DEFLECTS_PROJECTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("deflects_projectiles"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_DISMOUNTS_UNDERWATER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("dismounts_underwater"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_FALL_DAMAGE_IMMUNE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("fall_damage_immune"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_FOLLOWABLE_FRIENDLY_MOBS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("followable_friendly_mobs"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_FREEZE_HURTS_EXTRA_TYPES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("freeze_hurts_extra_types"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_FREEZE_IMMUNE_ENTITY_TYPES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("freeze_immune_entity_types"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_FROG_FOOD = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("frog_food"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_IGNORES_POISON_AND_REGEN = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("ignores_poison_and_regen"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_ILLAGER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("illager"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_ILLAGER_FRIENDS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("illager_friends"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_IMMUNE_TO_INFESTED = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("immune_to_infested"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_IMMUNE_TO_OOZING = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("immune_to_oozing"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_IMPACT_PROJECTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("impact_projectiles"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_INVERTED_HEALING_AND_HARM = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("inverted_healing_and_harm"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_NO_ANGER_FROM_WIND_CHARGE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("no_anger_from_wind_charge"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_NON_CONTROLLING_RIDER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("non_controlling_rider"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_NOT_SCARY_FOR_PUFFERFISH = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("not_scary_for_pufferfish"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_POWDER_SNOW_WALKABLE_MOBS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("powder_snow_walkable_mobs"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_RAIDERS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("raiders"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_REDIRECTABLE_PROJECTILE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("redirectable_projectile"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_BANE_OF_ARTHROPODS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_bane_of_arthropods"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_IMPALING = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_impaling"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_SMITE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_smite"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_SKELETONS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("skeletons"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_UNDEAD = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("undead"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_WITHER_FRIENDS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("wither_friends"), EntityType.class);

    Tag<EntityType> ENTITY_TYPES_ZOMBIES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("zombies"), EntityType.class);

    String REGISTRY_GAME_EVENTS = "game_events";

    Tag<GameEvent> GAME_EVENT_ALLAY_CAN_LISTEN = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("allay_can_listen"), GameEvent.class);

    Tag<GameEvent> GAME_EVENT_IGNORE_VIBRATIONS_SNEAKING = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("ignore_vibrations_sneaking"), GameEvent.class);

    Tag<GameEvent> GAME_EVENT_SHRIEKER_CAN_LISTEN = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("shrieker_can_listen"), GameEvent.class);

    Tag<GameEvent> GAME_EVENT_VIBRATIONS = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("vibrations"), GameEvent.class);

    Tag<GameEvent> GAME_EVENT_WARDEN_CAN_LISTEN = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("warden_can_listen"), GameEvent.class);
    // End generate - Tag
    /**
     * @deprecated {@link #WOOL_CARPETS}.
     */
    @Deprecated(since = "1.19")
    Tag<Material> CARPETS = WOOL_CARPETS;
    /**
     * Vanilla block tag representing all blocks which dead bushes may be placed on.
     *
     * @deprecated partially replaced by {@link #DRY_VEGETATION_MAY_PLACE_ON}
     */
    @Deprecated(since = "1.21.5", forRemoval = true)
    Tag<Material> DEAD_BUSH_MAY_PLACE_ON = DRY_VEGETATION_MAY_PLACE_ON;
    /**
     * Vanilla block tag representing all blocks that are replaceable by
     * dripstone.
     *
     * @deprecated use {@link #DRIPSTONE_REPLACEABLE_BLOCKS}
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    Tag<Material> DRIPSTONE_REPLACEABLE = DRIPSTONE_REPLACEABLE_BLOCKS;
    /**
     * Vanilla item tag representing all piglin food.
     *
     * @deprecated use {@link #ITEMS_PIGLIN_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> PIGLIN_FOOD = ITEMS_PIGLIN_FOOD;
    /**
     * Vanilla item tag representing all fox food.
     *
     * @deprecated use {@link #ITEMS_FOX_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> FOX_FOOD = ITEMS_FOX_FOOD;
    /**
     * Vanilla item tag representing all furnace materials.
     *
     * @deprecated partially replaced by {@link #ITEMS_STONE_CRAFTING_MATERIALS}
     */
    @Deprecated(since = "1.16.2", forRemoval = true)
    Tag<Material> ITEMS_FURNACE_MATERIALS = ITEMS_STONE_CRAFTING_MATERIALS;
    /**
     * Vanilla item tag representing all items which modify note block sounds when placed on top.
     *
     * @deprecated use {@link #ITEMS_NOTEBLOCK_TOP_INSTRUMENTS}
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    Tag<Material> ITEMS_NOTE_BLOCK_TOP_INSTRUMENTS = ITEMS_NOTEBLOCK_TOP_INSTRUMENTS;
    /**
     * Vanilla item tag representing all tools.
     *
     * @deprecated removed in Minecraft 1.20.5. Do not use. Will be removed at a later date. Until then,
     * this constant now acts as a reference to {@link #ITEMS_BREAKS_DECORATED_POTS} which largely shares
     * the same contents of the old "minecraft:tools" tag.
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    Tag<Material> ITEMS_TOOLS = ITEMS_BREAKS_DECORATED_POTS;
    /**
     * Vanilla item tag representing all items which tempt axolotls.
     *
     * @deprecated use {@link #ITEMS_AXOLOTL_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> AXOLOTL_TEMPT_ITEMS = ITEMS_AXOLOTL_FOOD;
    /**
     * Vanilla tag representing entities which deflect arrows.
     * @deprecated use {@link #ENTITY_TYPES_DEFLECTS_PROJECTILES}
     */
    @Deprecated(since = "1.20.5")
    Tag<EntityType> ENTITY_TYPES_DEFLECTS_ARROWS = ENTITY_TYPES_DEFLECTS_PROJECTILES;

    /**
     * Returns whether or not this tag has an entry for the specified item.
     *
     * @param item to check
     * @return if it is tagged
     */
    boolean isTagged(@NotNull T item);

    /**
     * Gets an immutable set of all tagged items.
     *
     * @return set of tagged items
     */
    @NotNull
    Set<T> getValues();

}
