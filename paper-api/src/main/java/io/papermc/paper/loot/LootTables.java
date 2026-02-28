package io.papermc.paper.loot;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.NullMarked;

/**
 * All the vanilla loot tables
 */
@NullMarked
public final class LootTables {

    // Start generate - LootTables
    public static final LootTable ARCHAEOLOGY_DESERT_PYRAMID = getTable("archaeology/desert_pyramid");

    public static final LootTable ARCHAEOLOGY_DESERT_WELL = getTable("archaeology/desert_well");

    public static final LootTable ARCHAEOLOGY_OCEAN_RUIN_COLD = getTable("archaeology/ocean_ruin_cold");

    public static final LootTable ARCHAEOLOGY_OCEAN_RUIN_WARM = getTable("archaeology/ocean_ruin_warm");

    public static final LootTable ARCHAEOLOGY_TRAIL_RUINS_COMMON = getTable("archaeology/trail_ruins_common");

    public static final LootTable ARCHAEOLOGY_TRAIL_RUINS_RARE = getTable("archaeology/trail_ruins_rare");

    public static final LootTable BLOCKS_ACACIA_BUTTON = getTable("blocks/acacia_button");

    public static final LootTable BLOCKS_ACACIA_DOOR = getTable("blocks/acacia_door");

    public static final LootTable BLOCKS_ACACIA_FENCE = getTable("blocks/acacia_fence");

    public static final LootTable BLOCKS_ACACIA_FENCE_GATE = getTable("blocks/acacia_fence_gate");

    public static final LootTable BLOCKS_ACACIA_HANGING_SIGN = getTable("blocks/acacia_hanging_sign");

    public static final LootTable BLOCKS_ACACIA_LEAVES = getTable("blocks/acacia_leaves");

    public static final LootTable BLOCKS_ACACIA_LOG = getTable("blocks/acacia_log");

    public static final LootTable BLOCKS_ACACIA_PLANKS = getTable("blocks/acacia_planks");

    public static final LootTable BLOCKS_ACACIA_PRESSURE_PLATE = getTable("blocks/acacia_pressure_plate");

    public static final LootTable BLOCKS_ACACIA_SAPLING = getTable("blocks/acacia_sapling");

    public static final LootTable BLOCKS_ACACIA_SHELF = getTable("blocks/acacia_shelf");

    public static final LootTable BLOCKS_ACACIA_SIGN = getTable("blocks/acacia_sign");

    public static final LootTable BLOCKS_ACACIA_SLAB = getTable("blocks/acacia_slab");

    public static final LootTable BLOCKS_ACACIA_STAIRS = getTable("blocks/acacia_stairs");

    public static final LootTable BLOCKS_ACACIA_TRAPDOOR = getTable("blocks/acacia_trapdoor");

    public static final LootTable BLOCKS_ACACIA_WOOD = getTable("blocks/acacia_wood");

    public static final LootTable BLOCKS_ACTIVATOR_RAIL = getTable("blocks/activator_rail");

    public static final LootTable BLOCKS_ALLIUM = getTable("blocks/allium");

    public static final LootTable BLOCKS_AMETHYST_BLOCK = getTable("blocks/amethyst_block");

    public static final LootTable BLOCKS_AMETHYST_CLUSTER = getTable("blocks/amethyst_cluster");

    public static final LootTable BLOCKS_ANCIENT_DEBRIS = getTable("blocks/ancient_debris");

    public static final LootTable BLOCKS_ANDESITE = getTable("blocks/andesite");

    public static final LootTable BLOCKS_ANDESITE_SLAB = getTable("blocks/andesite_slab");

    public static final LootTable BLOCKS_ANDESITE_STAIRS = getTable("blocks/andesite_stairs");

    public static final LootTable BLOCKS_ANDESITE_WALL = getTable("blocks/andesite_wall");

    public static final LootTable BLOCKS_ANVIL = getTable("blocks/anvil");

    public static final LootTable BLOCKS_ATTACHED_MELON_STEM = getTable("blocks/attached_melon_stem");

    public static final LootTable BLOCKS_ATTACHED_PUMPKIN_STEM = getTable("blocks/attached_pumpkin_stem");

    public static final LootTable BLOCKS_AZALEA = getTable("blocks/azalea");

    public static final LootTable BLOCKS_AZALEA_LEAVES = getTable("blocks/azalea_leaves");

    public static final LootTable BLOCKS_AZURE_BLUET = getTable("blocks/azure_bluet");

    public static final LootTable BLOCKS_BAMBOO = getTable("blocks/bamboo");

    public static final LootTable BLOCKS_BAMBOO_BLOCK = getTable("blocks/bamboo_block");

    public static final LootTable BLOCKS_BAMBOO_BUTTON = getTable("blocks/bamboo_button");

    public static final LootTable BLOCKS_BAMBOO_DOOR = getTable("blocks/bamboo_door");

    public static final LootTable BLOCKS_BAMBOO_FENCE = getTable("blocks/bamboo_fence");

    public static final LootTable BLOCKS_BAMBOO_FENCE_GATE = getTable("blocks/bamboo_fence_gate");

    public static final LootTable BLOCKS_BAMBOO_HANGING_SIGN = getTable("blocks/bamboo_hanging_sign");

    public static final LootTable BLOCKS_BAMBOO_MOSAIC = getTable("blocks/bamboo_mosaic");

    public static final LootTable BLOCKS_BAMBOO_MOSAIC_SLAB = getTable("blocks/bamboo_mosaic_slab");

    public static final LootTable BLOCKS_BAMBOO_MOSAIC_STAIRS = getTable("blocks/bamboo_mosaic_stairs");

    public static final LootTable BLOCKS_BAMBOO_PLANKS = getTable("blocks/bamboo_planks");

    public static final LootTable BLOCKS_BAMBOO_PRESSURE_PLATE = getTable("blocks/bamboo_pressure_plate");

    public static final LootTable BLOCKS_BAMBOO_SAPLING = getTable("blocks/bamboo_sapling");

    public static final LootTable BLOCKS_BAMBOO_SHELF = getTable("blocks/bamboo_shelf");

    public static final LootTable BLOCKS_BAMBOO_SIGN = getTable("blocks/bamboo_sign");

    public static final LootTable BLOCKS_BAMBOO_SLAB = getTable("blocks/bamboo_slab");

    public static final LootTable BLOCKS_BAMBOO_STAIRS = getTable("blocks/bamboo_stairs");

    public static final LootTable BLOCKS_BAMBOO_TRAPDOOR = getTable("blocks/bamboo_trapdoor");

    public static final LootTable BLOCKS_BARREL = getTable("blocks/barrel");

    public static final LootTable BLOCKS_BASALT = getTable("blocks/basalt");

    public static final LootTable BLOCKS_BEACON = getTable("blocks/beacon");

    public static final LootTable BLOCKS_BEE_NEST = getTable("blocks/bee_nest");

    public static final LootTable BLOCKS_BEEHIVE = getTable("blocks/beehive");

    public static final LootTable BLOCKS_BEETROOTS = getTable("blocks/beetroots");

    public static final LootTable BLOCKS_BELL = getTable("blocks/bell");

    public static final LootTable BLOCKS_BIG_DRIPLEAF = getTable("blocks/big_dripleaf");

    public static final LootTable BLOCKS_BIG_DRIPLEAF_STEM = getTable("blocks/big_dripleaf_stem");

    public static final LootTable BLOCKS_BIRCH_BUTTON = getTable("blocks/birch_button");

    public static final LootTable BLOCKS_BIRCH_DOOR = getTable("blocks/birch_door");

    public static final LootTable BLOCKS_BIRCH_FENCE = getTable("blocks/birch_fence");

    public static final LootTable BLOCKS_BIRCH_FENCE_GATE = getTable("blocks/birch_fence_gate");

    public static final LootTable BLOCKS_BIRCH_HANGING_SIGN = getTable("blocks/birch_hanging_sign");

    public static final LootTable BLOCKS_BIRCH_LEAVES = getTable("blocks/birch_leaves");

    public static final LootTable BLOCKS_BIRCH_LOG = getTable("blocks/birch_log");

    public static final LootTable BLOCKS_BIRCH_PLANKS = getTable("blocks/birch_planks");

    public static final LootTable BLOCKS_BIRCH_PRESSURE_PLATE = getTable("blocks/birch_pressure_plate");

    public static final LootTable BLOCKS_BIRCH_SAPLING = getTable("blocks/birch_sapling");

    public static final LootTable BLOCKS_BIRCH_SHELF = getTable("blocks/birch_shelf");

    public static final LootTable BLOCKS_BIRCH_SIGN = getTable("blocks/birch_sign");

    public static final LootTable BLOCKS_BIRCH_SLAB = getTable("blocks/birch_slab");

    public static final LootTable BLOCKS_BIRCH_STAIRS = getTable("blocks/birch_stairs");

    public static final LootTable BLOCKS_BIRCH_TRAPDOOR = getTable("blocks/birch_trapdoor");

    public static final LootTable BLOCKS_BIRCH_WOOD = getTable("blocks/birch_wood");

    public static final LootTable BLOCKS_BLACK_BANNER = getTable("blocks/black_banner");

    public static final LootTable BLOCKS_BLACK_BED = getTable("blocks/black_bed");

    public static final LootTable BLOCKS_BLACK_CANDLE = getTable("blocks/black_candle");

    public static final LootTable BLOCKS_BLACK_CANDLE_CAKE = getTable("blocks/black_candle_cake");

    public static final LootTable BLOCKS_BLACK_CARPET = getTable("blocks/black_carpet");

    public static final LootTable BLOCKS_BLACK_CONCRETE = getTable("blocks/black_concrete");

    public static final LootTable BLOCKS_BLACK_CONCRETE_POWDER = getTable("blocks/black_concrete_powder");

    public static final LootTable BLOCKS_BLACK_GLAZED_TERRACOTTA = getTable("blocks/black_glazed_terracotta");

    public static final LootTable BLOCKS_BLACK_SHULKER_BOX = getTable("blocks/black_shulker_box");

    public static final LootTable BLOCKS_BLACK_STAINED_GLASS = getTable("blocks/black_stained_glass");

    public static final LootTable BLOCKS_BLACK_STAINED_GLASS_PANE = getTable("blocks/black_stained_glass_pane");

    public static final LootTable BLOCKS_BLACK_TERRACOTTA = getTable("blocks/black_terracotta");

    public static final LootTable BLOCKS_BLACK_WOOL = getTable("blocks/black_wool");

    public static final LootTable BLOCKS_BLACKSTONE = getTable("blocks/blackstone");

    public static final LootTable BLOCKS_BLACKSTONE_SLAB = getTable("blocks/blackstone_slab");

    public static final LootTable BLOCKS_BLACKSTONE_STAIRS = getTable("blocks/blackstone_stairs");

    public static final LootTable BLOCKS_BLACKSTONE_WALL = getTable("blocks/blackstone_wall");

    public static final LootTable BLOCKS_BLAST_FURNACE = getTable("blocks/blast_furnace");

    public static final LootTable BLOCKS_BLUE_BANNER = getTable("blocks/blue_banner");

    public static final LootTable BLOCKS_BLUE_BED = getTable("blocks/blue_bed");

    public static final LootTable BLOCKS_BLUE_CANDLE = getTable("blocks/blue_candle");

    public static final LootTable BLOCKS_BLUE_CANDLE_CAKE = getTable("blocks/blue_candle_cake");

    public static final LootTable BLOCKS_BLUE_CARPET = getTable("blocks/blue_carpet");

    public static final LootTable BLOCKS_BLUE_CONCRETE = getTable("blocks/blue_concrete");

    public static final LootTable BLOCKS_BLUE_CONCRETE_POWDER = getTable("blocks/blue_concrete_powder");

    public static final LootTable BLOCKS_BLUE_GLAZED_TERRACOTTA = getTable("blocks/blue_glazed_terracotta");

    public static final LootTable BLOCKS_BLUE_ICE = getTable("blocks/blue_ice");

    public static final LootTable BLOCKS_BLUE_ORCHID = getTable("blocks/blue_orchid");

    public static final LootTable BLOCKS_BLUE_SHULKER_BOX = getTable("blocks/blue_shulker_box");

    public static final LootTable BLOCKS_BLUE_STAINED_GLASS = getTable("blocks/blue_stained_glass");

    public static final LootTable BLOCKS_BLUE_STAINED_GLASS_PANE = getTable("blocks/blue_stained_glass_pane");

    public static final LootTable BLOCKS_BLUE_TERRACOTTA = getTable("blocks/blue_terracotta");

    public static final LootTable BLOCKS_BLUE_WOOL = getTable("blocks/blue_wool");

    public static final LootTable BLOCKS_BONE_BLOCK = getTable("blocks/bone_block");

    public static final LootTable BLOCKS_BOOKSHELF = getTable("blocks/bookshelf");

    public static final LootTable BLOCKS_BRAIN_CORAL = getTable("blocks/brain_coral");

    public static final LootTable BLOCKS_BRAIN_CORAL_BLOCK = getTable("blocks/brain_coral_block");

    public static final LootTable BLOCKS_BRAIN_CORAL_FAN = getTable("blocks/brain_coral_fan");

    public static final LootTable BLOCKS_BREWING_STAND = getTable("blocks/brewing_stand");

    public static final LootTable BLOCKS_BRICK_SLAB = getTable("blocks/brick_slab");

    public static final LootTable BLOCKS_BRICK_STAIRS = getTable("blocks/brick_stairs");

    public static final LootTable BLOCKS_BRICK_WALL = getTable("blocks/brick_wall");

    public static final LootTable BLOCKS_BRICKS = getTable("blocks/bricks");

    public static final LootTable BLOCKS_BROWN_BANNER = getTable("blocks/brown_banner");

    public static final LootTable BLOCKS_BROWN_BED = getTable("blocks/brown_bed");

    public static final LootTable BLOCKS_BROWN_CANDLE = getTable("blocks/brown_candle");

    public static final LootTable BLOCKS_BROWN_CANDLE_CAKE = getTable("blocks/brown_candle_cake");

    public static final LootTable BLOCKS_BROWN_CARPET = getTable("blocks/brown_carpet");

    public static final LootTable BLOCKS_BROWN_CONCRETE = getTable("blocks/brown_concrete");

    public static final LootTable BLOCKS_BROWN_CONCRETE_POWDER = getTable("blocks/brown_concrete_powder");

    public static final LootTable BLOCKS_BROWN_GLAZED_TERRACOTTA = getTable("blocks/brown_glazed_terracotta");

    public static final LootTable BLOCKS_BROWN_MUSHROOM = getTable("blocks/brown_mushroom");

    public static final LootTable BLOCKS_BROWN_MUSHROOM_BLOCK = getTable("blocks/brown_mushroom_block");

    public static final LootTable BLOCKS_BROWN_SHULKER_BOX = getTable("blocks/brown_shulker_box");

    public static final LootTable BLOCKS_BROWN_STAINED_GLASS = getTable("blocks/brown_stained_glass");

    public static final LootTable BLOCKS_BROWN_STAINED_GLASS_PANE = getTable("blocks/brown_stained_glass_pane");

    public static final LootTable BLOCKS_BROWN_TERRACOTTA = getTable("blocks/brown_terracotta");

    public static final LootTable BLOCKS_BROWN_WOOL = getTable("blocks/brown_wool");

    public static final LootTable BLOCKS_BUBBLE_CORAL = getTable("blocks/bubble_coral");

    public static final LootTable BLOCKS_BUBBLE_CORAL_BLOCK = getTable("blocks/bubble_coral_block");

    public static final LootTable BLOCKS_BUBBLE_CORAL_FAN = getTable("blocks/bubble_coral_fan");

    public static final LootTable BLOCKS_BUDDING_AMETHYST = getTable("blocks/budding_amethyst");

    public static final LootTable BLOCKS_BUSH = getTable("blocks/bush");

    public static final LootTable BLOCKS_CACTUS = getTable("blocks/cactus");

    public static final LootTable BLOCKS_CACTUS_FLOWER = getTable("blocks/cactus_flower");

    public static final LootTable BLOCKS_CAKE = getTable("blocks/cake");

    public static final LootTable BLOCKS_CALCITE = getTable("blocks/calcite");

    public static final LootTable BLOCKS_CALIBRATED_SCULK_SENSOR = getTable("blocks/calibrated_sculk_sensor");

    public static final LootTable BLOCKS_CAMPFIRE = getTable("blocks/campfire");

    public static final LootTable BLOCKS_CANDLE = getTable("blocks/candle");

    public static final LootTable BLOCKS_CANDLE_CAKE = getTable("blocks/candle_cake");

    public static final LootTable BLOCKS_CARROTS = getTable("blocks/carrots");

    public static final LootTable BLOCKS_CARTOGRAPHY_TABLE = getTable("blocks/cartography_table");

    public static final LootTable BLOCKS_CARVED_PUMPKIN = getTable("blocks/carved_pumpkin");

    public static final LootTable BLOCKS_CAULDRON = getTable("blocks/cauldron");

    public static final LootTable BLOCKS_CAVE_VINES = getTable("blocks/cave_vines");

    public static final LootTable BLOCKS_CAVE_VINES_PLANT = getTable("blocks/cave_vines_plant");

    public static final LootTable BLOCKS_CHERRY_BUTTON = getTable("blocks/cherry_button");

    public static final LootTable BLOCKS_CHERRY_DOOR = getTable("blocks/cherry_door");

    public static final LootTable BLOCKS_CHERRY_FENCE = getTable("blocks/cherry_fence");

    public static final LootTable BLOCKS_CHERRY_FENCE_GATE = getTable("blocks/cherry_fence_gate");

    public static final LootTable BLOCKS_CHERRY_HANGING_SIGN = getTable("blocks/cherry_hanging_sign");

    public static final LootTable BLOCKS_CHERRY_LEAVES = getTable("blocks/cherry_leaves");

