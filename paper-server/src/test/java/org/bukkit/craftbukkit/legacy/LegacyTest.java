package org.bukkit.craftbukkit.legacy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class LegacyTest extends AbstractTestingBase {

    private final Set<Material> INVALIDATED_MATERIALS = new HashSet<>(Arrays.asList(Material.ACACIA_BUTTON, Material.ACACIA_PRESSURE_PLATE, Material.ACACIA_TRAPDOOR, Material.AIR, Material.ATTACHED_MELON_STEM, Material.ATTACHED_PUMPKIN_STEM,
            Material.BIRCH_BUTTON, Material.BIRCH_PRESSURE_PLATE, Material.BIRCH_TRAPDOOR, Material.BLACK_WALL_BANNER, Material.BLUE_WALL_BANNER, Material.BROWN_WALL_BANNER, Material.BUBBLE_COLUMN, Material.CAVE_AIR, Material.CREEPER_WALL_HEAD,
            Material.CYAN_WALL_BANNER, Material.DARK_OAK_BUTTON, Material.DARK_OAK_PRESSURE_PLATE, Material.DARK_OAK_TRAPDOOR, Material.DARK_PRISMARINE_SLAB, Material.DARK_PRISMARINE_STAIRS, Material.DEBUG_STICK, Material.DONKEY_SPAWN_EGG,
            Material.DRAGON_WALL_HEAD, Material.DRIED_KELP, Material.DRIED_KELP_BLOCK, Material.ELDER_GUARDIAN_SPAWN_EGG, Material.EVOKER_SPAWN_EGG, Material.GRAY_WALL_BANNER, Material.GREEN_WALL_BANNER, Material.HUSK_SPAWN_EGG,
            Material.JUNGLE_BUTTON, Material.JUNGLE_PRESSURE_PLATE, Material.JUNGLE_TRAPDOOR, Material.KELP, Material.KELP_PLANT, Material.LIGHT_BLUE_WALL_BANNER, Material.LIGHT_GRAY_WALL_BANNER, Material.LIME_WALL_BANNER, Material.LLAMA_SPAWN_EGG,
            Material.MAGENTA_WALL_BANNER, Material.MULE_SPAWN_EGG, Material.ORANGE_WALL_BANNER, Material.PARROT_SPAWN_EGG, Material.PHANTOM_SPAWN_EGG, Material.PINK_WALL_BANNER, Material.PLAYER_WALL_HEAD, Material.POLAR_BEAR_SPAWN_EGG,
            Material.POTTED_ACACIA_SAPLING, Material.POTTED_ALLIUM, Material.POTTED_AZURE_BLUET, Material.POTTED_BIRCH_SAPLING, Material.POTTED_BLUE_ORCHID, Material.POTTED_BROWN_MUSHROOM, Material.POTTED_DANDELION, Material.POTTED_DARK_OAK_SAPLING,
            Material.POTTED_DEAD_BUSH, Material.POTTED_FERN, Material.POTTED_JUNGLE_SAPLING, Material.POTTED_OAK_SAPLING, Material.POTTED_ORANGE_TULIP, Material.POTTED_OXEYE_DAISY, Material.POTTED_PINK_TULIP, Material.POTTED_POPPY,
            Material.POTTED_RED_MUSHROOM, Material.POTTED_RED_TULIP, Material.POTTED_SPRUCE_SAPLING, Material.POTTED_WHITE_TULIP, Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_BRICK_STAIRS, Material.PRISMARINE_SLAB, Material.PRISMARINE_STAIRS,
            Material.PUMPKIN, Material.PURPLE_SHULKER_BOX, Material.PURPLE_WALL_BANNER, Material.RED_WALL_BANNER, Material.SEAGRASS, Material.SKELETON_HORSE_SPAWN_EGG, Material.SKELETON_WALL_SKULL, Material.SPRUCE_BUTTON, Material.SPRUCE_PRESSURE_PLATE, Material.SPRUCE_TRAPDOOR,
            Material.STRAY_SPAWN_EGG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.TALL_SEAGRASS,
            Material.TRIDENT, Material.TURTLE_EGG, Material.TURTLE_HELMET, Material.SCUTE, Material.TURTLE_SPAWN_EGG, Material.VEX_SPAWN_EGG, Material.VINDICATOR_SPAWN_EGG, Material.VOID_AIR, Material.WHITE_BED,
            Material.WITHER_SKELETON_SPAWN_EGG, Material.WITHER_SKELETON_WALL_SKULL, Material.YELLOW_WALL_BANNER, Material.ZOMBIE_HORSE_SPAWN_EGG, Material.ZOMBIE_VILLAGER_SPAWN_EGG, Material.ZOMBIE_WALL_HEAD,
            Material.COD_BUCKET, Material.COD_SPAWN_EGG, Material.PUFFERFISH_BUCKET, Material.PUFFERFISH_SPAWN_EGG, Material.SALMON_BUCKET, Material.SALMON_SPAWN_EGG,
            Material.TROPICAL_FISH_BUCKET, Material.DROWNED_SPAWN_EGG, Material.TROPICAL_FISH_SPAWN_EGG,
            Material.BLUE_ICE, Material.BRAIN_CORAL, Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL_FAN, Material.CONDUIT, Material.DEAD_BRAIN_CORAL_BLOCK,
            Material.DEAD_BUBBLE_CORAL_BLOCK, Material.DEAD_FIRE_CORAL_BLOCK, Material.DEAD_HORN_CORAL_BLOCK, Material.DEAD_TUBE_CORAL_BLOCK, Material.DOLPHIN_SPAWN_EGG, Material.FIRE_CORAL, Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL_FAN,
            Material.HEART_OF_THE_SEA, Material.HORN_CORAL, Material.HORN_CORAL_BLOCK, Material.HORN_CORAL_FAN, Material.NAUTILUS_SHELL, Material.PHANTOM_MEMBRANE, Material.SEA_PICKLE, Material.TUBE_CORAL, Material.TUBE_CORAL_BLOCK,
            Material.TUBE_CORAL_FAN, Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD,
            Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD,
            Material.TUBE_CORAL_WALL_FAN, Material.BRAIN_CORAL_WALL_FAN, Material.BUBBLE_CORAL_WALL_FAN, Material.FIRE_CORAL_WALL_FAN, Material.HORN_CORAL_WALL_FAN, Material.DEAD_TUBE_CORAL_WALL_FAN, Material.DEAD_BRAIN_CORAL_WALL_FAN,
            Material.DEAD_BUBBLE_CORAL_WALL_FAN, Material.DEAD_FIRE_CORAL_WALL_FAN, Material.DEAD_HORN_CORAL_WALL_FAN, Material.DEAD_TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL_FAN,
            Material.DEAD_HORN_CORAL_FAN, Material.DEAD_BRAIN_CORAL, Material.DEAD_BUBBLE_CORAL, Material.DEAD_FIRE_CORAL, Material.DEAD_HORN_CORAL, Material.DEAD_TUBE_CORAL,
            // 1.14
            Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.ANDESITE_SLAB, Material.ANDESITE_STAIRS, Material.ANDESITE_WALL, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.BARREL, Material.BELL, Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN,
            Material.BLACK_DYE, Material.BLAST_FURNACE, Material.BLUE_DYE, Material.BRICK_WALL, Material.BROWN_DYE, Material.CAMPFIRE, Material.CARTOGRAPHY_TABLE, Material.CAT_SPAWN_EGG, Material.CORNFLOWER, Material.CREEPER_BANNER_PATTERN, Material.CROSSBOW,
            Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DIORITE_SLAB, Material.DIORITE_STAIRS, Material.DIORITE_WALL, Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICK_STAIRS, Material.END_STONE_BRICK_WALL, Material.FLETCHING_TABLE,
            Material.FLOWER_BANNER_PATTERN, Material.GRANITE_SLAB, Material.GRANITE_STAIRS, Material.GRANITE_WALL, Material.GREEN_DYE, Material.GRINDSTONE, Material.RAVAGER_SPAWN_EGG, Material.JIGSAW, Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN,
            Material.LANTERN, Material.LECTERN, Material.LILY_OF_THE_VALLEY, Material.LOOM, Material.MOJANG_BANNER_PATTERN, Material.MOSSY_COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_STAIRS, Material.MOSSY_STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_STAIRS,
            Material.MOSSY_STONE_BRICK_WALL, Material.NETHER_BRICK_WALL, Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.PANDA_SPAWN_EGG, Material.PILLAGER_SPAWN_EGG, Material.POLISHED_ANDESITE_SLAB, Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_DIORITE_SLAB,
            Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_SLAB, Material.POLISHED_GRANITE_STAIRS, Material.POTTED_BAMBOO, Material.POTTED_CORNFLOWER, Material.POTTED_LILY_OF_THE_VALLEY, Material.POTTED_WITHER_ROSE, Material.PRISMARINE_WALL, Material.RED_DYE,
            Material.RED_NETHER_BRICK_SLAB, Material.RED_NETHER_BRICK_STAIRS, Material.RED_NETHER_BRICK_WALL, Material.RED_SANDSTONE_WALL, Material.SANDSTONE_WALL, Material.SCAFFOLDING, Material.SKULL_BANNER_PATTERN, Material.SMITHING_TABLE, Material.SMOKER,
            Material.SMOOTH_QUARTZ_SLAB, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_RED_SANDSTONE_SLAB, Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE_STAIRS, Material.STONE_SLAB, Material.SPRUCE_SIGN,
            Material.SPRUCE_WALL_SIGN, Material.STONECUTTER, Material.STONE_BRICK_WALL, Material.STONE_STAIRS, Material.SUSPICIOUS_STEW, Material.SWEET_BERRIES, Material.SWEET_BERRY_BUSH, Material.WHITE_DYE, Material.WITHER_ROSE, Material.YELLOW_DYE,
            Material.COMPOSTER, Material.TRADER_LLAMA_SPAWN_EGG, Material.WANDERING_TRADER_SPAWN_EGG, Material.FOX_SPAWN_EGG, Material.LEATHER_HORSE_ARMOR, Material.GLOBE_BANNER_PATTERN, Material.CUT_RED_SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB,
            // 1.15
            Material.BEEHIVE, Material.BEE_NEST, Material.BEE_SPAWN_EGG, Material.HONEYCOMB, Material.HONEYCOMB_BLOCK, Material.HONEY_BLOCK, Material.HONEY_BOTTLE,
            // 1.16
            Material.ANCIENT_DEBRIS, Material.BASALT, Material.CRIMSON_BUTTON, Material.CRIMSON_DOOR, Material.CRIMSON_FENCE, Material.CRIMSON_FENCE_GATE, Material.CRIMSON_NYLIUM, Material.CRIMSON_PLANKS, Material.CRIMSON_PRESSURE_PLATE,
            Material.CRIMSON_ROOTS, Material.CRIMSON_SIGN, Material.CRIMSON_SLAB, Material.CRIMSON_STAIRS, Material.CRIMSON_STEM, Material.CRIMSON_TRAPDOOR, Material.CRIMSON_WALL_SIGN, Material.HOGLIN_SPAWN_EGG, Material.NETHERITE_AXE, Material.NETHERITE_BLOCK,
            Material.NETHERITE_BOOTS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET, Material.NETHERITE_HOE, Material.NETHERITE_INGOT, Material.NETHERITE_LEGGINGS, Material.NETHERITE_PICKAXE, Material.NETHERITE_SCRAP, Material.NETHERITE_SHOVEL,
            Material.NETHERITE_SWORD, Material.NETHER_SPROUTS, Material.PIGLIN_SPAWN_EGG, Material.SHROOMLIGHT, Material.SOUL_FIRE, Material.SOUL_LANTERN, Material.SOUL_TORCH, Material.SOUL_WALL_TORCH, Material.SOUL_SOIL, Material.STRIPPED_CRIMSON_STEM,
            Material.STRIPPED_WARPED_STEM, Material.WARPED_BUTTON, Material.WARPED_DOOR, Material.WARPED_FENCE, Material.WARPED_FENCE_GATE, Material.WARPED_FUNGUS, Material.WARPED_NYLIUM, Material.WARPED_PLANKS, Material.WARPED_PRESSURE_PLATE, Material.WARPED_ROOTS,
            Material.WARPED_SIGN, Material.WARPED_SLAB, Material.WARPED_STAIRS, Material.WARPED_STEM, Material.WARPED_TRAPDOOR, Material.WARPED_WALL_SIGN, Material.WARPED_WART_BLOCK, Material.WEEPING_VINES, Material.WEEPING_VINES_PLANT,
            Material.CRIMSON_FUNGUS, Material.CRIMSON_HYPHAE, Material.CRYING_OBSIDIAN, Material.NETHER_GOLD_ORE, Material.POLISHED_BASALT, Material.POTTED_CRIMSON_FUNGUS, Material.POTTED_CRIMSON_ROOTS, Material.POTTED_WARPED_FUNGUS, Material.POTTED_WARPED_ROOTS,
            Material.RESPAWN_ANCHOR, Material.SLIME_SPAWN_EGG, Material.STRIPPED_CRIMSON_HYPHAE, Material.STRIPPED_WARPED_HYPHAE, Material.TARGET, Material.TWISTING_VINES, Material.TWISTING_VINES_PLANT, Material.WARPED_FUNGUS, Material.WARPED_HYPHAE,
            Material.BLACKSTONE, Material.BLACKSTONE_SLAB, Material.BLACKSTONE_STAIRS, Material.BLACKSTONE_WALL, Material.CHISELED_NETHER_BRICKS, Material.CHISELED_POLISHED_BLACKSTONE, Material.CRACKED_NETHER_BRICKS, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            Material.GILDED_BLACKSTONE, Material.LODESTONE, Material.PIGLIN_BANNER_PATTERN, Material.POLISHED_BLACKSTONE, Material.POLISHED_BLACKSTONE_BRICKS, Material.POLISHED_BLACKSTONE_BRICK_SLAB, Material.POLISHED_BLACKSTONE_BRICK_STAIRS,
            Material.POLISHED_BLACKSTONE_BRICK_WALL, Material.POLISHED_BLACKSTONE_BUTTON, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_SLAB, Material.POLISHED_BLACKSTONE_STAIRS, Material.POLISHED_BLACKSTONE_WALL, Material.QUARTZ_BRICKS,
            Material.SOUL_CAMPFIRE, Material.STRIDER_SPAWN_EGG, Material.WARPED_FUNGUS_ON_A_STICK, Material.ZOGLIN_SPAWN_EGG, Material.CHAIN, Material.MUSIC_DISC_PIGSTEP,
            // 1.16.2
            Material.PIGLIN_BRUTE_SPAWN_EGG,
            // 1.17
            Material.AMETHYST_BLOCK, Material.AMETHYST_CLUSTER, Material.AMETHYST_SHARD, Material.AXOLOTL_BUCKET, Material.AXOLOTL_SPAWN_EGG, Material.AZALEA, Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES, Material.BIG_DRIPLEAF,
            Material.BIG_DRIPLEAF_STEM, Material.BLACK_CANDLE, Material.BLACK_CANDLE_CAKE, Material.BLUE_CANDLE, Material.BLUE_CANDLE_CAKE, Material.BROWN_CANDLE, Material.BROWN_CANDLE_CAKE, Material.BUDDING_AMETHYST, Material.BUNDLE, Material.CALCITE,
            Material.CANDLE, Material.CANDLE_CAKE, Material.CAVE_VINES, Material.CAVE_VINES_PLANT, Material.CHISELED_DEEPSLATE, Material.COBBLED_DEEPSLATE, Material.COBBLED_DEEPSLATE_SLAB, Material.COBBLED_DEEPSLATE_STAIRS, Material.COBBLED_DEEPSLATE_WALL,
            Material.COPPER_BLOCK, Material.COPPER_INGOT, Material.COPPER_ORE, Material.CRACKED_DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_TILES, Material.CUT_COPPER, Material.CUT_COPPER_SLAB, Material.CUT_COPPER_STAIRS, Material.CYAN_CANDLE,
            Material.CYAN_CANDLE_CAKE, Material.DEEPSLATE, Material.DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_SLAB, Material.DEEPSLATE_BRICK_STAIRS, Material.DEEPSLATE_BRICK_WALL, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
            Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DEEPSLATE_TILES, Material.DEEPSLATE_TILE_SLAB,
            Material.DEEPSLATE_TILE_STAIRS, Material.DEEPSLATE_TILE_WALL, Material.DRIPSTONE_BLOCK, Material.EXPOSED_COPPER, Material.EXPOSED_CUT_COPPER, Material.EXPOSED_CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_STAIRS, Material.FLOWERING_AZALEA,
            Material.GLOW_BERRIES, Material.GLOW_INK_SAC, Material.GLOW_ITEM_FRAME, Material.GLOW_LICHEN, Material.GLOW_SQUID_SPAWN_EGG, Material.GOAT_SPAWN_EGG, Material.GRAY_CANDLE, Material.GRAY_CANDLE_CAKE, Material.GREEN_CANDLE,
            Material.GREEN_CANDLE_CAKE, Material.HANGING_ROOTS, Material.INFESTED_DEEPSLATE, Material.LARGE_AMETHYST_BUD, Material.LAVA_CAULDRON, Material.LIGHT, Material.LIGHTNING_ROD, Material.LIGHT_BLUE_CANDLE, Material.LIGHT_BLUE_CANDLE_CAKE,
            Material.LIGHT_GRAY_CANDLE, Material.LIGHT_GRAY_CANDLE_CAKE, Material.LIME_CANDLE, Material.LIME_CANDLE_CAKE, Material.MAGENTA_CANDLE, Material.MAGENTA_CANDLE_CAKE, Material.MEDIUM_AMETHYST_BUD, Material.MOSS_BLOCK, Material.MOSS_CARPET,
            Material.ORANGE_CANDLE, Material.ORANGE_CANDLE_CAKE, Material.OXIDIZED_COPPER, Material.OXIDIZED_CUT_COPPER, Material.OXIDIZED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_STAIRS, Material.PINK_CANDLE, Material.PINK_CANDLE_CAKE,
            Material.POINTED_DRIPSTONE, Material.POLISHED_DEEPSLATE, Material.POLISHED_DEEPSLATE_SLAB, Material.POLISHED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_WALL, Material.POWDER_SNOW, Material.POWDER_SNOW_BUCKET, Material.POWDER_SNOW_CAULDRON,
            Material.PURPLE_CANDLE, Material.PURPLE_CANDLE_CAKE, Material.RAW_COPPER, Material.RAW_COPPER_BLOCK, Material.RAW_GOLD, Material.RAW_GOLD_BLOCK, Material.RAW_IRON, Material.RAW_IRON_BLOCK, Material.RED_CANDLE, Material.RED_CANDLE_CAKE,
            Material.ROOTED_DIRT, Material.SCULK_SENSOR, Material.SMALL_AMETHYST_BUD, Material.SMALL_DRIPLEAF, Material.SMOOTH_BASALT, Material.SPORE_BLOSSOM, Material.SPYGLASS, Material.TINTED_GLASS, Material.TUFF, Material.WATER_CAULDRON,
            Material.WAXED_COPPER_BLOCK, Material.WAXED_CUT_COPPER, Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_COPPER, Material.WAXED_EXPOSED_CUT_COPPER, Material.WAXED_EXPOSED_CUT_COPPER_SLAB,
            Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_COPPER,
            Material.WAXED_WEATHERED_CUT_COPPER, Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WEATHERED_COPPER, Material.WEATHERED_CUT_COPPER, Material.WEATHERED_CUT_COPPER_SLAB,
            Material.WEATHERED_CUT_COPPER_STAIRS, Material.WHITE_CANDLE, Material.WHITE_CANDLE_CAKE, Material.YELLOW_CANDLE, Material.YELLOW_CANDLE_CAKE, Material.POTTED_AZALEA_BUSH, Material.POTTED_FLOWERING_AZALEA_BUSH,
            // 1.18
            Material.MUSIC_DISC_OTHERSIDE,
            // 1.19
            Material.ACACIA_CHEST_BOAT, Material.ALLAY_SPAWN_EGG, Material.BIRCH_CHEST_BOAT, Material.DARK_OAK_CHEST_BOAT, Material.DISC_FRAGMENT_5, Material.ECHO_SHARD, Material.FROGSPAWN, Material.FROG_SPAWN_EGG, Material.GOAT_HORN,
            Material.JUNGLE_CHEST_BOAT, Material.MANGROVE_BOAT, Material.MANGROVE_BUTTON, Material.MANGROVE_CHEST_BOAT, Material.MANGROVE_DOOR, Material.MANGROVE_FENCE, Material.MANGROVE_FENCE_GATE, Material.MANGROVE_LEAVES, Material.MANGROVE_LOG,
            Material.MANGROVE_PLANKS, Material.MANGROVE_PRESSURE_PLATE, Material.MANGROVE_PROPAGULE, Material.MANGROVE_ROOTS, Material.MANGROVE_SIGN, Material.MANGROVE_SLAB, Material.MANGROVE_STAIRS, Material.MANGROVE_TRAPDOOR, Material.MANGROVE_WALL_SIGN,
            Material.MANGROVE_WOOD, Material.MUD, Material.MUDDY_MANGROVE_ROOTS, Material.MUD_BRICKS, Material.MUD_BRICK_SLAB, Material.MUD_BRICK_STAIRS, Material.MUD_BRICK_WALL, Material.MUSIC_DISC_5, Material.OAK_CHEST_BOAT, Material.OCHRE_FROGLIGHT,
            Material.PACKED_MUD, Material.PEARLESCENT_FROGLIGHT, Material.POTTED_MANGROVE_PROPAGULE, Material.RECOVERY_COMPASS, Material.REINFORCED_DEEPSLATE, Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SHRIEKER, Material.SCULK_VEIN,
            Material.SPRUCE_CHEST_BOAT, Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_MANGROVE_WOOD, Material.TADPOLE_BUCKET, Material.TADPOLE_SPAWN_EGG, Material.VERDANT_FROGLIGHT, Material.WARDEN_SPAWN_EGG,
            // 1.19.3
            Material.ACACIA_HANGING_SIGN, Material.ACACIA_WALL_HANGING_SIGN, Material.BAMBOO_BLOCK, Material.BAMBOO_BUTTON, Material.BAMBOO_CHEST_RAFT, Material.BAMBOO_DOOR, Material.BAMBOO_FENCE, Material.BAMBOO_FENCE_GATE, Material.BAMBOO_HANGING_SIGN,
            Material.BAMBOO_MOSAIC, Material.BAMBOO_MOSAIC_SLAB, Material.BAMBOO_MOSAIC_STAIRS, Material.BAMBOO_PLANKS, Material.BAMBOO_PRESSURE_PLATE, Material.BAMBOO_RAFT, Material.BAMBOO_SIGN, Material.BAMBOO_SLAB, Material.BAMBOO_STAIRS, Material.BAMBOO_TRAPDOOR,
            Material.BAMBOO_WALL_HANGING_SIGN, Material.BAMBOO_WALL_SIGN, Material.BIRCH_HANGING_SIGN, Material.BIRCH_WALL_HANGING_SIGN, Material.CAMEL_SPAWN_EGG, Material.CHISELED_BOOKSHELF, Material.CRIMSON_HANGING_SIGN, Material.CRIMSON_WALL_HANGING_SIGN,
            Material.DARK_OAK_HANGING_SIGN, Material.DARK_OAK_WALL_HANGING_SIGN, Material.ENDER_DRAGON_SPAWN_EGG, Material.IRON_GOLEM_SPAWN_EGG, Material.JUNGLE_HANGING_SIGN, Material.JUNGLE_WALL_HANGING_SIGN, Material.MANGROVE_HANGING_SIGN,
            Material.MANGROVE_WALL_HANGING_SIGN, Material.OAK_HANGING_SIGN, Material.OAK_WALL_HANGING_SIGN, Material.PIGLIN_HEAD, Material.PIGLIN_WALL_HEAD, Material.SNOW_GOLEM_SPAWN_EGG, Material.SPRUCE_HANGING_SIGN, Material.SPRUCE_WALL_HANGING_SIGN,
            Material.STRIPPED_BAMBOO_BLOCK, Material.WARPED_HANGING_SIGN, Material.WARPED_WALL_HANGING_SIGN, Material.WITHER_SPAWN_EGG,
            // 1.19.4
            Material.BRUSH, Material.CHERRY_BOAT, Material.CHERRY_BUTTON, Material.CHERRY_CHEST_BOAT, Material.CHERRY_DOOR, Material.CHERRY_FENCE, Material.CHERRY_FENCE_GATE, Material.CHERRY_HANGING_SIGN, Material.CHERRY_LEAVES, Material.CHERRY_LOG,
            Material.CHERRY_PLANKS, Material.CHERRY_PRESSURE_PLATE, Material.CHERRY_SAPLING, Material.CHERRY_SIGN, Material.CHERRY_SLAB, Material.CHERRY_STAIRS, Material.CHERRY_TRAPDOOR, Material.CHERRY_WALL_HANGING_SIGN, Material.CHERRY_WALL_SIGN,
            Material.CHERRY_WOOD, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, Material.DECORATED_POT, Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Material.PINK_PETALS,
            Material.POTTED_CHERRY_SAPLING, Material.POTTED_TORCHFLOWER, Material.POTTERY_SHARD_ARCHER, Material.POTTERY_SHARD_ARMS_UP, Material.POTTERY_SHARD_PRIZE, Material.POTTERY_SHARD_SKULL, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SNIFFER_SPAWN_EGG, Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.STRIPPED_CHERRY_LOG, Material.STRIPPED_CHERRY_WOOD, Material.SUSPICIOUS_SAND,
            Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.TORCHFLOWER, Material.TORCHFLOWER_CROP, Material.TORCHFLOWER_SEEDS, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
            //
            Material.LEGACY_AIR, Material.LEGACY_DEAD_BUSH, Material.LEGACY_BURNING_FURNACE, Material.LEGACY_WALL_SIGN, Material.LEGACY_REDSTONE_TORCH_OFF, Material.LEGACY_SKULL, Material.LEGACY_REDSTONE_COMPARATOR_ON, Material.LEGACY_WALL_BANNER, Material.LEGACY_MONSTER_EGG));

    private final Set<Material> INVERSION_FAILS = new HashSet<>(Arrays.asList(Material.LEGACY_DOUBLE_STEP, Material.LEGACY_GLOWING_REDSTONE_ORE, Material.LEGACY_DIODE_BLOCK_ON, Material.LEGACY_REDSTONE_LAMP_ON, Material.LEGACY_WOOD_DOUBLE_STEP,
            Material.LEGACY_DAYLIGHT_DETECTOR_INVERTED, Material.LEGACY_DOUBLE_STONE_SLAB2, Material.LEGACY_PURPUR_DOUBLE_SLAB, Material.LEGACY_WHEAT, Material.LEGACY_SIGN, Material.LEGACY_WOOD_DOOR, Material.LEGACY_IRON_DOOR, Material.LEGACY_SUGAR_CANE,
            Material.LEGACY_CAKE, Material.LEGACY_BED, Material.LEGACY_DIODE, Material.LEGACY_NETHER_STALK, Material.LEGACY_BREWING_STAND_ITEM, Material.LEGACY_CAULDRON_ITEM, Material.LEGACY_REDSTONE_COMPARATOR, Material.LEGACY_SPRUCE_DOOR_ITEM,
            Material.LEGACY_BIRCH_DOOR_ITEM, Material.LEGACY_JUNGLE_DOOR_ITEM, Material.LEGACY_ACACIA_DOOR_ITEM, Material.LEGACY_DARK_OAK_DOOR_ITEM, Material.LEGACY_STATIONARY_LAVA, Material.LEGACY_STATIONARY_WATER));

    @Test
    public void toLegacyMaterial() {
        for (Material material : Material.values()) {
            if (!INVALIDATED_MATERIALS.contains(material) && !material.isLegacy()) {
                MaterialData converted = CraftLegacy.toLegacyData(material);

                Assert.assertNotEquals("Could not toLegacy " + material, Material.LEGACY_AIR, converted.getItemType());

                if (!INVALIDATED_MATERIALS.contains(converted.getItemType())) {
                    Assert.assertNotEquals("Could not fromLegacy(toLegacy) " + converted + "(" + material + ")", Material.AIR, CraftLegacy.fromLegacy(converted));
                }
                if (!INVERSION_FAILS.contains(material)) {
                    Assert.assertEquals("Could not fromLegacy(toLegacy) " + converted + "(" + material + ")", material, CraftLegacy.fromLegacy(converted));
                }
            }
        }

        Assert.assertEquals("Could not toLegacy Air", Material.LEGACY_AIR, CraftLegacy.toLegacy(Material.AIR));
    }

    @Test
    public void fromLegacyMaterial() {
        for (Material material : Material.values()) {
            if (!INVALIDATED_MATERIALS.contains(material) && material.isLegacy()) {
                Material converted = CraftLegacy.fromLegacy(material);
                Assert.assertNotEquals("Could not fromLegacy " + material, Material.AIR, converted);

                Assert.assertNotEquals("Could not toLegacy(fromLegacy) " + converted + "(" + material + ")", Material.AIR, CraftLegacy.toLegacy(converted));
                if (!INVERSION_FAILS.contains(material)) {
                    Assert.assertEquals("Could not toLegacy(fromLegacy) " + converted + "(" + material + ")", material, CraftLegacy.toLegacy(converted));
                }
            }
        }

        Assert.assertEquals("Could not fromLegacy Air", Material.AIR, CraftLegacy.fromLegacy(Material.LEGACY_AIR));
    }

    @Test
    public void testRestricted() {
        for (Material material : CraftLegacy.values()) {
            Assert.assertTrue("Must iterate only legacy materials", material.isLegacy());
        }

        for (Material material : org.bukkit.craftbukkit.util.CraftLegacy.modern_values()) {
            Assert.assertFalse("Must iterate only modern materials", material.isLegacy());
        }
    }

    @Test
    public void testManual() {
        Assert.assertEquals(Material.YELLOW_DYE, CraftMagicNumbers.INSTANCE.getMaterial("dandelion_yellow", 1631));
        Assert.assertEquals(Material.OAK_WALL_SIGN, CraftMagicNumbers.INSTANCE.getMaterial("wall_sign", 1631));
    }
}