    public static final LootTable BLOCKS_CHERRY_LOG = getTable("blocks/cherry_log");

    public static final LootTable BLOCKS_CHERRY_PLANKS = getTable("blocks/cherry_planks");

    public static final LootTable BLOCKS_CHERRY_PRESSURE_PLATE = getTable("blocks/cherry_pressure_plate");

    public static final LootTable BLOCKS_CHERRY_SAPLING = getTable("blocks/cherry_sapling");

    public static final LootTable BLOCKS_CHERRY_SHELF = getTable("blocks/cherry_shelf");

    public static final LootTable BLOCKS_CHERRY_SIGN = getTable("blocks/cherry_sign");

    public static final LootTable BLOCKS_CHERRY_SLAB = getTable("blocks/cherry_slab");

    public static final LootTable BLOCKS_CHERRY_STAIRS = getTable("blocks/cherry_stairs");

    public static final LootTable BLOCKS_CHERRY_TRAPDOOR = getTable("blocks/cherry_trapdoor");

    public static final LootTable BLOCKS_CHERRY_WOOD = getTable("blocks/cherry_wood");

    public static final LootTable BLOCKS_CHEST = getTable("blocks/chest");

    public static final LootTable BLOCKS_CHIPPED_ANVIL = getTable("blocks/chipped_anvil");

    public static final LootTable BLOCKS_CHISELED_BOOKSHELF = getTable("blocks/chiseled_bookshelf");

    public static final LootTable BLOCKS_CHISELED_COPPER = getTable("blocks/chiseled_copper");

    public static final LootTable BLOCKS_CHISELED_DEEPSLATE = getTable("blocks/chiseled_deepslate");

    public static final LootTable BLOCKS_CHISELED_NETHER_BRICKS = getTable("blocks/chiseled_nether_bricks");

    public static final LootTable BLOCKS_CHISELED_POLISHED_BLACKSTONE = getTable("blocks/chiseled_polished_blackstone");

    public static final LootTable BLOCKS_CHISELED_QUARTZ_BLOCK = getTable("blocks/chiseled_quartz_block");

    public static final LootTable BLOCKS_CHISELED_RED_SANDSTONE = getTable("blocks/chiseled_red_sandstone");

    public static final LootTable BLOCKS_CHISELED_RESIN_BRICKS = getTable("blocks/chiseled_resin_bricks");

    public static final LootTable BLOCKS_CHISELED_SANDSTONE = getTable("blocks/chiseled_sandstone");

    public static final LootTable BLOCKS_CHISELED_STONE_BRICKS = getTable("blocks/chiseled_stone_bricks");

    public static final LootTable BLOCKS_CHISELED_TUFF = getTable("blocks/chiseled_tuff");

    public static final LootTable BLOCKS_CHISELED_TUFF_BRICKS = getTable("blocks/chiseled_tuff_bricks");

    public static final LootTable BLOCKS_CHORUS_FLOWER = getTable("blocks/chorus_flower");

    public static final LootTable BLOCKS_CHORUS_PLANT = getTable("blocks/chorus_plant");

    public static final LootTable BLOCKS_CLAY = getTable("blocks/clay");

    public static final LootTable BLOCKS_CLOSED_EYEBLOSSOM = getTable("blocks/closed_eyeblossom");

    public static final LootTable BLOCKS_COAL_BLOCK = getTable("blocks/coal_block");

    public static final LootTable BLOCKS_COAL_ORE = getTable("blocks/coal_ore");

    public static final LootTable BLOCKS_COARSE_DIRT = getTable("blocks/coarse_dirt");

    public static final LootTable BLOCKS_COBBLED_DEEPSLATE = getTable("blocks/cobbled_deepslate");

    public static final LootTable BLOCKS_COBBLED_DEEPSLATE_SLAB = getTable("blocks/cobbled_deepslate_slab");

    public static final LootTable BLOCKS_COBBLED_DEEPSLATE_STAIRS = getTable("blocks/cobbled_deepslate_stairs");

    public static final LootTable BLOCKS_COBBLED_DEEPSLATE_WALL = getTable("blocks/cobbled_deepslate_wall");

    public static final LootTable BLOCKS_COBBLESTONE = getTable("blocks/cobblestone");

    public static final LootTable BLOCKS_COBBLESTONE_SLAB = getTable("blocks/cobblestone_slab");

    public static final LootTable BLOCKS_COBBLESTONE_STAIRS = getTable("blocks/cobblestone_stairs");

    public static final LootTable BLOCKS_COBBLESTONE_WALL = getTable("blocks/cobblestone_wall");

    public static final LootTable BLOCKS_COBWEB = getTable("blocks/cobweb");

    public static final LootTable BLOCKS_COCOA = getTable("blocks/cocoa");

    public static final LootTable BLOCKS_COMPARATOR = getTable("blocks/comparator");

    public static final LootTable BLOCKS_COMPOSTER = getTable("blocks/composter");

    public static final LootTable BLOCKS_CONDUIT = getTable("blocks/conduit");

    public static final LootTable BLOCKS_COPPER_BARS = getTable("blocks/copper_bars");

    public static final LootTable BLOCKS_COPPER_BLOCK = getTable("blocks/copper_block");

    public static final LootTable BLOCKS_COPPER_BULB = getTable("blocks/copper_bulb");

    public static final LootTable BLOCKS_COPPER_CHAIN = getTable("blocks/copper_chain");

    public static final LootTable BLOCKS_COPPER_CHEST = getTable("blocks/copper_chest");

    public static final LootTable BLOCKS_COPPER_DOOR = getTable("blocks/copper_door");

    public static final LootTable BLOCKS_COPPER_GOLEM_STATUE = getTable("blocks/copper_golem_statue");

    public static final LootTable BLOCKS_COPPER_GRATE = getTable("blocks/copper_grate");

    public static final LootTable BLOCKS_COPPER_LANTERN = getTable("blocks/copper_lantern");

    public static final LootTable BLOCKS_COPPER_ORE = getTable("blocks/copper_ore");

    public static final LootTable BLOCKS_COPPER_TORCH = getTable("blocks/copper_torch");

    public static final LootTable BLOCKS_COPPER_TRAPDOOR = getTable("blocks/copper_trapdoor");

    public static final LootTable BLOCKS_CORNFLOWER = getTable("blocks/cornflower");

    public static final LootTable BLOCKS_CRACKED_DEEPSLATE_BRICKS = getTable("blocks/cracked_deepslate_bricks");

    public static final LootTable BLOCKS_CRACKED_DEEPSLATE_TILES = getTable("blocks/cracked_deepslate_tiles");

    public static final LootTable BLOCKS_CRACKED_NETHER_BRICKS = getTable("blocks/cracked_nether_bricks");

    public static final LootTable BLOCKS_CRACKED_POLISHED_BLACKSTONE_BRICKS = getTable("blocks/cracked_polished_blackstone_bricks");

    public static final LootTable BLOCKS_CRACKED_STONE_BRICKS = getTable("blocks/cracked_stone_bricks");

    public static final LootTable BLOCKS_CRAFTER = getTable("blocks/crafter");

    public static final LootTable BLOCKS_CRAFTING_TABLE = getTable("blocks/crafting_table");

    public static final LootTable BLOCKS_CREAKING_HEART = getTable("blocks/creaking_heart");

    public static final LootTable BLOCKS_CREEPER_HEAD = getTable("blocks/creeper_head");

    public static final LootTable BLOCKS_CRIMSON_BUTTON = getTable("blocks/crimson_button");

    public static final LootTable BLOCKS_CRIMSON_DOOR = getTable("blocks/crimson_door");

    public static final LootTable BLOCKS_CRIMSON_FENCE = getTable("blocks/crimson_fence");

    public static final LootTable BLOCKS_CRIMSON_FENCE_GATE = getTable("blocks/crimson_fence_gate");

    public static final LootTable BLOCKS_CRIMSON_FUNGUS = getTable("blocks/crimson_fungus");

    public static final LootTable BLOCKS_CRIMSON_HANGING_SIGN = getTable("blocks/crimson_hanging_sign");

    public static final LootTable BLOCKS_CRIMSON_HYPHAE = getTable("blocks/crimson_hyphae");

    public static final LootTable BLOCKS_CRIMSON_NYLIUM = getTable("blocks/crimson_nylium");

    public static final LootTable BLOCKS_CRIMSON_PLANKS = getTable("blocks/crimson_planks");

    public static final LootTable BLOCKS_CRIMSON_PRESSURE_PLATE = getTable("blocks/crimson_pressure_plate");

    public static final LootTable BLOCKS_CRIMSON_ROOTS = getTable("blocks/crimson_roots");

    public static final LootTable BLOCKS_CRIMSON_SHELF = getTable("blocks/crimson_shelf");

    public static final LootTable BLOCKS_CRIMSON_SIGN = getTable("blocks/crimson_sign");

    public static final LootTable BLOCKS_CRIMSON_SLAB = getTable("blocks/crimson_slab");

    public static final LootTable BLOCKS_CRIMSON_STAIRS = getTable("blocks/crimson_stairs");

    public static final LootTable BLOCKS_CRIMSON_STEM = getTable("blocks/crimson_stem");

    public static final LootTable BLOCKS_CRIMSON_TRAPDOOR = getTable("blocks/crimson_trapdoor");

    public static final LootTable BLOCKS_CRYING_OBSIDIAN = getTable("blocks/crying_obsidian");

    public static final LootTable BLOCKS_CUT_COPPER = getTable("blocks/cut_copper");

    public static final LootTable BLOCKS_CUT_COPPER_SLAB = getTable("blocks/cut_copper_slab");

    public static final LootTable BLOCKS_CUT_COPPER_STAIRS = getTable("blocks/cut_copper_stairs");

    public static final LootTable BLOCKS_CUT_RED_SANDSTONE = getTable("blocks/cut_red_sandstone");

    public static final LootTable BLOCKS_CUT_RED_SANDSTONE_SLAB = getTable("blocks/cut_red_sandstone_slab");

    public static final LootTable BLOCKS_CUT_SANDSTONE = getTable("blocks/cut_sandstone");

    public static final LootTable BLOCKS_CUT_SANDSTONE_SLAB = getTable("blocks/cut_sandstone_slab");

    public static final LootTable BLOCKS_CYAN_BANNER = getTable("blocks/cyan_banner");

    public static final LootTable BLOCKS_CYAN_BED = getTable("blocks/cyan_bed");

    public static final LootTable BLOCKS_CYAN_CANDLE = getTable("blocks/cyan_candle");

    public static final LootTable BLOCKS_CYAN_CANDLE_CAKE = getTable("blocks/cyan_candle_cake");

    public static final LootTable BLOCKS_CYAN_CARPET = getTable("blocks/cyan_carpet");

    public static final LootTable BLOCKS_CYAN_CONCRETE = getTable("blocks/cyan_concrete");

    public static final LootTable BLOCKS_CYAN_CONCRETE_POWDER = getTable("blocks/cyan_concrete_powder");

    public static final LootTable BLOCKS_CYAN_GLAZED_TERRACOTTA = getTable("blocks/cyan_glazed_terracotta");

    public static final LootTable BLOCKS_CYAN_SHULKER_BOX = getTable("blocks/cyan_shulker_box");

    public static final LootTable BLOCKS_CYAN_STAINED_GLASS = getTable("blocks/cyan_stained_glass");

    public static final LootTable BLOCKS_CYAN_STAINED_GLASS_PANE = getTable("blocks/cyan_stained_glass_pane");

    public static final LootTable BLOCKS_CYAN_TERRACOTTA = getTable("blocks/cyan_terracotta");

    public static final LootTable BLOCKS_CYAN_WOOL = getTable("blocks/cyan_wool");

    public static final LootTable BLOCKS_DAMAGED_ANVIL = getTable("blocks/damaged_anvil");

    public static final LootTable BLOCKS_DANDELION = getTable("blocks/dandelion");

    public static final LootTable BLOCKS_DARK_OAK_BUTTON = getTable("blocks/dark_oak_button");

    public static final LootTable BLOCKS_DARK_OAK_DOOR = getTable("blocks/dark_oak_door");

    public static final LootTable BLOCKS_DARK_OAK_FENCE = getTable("blocks/dark_oak_fence");

    public static final LootTable BLOCKS_DARK_OAK_FENCE_GATE = getTable("blocks/dark_oak_fence_gate");

    public static final LootTable BLOCKS_DARK_OAK_HANGING_SIGN = getTable("blocks/dark_oak_hanging_sign");

    public static final LootTable BLOCKS_DARK_OAK_LEAVES = getTable("blocks/dark_oak_leaves");

    public static final LootTable BLOCKS_DARK_OAK_LOG = getTable("blocks/dark_oak_log");

    public static final LootTable BLOCKS_DARK_OAK_PLANKS = getTable("blocks/dark_oak_planks");

    public static final LootTable BLOCKS_DARK_OAK_PRESSURE_PLATE = getTable("blocks/dark_oak_pressure_plate");

    public static final LootTable BLOCKS_DARK_OAK_SAPLING = getTable("blocks/dark_oak_sapling");

    public static final LootTable BLOCKS_DARK_OAK_SHELF = getTable("blocks/dark_oak_shelf");

    public static final LootTable BLOCKS_DARK_OAK_SIGN = getTable("blocks/dark_oak_sign");

    public static final LootTable BLOCKS_DARK_OAK_SLAB = getTable("blocks/dark_oak_slab");

    public static final LootTable BLOCKS_DARK_OAK_STAIRS = getTable("blocks/dark_oak_stairs");

    public static final LootTable BLOCKS_DARK_OAK_TRAPDOOR = getTable("blocks/dark_oak_trapdoor");

    public static final LootTable BLOCKS_DARK_OAK_WOOD = getTable("blocks/dark_oak_wood");

    public static final LootTable BLOCKS_DARK_PRISMARINE = getTable("blocks/dark_prismarine");

    public static final LootTable BLOCKS_DARK_PRISMARINE_SLAB = getTable("blocks/dark_prismarine_slab");

    public static final LootTable BLOCKS_DARK_PRISMARINE_STAIRS = getTable("blocks/dark_prismarine_stairs");

    public static final LootTable BLOCKS_DAYLIGHT_DETECTOR = getTable("blocks/daylight_detector");

    public static final LootTable BLOCKS_DEAD_BRAIN_CORAL = getTable("blocks/dead_brain_coral");

    public static final LootTable BLOCKS_DEAD_BRAIN_CORAL_BLOCK = getTable("blocks/dead_brain_coral_block");

    public static final LootTable BLOCKS_DEAD_BRAIN_CORAL_FAN = getTable("blocks/dead_brain_coral_fan");

    public static final LootTable BLOCKS_DEAD_BUBBLE_CORAL = getTable("blocks/dead_bubble_coral");

    public static final LootTable BLOCKS_DEAD_BUBBLE_CORAL_BLOCK = getTable("blocks/dead_bubble_coral_block");

    public static final LootTable BLOCKS_DEAD_BUBBLE_CORAL_FAN = getTable("blocks/dead_bubble_coral_fan");

    public static final LootTable BLOCKS_DEAD_BUSH = getTable("blocks/dead_bush");

    public static final LootTable BLOCKS_DEAD_FIRE_CORAL = getTable("blocks/dead_fire_coral");

    public static final LootTable BLOCKS_DEAD_FIRE_CORAL_BLOCK = getTable("blocks/dead_fire_coral_block");

    public static final LootTable BLOCKS_DEAD_FIRE_CORAL_FAN = getTable("blocks/dead_fire_coral_fan");

    public static final LootTable BLOCKS_DEAD_HORN_CORAL = getTable("blocks/dead_horn_coral");

    public static final LootTable BLOCKS_DEAD_HORN_CORAL_BLOCK = getTable("blocks/dead_horn_coral_block");

    public static final LootTable BLOCKS_DEAD_HORN_CORAL_FAN = getTable("blocks/dead_horn_coral_fan");

    public static final LootTable BLOCKS_DEAD_TUBE_CORAL = getTable("blocks/dead_tube_coral");

    public static final LootTable BLOCKS_DEAD_TUBE_CORAL_BLOCK = getTable("blocks/dead_tube_coral_block");

    public static final LootTable BLOCKS_DEAD_TUBE_CORAL_FAN = getTable("blocks/dead_tube_coral_fan");

    public static final LootTable BLOCKS_DECORATED_POT = getTable("blocks/decorated_pot");

    public static final LootTable BLOCKS_DEEPSLATE = getTable("blocks/deepslate");

    public static final LootTable BLOCKS_DEEPSLATE_BRICK_SLAB = getTable("blocks/deepslate_brick_slab");

    public static final LootTable BLOCKS_DEEPSLATE_BRICK_STAIRS = getTable("blocks/deepslate_brick_stairs");

    public static final LootTable BLOCKS_DEEPSLATE_BRICK_WALL = getTable("blocks/deepslate_brick_wall");

    public static final LootTable BLOCKS_DEEPSLATE_BRICKS = getTable("blocks/deepslate_bricks");

    public static final LootTable BLOCKS_DEEPSLATE_COAL_ORE = getTable("blocks/deepslate_coal_ore");

    public static final LootTable BLOCKS_DEEPSLATE_COPPER_ORE = getTable("blocks/deepslate_copper_ore");

    public static final LootTable BLOCKS_DEEPSLATE_DIAMOND_ORE = getTable("blocks/deepslate_diamond_ore");

    public static final LootTable BLOCKS_DEEPSLATE_EMERALD_ORE = getTable("blocks/deepslate_emerald_ore");

    public static final LootTable BLOCKS_DEEPSLATE_GOLD_ORE = getTable("blocks/deepslate_gold_ore");

    public static final LootTable BLOCKS_DEEPSLATE_IRON_ORE = getTable("blocks/deepslate_iron_ore");

    public static final LootTable BLOCKS_DEEPSLATE_LAPIS_ORE = getTable("blocks/deepslate_lapis_ore");

    public static final LootTable BLOCKS_DEEPSLATE_REDSTONE_ORE = getTable("blocks/deepslate_redstone_ore");

    public static final LootTable BLOCKS_DEEPSLATE_TILE_SLAB = getTable("blocks/deepslate_tile_slab");

    public static final LootTable BLOCKS_DEEPSLATE_TILE_STAIRS = getTable("blocks/deepslate_tile_stairs");

    public static final LootTable BLOCKS_DEEPSLATE_TILE_WALL = getTable("blocks/deepslate_tile_wall");

    public static final LootTable BLOCKS_DEEPSLATE_TILES = getTable("blocks/deepslate_tiles");

    public static final LootTable BLOCKS_DETECTOR_RAIL = getTable("blocks/detector_rail");

    public static final LootTable BLOCKS_DIAMOND_BLOCK = getTable("blocks/diamond_block");

    public static final LootTable BLOCKS_DIAMOND_ORE = getTable("blocks/diamond_ore");

    public static final LootTable BLOCKS_DIORITE = getTable("blocks/diorite");

    public static final LootTable BLOCKS_DIORITE_SLAB = getTable("blocks/diorite_slab");

    public static final LootTable BLOCKS_DIORITE_STAIRS = getTable("blocks/diorite_stairs");

    public static final LootTable BLOCKS_DIORITE_WALL = getTable("blocks/diorite_wall");

    public static final LootTable BLOCKS_DIRT = getTable("blocks/dirt");

    public static final LootTable BLOCKS_DIRT_PATH = getTable("blocks/dirt_path");

    public static final LootTable BLOCKS_DISPENSER = getTable("blocks/dispenser");

    public static final LootTable BLOCKS_DRAGON_EGG = getTable("blocks/dragon_egg");

    public static final LootTable BLOCKS_DRAGON_HEAD = getTable("blocks/dragon_head");

    public static final LootTable BLOCKS_DRIED_GHAST = getTable("blocks/dried_ghast");

    public static final LootTable BLOCKS_DRIED_KELP_BLOCK = getTable("blocks/dried_kelp_block");

    public static final LootTable BLOCKS_DRIPSTONE_BLOCK = getTable("blocks/dripstone_block");

    public static final LootTable BLOCKS_DROPPER = getTable("blocks/dropper");

    public static final LootTable BLOCKS_EMERALD_BLOCK = getTable("blocks/emerald_block");

    public static final LootTable BLOCKS_EMERALD_ORE = getTable("blocks/emerald_ore");

    public static final LootTable BLOCKS_ENCHANTING_TABLE = getTable("blocks/enchanting_table");

    public static final LootTable BLOCKS_END_ROD = getTable("blocks/end_rod");

    public static final LootTable BLOCKS_END_STONE = getTable("blocks/end_stone");

    public static final LootTable BLOCKS_END_STONE_BRICK_SLAB = getTable("blocks/end_stone_brick_slab");

    public static final LootTable BLOCKS_END_STONE_BRICK_STAIRS = getTable("blocks/end_stone_brick_stairs");

    public static final LootTable BLOCKS_END_STONE_BRICK_WALL = getTable("blocks/end_stone_brick_wall");

    public static final LootTable BLOCKS_END_STONE_BRICKS = getTable("blocks/end_stone_bricks");

    public static final LootTable BLOCKS_ENDER_CHEST = getTable("blocks/ender_chest");

    public static final LootTable BLOCKS_EXPOSED_CHISELED_COPPER = getTable("blocks/exposed_chiseled_copper");

    public static final LootTable BLOCKS_EXPOSED_COPPER = getTable("blocks/exposed_copper");

    public static final LootTable BLOCKS_EXPOSED_COPPER_BARS = getTable("blocks/exposed_copper_bars");

    public static final LootTable BLOCKS_EXPOSED_COPPER_BULB = getTable("blocks/exposed_copper_bulb");

    public static final LootTable BLOCKS_EXPOSED_COPPER_CHAIN = getTable("blocks/exposed_copper_chain");

    public static final LootTable BLOCKS_EXPOSED_COPPER_CHEST = getTable("blocks/exposed_copper_chest");

    public static final LootTable BLOCKS_EXPOSED_COPPER_DOOR = getTable("blocks/exposed_copper_door");

    public static final LootTable BLOCKS_EXPOSED_COPPER_GOLEM_STATUE = getTable("blocks/exposed_copper_golem_statue");

    public static final LootTable BLOCKS_EXPOSED_COPPER_GRATE = getTable("blocks/exposed_copper_grate");

    public static final LootTable BLOCKS_EXPOSED_COPPER_LANTERN = getTable("blocks/exposed_copper_lantern");

    public static final LootTable BLOCKS_EXPOSED_COPPER_TRAPDOOR = getTable("blocks/exposed_copper_trapdoor");

    public static final LootTable BLOCKS_EXPOSED_CUT_COPPER = getTable("blocks/exposed_cut_copper");

    public static final LootTable BLOCKS_EXPOSED_CUT_COPPER_SLAB = getTable("blocks/exposed_cut_copper_slab");

    public static final LootTable BLOCKS_EXPOSED_CUT_COPPER_STAIRS = getTable("blocks/exposed_cut_copper_stairs");

    public static final LootTable BLOCKS_EXPOSED_LIGHTNING_ROD = getTable("blocks/exposed_lightning_rod");

    public static final LootTable BLOCKS_FARMLAND = getTable("blocks/farmland");

    public static final LootTable BLOCKS_FERN = getTable("blocks/fern");

    public static final LootTable BLOCKS_FIRE = getTable("blocks/fire");

    public static final LootTable BLOCKS_FIRE_CORAL = getTable("blocks/fire_coral");

    public static final LootTable BLOCKS_FIRE_CORAL_BLOCK = getTable("blocks/fire_coral_block");

    public static final LootTable BLOCKS_FIRE_CORAL_FAN = getTable("blocks/fire_coral_fan");

    public static final LootTable BLOCKS_FIREFLY_BUSH = getTable("blocks/firefly_bush");

    public static final LootTable BLOCKS_FLETCHING_TABLE = getTable("blocks/fletching_table");

    public static final LootTable BLOCKS_FLOWER_POT = getTable("blocks/flower_pot");

    public static final LootTable BLOCKS_FLOWERING_AZALEA = getTable("blocks/flowering_azalea");

    public static final LootTable BLOCKS_FLOWERING_AZALEA_LEAVES = getTable("blocks/flowering_azalea_leaves");

    public static final LootTable BLOCKS_FROGSPAWN = getTable("blocks/frogspawn");

    public static final LootTable BLOCKS_FROSTED_ICE = getTable("blocks/frosted_ice");

    public static final LootTable BLOCKS_FURNACE = getTable("blocks/furnace");

    public static final LootTable BLOCKS_GILDED_BLACKSTONE = getTable("blocks/gilded_blackstone");

    public static final LootTable BLOCKS_GLASS = getTable("blocks/glass");

    public static final LootTable BLOCKS_GLASS_PANE = getTable("blocks/glass_pane");

    public static final LootTable BLOCKS_GLOW_LICHEN = getTable("blocks/glow_lichen");

    public static final LootTable BLOCKS_GLOWSTONE = getTable("blocks/glowstone");

    public static final LootTable BLOCKS_GOLD_BLOCK = getTable("blocks/gold_block");

    public static final LootTable BLOCKS_GOLD_ORE = getTable("blocks/gold_ore");

    public static final LootTable BLOCKS_GRANITE = getTable("blocks/granite");

    public static final LootTable BLOCKS_GRANITE_SLAB = getTable("blocks/granite_slab");

    public static final LootTable BLOCKS_GRANITE_STAIRS = getTable("blocks/granite_stairs");

    public static final LootTable BLOCKS_GRANITE_WALL = getTable("blocks/granite_wall");

    public static final LootTable BLOCKS_GRASS_BLOCK = getTable("blocks/grass_block");

    public static final LootTable BLOCKS_GRAVEL = getTable("blocks/gravel");

    public static final LootTable BLOCKS_GRAY_BANNER = getTable("blocks/gray_banner");

    public static final LootTable BLOCKS_GRAY_BED = getTable("blocks/gray_bed");

    public static final LootTable BLOCKS_GRAY_CANDLE = getTable("blocks/gray_candle");

    public static final LootTable BLOCKS_GRAY_CANDLE_CAKE = getTable("blocks/gray_candle_cake");

    public static final LootTable BLOCKS_GRAY_CARPET = getTable("blocks/gray_carpet");

    public static final LootTable BLOCKS_GRAY_CONCRETE = getTable("blocks/gray_concrete");

    public static final LootTable BLOCKS_GRAY_CONCRETE_POWDER = getTable("blocks/gray_concrete_powder");

    public static final LootTable BLOCKS_GRAY_GLAZED_TERRACOTTA = getTable("blocks/gray_glazed_terracotta");

    public static final LootTable BLOCKS_GRAY_SHULKER_BOX = getTable("blocks/gray_shulker_box");

    public static final LootTable BLOCKS_GRAY_STAINED_GLASS = getTable("blocks/gray_stained_glass");

    public static final LootTable BLOCKS_GRAY_STAINED_GLASS_PANE = getTable("blocks/gray_stained_glass_pane");

    public static final LootTable BLOCKS_GRAY_TERRACOTTA = getTable("blocks/gray_terracotta");

    public static final LootTable BLOCKS_GRAY_WOOL = getTable("blocks/gray_wool");

    public static final LootTable BLOCKS_GREEN_BANNER = getTable("blocks/green_banner");

    public static final LootTable BLOCKS_GREEN_BED = getTable("blocks/green_bed");

    public static final LootTable BLOCKS_GREEN_CANDLE = getTable("blocks/green_candle");

    public static final LootTable BLOCKS_GREEN_CANDLE_CAKE = getTable("blocks/green_candle_cake");

    public static final LootTable BLOCKS_GREEN_CARPET = getTable("blocks/green_carpet");

    public static final LootTable BLOCKS_GREEN_CONCRETE = getTable("blocks/green_concrete");

    public static final LootTable BLOCKS_GREEN_CONCRETE_POWDER = getTable("blocks/green_concrete_powder");

    public static final LootTable BLOCKS_GREEN_GLAZED_TERRACOTTA = getTable("blocks/green_glazed_terracotta");

    public static final LootTable BLOCKS_GREEN_SHULKER_BOX = getTable("blocks/green_shulker_box");

    public static final LootTable BLOCKS_GREEN_STAINED_GLASS = getTable("blocks/green_stained_glass");

    public static final LootTable BLOCKS_GREEN_STAINED_GLASS_PANE = getTable("blocks/green_stained_glass_pane");

    public static final LootTable BLOCKS_GREEN_TERRACOTTA = getTable("blocks/green_terracotta");

    public static final LootTable BLOCKS_GREEN_WOOL = getTable("blocks/green_wool");

    public static final LootTable BLOCKS_GRINDSTONE = getTable("blocks/grindstone");

    public static final LootTable BLOCKS_HANGING_ROOTS = getTable("blocks/hanging_roots");

    public static final LootTable BLOCKS_HAY_BLOCK = getTable("blocks/hay_block");

    public static final LootTable BLOCKS_HEAVY_CORE = getTable("blocks/heavy_core");

    public static final LootTable BLOCKS_HEAVY_WEIGHTED_PRESSURE_PLATE = getTable("blocks/heavy_weighted_pressure_plate");

    public static final LootTable BLOCKS_HONEY_BLOCK = getTable("blocks/honey_block");

    public static final LootTable BLOCKS_HONEYCOMB_BLOCK = getTable("blocks/honeycomb_block");

    public static final LootTable BLOCKS_HOPPER = getTable("blocks/hopper");

    public static final LootTable BLOCKS_HORN_CORAL = getTable("blocks/horn_coral");

    public static final LootTable BLOCKS_HORN_CORAL_BLOCK = getTable("blocks/horn_coral_block");

    public static final LootTable BLOCKS_HORN_CORAL_FAN = getTable("blocks/horn_coral_fan");

    public static final LootTable BLOCKS_ICE = getTable("blocks/ice");

    public static final LootTable BLOCKS_INFESTED_CHISELED_STONE_BRICKS = getTable("blocks/infested_chiseled_stone_bricks");

    public static final LootTable BLOCKS_INFESTED_COBBLESTONE = getTable("blocks/infested_cobblestone");

    public static final LootTable BLOCKS_INFESTED_CRACKED_STONE_BRICKS = getTable("blocks/infested_cracked_stone_bricks");

    public static final LootTable BLOCKS_INFESTED_DEEPSLATE = getTable("blocks/infested_deepslate");

    public static final LootTable BLOCKS_INFESTED_MOSSY_STONE_BRICKS = getTable("blocks/infested_mossy_stone_bricks");

    public static final LootTable BLOCKS_INFESTED_STONE = getTable("blocks/infested_stone");

    public static final LootTable BLOCKS_INFESTED_STONE_BRICKS = getTable("blocks/infested_stone_bricks");

    public static final LootTable BLOCKS_IRON_BARS = getTable("blocks/iron_bars");

    public static final LootTable BLOCKS_IRON_BLOCK = getTable("blocks/iron_block");

    public static final LootTable BLOCKS_IRON_CHAIN = getTable("blocks/iron_chain");

    public static final LootTable BLOCKS_IRON_DOOR = getTable("blocks/iron_door");

    public static final LootTable BLOCKS_IRON_ORE = getTable("blocks/iron_ore");

    public static final LootTable BLOCKS_IRON_TRAPDOOR = getTable("blocks/iron_trapdoor");

    public static final LootTable BLOCKS_JACK_O_LANTERN = getTable("blocks/jack_o_lantern");

    public static final LootTable BLOCKS_JUKEBOX = getTable("blocks/jukebox");

    public static final LootTable BLOCKS_JUNGLE_BUTTON = getTable("blocks/jungle_button");

    public static final LootTable BLOCKS_JUNGLE_DOOR = getTable("blocks/jungle_door");

    public static final LootTable BLOCKS_JUNGLE_FENCE = getTable("blocks/jungle_fence");

    public static final LootTable BLOCKS_JUNGLE_FENCE_GATE = getTable("blocks/jungle_fence_gate");

    public static final LootTable BLOCKS_JUNGLE_HANGING_SIGN = getTable("blocks/jungle_hanging_sign");

    public static final LootTable BLOCKS_JUNGLE_LEAVES = getTable("blocks/jungle_leaves");

    public static final LootTable BLOCKS_JUNGLE_LOG = getTable("blocks/jungle_log");

    public static final LootTable BLOCKS_JUNGLE_PLANKS = getTable("blocks/jungle_planks");

    public static final LootTable BLOCKS_JUNGLE_PRESSURE_PLATE = getTable("blocks/jungle_pressure_plate");

    public static final LootTable BLOCKS_JUNGLE_SAPLING = getTable("blocks/jungle_sapling");

    public static final LootTable BLOCKS_JUNGLE_SHELF = getTable("blocks/jungle_shelf");

    public static final LootTable BLOCKS_JUNGLE_SIGN = getTable("blocks/jungle_sign");

    public static final LootTable BLOCKS_JUNGLE_SLAB = getTable("blocks/jungle_slab");

    public static final LootTable BLOCKS_JUNGLE_STAIRS = getTable("blocks/jungle_stairs");

    public static final LootTable BLOCKS_JUNGLE_TRAPDOOR = getTable("blocks/jungle_trapdoor");

    public static final LootTable BLOCKS_JUNGLE_WOOD = getTable("blocks/jungle_wood");

    public static final LootTable BLOCKS_KELP = getTable("blocks/kelp");

    public static final LootTable BLOCKS_KELP_PLANT = getTable("blocks/kelp_plant");

    public static final LootTable BLOCKS_LADDER = getTable("blocks/ladder");

    public static final LootTable BLOCKS_LANTERN = getTable("blocks/lantern");

    public static final LootTable BLOCKS_LAPIS_BLOCK = getTable("blocks/lapis_block");

    public static final LootTable BLOCKS_LAPIS_ORE = getTable("blocks/lapis_ore");

    public static final LootTable BLOCKS_LARGE_AMETHYST_BUD = getTable("blocks/large_amethyst_bud");

    public static final LootTable BLOCKS_LARGE_FERN = getTable("blocks/large_fern");

    public static final LootTable BLOCKS_LAVA_CAULDRON = getTable("blocks/lava_cauldron");

    public static final LootTable BLOCKS_LEAF_LITTER = getTable("blocks/leaf_litter");

    public static final LootTable BLOCKS_LECTERN = getTable("blocks/lectern");

    public static final LootTable BLOCKS_LEVER = getTable("blocks/lever");

    public static final LootTable BLOCKS_LIGHT_BLUE_BANNER = getTable("blocks/light_blue_banner");

    public static final LootTable BLOCKS_LIGHT_BLUE_BED = getTable("blocks/light_blue_bed");

    public static final LootTable BLOCKS_LIGHT_BLUE_CANDLE = getTable("blocks/light_blue_candle");

    public static final LootTable BLOCKS_LIGHT_BLUE_CANDLE_CAKE = getTable("blocks/light_blue_candle_cake");

    public static final LootTable BLOCKS_LIGHT_BLUE_CARPET = getTable("blocks/light_blue_carpet");

    public static final LootTable BLOCKS_LIGHT_BLUE_CONCRETE = getTable("blocks/light_blue_concrete");

    public static final LootTable BLOCKS_LIGHT_BLUE_CONCRETE_POWDER = getTable("blocks/light_blue_concrete_powder");

    public static final LootTable BLOCKS_LIGHT_BLUE_GLAZED_TERRACOTTA = getTable("blocks/light_blue_glazed_terracotta");

    public static final LootTable BLOCKS_LIGHT_BLUE_SHULKER_BOX = getTable("blocks/light_blue_shulker_box");

    public static final LootTable BLOCKS_LIGHT_BLUE_STAINED_GLASS = getTable("blocks/light_blue_stained_glass");

    public static final LootTable BLOCKS_LIGHT_BLUE_STAINED_GLASS_PANE = getTable("blocks/light_blue_stained_glass_pane");

    public static final LootTable BLOCKS_LIGHT_BLUE_TERRACOTTA = getTable("blocks/light_blue_terracotta");

    public static final LootTable BLOCKS_LIGHT_BLUE_WOOL = getTable("blocks/light_blue_wool");

    public static final LootTable BLOCKS_LIGHT_GRAY_BANNER = getTable("blocks/light_gray_banner");

    public static final LootTable BLOCKS_LIGHT_GRAY_BED = getTable("blocks/light_gray_bed");

    public static final LootTable BLOCKS_LIGHT_GRAY_CANDLE = getTable("blocks/light_gray_candle");

    public static final LootTable BLOCKS_LIGHT_GRAY_CANDLE_CAKE = getTable("blocks/light_gray_candle_cake");

    public static final LootTable BLOCKS_LIGHT_GRAY_CARPET = getTable("blocks/light_gray_carpet");

    public static final LootTable BLOCKS_LIGHT_GRAY_CONCRETE = getTable("blocks/light_gray_concrete");

    public static final LootTable BLOCKS_LIGHT_GRAY_CONCRETE_POWDER = getTable("blocks/light_gray_concrete_powder");

    public static final LootTable BLOCKS_LIGHT_GRAY_GLAZED_TERRACOTTA = getTable("blocks/light_gray_glazed_terracotta");

    public static final LootTable BLOCKS_LIGHT_GRAY_SHULKER_BOX = getTable("blocks/light_gray_shulker_box");

    public static final LootTable BLOCKS_LIGHT_GRAY_STAINED_GLASS = getTable("blocks/light_gray_stained_glass");

    public static final LootTable BLOCKS_LIGHT_GRAY_STAINED_GLASS_PANE = getTable("blocks/light_gray_stained_glass_pane");

    public static final LootTable BLOCKS_LIGHT_GRAY_TERRACOTTA = getTable("blocks/light_gray_terracotta");

    public static final LootTable BLOCKS_LIGHT_GRAY_WOOL = getTable("blocks/light_gray_wool");

    public static final LootTable BLOCKS_LIGHT_WEIGHTED_PRESSURE_PLATE = getTable("blocks/light_weighted_pressure_plate");

    public static final LootTable BLOCKS_LIGHTNING_ROD = getTable("blocks/lightning_rod");

    public static final LootTable BLOCKS_LILAC = getTable("blocks/lilac");

    public static final LootTable BLOCKS_LILY_OF_THE_VALLEY = getTable("blocks/lily_of_the_valley");

    public static final LootTable BLOCKS_LILY_PAD = getTable("blocks/lily_pad");

    public static final LootTable BLOCKS_LIME_BANNER = getTable("blocks/lime_banner");

    public static final LootTable BLOCKS_LIME_BED = getTable("blocks/lime_bed");

    public static final LootTable BLOCKS_LIME_CANDLE = getTable("blocks/lime_candle");

    public static final LootTable BLOCKS_LIME_CANDLE_CAKE = getTable("blocks/lime_candle_cake");

    public static final LootTable BLOCKS_LIME_CARPET = getTable("blocks/lime_carpet");

    public static final LootTable BLOCKS_LIME_CONCRETE = getTable("blocks/lime_concrete");

    public static final LootTable BLOCKS_LIME_CONCRETE_POWDER = getTable("blocks/lime_concrete_powder");

    public static final LootTable BLOCKS_LIME_GLAZED_TERRACOTTA = getTable("blocks/lime_glazed_terracotta");

    public static final LootTable BLOCKS_LIME_SHULKER_BOX = getTable("blocks/lime_shulker_box");

    public static final LootTable BLOCKS_LIME_STAINED_GLASS = getTable("blocks/lime_stained_glass");

    public static final LootTable BLOCKS_LIME_STAINED_GLASS_PANE = getTable("blocks/lime_stained_glass_pane");

    public static final LootTable BLOCKS_LIME_TERRACOTTA = getTable("blocks/lime_terracotta");

    public static final LootTable BLOCKS_LIME_WOOL = getTable("blocks/lime_wool");

    public static final LootTable BLOCKS_LODESTONE = getTable("blocks/lodestone");

    public static final LootTable BLOCKS_LOOM = getTable("blocks/loom");

    public static final LootTable BLOCKS_MAGENTA_BANNER = getTable("blocks/magenta_banner");

    public static final LootTable BLOCKS_MAGENTA_BED = getTable("blocks/magenta_bed");

    public static final LootTable BLOCKS_MAGENTA_CANDLE = getTable("blocks/magenta_candle");

    public static final LootTable BLOCKS_MAGENTA_CANDLE_CAKE = getTable("blocks/magenta_candle_cake");

    public static final LootTable BLOCKS_MAGENTA_CARPET = getTable("blocks/magenta_carpet");

    public static final LootTable BLOCKS_MAGENTA_CONCRETE = getTable("blocks/magenta_concrete");

    public static final LootTable BLOCKS_MAGENTA_CONCRETE_POWDER = getTable("blocks/magenta_concrete_powder");

    public static final LootTable BLOCKS_MAGENTA_GLAZED_TERRACOTTA = getTable("blocks/magenta_glazed_terracotta");

    public static final LootTable BLOCKS_MAGENTA_SHULKER_BOX = getTable("blocks/magenta_shulker_box");

    public static final LootTable BLOCKS_MAGENTA_STAINED_GLASS = getTable("blocks/magenta_stained_glass");

    public static final LootTable BLOCKS_MAGENTA_STAINED_GLASS_PANE = getTable("blocks/magenta_stained_glass_pane");

    public static final LootTable BLOCKS_MAGENTA_TERRACOTTA = getTable("blocks/magenta_terracotta");

    public static final LootTable BLOCKS_MAGENTA_WOOL = getTable("blocks/magenta_wool");

    public static final LootTable BLOCKS_MAGMA_BLOCK = getTable("blocks/magma_block");

    public static final LootTable BLOCKS_MANGROVE_BUTTON = getTable("blocks/mangrove_button");

    public static final LootTable BLOCKS_MANGROVE_DOOR = getTable("blocks/mangrove_door");

    public static final LootTable BLOCKS_MANGROVE_FENCE = getTable("blocks/mangrove_fence");

    public static final LootTable BLOCKS_MANGROVE_FENCE_GATE = getTable("blocks/mangrove_fence_gate");

    public static final LootTable BLOCKS_MANGROVE_HANGING_SIGN = getTable("blocks/mangrove_hanging_sign");

    public static final LootTable BLOCKS_MANGROVE_LEAVES = getTable("blocks/mangrove_leaves");

    public static final LootTable BLOCKS_MANGROVE_LOG = getTable("blocks/mangrove_log");

    public static final LootTable BLOCKS_MANGROVE_PLANKS = getTable("blocks/mangrove_planks");

    public static final LootTable BLOCKS_MANGROVE_PRESSURE_PLATE = getTable("blocks/mangrove_pressure_plate");

    public static final LootTable BLOCKS_MANGROVE_PROPAGULE = getTable("blocks/mangrove_propagule");

    public static final LootTable BLOCKS_MANGROVE_ROOTS = getTable("blocks/mangrove_roots");

    public static final LootTable BLOCKS_MANGROVE_SHELF = getTable("blocks/mangrove_shelf");

    public static final LootTable BLOCKS_MANGROVE_SIGN = getTable("blocks/mangrove_sign");

    public static final LootTable BLOCKS_MANGROVE_SLAB = getTable("blocks/mangrove_slab");

    public static final LootTable BLOCKS_MANGROVE_STAIRS = getTable("blocks/mangrove_stairs");

    public static final LootTable BLOCKS_MANGROVE_TRAPDOOR = getTable("blocks/mangrove_trapdoor");

    public static final LootTable BLOCKS_MANGROVE_WOOD = getTable("blocks/mangrove_wood");

    public static final LootTable BLOCKS_MEDIUM_AMETHYST_BUD = getTable("blocks/medium_amethyst_bud");

    public static final LootTable BLOCKS_MELON = getTable("blocks/melon");

    public static final LootTable BLOCKS_MELON_STEM = getTable("blocks/melon_stem");

    public static final LootTable BLOCKS_MOSS_BLOCK = getTable("blocks/moss_block");

    public static final LootTable BLOCKS_MOSS_CARPET = getTable("blocks/moss_carpet");

    public static final LootTable BLOCKS_MOSSY_COBBLESTONE = getTable("blocks/mossy_cobblestone");

    public static final LootTable BLOCKS_MOSSY_COBBLESTONE_SLAB = getTable("blocks/mossy_cobblestone_slab");

    public static final LootTable BLOCKS_MOSSY_COBBLESTONE_STAIRS = getTable("blocks/mossy_cobblestone_stairs");

    public static final LootTable BLOCKS_MOSSY_COBBLESTONE_WALL = getTable("blocks/mossy_cobblestone_wall");

    public static final LootTable BLOCKS_MOSSY_STONE_BRICK_SLAB = getTable("blocks/mossy_stone_brick_slab");

    public static final LootTable BLOCKS_MOSSY_STONE_BRICK_STAIRS = getTable("blocks/mossy_stone_brick_stairs");

    public static final LootTable BLOCKS_MOSSY_STONE_BRICK_WALL = getTable("blocks/mossy_stone_brick_wall");

    public static final LootTable BLOCKS_MOSSY_STONE_BRICKS = getTable("blocks/mossy_stone_bricks");

    public static final LootTable BLOCKS_MUD = getTable("blocks/mud");

    public static final LootTable BLOCKS_MUD_BRICK_SLAB = getTable("blocks/mud_brick_slab");

    public static final LootTable BLOCKS_MUD_BRICK_STAIRS = getTable("blocks/mud_brick_stairs");

    public static final LootTable BLOCKS_MUD_BRICK_WALL = getTable("blocks/mud_brick_wall");

    public static final LootTable BLOCKS_MUD_BRICKS = getTable("blocks/mud_bricks");

    public static final LootTable BLOCKS_MUDDY_MANGROVE_ROOTS = getTable("blocks/muddy_mangrove_roots");

    public static final LootTable BLOCKS_MUSHROOM_STEM = getTable("blocks/mushroom_stem");

    public static final LootTable BLOCKS_MYCELIUM = getTable("blocks/mycelium");

    public static final LootTable BLOCKS_NETHER_BRICK_FENCE = getTable("blocks/nether_brick_fence");

    public static final LootTable BLOCKS_NETHER_BRICK_SLAB = getTable("blocks/nether_brick_slab");

    public static final LootTable BLOCKS_NETHER_BRICK_STAIRS = getTable("blocks/nether_brick_stairs");

    public static final LootTable BLOCKS_NETHER_BRICK_WALL = getTable("blocks/nether_brick_wall");

    public static final LootTable BLOCKS_NETHER_BRICKS = getTable("blocks/nether_bricks");

    public static final LootTable BLOCKS_NETHER_GOLD_ORE = getTable("blocks/nether_gold_ore");

    public static final LootTable BLOCKS_NETHER_PORTAL = getTable("blocks/nether_portal");

    public static final LootTable BLOCKS_NETHER_QUARTZ_ORE = getTable("blocks/nether_quartz_ore");

    public static final LootTable BLOCKS_NETHER_SPROUTS = getTable("blocks/nether_sprouts");

    public static final LootTable BLOCKS_NETHER_WART = getTable("blocks/nether_wart");

    public static final LootTable BLOCKS_NETHER_WART_BLOCK = getTable("blocks/nether_wart_block");

    public static final LootTable BLOCKS_NETHERITE_BLOCK = getTable("blocks/netherite_block");

    public static final LootTable BLOCKS_NETHERRACK = getTable("blocks/netherrack");

    public static final LootTable BLOCKS_NOTE_BLOCK = getTable("blocks/note_block");

    public static final LootTable BLOCKS_OAK_BUTTON = getTable("blocks/oak_button");

    public static final LootTable BLOCKS_OAK_DOOR = getTable("blocks/oak_door");

    public static final LootTable BLOCKS_OAK_FENCE = getTable("blocks/oak_fence");

    public static final LootTable BLOCKS_OAK_FENCE_GATE = getTable("blocks/oak_fence_gate");

    public static final LootTable BLOCKS_OAK_HANGING_SIGN = getTable("blocks/oak_hanging_sign");

    public static final LootTable BLOCKS_OAK_LEAVES = getTable("blocks/oak_leaves");

    public static final LootTable BLOCKS_OAK_LOG = getTable("blocks/oak_log");

    public static final LootTable BLOCKS_OAK_PLANKS = getTable("blocks/oak_planks");

    public static final LootTable BLOCKS_OAK_PRESSURE_PLATE = getTable("blocks/oak_pressure_plate");

    public static final LootTable BLOCKS_OAK_SAPLING = getTable("blocks/oak_sapling");

    public static final LootTable BLOCKS_OAK_SHELF = getTable("blocks/oak_shelf");

    public static final LootTable BLOCKS_OAK_SIGN = getTable("blocks/oak_sign");

    public static final LootTable BLOCKS_OAK_SLAB = getTable("blocks/oak_slab");

    public static final LootTable BLOCKS_OAK_STAIRS = getTable("blocks/oak_stairs");

    public static final LootTable BLOCKS_OAK_TRAPDOOR = getTable("blocks/oak_trapdoor");

    public static final LootTable BLOCKS_OAK_WOOD = getTable("blocks/oak_wood");

    public static final LootTable BLOCKS_OBSERVER = getTable("blocks/observer");

    public static final LootTable BLOCKS_OBSIDIAN = getTable("blocks/obsidian");

    public static final LootTable BLOCKS_OCHRE_FROGLIGHT = getTable("blocks/ochre_froglight");

    public static final LootTable BLOCKS_OPEN_EYEBLOSSOM = getTable("blocks/open_eyeblossom");

    public static final LootTable BLOCKS_ORANGE_BANNER = getTable("blocks/orange_banner");

    public static final LootTable BLOCKS_ORANGE_BED = getTable("blocks/orange_bed");

    public static final LootTable BLOCKS_ORANGE_CANDLE = getTable("blocks/orange_candle");

    public static final LootTable BLOCKS_ORANGE_CANDLE_CAKE = getTable("blocks/orange_candle_cake");

    public static final LootTable BLOCKS_ORANGE_CARPET = getTable("blocks/orange_carpet");

    public static final LootTable BLOCKS_ORANGE_CONCRETE = getTable("blocks/orange_concrete");

    public static final LootTable BLOCKS_ORANGE_CONCRETE_POWDER = getTable("blocks/orange_concrete_powder");

    public static final LootTable BLOCKS_ORANGE_GLAZED_TERRACOTTA = getTable("blocks/orange_glazed_terracotta");

    public static final LootTable BLOCKS_ORANGE_SHULKER_BOX = getTable("blocks/orange_shulker_box");

    public static final LootTable BLOCKS_ORANGE_STAINED_GLASS = getTable("blocks/orange_stained_glass");

    public static final LootTable BLOCKS_ORANGE_STAINED_GLASS_PANE = getTable("blocks/orange_stained_glass_pane");

    public static final LootTable BLOCKS_ORANGE_TERRACOTTA = getTable("blocks/orange_terracotta");

    public static final LootTable BLOCKS_ORANGE_TULIP = getTable("blocks/orange_tulip");

    public static final LootTable BLOCKS_ORANGE_WOOL = getTable("blocks/orange_wool");

    public static final LootTable BLOCKS_OXEYE_DAISY = getTable("blocks/oxeye_daisy");

    public static final LootTable BLOCKS_OXIDIZED_CHISELED_COPPER = getTable("blocks/oxidized_chiseled_copper");

    public static final LootTable BLOCKS_OXIDIZED_COPPER = getTable("blocks/oxidized_copper");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_BARS = getTable("blocks/oxidized_copper_bars");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_BULB = getTable("blocks/oxidized_copper_bulb");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_CHAIN = getTable("blocks/oxidized_copper_chain");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_CHEST = getTable("blocks/oxidized_copper_chest");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_DOOR = getTable("blocks/oxidized_copper_door");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_GOLEM_STATUE = getTable("blocks/oxidized_copper_golem_statue");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_GRATE = getTable("blocks/oxidized_copper_grate");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_LANTERN = getTable("blocks/oxidized_copper_lantern");

    public static final LootTable BLOCKS_OXIDIZED_COPPER_TRAPDOOR = getTable("blocks/oxidized_copper_trapdoor");

    public static final LootTable BLOCKS_OXIDIZED_CUT_COPPER = getTable("blocks/oxidized_cut_copper");

    public static final LootTable BLOCKS_OXIDIZED_CUT_COPPER_SLAB = getTable("blocks/oxidized_cut_copper_slab");

    public static final LootTable BLOCKS_OXIDIZED_CUT_COPPER_STAIRS = getTable("blocks/oxidized_cut_copper_stairs");

    public static final LootTable BLOCKS_OXIDIZED_LIGHTNING_ROD = getTable("blocks/oxidized_lightning_rod");

    public static final LootTable BLOCKS_PACKED_ICE = getTable("blocks/packed_ice");

    public static final LootTable BLOCKS_PACKED_MUD = getTable("blocks/packed_mud");

    public static final LootTable BLOCKS_PALE_HANGING_MOSS = getTable("blocks/pale_hanging_moss");

    public static final LootTable BLOCKS_PALE_MOSS_BLOCK = getTable("blocks/pale_moss_block");

    public static final LootTable BLOCKS_PALE_MOSS_CARPET = getTable("blocks/pale_moss_carpet");

    public static final LootTable BLOCKS_PALE_OAK_BUTTON = getTable("blocks/pale_oak_button");

    public static final LootTable BLOCKS_PALE_OAK_DOOR = getTable("blocks/pale_oak_door");

    public static final LootTable BLOCKS_PALE_OAK_FENCE = getTable("blocks/pale_oak_fence");

    public static final LootTable BLOCKS_PALE_OAK_FENCE_GATE = getTable("blocks/pale_oak_fence_gate");

    public static final LootTable BLOCKS_PALE_OAK_HANGING_SIGN = getTable("blocks/pale_oak_hanging_sign");

    public static final LootTable BLOCKS_PALE_OAK_LEAVES = getTable("blocks/pale_oak_leaves");

    public static final LootTable BLOCKS_PALE_OAK_LOG = getTable("blocks/pale_oak_log");

    public static final LootTable BLOCKS_PALE_OAK_PLANKS = getTable("blocks/pale_oak_planks");

    public static final LootTable BLOCKS_PALE_OAK_PRESSURE_PLATE = getTable("blocks/pale_oak_pressure_plate");

    public static final LootTable BLOCKS_PALE_OAK_SAPLING = getTable("blocks/pale_oak_sapling");

    public static final LootTable BLOCKS_PALE_OAK_SHELF = getTable("blocks/pale_oak_shelf");

    public static final LootTable BLOCKS_PALE_OAK_SIGN = getTable("blocks/pale_oak_sign");

    public static final LootTable BLOCKS_PALE_OAK_SLAB = getTable("blocks/pale_oak_slab");

    public static final LootTable BLOCKS_PALE_OAK_STAIRS = getTable("blocks/pale_oak_stairs");

    public static final LootTable BLOCKS_PALE_OAK_TRAPDOOR = getTable("blocks/pale_oak_trapdoor");

    public static final LootTable BLOCKS_PALE_OAK_WOOD = getTable("blocks/pale_oak_wood");

    public static final LootTable BLOCKS_PEARLESCENT_FROGLIGHT = getTable("blocks/pearlescent_froglight");

    public static final LootTable BLOCKS_PEONY = getTable("blocks/peony");

    public static final LootTable BLOCKS_PETRIFIED_OAK_SLAB = getTable("blocks/petrified_oak_slab");

    public static final LootTable BLOCKS_PIGLIN_HEAD = getTable("blocks/piglin_head");

    public static final LootTable BLOCKS_PINK_BANNER = getTable("blocks/pink_banner");

    public static final LootTable BLOCKS_PINK_BED = getTable("blocks/pink_bed");

    public static final LootTable BLOCKS_PINK_CANDLE = getTable("blocks/pink_candle");

    public static final LootTable BLOCKS_PINK_CANDLE_CAKE = getTable("blocks/pink_candle_cake");

    public static final LootTable BLOCKS_PINK_CARPET = getTable("blocks/pink_carpet");

    public static final LootTable BLOCKS_PINK_CONCRETE = getTable("blocks/pink_concrete");

    public static final LootTable BLOCKS_PINK_CONCRETE_POWDER = getTable("blocks/pink_concrete_powder");

    public static final LootTable BLOCKS_PINK_GLAZED_TERRACOTTA = getTable("blocks/pink_glazed_terracotta");

    public static final LootTable BLOCKS_PINK_PETALS = getTable("blocks/pink_petals");

    public static final LootTable BLOCKS_PINK_SHULKER_BOX = getTable("blocks/pink_shulker_box");

    public static final LootTable BLOCKS_PINK_STAINED_GLASS = getTable("blocks/pink_stained_glass");

    public static final LootTable BLOCKS_PINK_STAINED_GLASS_PANE = getTable("blocks/pink_stained_glass_pane");

    public static final LootTable BLOCKS_PINK_TERRACOTTA = getTable("blocks/pink_terracotta");

    public static final LootTable BLOCKS_PINK_TULIP = getTable("blocks/pink_tulip");

    public static final LootTable BLOCKS_PINK_WOOL = getTable("blocks/pink_wool");

    public static final LootTable BLOCKS_PISTON = getTable("blocks/piston");

    public static final LootTable BLOCKS_PITCHER_CROP = getTable("blocks/pitcher_crop");

    public static final LootTable BLOCKS_PITCHER_PLANT = getTable("blocks/pitcher_plant");

    public static final LootTable BLOCKS_PLAYER_HEAD = getTable("blocks/player_head");

    public static final LootTable BLOCKS_PODZOL = getTable("blocks/podzol");

    public static final LootTable BLOCKS_POINTED_DRIPSTONE = getTable("blocks/pointed_dripstone");

    public static final LootTable BLOCKS_POLISHED_ANDESITE = getTable("blocks/polished_andesite");

    public static final LootTable BLOCKS_POLISHED_ANDESITE_SLAB = getTable("blocks/polished_andesite_slab");

    public static final LootTable BLOCKS_POLISHED_ANDESITE_STAIRS = getTable("blocks/polished_andesite_stairs");

    public static final LootTable BLOCKS_POLISHED_BASALT = getTable("blocks/polished_basalt");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE = getTable("blocks/polished_blackstone");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_BRICK_SLAB = getTable("blocks/polished_blackstone_brick_slab");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_BRICK_STAIRS = getTable("blocks/polished_blackstone_brick_stairs");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_BRICK_WALL = getTable("blocks/polished_blackstone_brick_wall");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_BRICKS = getTable("blocks/polished_blackstone_bricks");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_BUTTON = getTable("blocks/polished_blackstone_button");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_PRESSURE_PLATE = getTable("blocks/polished_blackstone_pressure_plate");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_SLAB = getTable("blocks/polished_blackstone_slab");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_STAIRS = getTable("blocks/polished_blackstone_stairs");

    public static final LootTable BLOCKS_POLISHED_BLACKSTONE_WALL = getTable("blocks/polished_blackstone_wall");

    public static final LootTable BLOCKS_POLISHED_DEEPSLATE = getTable("blocks/polished_deepslate");

    public static final LootTable BLOCKS_POLISHED_DEEPSLATE_SLAB = getTable("blocks/polished_deepslate_slab");

    public static final LootTable BLOCKS_POLISHED_DEEPSLATE_STAIRS = getTable("blocks/polished_deepslate_stairs");

    public static final LootTable BLOCKS_POLISHED_DEEPSLATE_WALL = getTable("blocks/polished_deepslate_wall");

    public static final LootTable BLOCKS_POLISHED_DIORITE = getTable("blocks/polished_diorite");

    public static final LootTable BLOCKS_POLISHED_DIORITE_SLAB = getTable("blocks/polished_diorite_slab");

    public static final LootTable BLOCKS_POLISHED_DIORITE_STAIRS = getTable("blocks/polished_diorite_stairs");

    public static final LootTable BLOCKS_POLISHED_GRANITE = getTable("blocks/polished_granite");

    public static final LootTable BLOCKS_POLISHED_GRANITE_SLAB = getTable("blocks/polished_granite_slab");

    public static final LootTable BLOCKS_POLISHED_GRANITE_STAIRS = getTable("blocks/polished_granite_stairs");

    public static final LootTable BLOCKS_POLISHED_TUFF = getTable("blocks/polished_tuff");

    public static final LootTable BLOCKS_POLISHED_TUFF_SLAB = getTable("blocks/polished_tuff_slab");

    public static final LootTable BLOCKS_POLISHED_TUFF_STAIRS = getTable("blocks/polished_tuff_stairs");

    public static final LootTable BLOCKS_POLISHED_TUFF_WALL = getTable("blocks/polished_tuff_wall");

    public static final LootTable BLOCKS_POPPY = getTable("blocks/poppy");

    public static final LootTable BLOCKS_POTATOES = getTable("blocks/potatoes");

    public static final LootTable BLOCKS_POTTED_ACACIA_SAPLING = getTable("blocks/potted_acacia_sapling");

    public static final LootTable BLOCKS_POTTED_ALLIUM = getTable("blocks/potted_allium");

    public static final LootTable BLOCKS_POTTED_AZALEA_BUSH = getTable("blocks/potted_azalea_bush");

    public static final LootTable BLOCKS_POTTED_AZURE_BLUET = getTable("blocks/potted_azure_bluet");

    public static final LootTable BLOCKS_POTTED_BAMBOO = getTable("blocks/potted_bamboo");

    public static final LootTable BLOCKS_POTTED_BIRCH_SAPLING = getTable("blocks/potted_birch_sapling");

    public static final LootTable BLOCKS_POTTED_BLUE_ORCHID = getTable("blocks/potted_blue_orchid");

    public static final LootTable BLOCKS_POTTED_BROWN_MUSHROOM = getTable("blocks/potted_brown_mushroom");

    public static final LootTable BLOCKS_POTTED_CACTUS = getTable("blocks/potted_cactus");

    public static final LootTable BLOCKS_POTTED_CHERRY_SAPLING = getTable("blocks/potted_cherry_sapling");

    public static final LootTable BLOCKS_POTTED_CLOSED_EYEBLOSSOM = getTable("blocks/potted_closed_eyeblossom");

    public static final LootTable BLOCKS_POTTED_CORNFLOWER = getTable("blocks/potted_cornflower");

    public static final LootTable BLOCKS_POTTED_CRIMSON_FUNGUS = getTable("blocks/potted_crimson_fungus");

    public static final LootTable BLOCKS_POTTED_CRIMSON_ROOTS = getTable("blocks/potted_crimson_roots");

    public static final LootTable BLOCKS_POTTED_DANDELION = getTable("blocks/potted_dandelion");

    public static final LootTable BLOCKS_POTTED_DARK_OAK_SAPLING = getTable("blocks/potted_dark_oak_sapling");

    public static final LootTable BLOCKS_POTTED_DEAD_BUSH = getTable("blocks/potted_dead_bush");

    public static final LootTable BLOCKS_POTTED_FERN = getTable("blocks/potted_fern");

    public static final LootTable BLOCKS_POTTED_FLOWERING_AZALEA_BUSH = getTable("blocks/potted_flowering_azalea_bush");

    public static final LootTable BLOCKS_POTTED_JUNGLE_SAPLING = getTable("blocks/potted_jungle_sapling");

    public static final LootTable BLOCKS_POTTED_LILY_OF_THE_VALLEY = getTable("blocks/potted_lily_of_the_valley");

    public static final LootTable BLOCKS_POTTED_MANGROVE_PROPAGULE = getTable("blocks/potted_mangrove_propagule");

    public static final LootTable BLOCKS_POTTED_OAK_SAPLING = getTable("blocks/potted_oak_sapling");

    public static final LootTable BLOCKS_POTTED_OPEN_EYEBLOSSOM = getTable("blocks/potted_open_eyeblossom");

    public static final LootTable BLOCKS_POTTED_ORANGE_TULIP = getTable("blocks/potted_orange_tulip");

    public static final LootTable BLOCKS_POTTED_OXEYE_DAISY = getTable("blocks/potted_oxeye_daisy");

    public static final LootTable BLOCKS_POTTED_PALE_OAK_SAPLING = getTable("blocks/potted_pale_oak_sapling");

    public static final LootTable BLOCKS_POTTED_PINK_TULIP = getTable("blocks/potted_pink_tulip");

    public static final LootTable BLOCKS_POTTED_POPPY = getTable("blocks/potted_poppy");

    public static final LootTable BLOCKS_POTTED_RED_MUSHROOM = getTable("blocks/potted_red_mushroom");

    public static final LootTable BLOCKS_POTTED_RED_TULIP = getTable("blocks/potted_red_tulip");

    public static final LootTable BLOCKS_POTTED_SPRUCE_SAPLING = getTable("blocks/potted_spruce_sapling");

    public static final LootTable BLOCKS_POTTED_TORCHFLOWER = getTable("blocks/potted_torchflower");

    public static final LootTable BLOCKS_POTTED_WARPED_FUNGUS = getTable("blocks/potted_warped_fungus");

    public static final LootTable BLOCKS_POTTED_WARPED_ROOTS = getTable("blocks/potted_warped_roots");

    public static final LootTable BLOCKS_POTTED_WHITE_TULIP = getTable("blocks/potted_white_tulip");

    public static final LootTable BLOCKS_POTTED_WITHER_ROSE = getTable("blocks/potted_wither_rose");

    public static final LootTable BLOCKS_POWDER_SNOW = getTable("blocks/powder_snow");

    public static final LootTable BLOCKS_POWDER_SNOW_CAULDRON = getTable("blocks/powder_snow_cauldron");

    public static final LootTable BLOCKS_POWERED_RAIL = getTable("blocks/powered_rail");

    public static final LootTable BLOCKS_PRISMARINE = getTable("blocks/prismarine");

    public static final LootTable BLOCKS_PRISMARINE_BRICK_SLAB = getTable("blocks/prismarine_brick_slab");

    public static final LootTable BLOCKS_PRISMARINE_BRICK_STAIRS = getTable("blocks/prismarine_brick_stairs");

    public static final LootTable BLOCKS_PRISMARINE_BRICKS = getTable("blocks/prismarine_bricks");

    public static final LootTable BLOCKS_PRISMARINE_SLAB = getTable("blocks/prismarine_slab");

    public static final LootTable BLOCKS_PRISMARINE_STAIRS = getTable("blocks/prismarine_stairs");

    public static final LootTable BLOCKS_PRISMARINE_WALL = getTable("blocks/prismarine_wall");

    public static final LootTable BLOCKS_PUMPKIN = getTable("blocks/pumpkin");

    public static final LootTable BLOCKS_PUMPKIN_STEM = getTable("blocks/pumpkin_stem");

    public static final LootTable BLOCKS_PURPLE_BANNER = getTable("blocks/purple_banner");

    public static final LootTable BLOCKS_PURPLE_BED = getTable("blocks/purple_bed");

    public static final LootTable BLOCKS_PURPLE_CANDLE = getTable("blocks/purple_candle");

    public static final LootTable BLOCKS_PURPLE_CANDLE_CAKE = getTable("blocks/purple_candle_cake");

    public static final LootTable BLOCKS_PURPLE_CARPET = getTable("blocks/purple_carpet");

    public static final LootTable BLOCKS_PURPLE_CONCRETE = getTable("blocks/purple_concrete");

    public static final LootTable BLOCKS_PURPLE_CONCRETE_POWDER = getTable("blocks/purple_concrete_powder");

    public static final LootTable BLOCKS_PURPLE_GLAZED_TERRACOTTA = getTable("blocks/purple_glazed_terracotta");

    public static final LootTable BLOCKS_PURPLE_SHULKER_BOX = getTable("blocks/purple_shulker_box");

    public static final LootTable BLOCKS_PURPLE_STAINED_GLASS = getTable("blocks/purple_stained_glass");

    public static final LootTable BLOCKS_PURPLE_STAINED_GLASS_PANE = getTable("blocks/purple_stained_glass_pane");

    public static final LootTable BLOCKS_PURPLE_TERRACOTTA = getTable("blocks/purple_terracotta");

    public static final LootTable BLOCKS_PURPLE_WOOL = getTable("blocks/purple_wool");

    public static final LootTable BLOCKS_PURPUR_BLOCK = getTable("blocks/purpur_block");

    public static final LootTable BLOCKS_PURPUR_PILLAR = getTable("blocks/purpur_pillar");

    public static final LootTable BLOCKS_PURPUR_SLAB = getTable("blocks/purpur_slab");

    public static final LootTable BLOCKS_PURPUR_STAIRS = getTable("blocks/purpur_stairs");

    public static final LootTable BLOCKS_QUARTZ_BLOCK = getTable("blocks/quartz_block");

    public static final LootTable BLOCKS_QUARTZ_BRICKS = getTable("blocks/quartz_bricks");

    public static final LootTable BLOCKS_QUARTZ_PILLAR = getTable("blocks/quartz_pillar");

    public static final LootTable BLOCKS_QUARTZ_SLAB = getTable("blocks/quartz_slab");

    public static final LootTable BLOCKS_QUARTZ_STAIRS = getTable("blocks/quartz_stairs");

    public static final LootTable BLOCKS_RAIL = getTable("blocks/rail");

    public static final LootTable BLOCKS_RAW_COPPER_BLOCK = getTable("blocks/raw_copper_block");

    public static final LootTable BLOCKS_RAW_GOLD_BLOCK = getTable("blocks/raw_gold_block");

    public static final LootTable BLOCKS_RAW_IRON_BLOCK = getTable("blocks/raw_iron_block");

    public static final LootTable BLOCKS_RED_BANNER = getTable("blocks/red_banner");

    public static final LootTable BLOCKS_RED_BED = getTable("blocks/red_bed");

    public static final LootTable BLOCKS_RED_CANDLE = getTable("blocks/red_candle");

    public static final LootTable BLOCKS_RED_CANDLE_CAKE = getTable("blocks/red_candle_cake");

    public static final LootTable BLOCKS_RED_CARPET = getTable("blocks/red_carpet");

    public static final LootTable BLOCKS_RED_CONCRETE = getTable("blocks/red_concrete");

    public static final LootTable BLOCKS_RED_CONCRETE_POWDER = getTable("blocks/red_concrete_powder");

    public static final LootTable BLOCKS_RED_GLAZED_TERRACOTTA = getTable("blocks/red_glazed_terracotta");

    public static final LootTable BLOCKS_RED_MUSHROOM = getTable("blocks/red_mushroom");

    public static final LootTable BLOCKS_RED_MUSHROOM_BLOCK = getTable("blocks/red_mushroom_block");

    public static final LootTable BLOCKS_RED_NETHER_BRICK_SLAB = getTable("blocks/red_nether_brick_slab");

    public static final LootTable BLOCKS_RED_NETHER_BRICK_STAIRS = getTable("blocks/red_nether_brick_stairs");

    public static final LootTable BLOCKS_RED_NETHER_BRICK_WALL = getTable("blocks/red_nether_brick_wall");

    public static final LootTable BLOCKS_RED_NETHER_BRICKS = getTable("blocks/red_nether_bricks");

    public static final LootTable BLOCKS_RED_SAND = getTable("blocks/red_sand");

    public static final LootTable BLOCKS_RED_SANDSTONE = getTable("blocks/red_sandstone");

    public static final LootTable BLOCKS_RED_SANDSTONE_SLAB = getTable("blocks/red_sandstone_slab");

    public static final LootTable BLOCKS_RED_SANDSTONE_STAIRS = getTable("blocks/red_sandstone_stairs");

    public static final LootTable BLOCKS_RED_SANDSTONE_WALL = getTable("blocks/red_sandstone_wall");

    public static final LootTable BLOCKS_RED_SHULKER_BOX = getTable("blocks/red_shulker_box");

    public static final LootTable BLOCKS_RED_STAINED_GLASS = getTable("blocks/red_stained_glass");

    public static final LootTable BLOCKS_RED_STAINED_GLASS_PANE = getTable("blocks/red_stained_glass_pane");

    public static final LootTable BLOCKS_RED_TERRACOTTA = getTable("blocks/red_terracotta");

    public static final LootTable BLOCKS_RED_TULIP = getTable("blocks/red_tulip");

    public static final LootTable BLOCKS_RED_WOOL = getTable("blocks/red_wool");

    public static final LootTable BLOCKS_REDSTONE_BLOCK = getTable("blocks/redstone_block");

    public static final LootTable BLOCKS_REDSTONE_LAMP = getTable("blocks/redstone_lamp");

    public static final LootTable BLOCKS_REDSTONE_ORE = getTable("blocks/redstone_ore");

    public static final LootTable BLOCKS_REDSTONE_TORCH = getTable("blocks/redstone_torch");

    public static final LootTable BLOCKS_REDSTONE_WIRE = getTable("blocks/redstone_wire");

    public static final LootTable BLOCKS_REINFORCED_DEEPSLATE = getTable("blocks/reinforced_deepslate");

    public static final LootTable BLOCKS_REPEATER = getTable("blocks/repeater");

    public static final LootTable BLOCKS_RESIN_BLOCK = getTable("blocks/resin_block");

    public static final LootTable BLOCKS_RESIN_BRICK_SLAB = getTable("blocks/resin_brick_slab");

    public static final LootTable BLOCKS_RESIN_BRICK_STAIRS = getTable("blocks/resin_brick_stairs");

    public static final LootTable BLOCKS_RESIN_BRICK_WALL = getTable("blocks/resin_brick_wall");

    public static final LootTable BLOCKS_RESIN_BRICKS = getTable("blocks/resin_bricks");

    public static final LootTable BLOCKS_RESIN_CLUMP = getTable("blocks/resin_clump");

    public static final LootTable BLOCKS_RESPAWN_ANCHOR = getTable("blocks/respawn_anchor");

    public static final LootTable BLOCKS_ROOTED_DIRT = getTable("blocks/rooted_dirt");

    public static final LootTable BLOCKS_ROSE_BUSH = getTable("blocks/rose_bush");

    public static final LootTable BLOCKS_SAND = getTable("blocks/sand");

    public static final LootTable BLOCKS_SANDSTONE = getTable("blocks/sandstone");

    public static final LootTable BLOCKS_SANDSTONE_SLAB = getTable("blocks/sandstone_slab");

    public static final LootTable BLOCKS_SANDSTONE_STAIRS = getTable("blocks/sandstone_stairs");

    public static final LootTable BLOCKS_SANDSTONE_WALL = getTable("blocks/sandstone_wall");

    public static final LootTable BLOCKS_SCAFFOLDING = getTable("blocks/scaffolding");

    public static final LootTable BLOCKS_SCULK = getTable("blocks/sculk");

    public static final LootTable BLOCKS_SCULK_CATALYST = getTable("blocks/sculk_catalyst");

    public static final LootTable BLOCKS_SCULK_SENSOR = getTable("blocks/sculk_sensor");

    public static final LootTable BLOCKS_SCULK_SHRIEKER = getTable("blocks/sculk_shrieker");

    public static final LootTable BLOCKS_SCULK_VEIN = getTable("blocks/sculk_vein");

    public static final LootTable BLOCKS_SEA_LANTERN = getTable("blocks/sea_lantern");

    public static final LootTable BLOCKS_SEA_PICKLE = getTable("blocks/sea_pickle");

    public static final LootTable BLOCKS_SEAGRASS = getTable("blocks/seagrass");

    public static final LootTable BLOCKS_SHORT_DRY_GRASS = getTable("blocks/short_dry_grass");

    public static final LootTable BLOCKS_SHORT_GRASS = getTable("blocks/short_grass");

    public static final LootTable BLOCKS_SHROOMLIGHT = getTable("blocks/shroomlight");

    public static final LootTable BLOCKS_SHULKER_BOX = getTable("blocks/shulker_box");

    public static final LootTable BLOCKS_SKELETON_SKULL = getTable("blocks/skeleton_skull");

    public static final LootTable BLOCKS_SLIME_BLOCK = getTable("blocks/slime_block");

    public static final LootTable BLOCKS_SMALL_AMETHYST_BUD = getTable("blocks/small_amethyst_bud");

    public static final LootTable BLOCKS_SMALL_DRIPLEAF = getTable("blocks/small_dripleaf");

    public static final LootTable BLOCKS_SMITHING_TABLE = getTable("blocks/smithing_table");

    public static final LootTable BLOCKS_SMOKER = getTable("blocks/smoker");

    public static final LootTable BLOCKS_SMOOTH_BASALT = getTable("blocks/smooth_basalt");

    public static final LootTable BLOCKS_SMOOTH_QUARTZ = getTable("blocks/smooth_quartz");

    public static final LootTable BLOCKS_SMOOTH_QUARTZ_SLAB = getTable("blocks/smooth_quartz_slab");

    public static final LootTable BLOCKS_SMOOTH_QUARTZ_STAIRS = getTable("blocks/smooth_quartz_stairs");

    public static final LootTable BLOCKS_SMOOTH_RED_SANDSTONE = getTable("blocks/smooth_red_sandstone");

    public static final LootTable BLOCKS_SMOOTH_RED_SANDSTONE_SLAB = getTable("blocks/smooth_red_sandstone_slab");

    public static final LootTable BLOCKS_SMOOTH_RED_SANDSTONE_STAIRS = getTable("blocks/smooth_red_sandstone_stairs");

    public static final LootTable BLOCKS_SMOOTH_SANDSTONE = getTable("blocks/smooth_sandstone");

    public static final LootTable BLOCKS_SMOOTH_SANDSTONE_SLAB = getTable("blocks/smooth_sandstone_slab");

    public static final LootTable BLOCKS_SMOOTH_SANDSTONE_STAIRS = getTable("blocks/smooth_sandstone_stairs");

    public static final LootTable BLOCKS_SMOOTH_STONE = getTable("blocks/smooth_stone");

    public static final LootTable BLOCKS_SMOOTH_STONE_SLAB = getTable("blocks/smooth_stone_slab");

    public static final LootTable BLOCKS_SNIFFER_EGG = getTable("blocks/sniffer_egg");

    public static final LootTable BLOCKS_SNOW = getTable("blocks/snow");

    public static final LootTable BLOCKS_SNOW_BLOCK = getTable("blocks/snow_block");

    public static final LootTable BLOCKS_SOUL_CAMPFIRE = getTable("blocks/soul_campfire");

    public static final LootTable BLOCKS_SOUL_FIRE = getTable("blocks/soul_fire");

    public static final LootTable BLOCKS_SOUL_LANTERN = getTable("blocks/soul_lantern");

    public static final LootTable BLOCKS_SOUL_SAND = getTable("blocks/soul_sand");

    public static final LootTable BLOCKS_SOUL_SOIL = getTable("blocks/soul_soil");

    public static final LootTable BLOCKS_SOUL_TORCH = getTable("blocks/soul_torch");

    public static final LootTable BLOCKS_SPAWNER = getTable("blocks/spawner");

    public static final LootTable BLOCKS_SPONGE = getTable("blocks/sponge");

    public static final LootTable BLOCKS_SPORE_BLOSSOM = getTable("blocks/spore_blossom");

    public static final LootTable BLOCKS_SPRUCE_BUTTON = getTable("blocks/spruce_button");

    public static final LootTable BLOCKS_SPRUCE_DOOR = getTable("blocks/spruce_door");

    public static final LootTable BLOCKS_SPRUCE_FENCE = getTable("blocks/spruce_fence");

    public static final LootTable BLOCKS_SPRUCE_FENCE_GATE = getTable("blocks/spruce_fence_gate");

    public static final LootTable BLOCKS_SPRUCE_HANGING_SIGN = getTable("blocks/spruce_hanging_sign");

    public static final LootTable BLOCKS_SPRUCE_LEAVES = getTable("blocks/spruce_leaves");

    public static final LootTable BLOCKS_SPRUCE_LOG = getTable("blocks/spruce_log");

    public static final LootTable BLOCKS_SPRUCE_PLANKS = getTable("blocks/spruce_planks");

    public static final LootTable BLOCKS_SPRUCE_PRESSURE_PLATE = getTable("blocks/spruce_pressure_plate");

    public static final LootTable BLOCKS_SPRUCE_SAPLING = getTable("blocks/spruce_sapling");

    public static final LootTable BLOCKS_SPRUCE_SHELF = getTable("blocks/spruce_shelf");

    public static final LootTable BLOCKS_SPRUCE_SIGN = getTable("blocks/spruce_sign");

    public static final LootTable BLOCKS_SPRUCE_SLAB = getTable("blocks/spruce_slab");

    public static final LootTable BLOCKS_SPRUCE_STAIRS = getTable("blocks/spruce_stairs");

    public static final LootTable BLOCKS_SPRUCE_TRAPDOOR = getTable("blocks/spruce_trapdoor");

    public static final LootTable BLOCKS_SPRUCE_WOOD = getTable("blocks/spruce_wood");

    public static final LootTable BLOCKS_STICKY_PISTON = getTable("blocks/sticky_piston");

    public static final LootTable BLOCKS_STONE = getTable("blocks/stone");

    public static final LootTable BLOCKS_STONE_BRICK_SLAB = getTable("blocks/stone_brick_slab");

    public static final LootTable BLOCKS_STONE_BRICK_STAIRS = getTable("blocks/stone_brick_stairs");

    public static final LootTable BLOCKS_STONE_BRICK_WALL = getTable("blocks/stone_brick_wall");

    public static final LootTable BLOCKS_STONE_BRICKS = getTable("blocks/stone_bricks");

    public static final LootTable BLOCKS_STONE_BUTTON = getTable("blocks/stone_button");

    public static final LootTable BLOCKS_STONE_PRESSURE_PLATE = getTable("blocks/stone_pressure_plate");

    public static final LootTable BLOCKS_STONE_SLAB = getTable("blocks/stone_slab");

    public static final LootTable BLOCKS_STONE_STAIRS = getTable("blocks/stone_stairs");

    public static final LootTable BLOCKS_STONECUTTER = getTable("blocks/stonecutter");

    public static final LootTable BLOCKS_STRIPPED_ACACIA_LOG = getTable("blocks/stripped_acacia_log");

    public static final LootTable BLOCKS_STRIPPED_ACACIA_WOOD = getTable("blocks/stripped_acacia_wood");

    public static final LootTable BLOCKS_STRIPPED_BAMBOO_BLOCK = getTable("blocks/stripped_bamboo_block");

    public static final LootTable BLOCKS_STRIPPED_BIRCH_LOG = getTable("blocks/stripped_birch_log");

    public static final LootTable BLOCKS_STRIPPED_BIRCH_WOOD = getTable("blocks/stripped_birch_wood");

    public static final LootTable BLOCKS_STRIPPED_CHERRY_LOG = getTable("blocks/stripped_cherry_log");

    public static final LootTable BLOCKS_STRIPPED_CHERRY_WOOD = getTable("blocks/stripped_cherry_wood");

    public static final LootTable BLOCKS_STRIPPED_CRIMSON_HYPHAE = getTable("blocks/stripped_crimson_hyphae");

    public static final LootTable BLOCKS_STRIPPED_CRIMSON_STEM = getTable("blocks/stripped_crimson_stem");

    public static final LootTable BLOCKS_STRIPPED_DARK_OAK_LOG = getTable("blocks/stripped_dark_oak_log");

    public static final LootTable BLOCKS_STRIPPED_DARK_OAK_WOOD = getTable("blocks/stripped_dark_oak_wood");

    public static final LootTable BLOCKS_STRIPPED_JUNGLE_LOG = getTable("blocks/stripped_jungle_log");

    public static final LootTable BLOCKS_STRIPPED_JUNGLE_WOOD = getTable("blocks/stripped_jungle_wood");

    public static final LootTable BLOCKS_STRIPPED_MANGROVE_LOG = getTable("blocks/stripped_mangrove_log");

    public static final LootTable BLOCKS_STRIPPED_MANGROVE_WOOD = getTable("blocks/stripped_mangrove_wood");

    public static final LootTable BLOCKS_STRIPPED_OAK_LOG = getTable("blocks/stripped_oak_log");

    public static final LootTable BLOCKS_STRIPPED_OAK_WOOD = getTable("blocks/stripped_oak_wood");

    public static final LootTable BLOCKS_STRIPPED_PALE_OAK_LOG = getTable("blocks/stripped_pale_oak_log");

    public static final LootTable BLOCKS_STRIPPED_PALE_OAK_WOOD = getTable("blocks/stripped_pale_oak_wood");

    public static final LootTable BLOCKS_STRIPPED_SPRUCE_LOG = getTable("blocks/stripped_spruce_log");

    public static final LootTable BLOCKS_STRIPPED_SPRUCE_WOOD = getTable("blocks/stripped_spruce_wood");

    public static final LootTable BLOCKS_STRIPPED_WARPED_HYPHAE = getTable("blocks/stripped_warped_hyphae");

    public static final LootTable BLOCKS_STRIPPED_WARPED_STEM = getTable("blocks/stripped_warped_stem");

    public static final LootTable BLOCKS_SUGAR_CANE = getTable("blocks/sugar_cane");

    public static final LootTable BLOCKS_SUNFLOWER = getTable("blocks/sunflower");

    public static final LootTable BLOCKS_SUSPICIOUS_GRAVEL = getTable("blocks/suspicious_gravel");

    public static final LootTable BLOCKS_SUSPICIOUS_SAND = getTable("blocks/suspicious_sand");

    public static final LootTable BLOCKS_SWEET_BERRY_BUSH = getTable("blocks/sweet_berry_bush");

    public static final LootTable BLOCKS_TALL_DRY_GRASS = getTable("blocks/tall_dry_grass");

    public static final LootTable BLOCKS_TALL_GRASS = getTable("blocks/tall_grass");

    public static final LootTable BLOCKS_TALL_SEAGRASS = getTable("blocks/tall_seagrass");

    public static final LootTable BLOCKS_TARGET = getTable("blocks/target");

    public static final LootTable BLOCKS_TERRACOTTA = getTable("blocks/terracotta");

    public static final LootTable BLOCKS_TINTED_GLASS = getTable("blocks/tinted_glass");

    public static final LootTable BLOCKS_TNT = getTable("blocks/tnt");

    public static final LootTable BLOCKS_TORCH = getTable("blocks/torch");

    public static final LootTable BLOCKS_TORCHFLOWER = getTable("blocks/torchflower");

    public static final LootTable BLOCKS_TORCHFLOWER_CROP = getTable("blocks/torchflower_crop");

    public static final LootTable BLOCKS_TRAPPED_CHEST = getTable("blocks/trapped_chest");

    public static final LootTable BLOCKS_TRIAL_SPAWNER = getTable("blocks/trial_spawner");

    public static final LootTable BLOCKS_TRIPWIRE = getTable("blocks/tripwire");

    public static final LootTable BLOCKS_TRIPWIRE_HOOK = getTable("blocks/tripwire_hook");

    public static final LootTable BLOCKS_TUBE_CORAL = getTable("blocks/tube_coral");

    public static final LootTable BLOCKS_TUBE_CORAL_BLOCK = getTable("blocks/tube_coral_block");

    public static final LootTable BLOCKS_TUBE_CORAL_FAN = getTable("blocks/tube_coral_fan");

    public static final LootTable BLOCKS_TUFF = getTable("blocks/tuff");

    public static final LootTable BLOCKS_TUFF_BRICK_SLAB = getTable("blocks/tuff_brick_slab");

    public static final LootTable BLOCKS_TUFF_BRICK_STAIRS = getTable("blocks/tuff_brick_stairs");

    public static final LootTable BLOCKS_TUFF_BRICK_WALL = getTable("blocks/tuff_brick_wall");

    public static final LootTable BLOCKS_TUFF_BRICKS = getTable("blocks/tuff_bricks");

    public static final LootTable BLOCKS_TUFF_SLAB = getTable("blocks/tuff_slab");

    public static final LootTable BLOCKS_TUFF_STAIRS = getTable("blocks/tuff_stairs");

    public static final LootTable BLOCKS_TUFF_WALL = getTable("blocks/tuff_wall");

    public static final LootTable BLOCKS_TURTLE_EGG = getTable("blocks/turtle_egg");

    public static final LootTable BLOCKS_TWISTING_VINES = getTable("blocks/twisting_vines");

    public static final LootTable BLOCKS_TWISTING_VINES_PLANT = getTable("blocks/twisting_vines_plant");

    public static final LootTable BLOCKS_VAULT = getTable("blocks/vault");

    public static final LootTable BLOCKS_VERDANT_FROGLIGHT = getTable("blocks/verdant_froglight");

    public static final LootTable BLOCKS_VINE = getTable("blocks/vine");

    public static final LootTable BLOCKS_WARPED_BUTTON = getTable("blocks/warped_button");

    public static final LootTable BLOCKS_WARPED_DOOR = getTable("blocks/warped_door");

    public static final LootTable BLOCKS_WARPED_FENCE = getTable("blocks/warped_fence");

    public static final LootTable BLOCKS_WARPED_FENCE_GATE = getTable("blocks/warped_fence_gate");

    public static final LootTable BLOCKS_WARPED_FUNGUS = getTable("blocks/warped_fungus");

    public static final LootTable BLOCKS_WARPED_HANGING_SIGN = getTable("blocks/warped_hanging_sign");

    public static final LootTable BLOCKS_WARPED_HYPHAE = getTable("blocks/warped_hyphae");

    public static final LootTable BLOCKS_WARPED_NYLIUM = getTable("blocks/warped_nylium");

    public static final LootTable BLOCKS_WARPED_PLANKS = getTable("blocks/warped_planks");

    public static final LootTable BLOCKS_WARPED_PRESSURE_PLATE = getTable("blocks/warped_pressure_plate");

    public static final LootTable BLOCKS_WARPED_ROOTS = getTable("blocks/warped_roots");

    public static final LootTable BLOCKS_WARPED_SHELF = getTable("blocks/warped_shelf");

    public static final LootTable BLOCKS_WARPED_SIGN = getTable("blocks/warped_sign");

    public static final LootTable BLOCKS_WARPED_SLAB = getTable("blocks/warped_slab");

    public static final LootTable BLOCKS_WARPED_STAIRS = getTable("blocks/warped_stairs");

    public static final LootTable BLOCKS_WARPED_STEM = getTable("blocks/warped_stem");

    public static final LootTable BLOCKS_WARPED_TRAPDOOR = getTable("blocks/warped_trapdoor");

    public static final LootTable BLOCKS_WARPED_WART_BLOCK = getTable("blocks/warped_wart_block");

    public static final LootTable BLOCKS_WATER_CAULDRON = getTable("blocks/water_cauldron");

    public static final LootTable BLOCKS_WAXED_CHISELED_COPPER = getTable("blocks/waxed_chiseled_copper");

    public static final LootTable BLOCKS_WAXED_COPPER_BARS = getTable("blocks/waxed_copper_bars");

    public static final LootTable BLOCKS_WAXED_COPPER_BLOCK = getTable("blocks/waxed_copper_block");

    public static final LootTable BLOCKS_WAXED_COPPER_BULB = getTable("blocks/waxed_copper_bulb");

    public static final LootTable BLOCKS_WAXED_COPPER_CHAIN = getTable("blocks/waxed_copper_chain");

    public static final LootTable BLOCKS_WAXED_COPPER_CHEST = getTable("blocks/waxed_copper_chest");

    public static final LootTable BLOCKS_WAXED_COPPER_DOOR = getTable("blocks/waxed_copper_door");

    public static final LootTable BLOCKS_WAXED_COPPER_GOLEM_STATUE = getTable("blocks/waxed_copper_golem_statue");

    public static final LootTable BLOCKS_WAXED_COPPER_GRATE = getTable("blocks/waxed_copper_grate");

    public static final LootTable BLOCKS_WAXED_COPPER_LANTERN = getTable("blocks/waxed_copper_lantern");

    public static final LootTable BLOCKS_WAXED_COPPER_TRAPDOOR = getTable("blocks/waxed_copper_trapdoor");

    public static final LootTable BLOCKS_WAXED_CUT_COPPER = getTable("blocks/waxed_cut_copper");

    public static final LootTable BLOCKS_WAXED_CUT_COPPER_SLAB = getTable("blocks/waxed_cut_copper_slab");

    public static final LootTable BLOCKS_WAXED_CUT_COPPER_STAIRS = getTable("blocks/waxed_cut_copper_stairs");

    public static final LootTable BLOCKS_WAXED_EXPOSED_CHISELED_COPPER = getTable("blocks/waxed_exposed_chiseled_copper");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER = getTable("blocks/waxed_exposed_copper");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_BARS = getTable("blocks/waxed_exposed_copper_bars");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_BULB = getTable("blocks/waxed_exposed_copper_bulb");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_CHAIN = getTable("blocks/waxed_exposed_copper_chain");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_CHEST = getTable("blocks/waxed_exposed_copper_chest");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_DOOR = getTable("blocks/waxed_exposed_copper_door");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_GOLEM_STATUE = getTable("blocks/waxed_exposed_copper_golem_statue");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_GRATE = getTable("blocks/waxed_exposed_copper_grate");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_LANTERN = getTable("blocks/waxed_exposed_copper_lantern");

    public static final LootTable BLOCKS_WAXED_EXPOSED_COPPER_TRAPDOOR = getTable("blocks/waxed_exposed_copper_trapdoor");

    public static final LootTable BLOCKS_WAXED_EXPOSED_CUT_COPPER = getTable("blocks/waxed_exposed_cut_copper");

    public static final LootTable BLOCKS_WAXED_EXPOSED_CUT_COPPER_SLAB = getTable("blocks/waxed_exposed_cut_copper_slab");

    public static final LootTable BLOCKS_WAXED_EXPOSED_CUT_COPPER_STAIRS = getTable("blocks/waxed_exposed_cut_copper_stairs");

    public static final LootTable BLOCKS_WAXED_EXPOSED_LIGHTNING_ROD = getTable("blocks/waxed_exposed_lightning_rod");

    public static final LootTable BLOCKS_WAXED_LIGHTNING_ROD = getTable("blocks/waxed_lightning_rod");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_CHISELED_COPPER = getTable("blocks/waxed_oxidized_chiseled_copper");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER = getTable("blocks/waxed_oxidized_copper");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_BARS = getTable("blocks/waxed_oxidized_copper_bars");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_BULB = getTable("blocks/waxed_oxidized_copper_bulb");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_CHAIN = getTable("blocks/waxed_oxidized_copper_chain");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_CHEST = getTable("blocks/waxed_oxidized_copper_chest");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_DOOR = getTable("blocks/waxed_oxidized_copper_door");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_GOLEM_STATUE = getTable("blocks/waxed_oxidized_copper_golem_statue");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_GRATE = getTable("blocks/waxed_oxidized_copper_grate");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_LANTERN = getTable("blocks/waxed_oxidized_copper_lantern");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_COPPER_TRAPDOOR = getTable("blocks/waxed_oxidized_copper_trapdoor");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_CUT_COPPER = getTable("blocks/waxed_oxidized_cut_copper");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_CUT_COPPER_SLAB = getTable("blocks/waxed_oxidized_cut_copper_slab");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_CUT_COPPER_STAIRS = getTable("blocks/waxed_oxidized_cut_copper_stairs");

    public static final LootTable BLOCKS_WAXED_OXIDIZED_LIGHTNING_ROD = getTable("blocks/waxed_oxidized_lightning_rod");

    public static final LootTable BLOCKS_WAXED_WEATHERED_CHISELED_COPPER = getTable("blocks/waxed_weathered_chiseled_copper");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER = getTable("blocks/waxed_weathered_copper");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_BARS = getTable("blocks/waxed_weathered_copper_bars");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_BULB = getTable("blocks/waxed_weathered_copper_bulb");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_CHAIN = getTable("blocks/waxed_weathered_copper_chain");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_CHEST = getTable("blocks/waxed_weathered_copper_chest");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_DOOR = getTable("blocks/waxed_weathered_copper_door");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_GOLEM_STATUE = getTable("blocks/waxed_weathered_copper_golem_statue");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_GRATE = getTable("blocks/waxed_weathered_copper_grate");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_LANTERN = getTable("blocks/waxed_weathered_copper_lantern");

    public static final LootTable BLOCKS_WAXED_WEATHERED_COPPER_TRAPDOOR = getTable("blocks/waxed_weathered_copper_trapdoor");

    public static final LootTable BLOCKS_WAXED_WEATHERED_CUT_COPPER = getTable("blocks/waxed_weathered_cut_copper");

    public static final LootTable BLOCKS_WAXED_WEATHERED_CUT_COPPER_SLAB = getTable("blocks/waxed_weathered_cut_copper_slab");

    public static final LootTable BLOCKS_WAXED_WEATHERED_CUT_COPPER_STAIRS = getTable("blocks/waxed_weathered_cut_copper_stairs");

    public static final LootTable BLOCKS_WAXED_WEATHERED_LIGHTNING_ROD = getTable("blocks/waxed_weathered_lightning_rod");

    public static final LootTable BLOCKS_WEATHERED_CHISELED_COPPER = getTable("blocks/weathered_chiseled_copper");

    public static final LootTable BLOCKS_WEATHERED_COPPER = getTable("blocks/weathered_copper");

    public static final LootTable BLOCKS_WEATHERED_COPPER_BARS = getTable("blocks/weathered_copper_bars");

    public static final LootTable BLOCKS_WEATHERED_COPPER_BULB = getTable("blocks/weathered_copper_bulb");

    public static final LootTable BLOCKS_WEATHERED_COPPER_CHAIN = getTable("blocks/weathered_copper_chain");

    public static final LootTable BLOCKS_WEATHERED_COPPER_CHEST = getTable("blocks/weathered_copper_chest");

    public static final LootTable BLOCKS_WEATHERED_COPPER_DOOR = getTable("blocks/weathered_copper_door");

    public static final LootTable BLOCKS_WEATHERED_COPPER_GOLEM_STATUE = getTable("blocks/weathered_copper_golem_statue");

    public static final LootTable BLOCKS_WEATHERED_COPPER_GRATE = getTable("blocks/weathered_copper_grate");

    public static final LootTable BLOCKS_WEATHERED_COPPER_LANTERN = getTable("blocks/weathered_copper_lantern");

    public static final LootTable BLOCKS_WEATHERED_COPPER_TRAPDOOR = getTable("blocks/weathered_copper_trapdoor");

    public static final LootTable BLOCKS_WEATHERED_CUT_COPPER = getTable("blocks/weathered_cut_copper");

    public static final LootTable BLOCKS_WEATHERED_CUT_COPPER_SLAB = getTable("blocks/weathered_cut_copper_slab");

    public static final LootTable BLOCKS_WEATHERED_CUT_COPPER_STAIRS = getTable("blocks/weathered_cut_copper_stairs");

    public static final LootTable BLOCKS_WEATHERED_LIGHTNING_ROD = getTable("blocks/weathered_lightning_rod");

    public static final LootTable BLOCKS_WEEPING_VINES = getTable("blocks/weeping_vines");

    public static final LootTable BLOCKS_WEEPING_VINES_PLANT = getTable("blocks/weeping_vines_plant");

    public static final LootTable BLOCKS_WET_SPONGE = getTable("blocks/wet_sponge");

    public static final LootTable BLOCKS_WHEAT = getTable("blocks/wheat");

    public static final LootTable BLOCKS_WHITE_BANNER = getTable("blocks/white_banner");

    public static final LootTable BLOCKS_WHITE_BED = getTable("blocks/white_bed");

    public static final LootTable BLOCKS_WHITE_CANDLE = getTable("blocks/white_candle");

    public static final LootTable BLOCKS_WHITE_CANDLE_CAKE = getTable("blocks/white_candle_cake");

    public static final LootTable BLOCKS_WHITE_CARPET = getTable("blocks/white_carpet");

    public static final LootTable BLOCKS_WHITE_CONCRETE = getTable("blocks/white_concrete");

    public static final LootTable BLOCKS_WHITE_CONCRETE_POWDER = getTable("blocks/white_concrete_powder");

    public static final LootTable BLOCKS_WHITE_GLAZED_TERRACOTTA = getTable("blocks/white_glazed_terracotta");

    public static final LootTable BLOCKS_WHITE_SHULKER_BOX = getTable("blocks/white_shulker_box");

    public static final LootTable BLOCKS_WHITE_STAINED_GLASS = getTable("blocks/white_stained_glass");

    public static final LootTable BLOCKS_WHITE_STAINED_GLASS_PANE = getTable("blocks/white_stained_glass_pane");

    public static final LootTable BLOCKS_WHITE_TERRACOTTA = getTable("blocks/white_terracotta");

    public static final LootTable BLOCKS_WHITE_TULIP = getTable("blocks/white_tulip");

    public static final LootTable BLOCKS_WHITE_WOOL = getTable("blocks/white_wool");

    public static final LootTable BLOCKS_WILDFLOWERS = getTable("blocks/wildflowers");

    public static final LootTable BLOCKS_WITHER_ROSE = getTable("blocks/wither_rose");

    public static final LootTable BLOCKS_WITHER_SKELETON_SKULL = getTable("blocks/wither_skeleton_skull");

    public static final LootTable BLOCKS_YELLOW_BANNER = getTable("blocks/yellow_banner");

    public static final LootTable BLOCKS_YELLOW_BED = getTable("blocks/yellow_bed");

    public static final LootTable BLOCKS_YELLOW_CANDLE = getTable("blocks/yellow_candle");

    public static final LootTable BLOCKS_YELLOW_CANDLE_CAKE = getTable("blocks/yellow_candle_cake");

    public static final LootTable BLOCKS_YELLOW_CARPET = getTable("blocks/yellow_carpet");

    public static final LootTable BLOCKS_YELLOW_CONCRETE = getTable("blocks/yellow_concrete");

    public static final LootTable BLOCKS_YELLOW_CONCRETE_POWDER = getTable("blocks/yellow_concrete_powder");

    public static final LootTable BLOCKS_YELLOW_GLAZED_TERRACOTTA = getTable("blocks/yellow_glazed_terracotta");

    public static final LootTable BLOCKS_YELLOW_SHULKER_BOX = getTable("blocks/yellow_shulker_box");

    public static final LootTable BLOCKS_YELLOW_STAINED_GLASS = getTable("blocks/yellow_stained_glass");

    public static final LootTable BLOCKS_YELLOW_STAINED_GLASS_PANE = getTable("blocks/yellow_stained_glass_pane");

    public static final LootTable BLOCKS_YELLOW_TERRACOTTA = getTable("blocks/yellow_terracotta");

    public static final LootTable BLOCKS_YELLOW_WOOL = getTable("blocks/yellow_wool");

    public static final LootTable BLOCKS_ZOMBIE_HEAD = getTable("blocks/zombie_head");

    public static final LootTable BRUSH_ARMADILLO = getTable("brush/armadillo");

    public static final LootTable CARVE_PUMPKIN = getTable("carve/pumpkin");

    public static final LootTable CHARGED_CREEPER_CREEPER = getTable("charged_creeper/creeper");

    public static final LootTable CHARGED_CREEPER_PIGLIN = getTable("charged_creeper/piglin");

    public static final LootTable CHARGED_CREEPER_ROOT = getTable("charged_creeper/root");

    public static final LootTable CHARGED_CREEPER_SKELETON = getTable("charged_creeper/skeleton");

    public static final LootTable CHARGED_CREEPER_WITHER_SKELETON = getTable("charged_creeper/wither_skeleton");

    public static final LootTable CHARGED_CREEPER_ZOMBIE = getTable("charged_creeper/zombie");

    public static final LootTable CHESTS_ABANDONED_MINESHAFT = getTable("chests/abandoned_mineshaft");

    public static final LootTable CHESTS_ANCIENT_CITY = getTable("chests/ancient_city");

    public static final LootTable CHESTS_ANCIENT_CITY_ICE_BOX = getTable("chests/ancient_city_ice_box");

    public static final LootTable CHESTS_BASTION_BRIDGE = getTable("chests/bastion_bridge");

    public static final LootTable CHESTS_BASTION_HOGLIN_STABLE = getTable("chests/bastion_hoglin_stable");

    public static final LootTable CHESTS_BASTION_OTHER = getTable("chests/bastion_other");

    public static final LootTable CHESTS_BASTION_TREASURE = getTable("chests/bastion_treasure");

    public static final LootTable CHESTS_BURIED_TREASURE = getTable("chests/buried_treasure");

    public static final LootTable CHESTS_DESERT_PYRAMID = getTable("chests/desert_pyramid");

    public static final LootTable CHESTS_END_CITY_TREASURE = getTable("chests/end_city_treasure");

    public static final LootTable CHESTS_IGLOO_CHEST = getTable("chests/igloo_chest");

    public static final LootTable CHESTS_JUNGLE_TEMPLE = getTable("chests/jungle_temple");

    public static final LootTable CHESTS_JUNGLE_TEMPLE_DISPENSER = getTable("chests/jungle_temple_dispenser");

    public static final LootTable CHESTS_NETHER_BRIDGE = getTable("chests/nether_bridge");

    public static final LootTable CHESTS_PILLAGER_OUTPOST = getTable("chests/pillager_outpost");

    public static final LootTable CHESTS_RUINED_PORTAL = getTable("chests/ruined_portal");

    public static final LootTable CHESTS_SHIPWRECK_MAP = getTable("chests/shipwreck_map");

    public static final LootTable CHESTS_SHIPWRECK_SUPPLY = getTable("chests/shipwreck_supply");

    public static final LootTable CHESTS_SHIPWRECK_TREASURE = getTable("chests/shipwreck_treasure");

    public static final LootTable CHESTS_SIMPLE_DUNGEON = getTable("chests/simple_dungeon");

    public static final LootTable CHESTS_SPAWN_BONUS_CHEST = getTable("chests/spawn_bonus_chest");

    public static final LootTable CHESTS_STRONGHOLD_CORRIDOR = getTable("chests/stronghold_corridor");

    public static final LootTable CHESTS_STRONGHOLD_CROSSING = getTable("chests/stronghold_crossing");

    public static final LootTable CHESTS_STRONGHOLD_LIBRARY = getTable("chests/stronghold_library");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_CORRIDOR = getTable("chests/trial_chambers/corridor");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_ENTRANCE = getTable("chests/trial_chambers/entrance");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_INTERSECTION = getTable("chests/trial_chambers/intersection");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_INTERSECTION_BARREL = getTable("chests/trial_chambers/intersection_barrel");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD = getTable("chests/trial_chambers/reward");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_COMMON = getTable("chests/trial_chambers/reward_common");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_OMINOUS = getTable("chests/trial_chambers/reward_ominous");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON = getTable("chests/trial_chambers/reward_ominous_common");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_OMINOUS_RARE = getTable("chests/trial_chambers/reward_ominous_rare");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE = getTable("chests/trial_chambers/reward_ominous_unique");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_RARE = getTable("chests/trial_chambers/reward_rare");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_REWARD_UNIQUE = getTable("chests/trial_chambers/reward_unique");

    public static final LootTable CHESTS_TRIAL_CHAMBERS_SUPPLY = getTable("chests/trial_chambers/supply");

    public static final LootTable CHESTS_UNDERWATER_RUIN_BIG = getTable("chests/underwater_ruin_big");

    public static final LootTable CHESTS_UNDERWATER_RUIN_SMALL = getTable("chests/underwater_ruin_small");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_ARMORER = getTable("chests/village/village_armorer");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_BUTCHER = getTable("chests/village/village_butcher");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER = getTable("chests/village/village_cartographer");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE = getTable("chests/village/village_desert_house");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_FISHER = getTable("chests/village/village_fisher");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_FLETCHER = getTable("chests/village/village_fletcher");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_MASON = getTable("chests/village/village_mason");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE = getTable("chests/village/village_plains_house");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE = getTable("chests/village/village_savanna_house");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_SHEPHERD = getTable("chests/village/village_shepherd");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE = getTable("chests/village/village_snowy_house");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE = getTable("chests/village/village_taiga_house");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_TANNERY = getTable("chests/village/village_tannery");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_TEMPLE = getTable("chests/village/village_temple");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_TOOLSMITH = getTable("chests/village/village_toolsmith");

    public static final LootTable CHESTS_VILLAGE_VILLAGE_WEAPONSMITH = getTable("chests/village/village_weaponsmith");

    public static final LootTable CHESTS_WOODLAND_MANSION = getTable("chests/woodland_mansion");

    public static final LootTable DISPENSERS_TRIAL_CHAMBERS_CHAMBER = getTable("dispensers/trial_chambers/chamber");

    public static final LootTable DISPENSERS_TRIAL_CHAMBERS_CORRIDOR = getTable("dispensers/trial_chambers/corridor");

    public static final LootTable DISPENSERS_TRIAL_CHAMBERS_WATER = getTable("dispensers/trial_chambers/water");

    public static final LootTable ENTITIES_ALLAY = getTable("entities/allay");

    public static final LootTable ENTITIES_ARMADILLO = getTable("entities/armadillo");

    public static final LootTable ENTITIES_ARMOR_STAND = getTable("entities/armor_stand");

    public static final LootTable ENTITIES_AXOLOTL = getTable("entities/axolotl");

    public static final LootTable ENTITIES_BAT = getTable("entities/bat");

    public static final LootTable ENTITIES_BEE = getTable("entities/bee");

    public static final LootTable ENTITIES_BLAZE = getTable("entities/blaze");

    public static final LootTable ENTITIES_BOGGED = getTable("entities/bogged");

    public static final LootTable ENTITIES_BREEZE = getTable("entities/breeze");

    public static final LootTable ENTITIES_CAMEL = getTable("entities/camel");

    public static final LootTable ENTITIES_CAMEL_HUSK = getTable("entities/camel_husk");

    public static final LootTable ENTITIES_CAT = getTable("entities/cat");

    public static final LootTable ENTITIES_CAVE_SPIDER = getTable("entities/cave_spider");

    public static final LootTable ENTITIES_CHICKEN = getTable("entities/chicken");

    public static final LootTable ENTITIES_COD = getTable("entities/cod");

    public static final LootTable ENTITIES_COPPER_GOLEM = getTable("entities/copper_golem");

    public static final LootTable ENTITIES_COW = getTable("entities/cow");

    public static final LootTable ENTITIES_CREAKING = getTable("entities/creaking");

    public static final LootTable ENTITIES_CREEPER = getTable("entities/creeper");

    public static final LootTable ENTITIES_DOLPHIN = getTable("entities/dolphin");

    public static final LootTable ENTITIES_DONKEY = getTable("entities/donkey");

    public static final LootTable ENTITIES_DROWNED = getTable("entities/drowned");

    public static final LootTable ENTITIES_ELDER_GUARDIAN = getTable("entities/elder_guardian");

    public static final LootTable ENTITIES_ENDER_DRAGON = getTable("entities/ender_dragon");

    public static final LootTable ENTITIES_ENDERMAN = getTable("entities/enderman");

    public static final LootTable ENTITIES_ENDERMITE = getTable("entities/endermite");

    public static final LootTable ENTITIES_EVOKER = getTable("entities/evoker");

    public static final LootTable ENTITIES_FOX = getTable("entities/fox");

    public static final LootTable ENTITIES_FROG = getTable("entities/frog");

    public static final LootTable ENTITIES_GHAST = getTable("entities/ghast");

    public static final LootTable ENTITIES_GIANT = getTable("entities/giant");

    public static final LootTable ENTITIES_GLOW_SQUID = getTable("entities/glow_squid");

    public static final LootTable ENTITIES_GOAT = getTable("entities/goat");

    public static final LootTable ENTITIES_GUARDIAN = getTable("entities/guardian");

    public static final LootTable ENTITIES_HAPPY_GHAST = getTable("entities/happy_ghast");

    public static final LootTable ENTITIES_HOGLIN = getTable("entities/hoglin");

    public static final LootTable ENTITIES_HORSE = getTable("entities/horse");

    public static final LootTable ENTITIES_HUSK = getTable("entities/husk");

    public static final LootTable ENTITIES_ILLUSIONER = getTable("entities/illusioner");

    public static final LootTable ENTITIES_IRON_GOLEM = getTable("entities/iron_golem");

    public static final LootTable ENTITIES_LLAMA = getTable("entities/llama");

    public static final LootTable ENTITIES_MAGMA_CUBE = getTable("entities/magma_cube");

    public static final LootTable ENTITIES_MANNEQUIN = getTable("entities/mannequin");

    public static final LootTable ENTITIES_MOOSHROOM = getTable("entities/mooshroom");

    public static final LootTable ENTITIES_MULE = getTable("entities/mule");

    public static final LootTable ENTITIES_NAUTILUS = getTable("entities/nautilus");

    public static final LootTable ENTITIES_OCELOT = getTable("entities/ocelot");

    public static final LootTable ENTITIES_PANDA = getTable("entities/panda");

    public static final LootTable ENTITIES_PARCHED = getTable("entities/parched");

    public static final LootTable ENTITIES_PARROT = getTable("entities/parrot");

    public static final LootTable ENTITIES_PHANTOM = getTable("entities/phantom");

    public static final LootTable ENTITIES_PIG = getTable("entities/pig");

    public static final LootTable ENTITIES_PIGLIN = getTable("entities/piglin");

    public static final LootTable ENTITIES_PIGLIN_BRUTE = getTable("entities/piglin_brute");

    public static final LootTable ENTITIES_PILLAGER = getTable("entities/pillager");

    public static final LootTable ENTITIES_PLAYER = getTable("entities/player");

    public static final LootTable ENTITIES_POLAR_BEAR = getTable("entities/polar_bear");

    public static final LootTable ENTITIES_PUFFERFISH = getTable("entities/pufferfish");

    public static final LootTable ENTITIES_RABBIT = getTable("entities/rabbit");

    public static final LootTable ENTITIES_RAVAGER = getTable("entities/ravager");

    public static final LootTable ENTITIES_SALMON = getTable("entities/salmon");

    public static final LootTable ENTITIES_SHEEP = getTable("entities/sheep");

    public static final LootTable ENTITIES_SHEEP_BLACK = getTable("entities/sheep/black");

    public static final LootTable ENTITIES_SHEEP_BLUE = getTable("entities/sheep/blue");

    public static final LootTable ENTITIES_SHEEP_BROWN = getTable("entities/sheep/brown");

    public static final LootTable ENTITIES_SHEEP_CYAN = getTable("entities/sheep/cyan");

    public static final LootTable ENTITIES_SHEEP_GRAY = getTable("entities/sheep/gray");

    public static final LootTable ENTITIES_SHEEP_GREEN = getTable("entities/sheep/green");

    public static final LootTable ENTITIES_SHEEP_LIGHT_BLUE = getTable("entities/sheep/light_blue");

    public static final LootTable ENTITIES_SHEEP_LIGHT_GRAY = getTable("entities/sheep/light_gray");

    public static final LootTable ENTITIES_SHEEP_LIME = getTable("entities/sheep/lime");

    public static final LootTable ENTITIES_SHEEP_MAGENTA = getTable("entities/sheep/magenta");

    public static final LootTable ENTITIES_SHEEP_ORANGE = getTable("entities/sheep/orange");

    public static final LootTable ENTITIES_SHEEP_PINK = getTable("entities/sheep/pink");

    public static final LootTable ENTITIES_SHEEP_PURPLE = getTable("entities/sheep/purple");

    public static final LootTable ENTITIES_SHEEP_RED = getTable("entities/sheep/red");

    public static final LootTable ENTITIES_SHEEP_WHITE = getTable("entities/sheep/white");

    public static final LootTable ENTITIES_SHEEP_YELLOW = getTable("entities/sheep/yellow");

    public static final LootTable ENTITIES_SHULKER = getTable("entities/shulker");

    public static final LootTable ENTITIES_SILVERFISH = getTable("entities/silverfish");

    public static final LootTable ENTITIES_SKELETON = getTable("entities/skeleton");

    public static final LootTable ENTITIES_SKELETON_HORSE = getTable("entities/skeleton_horse");

    public static final LootTable ENTITIES_SLIME = getTable("entities/slime");

    public static final LootTable ENTITIES_SNIFFER = getTable("entities/sniffer");

    public static final LootTable ENTITIES_SNOW_GOLEM = getTable("entities/snow_golem");

    public static final LootTable ENTITIES_SPIDER = getTable("entities/spider");

    public static final LootTable ENTITIES_SQUID = getTable("entities/squid");

    public static final LootTable ENTITIES_STRAY = getTable("entities/stray");

    public static final LootTable ENTITIES_STRIDER = getTable("entities/strider");

    public static final LootTable ENTITIES_TADPOLE = getTable("entities/tadpole");

    public static final LootTable ENTITIES_TRADER_LLAMA = getTable("entities/trader_llama");

    public static final LootTable ENTITIES_TROPICAL_FISH = getTable("entities/tropical_fish");

    public static final LootTable ENTITIES_TURTLE = getTable("entities/turtle");

    public static final LootTable ENTITIES_VEX = getTable("entities/vex");

    public static final LootTable ENTITIES_VILLAGER = getTable("entities/villager");

    public static final LootTable ENTITIES_VINDICATOR = getTable("entities/vindicator");

    public static final LootTable ENTITIES_WANDERING_TRADER = getTable("entities/wandering_trader");

    public static final LootTable ENTITIES_WARDEN = getTable("entities/warden");

    public static final LootTable ENTITIES_WITCH = getTable("entities/witch");

    public static final LootTable ENTITIES_WITHER = getTable("entities/wither");

    public static final LootTable ENTITIES_WITHER_SKELETON = getTable("entities/wither_skeleton");

    public static final LootTable ENTITIES_WOLF = getTable("entities/wolf");

    public static final LootTable ENTITIES_ZOGLIN = getTable("entities/zoglin");

    public static final LootTable ENTITIES_ZOMBIE = getTable("entities/zombie");

    public static final LootTable ENTITIES_ZOMBIE_HORSE = getTable("entities/zombie_horse");

    public static final LootTable ENTITIES_ZOMBIE_NAUTILUS = getTable("entities/zombie_nautilus");

    public static final LootTable ENTITIES_ZOMBIE_VILLAGER = getTable("entities/zombie_villager");

    public static final LootTable ENTITIES_ZOMBIFIED_PIGLIN = getTable("entities/zombified_piglin");

    public static final LootTable EQUIPMENT_TRIAL_CHAMBER = getTable("equipment/trial_chamber");

    public static final LootTable EQUIPMENT_TRIAL_CHAMBER_MELEE = getTable("equipment/trial_chamber_melee");

    public static final LootTable EQUIPMENT_TRIAL_CHAMBER_RANGED = getTable("equipment/trial_chamber_ranged");

    public static final LootTable GAMEPLAY_ARMADILLO_SHED = getTable("gameplay/armadillo_shed");

    public static final LootTable GAMEPLAY_CAT_MORNING_GIFT = getTable("gameplay/cat_morning_gift");

    public static final LootTable GAMEPLAY_CHICKEN_LAY = getTable("gameplay/chicken_lay");

    public static final LootTable GAMEPLAY_FISHING = getTable("gameplay/fishing");

    public static final LootTable GAMEPLAY_FISHING_FISH = getTable("gameplay/fishing/fish");

    public static final LootTable GAMEPLAY_FISHING_JUNK = getTable("gameplay/fishing/junk");

    public static final LootTable GAMEPLAY_FISHING_TREASURE = getTable("gameplay/fishing/treasure");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_ARMORER_GIFT = getTable("gameplay/hero_of_the_village/armorer_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_BABY_GIFT = getTable("gameplay/hero_of_the_village/baby_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_BUTCHER_GIFT = getTable("gameplay/hero_of_the_village/butcher_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_CARTOGRAPHER_GIFT = getTable("gameplay/hero_of_the_village/cartographer_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_CLERIC_GIFT = getTable("gameplay/hero_of_the_village/cleric_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_FARMER_GIFT = getTable("gameplay/hero_of_the_village/farmer_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_FISHERMAN_GIFT = getTable("gameplay/hero_of_the_village/fisherman_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_FLETCHER_GIFT = getTable("gameplay/hero_of_the_village/fletcher_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_LEATHERWORKER_GIFT = getTable("gameplay/hero_of_the_village/leatherworker_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT = getTable("gameplay/hero_of_the_village/librarian_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_MASON_GIFT = getTable("gameplay/hero_of_the_village/mason_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_SHEPHERD_GIFT = getTable("gameplay/hero_of_the_village/shepherd_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT = getTable("gameplay/hero_of_the_village/toolsmith_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_UNEMPLOYED_GIFT = getTable("gameplay/hero_of_the_village/unemployed_gift");

    public static final LootTable GAMEPLAY_HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT = getTable("gameplay/hero_of_the_village/weaponsmith_gift");

    public static final LootTable GAMEPLAY_PANDA_SNEEZE = getTable("gameplay/panda_sneeze");

    public static final LootTable GAMEPLAY_PIGLIN_BARTERING = getTable("gameplay/piglin_bartering");

    public static final LootTable GAMEPLAY_SNIFFER_DIGGING = getTable("gameplay/sniffer_digging");

    public static final LootTable GAMEPLAY_TURTLE_GROW = getTable("gameplay/turtle_grow");

    public static final LootTable HARVEST_BEEHIVE = getTable("harvest/beehive");

    public static final LootTable HARVEST_CAVE_VINE = getTable("harvest/cave_vine");

    public static final LootTable HARVEST_SWEET_BERRY_BUSH = getTable("harvest/sweet_berry_bush");

    public static final LootTable POTS_TRIAL_CHAMBERS_CORRIDOR = getTable("pots/trial_chambers/corridor");

    public static final LootTable SHEARING_BOGGED = getTable("shearing/bogged");

    public static final LootTable SHEARING_MOOSHROOM = getTable("shearing/mooshroom");

    public static final LootTable SHEARING_MOOSHROOM_BROWN = getTable("shearing/mooshroom/brown");

    public static final LootTable SHEARING_MOOSHROOM_RED = getTable("shearing/mooshroom/red");

    public static final LootTable SHEARING_SHEEP = getTable("shearing/sheep");

    public static final LootTable SHEARING_SHEEP_BLACK = getTable("shearing/sheep/black");

    public static final LootTable SHEARING_SHEEP_BLUE = getTable("shearing/sheep/blue");

    public static final LootTable SHEARING_SHEEP_BROWN = getTable("shearing/sheep/brown");

    public static final LootTable SHEARING_SHEEP_CYAN = getTable("shearing/sheep/cyan");

    public static final LootTable SHEARING_SHEEP_GRAY = getTable("shearing/sheep/gray");

    public static final LootTable SHEARING_SHEEP_GREEN = getTable("shearing/sheep/green");

    public static final LootTable SHEARING_SHEEP_LIGHT_BLUE = getTable("shearing/sheep/light_blue");

    public static final LootTable SHEARING_SHEEP_LIGHT_GRAY = getTable("shearing/sheep/light_gray");

    public static final LootTable SHEARING_SHEEP_LIME = getTable("shearing/sheep/lime");

    public static final LootTable SHEARING_SHEEP_MAGENTA = getTable("shearing/sheep/magenta");

    public static final LootTable SHEARING_SHEEP_ORANGE = getTable("shearing/sheep/orange");

    public static final LootTable SHEARING_SHEEP_PINK = getTable("shearing/sheep/pink");

    public static final LootTable SHEARING_SHEEP_PURPLE = getTable("shearing/sheep/purple");

    public static final LootTable SHEARING_SHEEP_RED = getTable("shearing/sheep/red");

    public static final LootTable SHEARING_SHEEP_WHITE = getTable("shearing/sheep/white");

    public static final LootTable SHEARING_SHEEP_YELLOW = getTable("shearing/sheep/yellow");

    public static final LootTable SHEARING_SNOW_GOLEM = getTable("shearing/snow_golem");

    public static final LootTable SPAWNERS_OMINOUS_TRIAL_CHAMBER_CONSUMABLES = getTable("spawners/ominous/trial_chamber/consumables");

    public static final LootTable SPAWNERS_OMINOUS_TRIAL_CHAMBER_KEY = getTable("spawners/ominous/trial_chamber/key");

    public static final LootTable SPAWNERS_TRIAL_CHAMBER_CONSUMABLES = getTable("spawners/trial_chamber/consumables");

    public static final LootTable SPAWNERS_TRIAL_CHAMBER_ITEMS_TO_DROP_WHEN_OMINOUS = getTable("spawners/trial_chamber/items_to_drop_when_ominous");

    public static final LootTable SPAWNERS_TRIAL_CHAMBER_KEY = getTable("spawners/trial_chamber/key");
    // End generate - LootTables

    private static LootTable getTable(final @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.LOOT_TABLE).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private LootTables() {
    }
}
