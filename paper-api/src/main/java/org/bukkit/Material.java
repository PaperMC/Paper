package org.bukkit;

import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang.Validate;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Fire;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Observer;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.block.data.type.TripwireHook;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.material.MaterialData;

/**
 * An enum of all material IDs accepted by the official server and client
 */
public enum Material implements Keyed {
    //<editor-fold desc="Materials" defaultstate="collapsed">
    ACACIA_BOAT(27326, 1),
    ACACIA_BUTTON(13993, Switch.class),
    ACACIA_DOOR(23797, Door.class),
    ACACIA_FENCE(4569, Fence.class),
    ACACIA_FENCE_GATE(14145, Gate.class),
    ACACIA_LEAVES(16606, Leaves.class),
    ACACIA_LOG(8385, Orientable.class),
    ACACIA_PLANKS(31312),
    ACACIA_PRESSURE_PLATE(17586, Powerable.class),
    ACACIA_SAPLING(20806, Sapling.class),
    ACACIA_SLAB(23730, Slab.class),
    ACACIA_STAIRS(17453, Stairs.class),
    ACACIA_TRAPDOOR(18343, TrapDoor.class),
    ACACIA_WOOD(9541, Orientable.class),
    ACTIVATOR_RAIL(5834, RedstoneRail.class),
    AIR(9648, 0),
    ALLIUM(6871),
    ANDESITE(25975),
    ANVIL(18718, Directional.class),
    APPLE(7720),
    ARMOR_STAND(12852, 16),
    ARROW(31091),
    ATTACHED_MELON_STEM(30882, Directional.class),
    ATTACHED_PUMPKIN_STEM(12724, Directional.class),
    AZURE_BLUET(17608),
    BAKED_POTATO(14624),
    BARRIER(26453),
    BAT_SPAWN_EGG(14607),
    BEACON(6608),
    BEDROCK(23130),
    BEEF(4803),
    BEETROOT(23305),
    BEETROOTS(22075, Ageable.class),
    BEETROOT_SEEDS(21282),
    BEETROOT_SOUP(16036, 1),
    BIRCH_BOAT(28104, 1),
    BIRCH_BUTTON(26934, Switch.class),
    BIRCH_DOOR(14759, Door.class),
    BIRCH_FENCE(17347, Fence.class),
    BIRCH_FENCE_GATE(6322, Gate.class),
    BIRCH_LEAVES(12601, Leaves.class),
    BIRCH_LOG(26727, Orientable.class),
    BIRCH_PLANKS(29322),
    BIRCH_PRESSURE_PLATE(9664, Powerable.class),
    BIRCH_SAPLING(31533, Sapling.class),
    BIRCH_SLAB(13807, Slab.class),
    BIRCH_STAIRS(7657, Stairs.class),
    BIRCH_TRAPDOOR(32585, TrapDoor.class),
    BIRCH_WOOD(20913, Orientable.class),
    BLACK_BANNER(9365, 16, Rotatable.class),
    BLACK_BED(20490, 1, Bed.class),
    BLACK_CARPET(6056),
    BLACK_CONCRETE(13338),
    BLACK_CONCRETE_POWDER(16150),
    BLACK_GLAZED_TERRACOTTA(29678, Directional.class),
    BLACK_SHULKER_BOX(24076, 1, Directional.class),
    BLACK_STAINED_GLASS(13941),
    BLACK_STAINED_GLASS_PANE(13201, GlassPane.class),
    BLACK_TERRACOTTA(26691),
    BLACK_WALL_BANNER(4919, Directional.class),
    BLACK_WOOL(16693),
    BLAZE_POWDER(18941),
    BLAZE_ROD(8289),
    BLAZE_SPAWN_EGG(4759),
    BLUE_BANNER(18481, 16, Rotatable.class),
    BLUE_BED(12714, 1, Bed.class),
    BLUE_CARPET(13292),
    BLUE_CONCRETE(18756),
    BLUE_CONCRETE_POWDER(17773),
    BLUE_GLAZED_TERRACOTTA(23823, Directional.class),
    BLUE_ICE(22449),
    BLUE_ORCHID(13432),
    BLUE_SHULKER_BOX(11476, 1, Directional.class),
    BLUE_STAINED_GLASS(7107),
    BLUE_STAINED_GLASS_PANE(28484, GlassPane.class),
    BLUE_TERRACOTTA(5236),
    BLUE_WALL_BANNER(17757, Directional.class),
    BLUE_WOOL(15738),
    BONE(5686),
    BONE_BLOCK(17312, Orientable.class),
    BONE_MEAL(32458),
    BOOK(23097),
    BOOKSHELF(10069),
    BOW(8745, 1, 384),
    BOWL(32661),
    BRAIN_CORAL(31316),
    BRAIN_CORAL_BLOCK(30618),
    BRAIN_CORAL_FAN(13849, Waterlogged.class),
    BRAIN_CORAL_WALL_FAN(22685, CoralWallFan.class),
    BREAD(32049),
    BREWING_STAND(14539, BrewingStand.class),
    BRICK(6820),
    BRICKS(14165),
    BRICK_SLAB(26333, Slab.class),
    BRICK_STAIRS(21534, Stairs.class),
    BROWN_BANNER(11481, 16, Rotatable.class),
    BROWN_BED(25624, 1, Bed.class),
    BROWN_CARPET(23352),
    BROWN_CONCRETE(19006),
    BROWN_CONCRETE_POWDER(21485),
    BROWN_GLAZED_TERRACOTTA(5655, Directional.class),
    BROWN_MUSHROOM(9665),
    BROWN_MUSHROOM_BLOCK(6291, MultipleFacing.class),
    BROWN_SHULKER_BOX(24230, 1, Directional.class),
    BROWN_STAINED_GLASS(20945),
    BROWN_STAINED_GLASS_PANE(17557, GlassPane.class),
    BROWN_TERRACOTTA(23664),
    BROWN_WALL_BANNER(14731, Directional.class),
    BROWN_WOOL(32638),
    BUBBLE_COLUMN(13758, BubbleColumn.class),
    BUBBLE_CORAL(12464),
    BUBBLE_CORAL_BLOCK(15437),
    BUBBLE_CORAL_FAN(10795, Waterlogged.class),
    BUBBLE_CORAL_WALL_FAN(20382, CoralWallFan.class),
    BUCKET(15215, 16),
    CACTUS(12191, Ageable.class),
    CACTUS_GREEN(17296),
    CAKE(27048, 1, Cake.class),
    CARROT(22824),
    CARROTS(17258, Ageable.class),
    CARROT_ON_A_STICK(27809, 1, 25),
    CARVED_PUMPKIN(25833, Directional.class),
    CAULDRON(26531, Levelled.class),
    CAVE_AIR(17422),
    CAVE_SPIDER_SPAWN_EGG(23341),
    CHAINMAIL_BOOTS(17953, 1, 195),
    CHAINMAIL_CHESTPLATE(23602, 1, 240),
    CHAINMAIL_HELMET(26114, 1, 165),
    CHAINMAIL_LEGGINGS(19087, 1, 225),
    CHAIN_COMMAND_BLOCK(26798, CommandBlock.class),
    CHARCOAL(5390),
    CHEST(22969, Chest.class),
    CHEST_MINECART(4497, 1),
    CHICKEN(17281),
    CHICKEN_SPAWN_EGG(5462),
    CHIPPED_ANVIL(10623, Directional.class),
    CHISELED_QUARTZ_BLOCK(30964),
    CHISELED_RED_SANDSTONE(15529),
    CHISELED_SANDSTONE(31763),
    CHISELED_STONE_BRICKS(9087),
    CHORUS_FLOWER(28542, Ageable.class),
    CHORUS_FRUIT(7652),
    CHORUS_PLANT(28243, MultipleFacing.class),
    CLAY(27880),
    CLAY_BALL(24603),
    CLOCK(14980),
    COAL(29067),
    COAL_BLOCK(27968),
    COAL_ORE(30965),
    COARSE_DIRT(15411),
    COBBLESTONE(32147),
    COBBLESTONE_SLAB(6340, Slab.class),
    COBBLESTONE_STAIRS(24715, Stairs.class),
    COBBLESTONE_WALL(12616, Fence.class),
    COBWEB(9469),
    COCOA(29709, Cocoa.class),
    COCOA_BEANS(27381),
    COD(24691),
    COD_BUCKET(28601, 1),
    COD_SPAWN_EGG(27248),
    COMMAND_BLOCK(4355, CommandBlock.class),
    COMMAND_BLOCK_MINECART(7992, 1),
    COMPARATOR(18911, Comparator.class),
    COMPASS(24139),
    CONDUIT(5148),
    COOKED_BEEF(21595),
    COOKED_CHICKEN(20780),
    COOKED_COD(9681),
    COOKED_MUTTON(31447),
    COOKED_PORKCHOP(27231),
    COOKED_RABBIT(4454),
    COOKED_SALMON(5615),
    COOKIE(27431),
    COW_SPAWN_EGG(14761),
    CRACKED_STONE_BRICKS(27869),
    CRAFTING_TABLE(20706),
    CREEPER_HEAD(29146, Rotatable.class),
    CREEPER_SPAWN_EGG(9653),
    CREEPER_WALL_HEAD(30123, Directional.class),
    CUT_RED_SANDSTONE(26842),
    CUT_SANDSTONE(6118),
    CYAN_BANNER(9839, 16, Rotatable.class),
    CYAN_BED(16746, 1, Bed.class),
    CYAN_CARPET(31495),
    CYAN_CONCRETE(26522),
    CYAN_CONCRETE_POWDER(15734),
    CYAN_DYE(8043),
    CYAN_GLAZED_TERRACOTTA(9550, Directional.class),
    CYAN_SHULKER_BOX(28123, 1, Directional.class),
    CYAN_STAINED_GLASS(30604),
    CYAN_STAINED_GLASS_PANE(11784, GlassPane.class),
    CYAN_TERRACOTTA(25940),
    CYAN_WALL_BANNER(10889, Directional.class),
    CYAN_WOOL(12221),
    DAMAGED_ANVIL(10274, Directional.class),
    DANDELION(30558),
    DANDELION_YELLOW(21789),
    DARK_OAK_BOAT(28618, 1),
    DARK_OAK_BUTTON(6214, Switch.class),
    DARK_OAK_DOOR(10669, Door.class),
    DARK_OAK_FENCE(21767, Fence.class),
    DARK_OAK_FENCE_GATE(10679, Gate.class),
    DARK_OAK_LEAVES(22254, Leaves.class),
    DARK_OAK_LOG(14831, Orientable.class),
    DARK_OAK_PLANKS(20869),
    DARK_OAK_PRESSURE_PLATE(31375, Powerable.class),
    DARK_OAK_SAPLING(14933, Sapling.class),
    DARK_OAK_SLAB(28852, Slab.class),
    DARK_OAK_STAIRS(22921, Stairs.class),
    DARK_OAK_TRAPDOOR(10355, TrapDoor.class),
    DARK_OAK_WOOD(16995, Orientable.class),
    DARK_PRISMARINE(19940),
    DARK_PRISMARINE_SLAB(7577, Slab.class),
    DARK_PRISMARINE_STAIRS(26511, Stairs.class),
    DAYLIGHT_DETECTOR(8864, DaylightDetector.class),
    DEAD_BRAIN_CORAL_BLOCK(12979),
    DEAD_BRAIN_CORAL_FAN(26150, Waterlogged.class),
    DEAD_BRAIN_CORAL_WALL_FAN(23718, CoralWallFan.class),
    DEAD_BUBBLE_CORAL_BLOCK(28220),
    DEAD_BUBBLE_CORAL_FAN(17322, Waterlogged.class),
    DEAD_BUBBLE_CORAL_WALL_FAN(18453, CoralWallFan.class),
    DEAD_BUSH(22888),
    DEAD_FIRE_CORAL_BLOCK(5307),
    DEAD_FIRE_CORAL_FAN(27073, Waterlogged.class),
    DEAD_FIRE_CORAL_WALL_FAN(23375, CoralWallFan.class),
    DEAD_HORN_CORAL_BLOCK(15103),
    DEAD_HORN_CORAL_FAN(11387, Waterlogged.class),
    DEAD_HORN_CORAL_WALL_FAN(27550, CoralWallFan.class),
    DEAD_TUBE_CORAL_BLOCK(28350),
    DEAD_TUBE_CORAL_FAN(17628, Waterlogged.class),
    DEAD_TUBE_CORAL_WALL_FAN(5128, CoralWallFan.class),
    DEBUG_STICK(24562, 1),
    DETECTOR_RAIL(13475, RedstoneRail.class),
    DIAMOND(20865),
    DIAMOND_AXE(27277, 1, 1561),
    DIAMOND_BLOCK(5944),
    DIAMOND_BOOTS(16522, 1, 429),
    DIAMOND_CHESTPLATE(32099, 1, 528),
    DIAMOND_HELMET(10755, 1, 363),
    DIAMOND_HOE(24050, 1, 1561),
    DIAMOND_HORSE_ARMOR(10321, 1),
    DIAMOND_LEGGINGS(11202, 1, 495),
    DIAMOND_ORE(9292),
    DIAMOND_PICKAXE(24291, 1, 1561),
    DIAMOND_SHOVEL(25415, 1, 1561),
    DIAMOND_SWORD(27707, 1, 1561),
    DIORITE(24688),
    DIRT(10580),
    DISPENSER(20871, Dispenser.class),
    DOLPHIN_SPAWN_EGG(20787),
    DONKEY_SPAWN_EGG(14513),
    DRAGON_BREATH(20154),
    DRAGON_EGG(29946),
    DRAGON_HEAD(20084, Rotatable.class),
    DRAGON_WALL_HEAD(19818, Directional.class),
    DRIED_KELP(21042),
    DRIED_KELP_BLOCK(12966),
    DROPPER(31273, Dispenser.class),
    DROWNED_SPAWN_EGG(19368),
    EGG(21603, 16),
    ELDER_GUARDIAN_SPAWN_EGG(11418),
    ELYTRA(23829, 1, 432),
    EMERALD(5654),
    EMERALD_BLOCK(9914),
    EMERALD_ORE(16630),
    ENCHANTED_BOOK(11741, 1),
    ENCHANTED_GOLDEN_APPLE(8280),
    ENCHANTING_TABLE(16255),
    ENDERMAN_SPAWN_EGG(29488),
    ENDERMITE_SPAWN_EGG(16617),
    ENDER_CHEST(32349, EnderChest.class),
    ENDER_EYE(24860),
    ENDER_PEARL(5259, 16),
    END_CRYSTAL(19090),
    END_GATEWAY(26605),
    END_PORTAL(16782),
    END_PORTAL_FRAME(15480, EndPortalFrame.class),
    END_ROD(24832, Directional.class),
    END_STONE(29686),
    END_STONE_BRICKS(20314),
    EVOKER_SPAWN_EGG(21271),
    EXPERIENCE_BOTTLE(12858),
    FARMLAND(31166, Farmland.class),
    FEATHER(30548),
    FERMENTED_SPIDER_EYE(19386),
    FERN(15794),
    FILLED_MAP(23504),
    FIRE(16396, Fire.class),
    FIREWORK_ROCKET(23841),
    FIREWORK_STAR(12190),
    FIRE_CHARGE(4842),
    FIRE_CORAL(29151),
    FIRE_CORAL_BLOCK(12119),
    FIRE_CORAL_FAN(11112, Waterlogged.class),
    FIRE_CORAL_WALL_FAN(20100, CoralWallFan.class),
    FISHING_ROD(4167, 1, 64),
    FLINT(23596),
    FLINT_AND_STEEL(28620, 1, 64),
    FLOWER_POT(30567),
    FROSTED_ICE(21814, Ageable.class),
    FURNACE(8133, Furnace.class),
    FURNACE_MINECART(14196, 1),
    GHAST_SPAWN_EGG(9970),
    GHAST_TEAR(18222),
    GLASS(6195),
    GLASS_BOTTLE(6116),
    GLASS_PANE(5709, GlassPane.class),
    GLISTERING_MELON_SLICE(20158),
    GLOWSTONE(32713),
    GLOWSTONE_DUST(6665),
    GOLDEN_APPLE(27732),
    GOLDEN_AXE(4878, 1, 32),
    GOLDEN_BOOTS(7859, 1, 91),
    GOLDEN_CARROT(5300),
    GOLDEN_CHESTPLATE(4507, 1, 112),
    GOLDEN_HELMET(7945, 1, 77),
    GOLDEN_HOE(19337, 1, 32),
    GOLDEN_HORSE_ARMOR(7996, 1),
    GOLDEN_LEGGINGS(21002, 1, 105),
    GOLDEN_PICKAXE(10901, 1, 32),
    GOLDEN_SHOVEL(15597, 1, 32),
    GOLDEN_SWORD(10505, 1, 32),
    GOLD_BLOCK(27392),
    GOLD_INGOT(28927),
    GOLD_NUGGET(28814),
    GOLD_ORE(32625),
    GRANITE(21091),
    GRASS(6155),
    GRASS_BLOCK(28346, Snowable.class),
    GRASS_PATH(8604),
    GRAVEL(7804),
    GRAY_BANNER(12053, 16, Rotatable.class),
    GRAY_BED(15745, 1, Bed.class),
    GRAY_CARPET(26991),
    GRAY_CONCRETE(13959),
    GRAY_CONCRETE_POWDER(13031),
    GRAY_DYE(9184),
    GRAY_GLAZED_TERRACOTTA(6256, Directional.class),
    GRAY_SHULKER_BOX(12754, 1, Directional.class),
    GRAY_STAINED_GLASS(29979),
    GRAY_STAINED_GLASS_PANE(25272, GlassPane.class),
    GRAY_TERRACOTTA(18004),
    GRAY_WALL_BANNER(24275, Directional.class),
    GRAY_WOOL(27209),
    GREEN_BANNER(10698, 16, Rotatable.class),
    GREEN_BED(13797, 1, Bed.class),
    GREEN_CARPET(7780),
    GREEN_CONCRETE(17949),
    GREEN_CONCRETE_POWDER(6904),
    GREEN_GLAZED_TERRACOTTA(6958, Directional.class),
    GREEN_SHULKER_BOX(9377, 1, Directional.class),
    GREEN_STAINED_GLASS(22503),
    GREEN_STAINED_GLASS_PANE(4767, GlassPane.class),
    GREEN_TERRACOTTA(4105),
    GREEN_WALL_BANNER(15046, Directional.class),
    GREEN_WOOL(25085),
    GUARDIAN_SPAWN_EGG(20113),
    GUNPOWDER(29974),
    HAY_BLOCK(17461, Orientable.class),
    HEART_OF_THE_SEA(11807),
    HEAVY_WEIGHTED_PRESSURE_PLATE(16970, AnaloguePowerable.class),
    HOPPER(31974, Hopper.class),
    HOPPER_MINECART(19024, 1),
    HORN_CORAL(19511),
    HORN_CORAL_BLOCK(19958),
    HORN_CORAL_FAN(13610, Waterlogged.class),
    HORN_CORAL_WALL_FAN(28883, CoralWallFan.class),
    HORSE_SPAWN_EGG(25981),
    HUSK_SPAWN_EGG(20178),
    ICE(30428),
    INFESTED_CHISELED_STONE_BRICKS(4728),
    INFESTED_COBBLESTONE(28798),
    INFESTED_CRACKED_STONE_BRICKS(7476),
    INFESTED_MOSSY_STONE_BRICKS(9850),
    INFESTED_STONE(18440),
    INFESTED_STONE_BRICKS(19749),
    INK_SAC(7184),
    IRON_AXE(15894, 1, 250),
    IRON_BARS(9378, Fence.class),
    IRON_BLOCK(24754),
    IRON_BOOTS(8531, 1, 195),
    IRON_CHESTPLATE(28112, 1, 240),
    IRON_DOOR(4788, Door.class),
    IRON_HELMET(12025, 1, 165),
    IRON_HOE(11339, 1, 250),
    IRON_HORSE_ARMOR(30108, 1),
    IRON_INGOT(24895),
    IRON_LEGGINGS(18951, 1, 225),
    IRON_NUGGET(13715),
    IRON_ORE(19834),
    IRON_PICKAXE(8842, 1, 250),
    IRON_SHOVEL(30045, 1, 250),
    IRON_SWORD(10904, 1, 250),
    IRON_TRAPDOOR(17095, TrapDoor.class),
    ITEM_FRAME(27318),
    JACK_O_LANTERN(31612, Directional.class),
    JUKEBOX(19264, Jukebox.class),
    JUNGLE_BOAT(4495, 1),
    JUNGLE_BUTTON(25317, Switch.class),
    JUNGLE_DOOR(28163, Door.class),
    JUNGLE_FENCE(14358, Fence.class),
    JUNGLE_FENCE_GATE(21360, Gate.class),
    JUNGLE_LEAVES(5133, Leaves.class),
    JUNGLE_LOG(20721, Orientable.class),
    JUNGLE_PLANKS(26445),
    JUNGLE_PRESSURE_PLATE(11376, Powerable.class),
    JUNGLE_SAPLING(17951, Sapling.class),
    JUNGLE_SLAB(19117, Slab.class),
    JUNGLE_STAIRS(20636, Stairs.class),
    JUNGLE_TRAPDOOR(8626, TrapDoor.class),
    JUNGLE_WOOD(10341, Orientable.class),
    KELP(21916, Ageable.class),
    KELP_PLANT(29697),
    KNOWLEDGE_BOOK(12646, 1),
    LADDER(23599, Ladder.class),
    LAPIS_BLOCK(14485),
    LAPIS_LAZULI(11075),
    LAPIS_ORE(22934),
    LARGE_FERN(30177, Bisected.class),
    LAVA(8415, Levelled.class),
    LAVA_BUCKET(9228, 1),
    LEAD(29539),
    LEATHER(16414),
    LEATHER_BOOTS(15282, 1, 65),
    LEATHER_CHESTPLATE(29275, 1, 80),
    LEATHER_HELMET(11624, 1, 55),
    LEATHER_LEGGINGS(28210, 1, 75),
    LEVER(15319, Switch.class),
    LIGHT_BLUE_BANNER(18060, 16, Rotatable.class),
    LIGHT_BLUE_BED(20957, 1, Bed.class),
    LIGHT_BLUE_CARPET(21194),
    LIGHT_BLUE_CONCRETE(29481),
    LIGHT_BLUE_CONCRETE_POWDER(31206),
    LIGHT_BLUE_DYE(28738),
    LIGHT_BLUE_GLAZED_TERRACOTTA(4336, Directional.class),
    LIGHT_BLUE_SHULKER_BOX(18226, 1, Directional.class),
    LIGHT_BLUE_STAINED_GLASS(17162),
    LIGHT_BLUE_STAINED_GLASS_PANE(18721, GlassPane.class),
    LIGHT_BLUE_TERRACOTTA(31779),
    LIGHT_BLUE_WALL_BANNER(12011, Directional.class),
    LIGHT_BLUE_WOOL(21073),
    LIGHT_GRAY_BANNER(11417, 16, Rotatable.class),
    LIGHT_GRAY_BED(5090, 1, Bed.class),
    LIGHT_GRAY_CARPET(11317),
    LIGHT_GRAY_CONCRETE(14453),
    LIGHT_GRAY_CONCRETE_POWDER(21589),
    LIGHT_GRAY_DYE(27643),
    LIGHT_GRAY_GLAZED_TERRACOTTA(10707, Directional.class),
    LIGHT_GRAY_SHULKER_BOX(21345, 1, Directional.class),
    LIGHT_GRAY_STAINED_GLASS(5843),
    LIGHT_GRAY_STAINED_GLASS_PANE(19008, GlassPane.class),
    LIGHT_GRAY_TERRACOTTA(26388),
    LIGHT_GRAY_WALL_BANNER(31088, Directional.class),
    LIGHT_GRAY_WOOL(22936),
    LIGHT_WEIGHTED_PRESSURE_PLATE(14875, AnaloguePowerable.class),
    LILAC(22837, Bisected.class),
    LILY_PAD(19271),
    LIME_BANNER(18887, 16, Rotatable.class),
    LIME_BED(27860, 1, Bed.class),
    LIME_CARPET(15443),
    LIME_CONCRETE(5863),
    LIME_CONCRETE_POWDER(28859),
    LIME_DYE(6147),
    LIME_GLAZED_TERRACOTTA(13861, Directional.class),
    LIME_SHULKER_BOX(28360, 1, Directional.class),
    LIME_STAINED_GLASS(24266),
    LIME_STAINED_GLASS_PANE(10610, GlassPane.class),
    LIME_TERRACOTTA(24013),
    LIME_WALL_BANNER(21422, Directional.class),
    LIME_WOOL(10443),
    LINGERING_POTION(25857, 1),
    LLAMA_SPAWN_EGG(23640),
    MAGENTA_BANNER(15591, 16, Rotatable.class),
    MAGENTA_BED(20061, 1, Bed.class),
    MAGENTA_CARPET(6180),
    MAGENTA_CONCRETE(20591),
    MAGENTA_CONCRETE_POWDER(8272),
    MAGENTA_DYE(11788),
    MAGENTA_GLAZED_TERRACOTTA(8067, Directional.class),
    MAGENTA_SHULKER_BOX(21566, 1, Directional.class),
    MAGENTA_STAINED_GLASS(26814),
    MAGENTA_STAINED_GLASS_PANE(14082, GlassPane.class),
    MAGENTA_TERRACOTTA(25900),
    MAGENTA_WALL_BANNER(23291, Directional.class),
    MAGENTA_WOOL(11853),
    MAGMA_BLOCK(25927),
    MAGMA_CREAM(25097),
    MAGMA_CUBE_SPAWN_EGG(26638),
    MAP(21655),
    MELON(25172),
    MELON_SEEDS(18340),
    MELON_SLICE(5347),
    MELON_STEM(8247, Ageable.class),
    MILK_BUCKET(9680, 1),
    MINECART(14352, 1),
    MOOSHROOM_SPAWN_EGG(22125),
    MOSSY_COBBLESTONE(21900, MultipleFacing.class),
    MOSSY_COBBLESTONE_WALL(11536, Fence.class),
    MOSSY_STONE_BRICKS(16415),
    MOVING_PISTON(13831, TechnicalPiston.class),
    MULE_SPAWN_EGG(11229),
    MUSHROOM_STEM(16543, MultipleFacing.class),
    MUSHROOM_STEW(16336, 1),
    MUSIC_DISC_11(27426, 1),
    MUSIC_DISC_13(16359, 1),
    MUSIC_DISC_BLOCKS(26667, 1),
    MUSIC_DISC_CAT(16246, 1),
    MUSIC_DISC_CHIRP(19436, 1),
    MUSIC_DISC_FAR(13823, 1),
    MUSIC_DISC_MALL(11517, 1),
    MUSIC_DISC_MELLOHI(26117, 1),
    MUSIC_DISC_STAL(14989, 1),
    MUSIC_DISC_STRAD(16785, 1),
    MUSIC_DISC_WAIT(26499, 1),
    MUSIC_DISC_WARD(24026, 1),
    MUTTON(4792),
    MYCELIUM(9913, Snowable.class),
    NAME_TAG(30731),
    NAUTILUS_SHELL(19989),
    NETHERRACK(23425),
    NETHER_BRICK(19996),
    NETHER_BRICKS(27802),
    NETHER_BRICK_FENCE(5286, Fence.class),
    NETHER_BRICK_SLAB(26586, Slab.class),
    NETHER_BRICK_STAIRS(12085, Stairs.class),
    NETHER_PORTAL(19469, Orientable.class),
    NETHER_QUARTZ_ORE(4807),
    NETHER_STAR(12469),
    NETHER_WART(29227, Ageable.class),
    NETHER_WART_BLOCK(15486),
    NOTE_BLOCK(20979, NoteBlock.class),
    OAK_BOAT(17570, 1),
    OAK_BUTTON(13510, Switch.class),
    OAK_DOOR(20341, Door.class),
    OAK_FENCE(6442, Fence.class),
    OAK_FENCE_GATE(16689, Gate.class),
    OAK_LEAVES(4385, Leaves.class),
    OAK_LOG(26723, Orientable.class),
    OAK_PLANKS(14905),
    OAK_PRESSURE_PLATE(20108, Powerable.class),
    OAK_SAPLING(9636, Sapling.class),
    OAK_SLAB(12002, Slab.class),
    OAK_STAIRS(5449, Stairs.class),
    OAK_TRAPDOOR(16927, TrapDoor.class),
    OAK_WOOD(7378, Orientable.class),
    OBSERVER(10726, Observer.class),
    OBSIDIAN(32723),
    OCELOT_SPAWN_EGG(30080),
    ORANGE_BANNER(4839, 16, Rotatable.class),
    ORANGE_BED(11194, 1, Bed.class),
    ORANGE_CARPET(24752),
    ORANGE_CONCRETE(19914),
    ORANGE_CONCRETE_POWDER(30159),
    ORANGE_DYE(13866),
    ORANGE_GLAZED_TERRACOTTA(27451, Directional.class),
    ORANGE_SHULKER_BOX(21673, 1, Directional.class),
    ORANGE_STAINED_GLASS(25142),
    ORANGE_STAINED_GLASS_PANE(21089, GlassPane.class),
    ORANGE_TERRACOTTA(18684),
    ORANGE_TULIP(26038),
    ORANGE_WALL_BANNER(9936, Directional.class),
    ORANGE_WOOL(23957),
    OXEYE_DAISY(11709),
    PACKED_ICE(28993),
    PAINTING(23945),
    PAPER(9923),
    PARROT_SPAWN_EGG(23614),
    PEONY(21155, Bisected.class),
    PETRIFIED_OAK_SLAB(18658, Slab.class),
    PHANTOM_MEMBRANE(18398),
    PHANTOM_SPAWN_EGG(24648),
    PIG_SPAWN_EGG(22584),
    PINK_BANNER(19439, 16, Rotatable.class),
    PINK_BED(13795, 1, Bed.class),
    PINK_CARPET(30186),
    PINK_CONCRETE(5227),
    PINK_CONCRETE_POWDER(6421),
    PINK_DYE(31151),
    PINK_GLAZED_TERRACOTTA(10260, Directional.class),
    PINK_SHULKER_BOX(24968, 1, Directional.class),
    PINK_STAINED_GLASS(16164),
    PINK_STAINED_GLASS_PANE(24637, GlassPane.class),
    PINK_TERRACOTTA(23727),
    PINK_TULIP(27319),
    PINK_WALL_BANNER(9421, Directional.class),
    PINK_WOOL(7611),
    PISTON(21130, Piston.class),
    PISTON_HEAD(30226, PistonHead.class),
    PLAYER_HEAD(21174, Rotatable.class),
    PLAYER_WALL_HEAD(13164, Directional.class),
    PODZOL(24068, Snowable.class),
    POISONOUS_POTATO(32640),
    POLAR_BEAR_SPAWN_EGG(17015),
    POLISHED_ANDESITE(8335),
    POLISHED_DIORITE(31615),
    POLISHED_GRANITE(5477),
    POPPED_CHORUS_FRUIT(27844),
    POPPY(12851),
    PORKCHOP(30896),
    POTATO(21088),
    POTATOES(10879, Ageable.class),
    POTION(24020, 1),
    POTTED_ACACIA_SAPLING(14096),
    POTTED_ALLIUM(13184),
    POTTED_AZURE_BLUET(8754),
    POTTED_BIRCH_SAPLING(32484),
    POTTED_BLUE_ORCHID(6599),
    POTTED_BROWN_MUSHROOM(14481),
    POTTED_CACTUS(8777),
    POTTED_DANDELION(9727),
    POTTED_DARK_OAK_SAPLING(6486),
    POTTED_DEAD_BUSH(13020),
    POTTED_FERN(23315),
    POTTED_JUNGLE_SAPLING(7525),
    POTTED_OAK_SAPLING(11905),
    POTTED_ORANGE_TULIP(28807),
    POTTED_OXEYE_DAISY(19707),
    POTTED_PINK_TULIP(10089),
    POTTED_POPPY(7457),
    POTTED_RED_MUSHROOM(22881),
    POTTED_RED_TULIP(28594),
    POTTED_SPRUCE_SAPLING(29498),
    POTTED_WHITE_TULIP(24330),
    POWERED_RAIL(11064, RedstoneRail.class),
    PRISMARINE(7539),
    PRISMARINE_BRICKS(29118),
    PRISMARINE_BRICK_SLAB(26672, Slab.class),
    PRISMARINE_BRICK_STAIRS(15445, Stairs.class),
    PRISMARINE_CRYSTALS(31546),
    PRISMARINE_SHARD(10993),
    PRISMARINE_SLAB(31323, Slab.class),
    PRISMARINE_STAIRS(19217, Stairs.class),
    PUFFERFISH(8115),
    PUFFERFISH_BUCKET(8861, 1),
    PUFFERFISH_SPAWN_EGG(24573),
    PUMPKIN(19170),
    PUMPKIN_PIE(28725),
    PUMPKIN_SEEDS(28985),
    PUMPKIN_STEM(19021, Ageable.class),
    PURPLE_BANNER(29027, 16, Rotatable.class),
    PURPLE_BED(29755, 1, Bed.class),
    PURPLE_CARPET(5574),
    PURPLE_CONCRETE(20623),
    PURPLE_CONCRETE_POWDER(26808),
    PURPLE_DYE(6347),
    PURPLE_GLAZED_TERRACOTTA(4818, Directional.class),
    PURPLE_SHULKER_BOX(10373, 1, Directional.class),
    PURPLE_STAINED_GLASS(21845),
    PURPLE_STAINED_GLASS_PANE(10948, GlassPane.class),
    PURPLE_TERRACOTTA(10387),
    PURPLE_WALL_BANNER(14298, Directional.class),
    PURPLE_WOOL(11922),
    PURPUR_BLOCK(7538),
    PURPUR_PILLAR(26718, Orientable.class),
    PURPUR_SLAB(11487, Slab.class),
    PURPUR_STAIRS(8921, Stairs.class),
    QUARTZ(23608),
    QUARTZ_BLOCK(11987),
    QUARTZ_PILLAR(16452, Orientable.class),
    QUARTZ_SLAB(4423, Slab.class),
    QUARTZ_STAIRS(24079, Stairs.class),
    RABBIT(23068),
    RABBIT_FOOT(13864),
    RABBIT_HIDE(12467),
    RABBIT_SPAWN_EGG(26496),
    RABBIT_STEW(10611, 1),
    RAIL(13285, Rail.class),
    REDSTONE(11233),
    REDSTONE_BLOCK(19496),
    REDSTONE_LAMP(8217, Lightable.class),
    REDSTONE_ORE(10887, Lightable.class),
    REDSTONE_TORCH(22547, Lightable.class),
    REDSTONE_WALL_TORCH(7595, RedstoneWallTorch.class),
    REDSTONE_WIRE(25984, RedstoneWire.class),
    RED_BANNER(26961, 16, Rotatable.class),
    RED_BED(30910, 1, Bed.class),
    RED_CARPET(5424),
    RED_CONCRETE(8032),
    RED_CONCRETE_POWDER(13286),
    RED_GLAZED_TERRACOTTA(24989, Directional.class),
    RED_MUSHROOM(19728),
    RED_MUSHROOM_BLOCK(20766, MultipleFacing.class),
    RED_NETHER_BRICKS(18056),
    RED_SAND(16279),
    RED_SANDSTONE(9092),
    RED_SANDSTONE_SLAB(17550, Slab.class),
    RED_SANDSTONE_STAIRS(25466, Stairs.class),
    RED_SHULKER_BOX(32448, 1, Directional.class),
    RED_STAINED_GLASS(9717),
    RED_STAINED_GLASS_PANE(8630, GlassPane.class),
    RED_TERRACOTTA(5086),
    RED_TULIP(16781),
    RED_WALL_BANNER(4378, Directional.class),
    RED_WOOL(11621),
    REPEATER(28823, Repeater.class),
    REPEATING_COMMAND_BLOCK(12405, CommandBlock.class),
    ROSE_BUSH(6080, Bisected.class),
    ROSE_RED(15694),
    ROTTEN_FLESH(21591),
    SADDLE(30206, 1),
    SALMON(18516),
    SALMON_BUCKET(31427, 1),
    SALMON_SPAWN_EGG(18739),
    SAND(11542),
    SANDSTONE(13141),
    SANDSTONE_SLAB(29830, Slab.class),
    SANDSTONE_STAIRS(18474, Stairs.class),
    SCUTE(11914),
    SEAGRASS(23942),
    SEA_LANTERN(16984),
    SEA_PICKLE(19562, SeaPickle.class),
    SHEARS(27971, 1, 238),
    SHEEP_SPAWN_EGG(24488),
    SHIELD(29943, 1, 336),
    SHULKER_BOX(7776, 1, Directional.class),
    SHULKER_SHELL(27848),
    SHULKER_SPAWN_EGG(31848),
    SIGN(16918, 16, Sign.class),
    SILVERFISH_SPAWN_EGG(14537),
    SKELETON_HORSE_SPAWN_EGG(21356),
    SKELETON_SKULL(13270, Rotatable.class),
    SKELETON_SPAWN_EGG(15261),
    SKELETON_WALL_SKULL(31650, Directional.class),
    SLIME_BALL(5242),
    SLIME_BLOCK(31892),
    SLIME_SPAWN_EGG(6550),
    SMOOTH_QUARTZ(14415),
    SMOOTH_RED_SANDSTONE(25180),
    SMOOTH_SANDSTONE(30039),
    SMOOTH_STONE(21910),
    SNOW(14146, Snow.class),
    SNOWBALL(19487, 16),
    SNOW_BLOCK(19913),
    SOUL_SAND(16841),
    SPAWNER(7018),
    SPECTRAL_ARROW(4568),
    SPIDER_EYE(9318),
    SPIDER_SPAWN_EGG(14984),
    SPLASH_POTION(30248, 1),
    SPONGE(15860),
    SPRUCE_BOAT(9606, 1),
    SPRUCE_BUTTON(23281, Switch.class),
    SPRUCE_DOOR(10642, Door.class),
    SPRUCE_FENCE(25416, Fence.class),
    SPRUCE_FENCE_GATE(26423, Gate.class),
    SPRUCE_LEAVES(20039, Leaves.class),
    SPRUCE_LOG(9726, Orientable.class),
    SPRUCE_PLANKS(14593),
    SPRUCE_PRESSURE_PLATE(15932, Powerable.class),
    SPRUCE_SAPLING(19874, Sapling.class),
    SPRUCE_SLAB(4348, Slab.class),
    SPRUCE_STAIRS(11192, Stairs.class),
    SPRUCE_TRAPDOOR(10289, TrapDoor.class),
    SPRUCE_WOOD(32328, Orientable.class),
    SQUID_SPAWN_EGG(10682),
    STICK(9773),
    STICKY_PISTON(18127, Piston.class),
    STONE(22948),
    STONE_AXE(6338, 1, 131),
    STONE_BRICKS(6962),
    STONE_BRICK_SLAB(19676, Slab.class),
    STONE_BRICK_STAIRS(27032, Stairs.class),
    STONE_BUTTON(12279, Switch.class),
    STONE_HOE(22855, 1, 131),
    STONE_PICKAXE(14611, 1, 131),
    STONE_PRESSURE_PLATE(22591, Powerable.class),
    STONE_SHOVEL(9520, 1, 131),
    STONE_SLAB(19838, Slab.class),
    STONE_SWORD(25084, 1, 131),
    STRAY_SPAWN_EGG(30153),
    STRING(12806),
    STRIPPED_ACACIA_LOG(18167, Orientable.class),
    STRIPPED_ACACIA_WOOD(27193, Orientable.class),
    STRIPPED_BIRCH_LOG(8838, Orientable.class),
    STRIPPED_BIRCH_WOOD(22350, Orientable.class),
    STRIPPED_DARK_OAK_LOG(6492, Orientable.class),
    STRIPPED_DARK_OAK_WOOD(16000, Orientable.class),
    STRIPPED_JUNGLE_LOG(15476, Orientable.class),
    STRIPPED_JUNGLE_WOOD(30315, Orientable.class),
    STRIPPED_OAK_LOG(20523, Orientable.class),
    STRIPPED_OAK_WOOD(31455, Orientable.class),
    STRIPPED_SPRUCE_LOG(6140, Orientable.class),
    STRIPPED_SPRUCE_WOOD(6467, Orientable.class),
    STRUCTURE_BLOCK(26831, StructureBlock.class),
    STRUCTURE_VOID(30806),
    SUGAR(30638),
    SUGAR_CANE(7726, Ageable.class),
    SUNFLOWER(7408, Bisected.class),
    TALL_GRASS(21559, Bisected.class),
    TALL_SEAGRASS(27189, Bisected.class),
    TERRACOTTA(16544),
    TIPPED_ARROW(25164),
    TNT(7896),
    TNT_MINECART(4277, 1),
    TORCH(6063),
    TOTEM_OF_UNDYING(10139, 1),
    TRAPPED_CHEST(18970, Chest.class),
    TRIDENT(7534, 1, 250),
    TRIPWIRE(8810, Tripwire.class),
    TRIPWIRE_HOOK(8130, TripwireHook.class),
    TROPICAL_FISH(24879),
    TROPICAL_FISH_BUCKET(29995, 1),
    TROPICAL_FISH_SPAWN_EGG(19713),
    TUBE_CORAL(23048),
    TUBE_CORAL_BLOCK(23723),
    TUBE_CORAL_FAN(19929, Waterlogged.class),
    TUBE_CORAL_WALL_FAN(25282, CoralWallFan.class),
    TURTLE_EGG(32101, TurtleEgg.class),
    TURTLE_HELMET(30120, 1, 275),
    TURTLE_SPAWN_EGG(17324),
    VEX_SPAWN_EGG(27751),
    VILLAGER_SPAWN_EGG(30348),
    VINDICATOR_SPAWN_EGG(25324),
    VINE(14564, MultipleFacing.class),
    VOID_AIR(13668),
    WALL_SIGN(10644, WallSign.class),
    WALL_TORCH(25890, Directional.class),
    WATER(24998, Levelled.class),
    WATER_BUCKET(8802, 1),
    WET_SPONGE(9043),
    WHEAT(27709, Ageable.class),
    WHEAT_SEEDS(28742),
    WHITE_BANNER(17562, 16, Rotatable.class),
    WHITE_BED(8185, 1, Bed.class),
    WHITE_CARPET(15117),
    WHITE_CONCRETE(6281),
    WHITE_CONCRETE_POWDER(10363),
    WHITE_GLAZED_TERRACOTTA(11326, Directional.class),
    WHITE_SHULKER_BOX(31750, 1, Directional.class),
    WHITE_STAINED_GLASS(31190),
    WHITE_STAINED_GLASS_PANE(10557, GlassPane.class),
    WHITE_TERRACOTTA(20975),
    WHITE_TULIP(9742),
    WHITE_WALL_BANNER(15967, Directional.class),
    WHITE_WOOL(8624),
    WITCH_SPAWN_EGG(11837),
    WITHER_SKELETON_SKULL(31487, Rotatable.class),
    WITHER_SKELETON_SPAWN_EGG(10073),
    WITHER_SKELETON_WALL_SKULL(9326, Directional.class),
    WOLF_SPAWN_EGG(21692),
    WOODEN_AXE(6292, 1, 59),
    WOODEN_HOE(16043, 1, 59),
    WOODEN_PICKAXE(12792, 1, 59),
    WOODEN_SHOVEL(28432, 1, 59),
    WOODEN_SWORD(7175, 1, 59),
    WRITABLE_BOOK(13393, 1),
    WRITTEN_BOOK(24164, 16),
    YELLOW_BANNER(30382, 16, Rotatable.class),
    YELLOW_BED(30410, 1, Bed.class),
    YELLOW_CARPET(18149),
    YELLOW_CONCRETE(15722),
    YELLOW_CONCRETE_POWDER(10655),
    YELLOW_GLAZED_TERRACOTTA(10914, Directional.class),
    YELLOW_SHULKER_BOX(28700, 1, Directional.class),
    YELLOW_STAINED_GLASS(12182),
    YELLOW_STAINED_GLASS_PANE(20298, GlassPane.class),
    YELLOW_TERRACOTTA(32129),
    YELLOW_WALL_BANNER(32004, Directional.class),
    YELLOW_WOOL(29507),
    ZOMBIE_HEAD(9304, Rotatable.class),
    ZOMBIE_HORSE_SPAWN_EGG(4275),
    ZOMBIE_PIGMAN_SPAWN_EGG(11531),
    ZOMBIE_SPAWN_EGG(5814),
    ZOMBIE_VILLAGER_SPAWN_EGG(10311),
    ZOMBIE_WALL_HEAD(16296, Directional.class),
    // ----- Legacy Separator -----
    @Deprecated
    LEGACY_AIR(0, 0),
    @Deprecated
    LEGACY_STONE(1),
    @Deprecated
    LEGACY_GRASS(2),
    @Deprecated
    LEGACY_DIRT(3),
    @Deprecated
    LEGACY_COBBLESTONE(4),
    @Deprecated
    LEGACY_WOOD(5, org.bukkit.material.Wood.class),
    @Deprecated
    LEGACY_SAPLING(6, org.bukkit.material.Sapling.class),
    @Deprecated
    LEGACY_BEDROCK(7),
    @Deprecated
    LEGACY_WATER(8, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_STATIONARY_WATER(9, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_LAVA(10, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_STATIONARY_LAVA(11, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_SAND(12),
    @Deprecated
    LEGACY_GRAVEL(13),
    @Deprecated
    LEGACY_GOLD_ORE(14),
    @Deprecated
    LEGACY_IRON_ORE(15),
    @Deprecated
    LEGACY_COAL_ORE(16),
    @Deprecated
    LEGACY_LOG(17, org.bukkit.material.Tree.class),
    @Deprecated
    LEGACY_LEAVES(18, org.bukkit.material.Leaves.class),
    @Deprecated
    LEGACY_SPONGE(19),
    @Deprecated
    LEGACY_GLASS(20),
    @Deprecated
    LEGACY_LAPIS_ORE(21),
    @Deprecated
    LEGACY_LAPIS_BLOCK(22),
    @Deprecated
    LEGACY_DISPENSER(23, org.bukkit.material.Dispenser.class),
    @Deprecated
    LEGACY_SANDSTONE(24, org.bukkit.material.Sandstone.class),
    @Deprecated
    LEGACY_NOTE_BLOCK(25),
    @Deprecated
    LEGACY_BED_BLOCK(26, org.bukkit.material.Bed.class),
    @Deprecated
    LEGACY_POWERED_RAIL(27, org.bukkit.material.PoweredRail.class),
    @Deprecated
    LEGACY_DETECTOR_RAIL(28, org.bukkit.material.DetectorRail.class),
    @Deprecated
    LEGACY_PISTON_STICKY_BASE(29, org.bukkit.material.PistonBaseMaterial.class),
    @Deprecated
    LEGACY_WEB(30),
    @Deprecated
    LEGACY_LONG_GRASS(31, org.bukkit.material.LongGrass.class),
    @Deprecated
    LEGACY_DEAD_BUSH(32),
    @Deprecated
    LEGACY_PISTON_BASE(33, org.bukkit.material.PistonBaseMaterial.class),
    @Deprecated
    LEGACY_PISTON_EXTENSION(34, org.bukkit.material.PistonExtensionMaterial.class),
    @Deprecated
    LEGACY_WOOL(35, org.bukkit.material.Wool.class),
    @Deprecated
    LEGACY_PISTON_MOVING_PIECE(36),
    @Deprecated
    LEGACY_YELLOW_FLOWER(37),
    @Deprecated
    LEGACY_RED_ROSE(38),
    @Deprecated
    LEGACY_BROWN_MUSHROOM(39),
    @Deprecated
    LEGACY_RED_MUSHROOM(40),
    @Deprecated
    LEGACY_GOLD_BLOCK(41),
    @Deprecated
    LEGACY_IRON_BLOCK(42),
    @Deprecated
    LEGACY_DOUBLE_STEP(43, org.bukkit.material.Step.class),
    @Deprecated
    LEGACY_STEP(44, org.bukkit.material.Step.class),
    @Deprecated
    LEGACY_BRICK(45),
    @Deprecated
    LEGACY_TNT(46),
    @Deprecated
    LEGACY_BOOKSHELF(47),
    @Deprecated
    LEGACY_MOSSY_COBBLESTONE(48),
    @Deprecated
    LEGACY_OBSIDIAN(49),
    @Deprecated
    LEGACY_TORCH(50, org.bukkit.material.Torch.class),
    @Deprecated
    LEGACY_FIRE(51),
    @Deprecated
    LEGACY_MOB_SPAWNER(52),
    @Deprecated
    LEGACY_WOOD_STAIRS(53, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_CHEST(54, org.bukkit.material.Chest.class),
    @Deprecated
    LEGACY_REDSTONE_WIRE(55, org.bukkit.material.RedstoneWire.class),
    @Deprecated
    LEGACY_DIAMOND_ORE(56),
    @Deprecated
    LEGACY_DIAMOND_BLOCK(57),
    @Deprecated
    LEGACY_WORKBENCH(58),
    @Deprecated
    LEGACY_CROPS(59, org.bukkit.material.Crops.class),
    @Deprecated
    LEGACY_SOIL(60, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_FURNACE(61, org.bukkit.material.Furnace.class),
    @Deprecated
    LEGACY_BURNING_FURNACE(62, org.bukkit.material.Furnace.class),
    @Deprecated
    LEGACY_SIGN_POST(63, 64, org.bukkit.material.Sign.class),
    @Deprecated
    LEGACY_WOODEN_DOOR(64, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_LADDER(65, org.bukkit.material.Ladder.class),
    @Deprecated
    LEGACY_RAILS(66, org.bukkit.material.Rails.class),
    @Deprecated
    LEGACY_COBBLESTONE_STAIRS(67, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_WALL_SIGN(68, 64, org.bukkit.material.Sign.class),
    @Deprecated
    LEGACY_LEVER(69, org.bukkit.material.Lever.class),
    @Deprecated
    LEGACY_STONE_PLATE(70, org.bukkit.material.PressurePlate.class),
    @Deprecated
    LEGACY_IRON_DOOR_BLOCK(71, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_WOOD_PLATE(72, org.bukkit.material.PressurePlate.class),
    @Deprecated
    LEGACY_REDSTONE_ORE(73),
    @Deprecated
    LEGACY_GLOWING_REDSTONE_ORE(74),
    @Deprecated
    LEGACY_REDSTONE_TORCH_OFF(75, org.bukkit.material.RedstoneTorch.class),
    @Deprecated
    LEGACY_REDSTONE_TORCH_ON(76, org.bukkit.material.RedstoneTorch.class),
    @Deprecated
    LEGACY_STONE_BUTTON(77, org.bukkit.material.Button.class),
    @Deprecated
    LEGACY_SNOW(78),
    @Deprecated
    LEGACY_ICE(79),
    @Deprecated
    LEGACY_SNOW_BLOCK(80),
    @Deprecated
    LEGACY_CACTUS(81, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_CLAY(82),
    @Deprecated
    LEGACY_SUGAR_CANE_BLOCK(83, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_JUKEBOX(84),
    @Deprecated
    LEGACY_FENCE(85),
    @Deprecated
    LEGACY_PUMPKIN(86, org.bukkit.material.Pumpkin.class),
    @Deprecated
    LEGACY_NETHERRACK(87),
    @Deprecated
    LEGACY_SOUL_SAND(88),
    @Deprecated
    LEGACY_GLOWSTONE(89),
    @Deprecated
    LEGACY_PORTAL(90),
    @Deprecated
    LEGACY_JACK_O_LANTERN(91, org.bukkit.material.Pumpkin.class),
    @Deprecated
    LEGACY_CAKE_BLOCK(92, 64, org.bukkit.material.Cake.class),
    @Deprecated
    LEGACY_DIODE_BLOCK_OFF(93, org.bukkit.material.Diode.class),
    @Deprecated
    LEGACY_DIODE_BLOCK_ON(94, org.bukkit.material.Diode.class),
    @Deprecated
    LEGACY_STAINED_GLASS(95),
    @Deprecated
    LEGACY_TRAP_DOOR(96, org.bukkit.material.TrapDoor.class),
    @Deprecated
    LEGACY_MONSTER_EGGS(97, org.bukkit.material.MonsterEggs.class),
    @Deprecated
    LEGACY_SMOOTH_BRICK(98, org.bukkit.material.SmoothBrick.class),
    @Deprecated
    LEGACY_HUGE_MUSHROOM_1(99, org.bukkit.material.Mushroom.class),
    @Deprecated
    LEGACY_HUGE_MUSHROOM_2(100, org.bukkit.material.Mushroom.class),
    @Deprecated
    LEGACY_IRON_FENCE(101),
    @Deprecated
    LEGACY_THIN_GLASS(102),
    @Deprecated
    LEGACY_MELON_BLOCK(103),
    @Deprecated
    LEGACY_PUMPKIN_STEM(104, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_MELON_STEM(105, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_VINE(106, org.bukkit.material.Vine.class),
    @Deprecated
    LEGACY_FENCE_GATE(107, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_BRICK_STAIRS(108, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_SMOOTH_STAIRS(109, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_MYCEL(110),
    @Deprecated
    LEGACY_WATER_LILY(111),
    @Deprecated
    LEGACY_NETHER_BRICK(112),
    @Deprecated
    LEGACY_NETHER_FENCE(113),
    @Deprecated
    LEGACY_NETHER_BRICK_STAIRS(114, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_NETHER_WARTS(115, org.bukkit.material.NetherWarts.class),
    @Deprecated
    LEGACY_ENCHANTMENT_TABLE(116),
    @Deprecated
    LEGACY_BREWING_STAND(117, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_CAULDRON(118, org.bukkit.material.Cauldron.class),
    @Deprecated
    LEGACY_ENDER_PORTAL(119),
    @Deprecated
    LEGACY_ENDER_PORTAL_FRAME(120),
    @Deprecated
    LEGACY_ENDER_STONE(121),
    @Deprecated
    LEGACY_DRAGON_EGG(122),
    @Deprecated
    LEGACY_REDSTONE_LAMP_OFF(123),
    @Deprecated
    LEGACY_REDSTONE_LAMP_ON(124),
    @Deprecated
    LEGACY_WOOD_DOUBLE_STEP(125, org.bukkit.material.Wood.class),
    @Deprecated
    LEGACY_WOOD_STEP(126, org.bukkit.material.WoodenStep.class),
    @Deprecated
    LEGACY_COCOA(127, org.bukkit.material.CocoaPlant.class),
    @Deprecated
    LEGACY_SANDSTONE_STAIRS(128, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_EMERALD_ORE(129),
    @Deprecated
    LEGACY_ENDER_CHEST(130, org.bukkit.material.EnderChest.class),
    @Deprecated
    LEGACY_TRIPWIRE_HOOK(131, org.bukkit.material.TripwireHook.class),
    @Deprecated
    LEGACY_TRIPWIRE(132, org.bukkit.material.Tripwire.class),
    @Deprecated
    LEGACY_EMERALD_BLOCK(133),
    @Deprecated
    LEGACY_SPRUCE_WOOD_STAIRS(134, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_BIRCH_WOOD_STAIRS(135, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_JUNGLE_WOOD_STAIRS(136, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_COMMAND(137, org.bukkit.material.Command.class),
    @Deprecated
    LEGACY_BEACON(138),
    @Deprecated
    LEGACY_COBBLE_WALL(139),
    @Deprecated
    LEGACY_FLOWER_POT(140, org.bukkit.material.FlowerPot.class),
    @Deprecated
    LEGACY_CARROT(141, org.bukkit.material.Crops.class),
    @Deprecated
    LEGACY_POTATO(142, org.bukkit.material.Crops.class),
    @Deprecated
    LEGACY_WOOD_BUTTON(143, org.bukkit.material.Button.class),
    @Deprecated
    LEGACY_SKULL(144, org.bukkit.material.Skull.class),
    @Deprecated
    LEGACY_ANVIL(145),
    @Deprecated
    LEGACY_TRAPPED_CHEST(146, org.bukkit.material.Chest.class),
    @Deprecated
    LEGACY_GOLD_PLATE(147),
    @Deprecated
    LEGACY_IRON_PLATE(148),
    @Deprecated
    LEGACY_REDSTONE_COMPARATOR_OFF(149, org.bukkit.material.Comparator.class),
    @Deprecated
    LEGACY_REDSTONE_COMPARATOR_ON(150, org.bukkit.material.Comparator.class),
    @Deprecated
    LEGACY_DAYLIGHT_DETECTOR(151),
    @Deprecated
    LEGACY_REDSTONE_BLOCK(152),
    @Deprecated
    LEGACY_QUARTZ_ORE(153),
    @Deprecated
    LEGACY_HOPPER(154, org.bukkit.material.Hopper.class),
    @Deprecated
    LEGACY_QUARTZ_BLOCK(155),
    @Deprecated
    LEGACY_QUARTZ_STAIRS(156, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_ACTIVATOR_RAIL(157, org.bukkit.material.PoweredRail.class),
    @Deprecated
    LEGACY_DROPPER(158, org.bukkit.material.Dispenser.class),
    @Deprecated
    LEGACY_STAINED_CLAY(159),
    @Deprecated
    LEGACY_STAINED_GLASS_PANE(160),
    @Deprecated
    LEGACY_LEAVES_2(161, org.bukkit.material.Leaves.class),
    @Deprecated
    LEGACY_LOG_2(162, org.bukkit.material.Tree.class),
    @Deprecated
    LEGACY_ACACIA_STAIRS(163, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_DARK_OAK_STAIRS(164, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_SLIME_BLOCK(165),
    @Deprecated
    LEGACY_BARRIER(166),
    @Deprecated
    LEGACY_IRON_TRAPDOOR(167, org.bukkit.material.TrapDoor.class),
    @Deprecated
    LEGACY_PRISMARINE(168),
    @Deprecated
    LEGACY_SEA_LANTERN(169),
    @Deprecated
    LEGACY_HAY_BLOCK(170),
    @Deprecated
    LEGACY_CARPET(171),
    @Deprecated
    LEGACY_HARD_CLAY(172),
    @Deprecated
    LEGACY_COAL_BLOCK(173),
    @Deprecated
    LEGACY_PACKED_ICE(174),
    @Deprecated
    LEGACY_DOUBLE_PLANT(175),
    @Deprecated
    LEGACY_STANDING_BANNER(176, org.bukkit.material.Banner.class),
    @Deprecated
    LEGACY_WALL_BANNER(177, org.bukkit.material.Banner.class),
    @Deprecated
    LEGACY_DAYLIGHT_DETECTOR_INVERTED(178),
    @Deprecated
    LEGACY_RED_SANDSTONE(179),
    @Deprecated
    LEGACY_RED_SANDSTONE_STAIRS(180, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_DOUBLE_STONE_SLAB2(181),
    @Deprecated
    LEGACY_STONE_SLAB2(182),
    @Deprecated
    LEGACY_SPRUCE_FENCE_GATE(183, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_BIRCH_FENCE_GATE(184, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_JUNGLE_FENCE_GATE(185, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_DARK_OAK_FENCE_GATE(186, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_ACACIA_FENCE_GATE(187, org.bukkit.material.Gate.class),
    @Deprecated
    LEGACY_SPRUCE_FENCE(188),
    @Deprecated
    LEGACY_BIRCH_FENCE(189),
    @Deprecated
    LEGACY_JUNGLE_FENCE(190),
    @Deprecated
    LEGACY_DARK_OAK_FENCE(191),
    @Deprecated
    LEGACY_ACACIA_FENCE(192),
    @Deprecated
    LEGACY_SPRUCE_DOOR(193, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_BIRCH_DOOR(194, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_JUNGLE_DOOR(195, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_ACACIA_DOOR(196, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_DARK_OAK_DOOR(197, org.bukkit.material.Door.class),
    @Deprecated
    LEGACY_END_ROD(198),
    @Deprecated
    LEGACY_CHORUS_PLANT(199),
    @Deprecated
    LEGACY_CHORUS_FLOWER(200),
    @Deprecated
    LEGACY_PURPUR_BLOCK(201),
    @Deprecated
    LEGACY_PURPUR_PILLAR(202),
    @Deprecated
    LEGACY_PURPUR_STAIRS(203, org.bukkit.material.Stairs.class),
    @Deprecated
    LEGACY_PURPUR_DOUBLE_SLAB(204),
    @Deprecated
    LEGACY_PURPUR_SLAB(205),
    @Deprecated
    LEGACY_END_BRICKS(206),
    @Deprecated
    LEGACY_BEETROOT_BLOCK(207, org.bukkit.material.Crops.class),
    @Deprecated
    LEGACY_GRASS_PATH(208),
    @Deprecated
    LEGACY_END_GATEWAY(209),
    @Deprecated
    LEGACY_COMMAND_REPEATING(210, org.bukkit.material.Command.class),
    @Deprecated
    LEGACY_COMMAND_CHAIN(211, org.bukkit.material.Command.class),
    @Deprecated
    LEGACY_FROSTED_ICE(212),
    @Deprecated
    LEGACY_MAGMA(213),
    @Deprecated
    LEGACY_NETHER_WART_BLOCK(214),
    @Deprecated
    LEGACY_RED_NETHER_BRICK(215),
    @Deprecated
    LEGACY_BONE_BLOCK(216),
    @Deprecated
    LEGACY_STRUCTURE_VOID(217),
    @Deprecated
    LEGACY_OBSERVER(218, org.bukkit.material.Observer.class),
    @Deprecated
    LEGACY_WHITE_SHULKER_BOX(219, 1),
    @Deprecated
    LEGACY_ORANGE_SHULKER_BOX(220, 1),
    @Deprecated
    LEGACY_MAGENTA_SHULKER_BOX(221, 1),
    @Deprecated
    LEGACY_LIGHT_BLUE_SHULKER_BOX(222, 1),
    @Deprecated
    LEGACY_YELLOW_SHULKER_BOX(223, 1),
    @Deprecated
    LEGACY_LIME_SHULKER_BOX(224, 1),
    @Deprecated
    LEGACY_PINK_SHULKER_BOX(225, 1),
    @Deprecated
    LEGACY_GRAY_SHULKER_BOX(226, 1),
    @Deprecated
    LEGACY_SILVER_SHULKER_BOX(227, 1),
    @Deprecated
    LEGACY_CYAN_SHULKER_BOX(228, 1),
    @Deprecated
    LEGACY_PURPLE_SHULKER_BOX(229, 1),
    @Deprecated
    LEGACY_BLUE_SHULKER_BOX(230, 1),
    @Deprecated
    LEGACY_BROWN_SHULKER_BOX(231, 1),
    @Deprecated
    LEGACY_GREEN_SHULKER_BOX(232, 1),
    @Deprecated
    LEGACY_RED_SHULKER_BOX(233, 1),
    @Deprecated
    LEGACY_BLACK_SHULKER_BOX(234, 1),
    @Deprecated
    LEGACY_WHITE_GLAZED_TERRACOTTA(235),
    @Deprecated
    LEGACY_ORANGE_GLAZED_TERRACOTTA(236),
    @Deprecated
    LEGACY_MAGENTA_GLAZED_TERRACOTTA(237),
    @Deprecated
    LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA(238),
    @Deprecated
    LEGACY_YELLOW_GLAZED_TERRACOTTA(239),
    @Deprecated
    LEGACY_LIME_GLAZED_TERRACOTTA(240),
    @Deprecated
    LEGACY_PINK_GLAZED_TERRACOTTA(241),
    @Deprecated
    LEGACY_GRAY_GLAZED_TERRACOTTA(242),
    @Deprecated
    LEGACY_SILVER_GLAZED_TERRACOTTA(243),
    @Deprecated
    LEGACY_CYAN_GLAZED_TERRACOTTA(244),
    @Deprecated
    LEGACY_PURPLE_GLAZED_TERRACOTTA(245),
    @Deprecated
    LEGACY_BLUE_GLAZED_TERRACOTTA(246),
    @Deprecated
    LEGACY_BROWN_GLAZED_TERRACOTTA(247),
    @Deprecated
    LEGACY_GREEN_GLAZED_TERRACOTTA(248),
    @Deprecated
    LEGACY_RED_GLAZED_TERRACOTTA(249),
    @Deprecated
    LEGACY_BLACK_GLAZED_TERRACOTTA(250),
    @Deprecated
    LEGACY_CONCRETE(251),
    @Deprecated
    LEGACY_CONCRETE_POWDER(252),
    @Deprecated
    LEGACY_STRUCTURE_BLOCK(255),
    // ----- Item Separator -----
    @Deprecated
    LEGACY_IRON_SPADE(256, 1, 250),
    @Deprecated
    LEGACY_IRON_PICKAXE(257, 1, 250),
    @Deprecated
    LEGACY_IRON_AXE(258, 1, 250),
    @Deprecated
    LEGACY_FLINT_AND_STEEL(259, 1, 64),
    @Deprecated
    LEGACY_APPLE(260),
    @Deprecated
    LEGACY_BOW(261, 1, 384),
    @Deprecated
    LEGACY_ARROW(262),
    @Deprecated
    LEGACY_COAL(263, org.bukkit.material.Coal.class),
    @Deprecated
    LEGACY_DIAMOND(264),
    @Deprecated
    LEGACY_IRON_INGOT(265),
    @Deprecated
    LEGACY_GOLD_INGOT(266),
    @Deprecated
    LEGACY_IRON_SWORD(267, 1, 250),
    @Deprecated
    LEGACY_WOOD_SWORD(268, 1, 59),
    @Deprecated
    LEGACY_WOOD_SPADE(269, 1, 59),
    @Deprecated
    LEGACY_WOOD_PICKAXE(270, 1, 59),
    @Deprecated
    LEGACY_WOOD_AXE(271, 1, 59),
    @Deprecated
    LEGACY_STONE_SWORD(272, 1, 131),
    @Deprecated
    LEGACY_STONE_SPADE(273, 1, 131),
    @Deprecated
    LEGACY_STONE_PICKAXE(274, 1, 131),
    @Deprecated
    LEGACY_STONE_AXE(275, 1, 131),
    @Deprecated
    LEGACY_DIAMOND_SWORD(276, 1, 1561),
    @Deprecated
    LEGACY_DIAMOND_SPADE(277, 1, 1561),
    @Deprecated
    LEGACY_DIAMOND_PICKAXE(278, 1, 1561),
    @Deprecated
    LEGACY_DIAMOND_AXE(279, 1, 1561),
    @Deprecated
    LEGACY_STICK(280),
    @Deprecated
    LEGACY_BOWL(281),
    @Deprecated
    LEGACY_MUSHROOM_SOUP(282, 1),
    @Deprecated
    LEGACY_GOLD_SWORD(283, 1, 32),
    @Deprecated
    LEGACY_GOLD_SPADE(284, 1, 32),
    @Deprecated
    LEGACY_GOLD_PICKAXE(285, 1, 32),
    @Deprecated
    LEGACY_GOLD_AXE(286, 1, 32),
    @Deprecated
    LEGACY_STRING(287),
    @Deprecated
    LEGACY_FEATHER(288),
    @Deprecated
    LEGACY_SULPHUR(289),
    @Deprecated
    LEGACY_WOOD_HOE(290, 1, 59),
    @Deprecated
    LEGACY_STONE_HOE(291, 1, 131),
    @Deprecated
    LEGACY_IRON_HOE(292, 1, 250),
    @Deprecated
    LEGACY_DIAMOND_HOE(293, 1, 1561),
    @Deprecated
    LEGACY_GOLD_HOE(294, 1, 32),
    @Deprecated
    LEGACY_SEEDS(295),
    @Deprecated
    LEGACY_WHEAT(296),
    @Deprecated
    LEGACY_BREAD(297),
    @Deprecated
    LEGACY_LEATHER_HELMET(298, 1, 55),
    @Deprecated
    LEGACY_LEATHER_CHESTPLATE(299, 1, 80),
    @Deprecated
    LEGACY_LEATHER_LEGGINGS(300, 1, 75),
    @Deprecated
    LEGACY_LEATHER_BOOTS(301, 1, 65),
    @Deprecated
    LEGACY_CHAINMAIL_HELMET(302, 1, 165),
    @Deprecated
    LEGACY_CHAINMAIL_CHESTPLATE(303, 1, 240),
    @Deprecated
    LEGACY_CHAINMAIL_LEGGINGS(304, 1, 225),
    @Deprecated
    LEGACY_CHAINMAIL_BOOTS(305, 1, 195),
    @Deprecated
    LEGACY_IRON_HELMET(306, 1, 165),
    @Deprecated
    LEGACY_IRON_CHESTPLATE(307, 1, 240),
    @Deprecated
    LEGACY_IRON_LEGGINGS(308, 1, 225),
    @Deprecated
    LEGACY_IRON_BOOTS(309, 1, 195),
    @Deprecated
    LEGACY_DIAMOND_HELMET(310, 1, 363),
    @Deprecated
    LEGACY_DIAMOND_CHESTPLATE(311, 1, 528),
    @Deprecated
    LEGACY_DIAMOND_LEGGINGS(312, 1, 495),
    @Deprecated
    LEGACY_DIAMOND_BOOTS(313, 1, 429),
    @Deprecated
    LEGACY_GOLD_HELMET(314, 1, 77),
    @Deprecated
    LEGACY_GOLD_CHESTPLATE(315, 1, 112),
    @Deprecated
    LEGACY_GOLD_LEGGINGS(316, 1, 105),
    @Deprecated
    LEGACY_GOLD_BOOTS(317, 1, 91),
    @Deprecated
    LEGACY_FLINT(318),
    @Deprecated
    LEGACY_PORK(319),
    @Deprecated
    LEGACY_GRILLED_PORK(320),
    @Deprecated
    LEGACY_PAINTING(321),
    @Deprecated
    LEGACY_GOLDEN_APPLE(322),
    @Deprecated
    LEGACY_SIGN(323, 16),
    @Deprecated
    LEGACY_WOOD_DOOR(324, 64),
    @Deprecated
    LEGACY_BUCKET(325, 16),
    @Deprecated
    LEGACY_WATER_BUCKET(326, 1),
    @Deprecated
    LEGACY_LAVA_BUCKET(327, 1),
    @Deprecated
    LEGACY_MINECART(328, 1),
    @Deprecated
    LEGACY_SADDLE(329, 1),
    @Deprecated
    LEGACY_IRON_DOOR(330, 64),
    @Deprecated
    LEGACY_REDSTONE(331),
    @Deprecated
    LEGACY_SNOW_BALL(332, 16),
    @Deprecated
    LEGACY_BOAT(333, 1),
    @Deprecated
    LEGACY_LEATHER(334),
    @Deprecated
    LEGACY_MILK_BUCKET(335, 1),
    @Deprecated
    LEGACY_CLAY_BRICK(336),
    @Deprecated
    LEGACY_CLAY_BALL(337),
    @Deprecated
    LEGACY_SUGAR_CANE(338),
    @Deprecated
    LEGACY_PAPER(339),
    @Deprecated
    LEGACY_BOOK(340),
    @Deprecated
    LEGACY_SLIME_BALL(341),
    @Deprecated
    LEGACY_STORAGE_MINECART(342, 1),
    @Deprecated
    LEGACY_POWERED_MINECART(343, 1),
    @Deprecated
    LEGACY_EGG(344, 16),
    @Deprecated
    LEGACY_COMPASS(345),
    @Deprecated
    LEGACY_FISHING_ROD(346, 1, 64),
    @Deprecated
    LEGACY_WATCH(347),
    @Deprecated
    LEGACY_GLOWSTONE_DUST(348),
    @Deprecated
    LEGACY_RAW_FISH(349),
    @Deprecated
    LEGACY_COOKED_FISH(350),
    @Deprecated
    LEGACY_INK_SACK(351, org.bukkit.material.Dye.class),
    @Deprecated
    LEGACY_BONE(352),
    @Deprecated
    LEGACY_SUGAR(353),
    @Deprecated
    LEGACY_CAKE(354, 1),
    @Deprecated
    LEGACY_BED(355, 1),
    @Deprecated
    LEGACY_DIODE(356),
    @Deprecated
    LEGACY_COOKIE(357),
    /**
     * @see org.bukkit.map.MapView
     */
    @Deprecated
    LEGACY_MAP(358, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_SHEARS(359, 1, 238),
    @Deprecated
    LEGACY_MELON(360),
    @Deprecated
    LEGACY_PUMPKIN_SEEDS(361),
    @Deprecated
    LEGACY_MELON_SEEDS(362),
    @Deprecated
    LEGACY_RAW_BEEF(363),
    @Deprecated
    LEGACY_COOKED_BEEF(364),
    @Deprecated
    LEGACY_RAW_CHICKEN(365),
    @Deprecated
    LEGACY_COOKED_CHICKEN(366),
    @Deprecated
    LEGACY_ROTTEN_FLESH(367),
    @Deprecated
    LEGACY_ENDER_PEARL(368, 16),
    @Deprecated
    LEGACY_BLAZE_ROD(369),
    @Deprecated
    LEGACY_GHAST_TEAR(370),
    @Deprecated
    LEGACY_GOLD_NUGGET(371),
    @Deprecated
    LEGACY_NETHER_STALK(372),
    @Deprecated
    LEGACY_POTION(373, 1, org.bukkit.material.MaterialData.class),
    @Deprecated
    LEGACY_GLASS_BOTTLE(374),
    @Deprecated
    LEGACY_SPIDER_EYE(375),
    @Deprecated
    LEGACY_FERMENTED_SPIDER_EYE(376),
    @Deprecated
    LEGACY_BLAZE_POWDER(377),
    @Deprecated
    LEGACY_MAGMA_CREAM(378),
    @Deprecated
    LEGACY_BREWING_STAND_ITEM(379),
    @Deprecated
    LEGACY_CAULDRON_ITEM(380),
    @Deprecated
    LEGACY_EYE_OF_ENDER(381),
    @Deprecated
    LEGACY_SPECKLED_MELON(382),
    @Deprecated
    LEGACY_MONSTER_EGG(383, 64, org.bukkit.material.SpawnEgg.class),
    @Deprecated
    LEGACY_EXP_BOTTLE(384, 64),
    @Deprecated
    LEGACY_FIREBALL(385, 64),
    @Deprecated
    LEGACY_BOOK_AND_QUILL(386, 1),
    @Deprecated
    LEGACY_WRITTEN_BOOK(387, 16),
    @Deprecated
    LEGACY_EMERALD(388, 64),
    @Deprecated
    LEGACY_ITEM_FRAME(389),
    @Deprecated
    LEGACY_FLOWER_POT_ITEM(390),
    @Deprecated
    LEGACY_CARROT_ITEM(391),
    @Deprecated
    LEGACY_POTATO_ITEM(392),
    @Deprecated
    LEGACY_BAKED_POTATO(393),
    @Deprecated
    LEGACY_POISONOUS_POTATO(394),
    @Deprecated
    LEGACY_EMPTY_MAP(395),
    @Deprecated
    LEGACY_GOLDEN_CARROT(396),
    @Deprecated
    LEGACY_SKULL_ITEM(397),
    @Deprecated
    LEGACY_CARROT_STICK(398, 1, 25),
    @Deprecated
    LEGACY_NETHER_STAR(399),
    @Deprecated
    LEGACY_PUMPKIN_PIE(400),
    @Deprecated
    LEGACY_FIREWORK(401),
    @Deprecated
    LEGACY_FIREWORK_CHARGE(402),
    @Deprecated
    LEGACY_ENCHANTED_BOOK(403, 1),
    @Deprecated
    LEGACY_REDSTONE_COMPARATOR(404),
    @Deprecated
    LEGACY_NETHER_BRICK_ITEM(405),
    @Deprecated
    LEGACY_QUARTZ(406),
    @Deprecated
    LEGACY_EXPLOSIVE_MINECART(407, 1),
    @Deprecated
    LEGACY_HOPPER_MINECART(408, 1),
    @Deprecated
    LEGACY_PRISMARINE_SHARD(409),
    @Deprecated
    LEGACY_PRISMARINE_CRYSTALS(410),
    @Deprecated
    LEGACY_RABBIT(411),
    @Deprecated
    LEGACY_COOKED_RABBIT(412),
    @Deprecated
    LEGACY_RABBIT_STEW(413, 1),
    @Deprecated
    LEGACY_RABBIT_FOOT(414),
    @Deprecated
    LEGACY_RABBIT_HIDE(415),
    @Deprecated
    LEGACY_ARMOR_STAND(416, 16),
    @Deprecated
    LEGACY_IRON_BARDING(417, 1),
    @Deprecated
    LEGACY_GOLD_BARDING(418, 1),
    @Deprecated
    LEGACY_DIAMOND_BARDING(419, 1),
    @Deprecated
    LEGACY_LEASH(420),
    @Deprecated
    LEGACY_NAME_TAG(421),
    @Deprecated
    LEGACY_COMMAND_MINECART(422, 1),
    @Deprecated
    LEGACY_MUTTON(423),
    @Deprecated
    LEGACY_COOKED_MUTTON(424),
    @Deprecated
    LEGACY_BANNER(425, 16),
    @Deprecated
    LEGACY_END_CRYSTAL(426),
    @Deprecated
    LEGACY_SPRUCE_DOOR_ITEM(427),
    @Deprecated
    LEGACY_BIRCH_DOOR_ITEM(428),
    @Deprecated
    LEGACY_JUNGLE_DOOR_ITEM(429),
    @Deprecated
    LEGACY_ACACIA_DOOR_ITEM(430),
    @Deprecated
    LEGACY_DARK_OAK_DOOR_ITEM(431),
    @Deprecated
    LEGACY_CHORUS_FRUIT(432),
    @Deprecated
    LEGACY_CHORUS_FRUIT_POPPED(433),
    @Deprecated
    LEGACY_BEETROOT(434),
    @Deprecated
    LEGACY_BEETROOT_SEEDS(435),
    @Deprecated
    LEGACY_BEETROOT_SOUP(436, 1),
    @Deprecated
    LEGACY_DRAGONS_BREATH(437),
    @Deprecated
    LEGACY_SPLASH_POTION(438, 1),
    @Deprecated
    LEGACY_SPECTRAL_ARROW(439),
    @Deprecated
    LEGACY_TIPPED_ARROW(440),
    @Deprecated
    LEGACY_LINGERING_POTION(441, 1),
    @Deprecated
    LEGACY_SHIELD(442, 1, 336),
    @Deprecated
    LEGACY_ELYTRA(443, 1, 431),
    @Deprecated
    LEGACY_BOAT_SPRUCE(444, 1),
    @Deprecated
    LEGACY_BOAT_BIRCH(445, 1),
    @Deprecated
    LEGACY_BOAT_JUNGLE(446, 1),
    @Deprecated
    LEGACY_BOAT_ACACIA(447, 1),
    @Deprecated
    LEGACY_BOAT_DARK_OAK(448, 1),
    @Deprecated
    LEGACY_TOTEM(449, 1),
    @Deprecated
    LEGACY_SHULKER_SHELL(450),
    @Deprecated
    LEGACY_IRON_NUGGET(452),
    @Deprecated
    LEGACY_KNOWLEDGE_BOOK(453, 1),
    @Deprecated
    LEGACY_GOLD_RECORD(2256, 1),
    @Deprecated
    LEGACY_GREEN_RECORD(2257, 1),
    @Deprecated
    LEGACY_RECORD_3(2258, 1),
    @Deprecated
    LEGACY_RECORD_4(2259, 1),
    @Deprecated
    LEGACY_RECORD_5(2260, 1),
    @Deprecated
    LEGACY_RECORD_6(2261, 1),
    @Deprecated
    LEGACY_RECORD_7(2262, 1),
    @Deprecated
    LEGACY_RECORD_8(2263, 1),
    @Deprecated
    LEGACY_RECORD_9(2264, 1),
    @Deprecated
    LEGACY_RECORD_10(2265, 1),
    @Deprecated
    LEGACY_RECORD_11(2266, 1),
    @Deprecated
    LEGACY_RECORD_12(2267, 1),
    ;
    //</editor-fold>

    @Deprecated
    public static final String LEGACY_PREFIX = "LEGACY_";

    private final int id;
    private final Constructor<? extends MaterialData> ctor;
    private final static Map<String, Material> BY_NAME = Maps.newHashMap();
    private final int maxStack;
    private final short durability;
    public final Class<?> data;
    private final boolean legacy;
    private final NamespacedKey key;

    private Material(final int id) {
        this(id, 64);
    }

    private Material(final int id, final int stack) {
        this(id, stack, MaterialData.class);
    }

    private Material(final int id, final int stack, final int durability) {
        this(id, stack, durability, MaterialData.class);
    }

    private Material(final int id, final Class<?> data) {
        this(id, 64, data);
    }

    private Material(final int id, final int stack, final Class<?> data) {
        this(id, stack, 0, data);
    }

    private Material(final int id, final int stack, final int durability, final Class<?> data) {
        this.id = id;
        this.durability = (short) durability;
        this.maxStack = stack;
        this.data = data;
        this.legacy = this.name().startsWith(LEGACY_PREFIX);
        this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
        // try to cache the constructor for this material
        try {
            if (MaterialData.class.isAssignableFrom(data)) {
                this.ctor = (Constructor<? extends MaterialData>) data.getConstructor(Material.class, byte.class);
            } else {
                this.ctor = null;
            }
        } catch (NoSuchMethodException ex) {
            throw new AssertionError(ex);
        } catch (SecurityException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Do not use for any reason.
     *
     * @return ID of this material
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return id;
    }

    /**
     * Do not use for any reason.
     *
     * @return legacy status
     */
    @Deprecated
    public boolean isLegacy() {
        return legacy;
    }

    @Override
    public NamespacedKey getKey() {
        Validate.isTrue(!legacy, "Cannot get key of Legacy Material");
        return key;
    }

    /**
     * Gets the maximum amount of this material that can be held in a stack
     *
     * @return Maximum stack size for this material
     */
    public int getMaxStackSize() {
        return maxStack;
    }

    /**
     * Gets the maximum durability of this material
     *
     * @return Maximum durability for this material
     */
    public short getMaxDurability() {
        return durability;
    }

    /**
     * Creates a new {@link BlockData} instance for this Material, with all
     * properties initialized to unspecified defaults.
     *
     * @return new data instance
     */
    public BlockData createBlockData() {
        return Bukkit.createBlockData(this);
    }

    /**
     * Creates a new {@link BlockData} instance for the specified Material, with
     * all properties initialized to unspecified defaults.
     *
     * @param consumer consumer to run on new instance before returning
     * @return new data instance
     */
    public BlockData createBlockData(Consumer<BlockData> consumer) {
        return Bukkit.createBlockData(this, consumer);
    }

    /**
     * Creates a new {@link BlockData} instance with material and properties
     * parsed from provided data.
     *
     * @param data data string
     * @return new data instance
     * @throws IllegalArgumentException if the specified data is not valid
     */
    public BlockData createBlockData(String data) throws IllegalArgumentException {
        return Bukkit.createBlockData(data);
    }

    /**
     * Gets the MaterialData class associated with this Material
     *
     * @return MaterialData associated with this Material
     */
    public Class<? extends MaterialData> getData() {
        Validate.isTrue(legacy, "Cannot get data class of Modern Material");
        return ctor.getDeclaringClass();
    }

    /**
     * Constructs a new MaterialData relevant for this Material, with the
     * given initial data
     *
     * @param raw Initial data to construct the MaterialData with
     * @return New MaterialData with the given data
     * @deprecated Magic value
     */
    @Deprecated
    public MaterialData getNewData(final byte raw) {
        Validate.isTrue(legacy, "Cannot get new data of Modern Material");
        try {
            return ctor.newInstance(this, raw);
        } catch (InstantiationException ex) {
            final Throwable t = ex.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new AssertionError(t);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    /**
     * Checks if this Material is a placable block
     *
     * @return true if this material is a block
     */
    public boolean isBlock() {
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isBlock">
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LEAVES:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SAPLING:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ACACIA_WOOD:
            case ACTIVATOR_RAIL:
            case AIR:
            case ALLIUM:
            case ANDESITE:
            case ANVIL:
            case ATTACHED_MELON_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case AZURE_BLUET:
            case BARRIER:
            case BEACON:
            case BEDROCK:
            case BEETROOTS:
            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LEAVES:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SAPLING:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BIRCH_WOOD:
            case BLACK_BANNER:
            case BLACK_BED:
            case BLACK_CARPET:
            case BLACK_CONCRETE:
            case BLACK_CONCRETE_POWDER:
            case BLACK_GLAZED_TERRACOTTA:
            case BLACK_SHULKER_BOX:
            case BLACK_STAINED_GLASS:
            case BLACK_STAINED_GLASS_PANE:
            case BLACK_TERRACOTTA:
            case BLACK_WALL_BANNER:
            case BLACK_WOOL:
            case BLUE_BANNER:
            case BLUE_BED:
            case BLUE_CARPET:
            case BLUE_CONCRETE:
            case BLUE_CONCRETE_POWDER:
            case BLUE_GLAZED_TERRACOTTA:
            case BLUE_ICE:
            case BLUE_ORCHID:
            case BLUE_SHULKER_BOX:
            case BLUE_STAINED_GLASS:
            case BLUE_STAINED_GLASS_PANE:
            case BLUE_TERRACOTTA:
            case BLUE_WALL_BANNER:
            case BLUE_WOOL:
            case BONE_BLOCK:
            case BOOKSHELF:
            case BRAIN_CORAL:
            case BRAIN_CORAL_BLOCK:
            case BRAIN_CORAL_FAN:
            case BRAIN_CORAL_WALL_FAN:
            case BREWING_STAND:
            case BRICKS:
            case BRICK_SLAB:
            case BRICK_STAIRS:
            case BROWN_BANNER:
            case BROWN_BED:
            case BROWN_CARPET:
            case BROWN_CONCRETE:
            case BROWN_CONCRETE_POWDER:
            case BROWN_GLAZED_TERRACOTTA:
            case BROWN_MUSHROOM:
            case BROWN_MUSHROOM_BLOCK:
            case BROWN_SHULKER_BOX:
            case BROWN_STAINED_GLASS:
            case BROWN_STAINED_GLASS_PANE:
            case BROWN_TERRACOTTA:
            case BROWN_WALL_BANNER:
            case BROWN_WOOL:
            case BUBBLE_COLUMN:
            case BUBBLE_CORAL:
            case BUBBLE_CORAL_BLOCK:
            case BUBBLE_CORAL_FAN:
            case BUBBLE_CORAL_WALL_FAN:
            case CACTUS:
            case CAKE:
            case CARROTS:
            case CARVED_PUMPKIN:
            case CAULDRON:
            case CAVE_AIR:
            case CHAIN_COMMAND_BLOCK:
            case CHEST:
            case CHIPPED_ANVIL:
            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CHISELED_STONE_BRICKS:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
            case CLAY:
            case COAL_BLOCK:
            case COAL_ORE:
            case COARSE_DIRT:
            case COBBLESTONE:
            case COBBLESTONE_SLAB:
            case COBBLESTONE_STAIRS:
            case COBBLESTONE_WALL:
            case COBWEB:
            case COCOA:
            case COMMAND_BLOCK:
            case COMPARATOR:
            case CONDUIT:
            case CRACKED_STONE_BRICKS:
            case CRAFTING_TABLE:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case CUT_RED_SANDSTONE:
            case CUT_SANDSTONE:
            case CYAN_BANNER:
            case CYAN_BED:
            case CYAN_CARPET:
            case CYAN_CONCRETE:
            case CYAN_CONCRETE_POWDER:
            case CYAN_GLAZED_TERRACOTTA:
            case CYAN_SHULKER_BOX:
            case CYAN_STAINED_GLASS:
            case CYAN_STAINED_GLASS_PANE:
            case CYAN_TERRACOTTA:
            case CYAN_WALL_BANNER:
            case CYAN_WOOL:
            case DAMAGED_ANVIL:
            case DANDELION:
            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LEAVES:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SAPLING:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_OAK_WOOD:
            case DARK_PRISMARINE:
            case DARK_PRISMARINE_SLAB:
            case DARK_PRISMARINE_STAIRS:
            case DAYLIGHT_DETECTOR:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BRAIN_CORAL_FAN:
            case DEAD_BRAIN_CORAL_WALL_FAN:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_FAN:
            case DEAD_BUBBLE_CORAL_WALL_FAN:
            case DEAD_BUSH:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_FAN:
            case DEAD_FIRE_CORAL_WALL_FAN:
            case DEAD_HORN_CORAL_BLOCK:
            case DEAD_HORN_CORAL_FAN:
            case DEAD_HORN_CORAL_WALL_FAN:
            case DEAD_TUBE_CORAL_BLOCK:
            case DEAD_TUBE_CORAL_FAN:
            case DEAD_TUBE_CORAL_WALL_FAN:
            case DETECTOR_RAIL:
            case DIAMOND_BLOCK:
            case DIAMOND_ORE:
            case DIORITE:
            case DIRT:
            case DISPENSER:
            case DRAGON_EGG:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case DRIED_KELP_BLOCK:
            case DROPPER:
            case EMERALD_BLOCK:
            case EMERALD_ORE:
            case ENCHANTING_TABLE:
            case ENDER_CHEST:
            case END_GATEWAY:
            case END_PORTAL:
            case END_PORTAL_FRAME:
            case END_ROD:
            case END_STONE:
            case END_STONE_BRICKS:
            case FARMLAND:
            case FERN:
            case FIRE:
            case FIRE_CORAL:
            case FIRE_CORAL_BLOCK:
            case FIRE_CORAL_FAN:
            case FIRE_CORAL_WALL_FAN:
            case FLOWER_POT:
            case FROSTED_ICE:
            case FURNACE:
            case GLASS:
            case GLASS_PANE:
            case GLOWSTONE:
            case GOLD_BLOCK:
            case GOLD_ORE:
            case GRANITE:
            case GRASS:
            case GRASS_BLOCK:
            case GRASS_PATH:
            case GRAVEL:
            case GRAY_BANNER:
            case GRAY_BED:
            case GRAY_CARPET:
            case GRAY_CONCRETE:
            case GRAY_CONCRETE_POWDER:
            case GRAY_GLAZED_TERRACOTTA:
            case GRAY_SHULKER_BOX:
            case GRAY_STAINED_GLASS:
            case GRAY_STAINED_GLASS_PANE:
            case GRAY_TERRACOTTA:
            case GRAY_WALL_BANNER:
            case GRAY_WOOL:
            case GREEN_BANNER:
            case GREEN_BED:
            case GREEN_CARPET:
            case GREEN_CONCRETE:
            case GREEN_CONCRETE_POWDER:
            case GREEN_GLAZED_TERRACOTTA:
            case GREEN_SHULKER_BOX:
            case GREEN_STAINED_GLASS:
            case GREEN_STAINED_GLASS_PANE:
            case GREEN_TERRACOTTA:
            case GREEN_WALL_BANNER:
            case GREEN_WOOL:
            case HAY_BLOCK:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case HOPPER:
            case HORN_CORAL:
            case HORN_CORAL_BLOCK:
            case HORN_CORAL_FAN:
            case HORN_CORAL_WALL_FAN:
            case ICE:
            case INFESTED_CHISELED_STONE_BRICKS:
            case INFESTED_COBBLESTONE:
            case INFESTED_CRACKED_STONE_BRICKS:
            case INFESTED_MOSSY_STONE_BRICKS:
            case INFESTED_STONE:
            case INFESTED_STONE_BRICKS:
            case IRON_BARS:
            case IRON_BLOCK:
            case IRON_DOOR:
            case IRON_ORE:
            case IRON_TRAPDOOR:
            case JACK_O_LANTERN:
            case JUKEBOX:
            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LEAVES:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SAPLING:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case JUNGLE_WOOD:
            case KELP:
            case KELP_PLANT:
            case LADDER:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
            case LARGE_FERN:
            case LAVA:
            case LEVER:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_BED:
            case LIGHT_BLUE_CARPET:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_BED:
            case LIGHT_GRAY_CARPET:
            case LIGHT_GRAY_CONCRETE:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case LIGHT_GRAY_TERRACOTTA:
            case LIGHT_GRAY_WALL_BANNER:
            case LIGHT_GRAY_WOOL:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case LILAC:
            case LILY_PAD:
            case LIME_BANNER:
            case LIME_BED:
            case LIME_CARPET:
            case LIME_CONCRETE:
            case LIME_CONCRETE_POWDER:
            case LIME_GLAZED_TERRACOTTA:
            case LIME_SHULKER_BOX:
            case LIME_STAINED_GLASS:
            case LIME_STAINED_GLASS_PANE:
            case LIME_TERRACOTTA:
            case LIME_WALL_BANNER:
            case LIME_WOOL:
            case MAGENTA_BANNER:
            case MAGENTA_BED:
            case MAGENTA_CARPET:
            case MAGENTA_CONCRETE:
            case MAGENTA_CONCRETE_POWDER:
            case MAGENTA_GLAZED_TERRACOTTA:
            case MAGENTA_SHULKER_BOX:
            case MAGENTA_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS_PANE:
            case MAGENTA_TERRACOTTA:
            case MAGENTA_WALL_BANNER:
            case MAGENTA_WOOL:
            case MAGMA_BLOCK:
            case MELON:
            case MELON_STEM:
            case MOSSY_COBBLESTONE:
            case MOSSY_COBBLESTONE_WALL:
            case MOSSY_STONE_BRICKS:
            case MOVING_PISTON:
            case MUSHROOM_STEM:
            case MYCELIUM:
            case NETHERRACK:
            case NETHER_BRICKS:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_SLAB:
            case NETHER_BRICK_STAIRS:
            case NETHER_PORTAL:
            case NETHER_QUARTZ_ORE:
            case NETHER_WART:
            case NETHER_WART_BLOCK:
            case NOTE_BLOCK:
            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LEAVES:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SAPLING:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OAK_WOOD:
            case OBSERVER:
            case OBSIDIAN:
            case ORANGE_BANNER:
            case ORANGE_BED:
            case ORANGE_CARPET:
            case ORANGE_CONCRETE:
            case ORANGE_CONCRETE_POWDER:
            case ORANGE_GLAZED_TERRACOTTA:
            case ORANGE_SHULKER_BOX:
            case ORANGE_STAINED_GLASS:
            case ORANGE_STAINED_GLASS_PANE:
            case ORANGE_TERRACOTTA:
            case ORANGE_TULIP:
            case ORANGE_WALL_BANNER:
            case ORANGE_WOOL:
            case OXEYE_DAISY:
            case PACKED_ICE:
            case PEONY:
            case PETRIFIED_OAK_SLAB:
            case PINK_BANNER:
            case PINK_BED:
            case PINK_CARPET:
            case PINK_CONCRETE:
            case PINK_CONCRETE_POWDER:
            case PINK_GLAZED_TERRACOTTA:
            case PINK_SHULKER_BOX:
            case PINK_STAINED_GLASS:
            case PINK_STAINED_GLASS_PANE:
            case PINK_TERRACOTTA:
            case PINK_TULIP:
            case PINK_WALL_BANNER:
            case PINK_WOOL:
            case PISTON:
            case PISTON_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case PODZOL:
            case POLISHED_ANDESITE:
            case POLISHED_DIORITE:
            case POLISHED_GRANITE:
            case POPPY:
            case POTATOES:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case POWERED_RAIL:
            case PRISMARINE:
            case PRISMARINE_BRICKS:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_SLAB:
            case PRISMARINE_STAIRS:
            case PUMPKIN:
            case PUMPKIN_STEM:
            case PURPLE_BANNER:
            case PURPLE_BED:
            case PURPLE_CARPET:
            case PURPLE_CONCRETE:
            case PURPLE_CONCRETE_POWDER:
            case PURPLE_GLAZED_TERRACOTTA:
            case PURPLE_SHULKER_BOX:
            case PURPLE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS_PANE:
            case PURPLE_TERRACOTTA:
            case PURPLE_WALL_BANNER:
            case PURPLE_WOOL:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_SLAB:
            case PURPUR_STAIRS:
            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case QUARTZ_SLAB:
            case QUARTZ_STAIRS:
            case RAIL:
            case REDSTONE_BLOCK:
            case REDSTONE_LAMP:
            case REDSTONE_ORE:
            case REDSTONE_TORCH:
            case REDSTONE_WALL_TORCH:
            case REDSTONE_WIRE:
            case RED_BANNER:
            case RED_BED:
            case RED_CARPET:
            case RED_CONCRETE:
            case RED_CONCRETE_POWDER:
            case RED_GLAZED_TERRACOTTA:
            case RED_MUSHROOM:
            case RED_MUSHROOM_BLOCK:
            case RED_NETHER_BRICKS:
            case RED_SAND:
            case RED_SANDSTONE:
            case RED_SANDSTONE_SLAB:
            case RED_SANDSTONE_STAIRS:
            case RED_SHULKER_BOX:
            case RED_STAINED_GLASS:
            case RED_STAINED_GLASS_PANE:
            case RED_TERRACOTTA:
            case RED_TULIP:
            case RED_WALL_BANNER:
            case RED_WOOL:
            case REPEATER:
            case REPEATING_COMMAND_BLOCK:
            case ROSE_BUSH:
            case SAND:
            case SANDSTONE:
            case SANDSTONE_SLAB:
            case SANDSTONE_STAIRS:
            case SEAGRASS:
            case SEA_LANTERN:
            case SEA_PICKLE:
            case SHULKER_BOX:
            case SIGN:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case SLIME_BLOCK:
            case SMOOTH_QUARTZ:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_SANDSTONE:
            case SMOOTH_STONE:
            case SNOW:
            case SNOW_BLOCK:
            case SOUL_SAND:
            case SPAWNER:
            case SPONGE:
            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LEAVES:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SAPLING:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case SPRUCE_WOOD:
            case STICKY_PISTON:
            case STONE:
            case STONE_BRICKS:
            case STONE_BRICK_SLAB:
            case STONE_BRICK_STAIRS:
            case STONE_BUTTON:
            case STONE_PRESSURE_PLATE:
            case STONE_SLAB:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case STRUCTURE_BLOCK:
            case STRUCTURE_VOID:
            case SUGAR_CANE:
            case SUNFLOWER:
            case TALL_GRASS:
            case TALL_SEAGRASS:
            case TERRACOTTA:
            case TNT:
            case TORCH:
            case TRAPPED_CHEST:
            case TRIPWIRE:
            case TRIPWIRE_HOOK:
            case TUBE_CORAL:
            case TUBE_CORAL_BLOCK:
            case TUBE_CORAL_FAN:
            case TUBE_CORAL_WALL_FAN:
            case TURTLE_EGG:
            case VINE:
            case VOID_AIR:
            case WALL_SIGN:
            case WALL_TORCH:
            case WATER:
            case WET_SPONGE:
            case WHEAT:
            case WHITE_BANNER:
            case WHITE_BED:
            case WHITE_CARPET:
            case WHITE_CONCRETE:
            case WHITE_CONCRETE_POWDER:
            case WHITE_GLAZED_TERRACOTTA:
            case WHITE_SHULKER_BOX:
            case WHITE_STAINED_GLASS:
            case WHITE_STAINED_GLASS_PANE:
            case WHITE_TERRACOTTA:
            case WHITE_TULIP:
            case WHITE_WALL_BANNER:
            case WHITE_WOOL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_BANNER:
            case YELLOW_BED:
            case YELLOW_CARPET:
            case YELLOW_CONCRETE:
            case YELLOW_CONCRETE_POWDER:
            case YELLOW_GLAZED_TERRACOTTA:
            case YELLOW_SHULKER_BOX:
            case YELLOW_STAINED_GLASS:
            case YELLOW_STAINED_GLASS_PANE:
            case YELLOW_TERRACOTTA:
            case YELLOW_WALL_BANNER:
            case YELLOW_WOOL:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
            //</editor-fold>
                return true;
            default:
                return 0 <= id && id < 256;
        }
    }

    /**
     * Checks if this Material is edible.
     *
     * @return true if this Material is edible.
     */
    public boolean isEdible() {
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isEdible">
            case APPLE:
            case BAKED_POTATO:
            case BEEF:
            case BEETROOT:
            case BEETROOT_SOUP:
            case BREAD:
            case CARROT:
            case CHICKEN:
            case CHORUS_FRUIT:
            case TROPICAL_FISH:
            case COD:
            case COOKED_BEEF:
            case COOKED_CHICKEN:
            case COOKED_COD:
            case COOKED_MUTTON:
            case COOKED_PORKCHOP:
            case COOKED_RABBIT:
            case COOKED_SALMON:
            case COOKIE:
            case DRIED_KELP:
            case ENCHANTED_GOLDEN_APPLE:
            case GOLDEN_APPLE:
            case GOLDEN_CARROT:
            case MELON_SLICE:
            case MUSHROOM_STEW:
            case MUTTON:
            case POISONOUS_POTATO:
            case PORKCHOP:
            case POTATO:
            case PUFFERFISH:
            case PUMPKIN_PIE:
            case RABBIT:
            case RABBIT_STEW:
            case ROTTEN_FLESH:
            case SALMON:
            case SPIDER_EYE:
            // ----- Legacy Separator -----
            case LEGACY_BREAD:
            case LEGACY_CARROT_ITEM:
            case LEGACY_BAKED_POTATO:
            case LEGACY_POTATO_ITEM:
            case LEGACY_POISONOUS_POTATO:
            case LEGACY_GOLDEN_CARROT:
            case LEGACY_PUMPKIN_PIE:
            case LEGACY_COOKIE:
            case LEGACY_MELON:
            case LEGACY_MUSHROOM_SOUP:
            case LEGACY_RAW_CHICKEN:
            case LEGACY_COOKED_CHICKEN:
            case LEGACY_RAW_BEEF:
            case LEGACY_COOKED_BEEF:
            case LEGACY_RAW_FISH:
            case LEGACY_COOKED_FISH:
            case LEGACY_PORK:
            case LEGACY_GRILLED_PORK:
            case LEGACY_APPLE:
            case LEGACY_GOLDEN_APPLE:
            case LEGACY_ROTTEN_FLESH:
            case LEGACY_SPIDER_EYE:
            case LEGACY_RABBIT:
            case LEGACY_COOKED_RABBIT:
            case LEGACY_RABBIT_STEW:
            case LEGACY_MUTTON:
            case LEGACY_COOKED_MUTTON:
            case LEGACY_BEETROOT:
            case LEGACY_CHORUS_FRUIT:
            case LEGACY_BEETROOT_SOUP:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Attempts to get the Material with the given name.
     * <p>
     * This is a normal lookup, names must be the precise name they are given
     * in the enum.
     *
     * @param name Name of the material to get
     * @return Material if found, or null
     */
    public static Material getMaterial(final String name) {
        return getMaterial(name, false);
    }

    /**
     * Attempts to get the Material with the given name.
     * <p>
     * This is a normal lookup, names must be the precise name they are given
     * in the enum.
     *
     * @param name Name of the material to get
     * @param legacyName whether this is a legacy name
     * @return Material if found, or null
     */
    public static Material getMaterial(String name, boolean legacyName) {
        if (legacyName) {
            if (!name.startsWith(LEGACY_PREFIX)) {
                name = LEGACY_PREFIX + name;
            }

            Material match = BY_NAME.get(name);
            return Bukkit.getUnsafe().fromLegacy(match);
        }

        return BY_NAME.get(name);
    }

    /**
     * Attempts to match the Material with the given name.
     * <p>
     * This is a match lookup; names will be converted to uppercase, then
     * stripped of special characters in an attempt to format it like the
     * enum.
     *
     * @param name Name of the material to get
     * @return Material if found, or null
     */
    public static Material matchMaterial(final String name) {
        return matchMaterial(name, false);
    }

    /**
     * Attempts to match the Material with the given name.
     * <p>
     * This is a match lookup; names will be converted to uppercase, then
     * stripped of special characters in an attempt to format it like the
     * enum.
     *
     * @param name Name of the material to get
     * @param legacyName whether this is a legacy name
     * @return Material if found, or null
     */
    public static Material matchMaterial(final String name, boolean legacyName) {
        Validate.notNull(name, "Name cannot be null");

        String filtered = name.toUpperCase(java.util.Locale.ENGLISH);

        filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
        return getMaterial(filtered, legacyName);
    }

    static {
        for (Material material : values()) {
            BY_NAME.put(material.name(), material);
        }
    }

    /**
     * @return True if this material represents a playable music disk.
     */
    public boolean isRecord() {
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isRecord">
            case MUSIC_DISC_11:
            case MUSIC_DISC_13:
            case MUSIC_DISC_BLOCKS:
            case MUSIC_DISC_CAT:
            case MUSIC_DISC_CHIRP:
            case MUSIC_DISC_FAR:
            case MUSIC_DISC_MALL:
            case MUSIC_DISC_MELLOHI:
            case MUSIC_DISC_STAL:
            case MUSIC_DISC_STRAD:
            case MUSIC_DISC_WAIT:
            case MUSIC_DISC_WARD:
            //</editor-fold>
                return true;
            default:
                return id >= LEGACY_GOLD_RECORD.id && id <= LEGACY_RECORD_12.id;
        }
    }

    /**
     * Check if the material is a block and solid (can be built upon)
     *
     * @return True if this material is a block and solid
     */
    public boolean isSolid() {
        if (!isBlock() || id == 0) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isSolid">
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LEAVES:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ACACIA_WOOD:
            case ANDESITE:
            case ANVIL:
            case BARRIER:
            case BEACON:
            case BEDROCK:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LEAVES:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BIRCH_WOOD:
            case BLACK_BANNER:
            case BLACK_BED:
            case BLACK_CONCRETE:
            case BLACK_CONCRETE_POWDER:
            case BLACK_GLAZED_TERRACOTTA:
            case BLACK_SHULKER_BOX:
            case BLACK_STAINED_GLASS:
            case BLACK_STAINED_GLASS_PANE:
            case BLACK_TERRACOTTA:
            case BLACK_WALL_BANNER:
            case BLACK_WOOL:
            case BLUE_BANNER:
            case BLUE_BED:
            case BLUE_CONCRETE:
            case BLUE_CONCRETE_POWDER:
            case BLUE_GLAZED_TERRACOTTA:
            case BLUE_ICE:
            case BLUE_SHULKER_BOX:
            case BLUE_STAINED_GLASS:
            case BLUE_STAINED_GLASS_PANE:
            case BLUE_TERRACOTTA:
            case BLUE_WALL_BANNER:
            case BLUE_WOOL:
            case BONE_BLOCK:
            case BOOKSHELF:
            case BRAIN_CORAL_BLOCK:
            case BREWING_STAND:
            case BRICKS:
            case BRICK_SLAB:
            case BRICK_STAIRS:
            case BROWN_BANNER:
            case BROWN_BED:
            case BROWN_CONCRETE:
            case BROWN_CONCRETE_POWDER:
            case BROWN_GLAZED_TERRACOTTA:
            case BROWN_MUSHROOM_BLOCK:
            case BROWN_SHULKER_BOX:
            case BROWN_STAINED_GLASS:
            case BROWN_STAINED_GLASS_PANE:
            case BROWN_TERRACOTTA:
            case BROWN_WALL_BANNER:
            case BROWN_WOOL:
            case BUBBLE_CORAL_BLOCK:
            case CACTUS:
            case CAKE:
            case CARVED_PUMPKIN:
            case CAULDRON:
            case CHAIN_COMMAND_BLOCK:
            case CHEST:
            case CHIPPED_ANVIL:
            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CHISELED_STONE_BRICKS:
            case CLAY:
            case COAL_BLOCK:
            case COAL_ORE:
            case COARSE_DIRT:
            case COBBLESTONE:
            case COBBLESTONE_SLAB:
            case COBBLESTONE_STAIRS:
            case COBBLESTONE_WALL:
            case COMMAND_BLOCK:
            case CONDUIT:
            case CRACKED_STONE_BRICKS:
            case CRAFTING_TABLE:
            case CUT_RED_SANDSTONE:
            case CUT_SANDSTONE:
            case CYAN_BANNER:
            case CYAN_BED:
            case CYAN_CONCRETE:
            case CYAN_CONCRETE_POWDER:
            case CYAN_GLAZED_TERRACOTTA:
            case CYAN_SHULKER_BOX:
            case CYAN_STAINED_GLASS:
            case CYAN_STAINED_GLASS_PANE:
            case CYAN_TERRACOTTA:
            case CYAN_WALL_BANNER:
            case CYAN_WOOL:
            case DAMAGED_ANVIL:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LEAVES:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_OAK_WOOD:
            case DARK_PRISMARINE:
            case DARK_PRISMARINE_SLAB:
            case DARK_PRISMARINE_STAIRS:
            case DAYLIGHT_DETECTOR:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BRAIN_CORAL_FAN:
            case DEAD_BRAIN_CORAL_WALL_FAN:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_FAN:
            case DEAD_BUBBLE_CORAL_WALL_FAN:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_FAN:
            case DEAD_FIRE_CORAL_WALL_FAN:
            case DEAD_HORN_CORAL_BLOCK:
            case DEAD_HORN_CORAL_FAN:
            case DEAD_HORN_CORAL_WALL_FAN:
            case DEAD_TUBE_CORAL_BLOCK:
            case DEAD_TUBE_CORAL_FAN:
            case DEAD_TUBE_CORAL_WALL_FAN:
            case DIAMOND_BLOCK:
            case DIAMOND_ORE:
            case DIORITE:
            case DIRT:
            case DISPENSER:
            case DRAGON_EGG:
            case DRIED_KELP_BLOCK:
            case DROPPER:
            case EMERALD_BLOCK:
            case EMERALD_ORE:
            case ENCHANTING_TABLE:
            case ENDER_CHEST:
            case END_PORTAL_FRAME:
            case END_STONE:
            case END_STONE_BRICKS:
            case FARMLAND:
            case FIRE_CORAL_BLOCK:
            case FROSTED_ICE:
            case FURNACE:
            case GLASS:
            case GLASS_PANE:
            case GLOWSTONE:
            case GOLD_BLOCK:
            case GOLD_ORE:
            case GRANITE:
            case GRASS_BLOCK:
            case GRASS_PATH:
            case GRAVEL:
            case GRAY_BANNER:
            case GRAY_BED:
            case GRAY_CONCRETE:
            case GRAY_CONCRETE_POWDER:
            case GRAY_GLAZED_TERRACOTTA:
            case GRAY_SHULKER_BOX:
            case GRAY_STAINED_GLASS:
            case GRAY_STAINED_GLASS_PANE:
            case GRAY_TERRACOTTA:
            case GRAY_WALL_BANNER:
            case GRAY_WOOL:
            case GREEN_BANNER:
            case GREEN_BED:
            case GREEN_CONCRETE:
            case GREEN_CONCRETE_POWDER:
            case GREEN_GLAZED_TERRACOTTA:
            case GREEN_SHULKER_BOX:
            case GREEN_STAINED_GLASS:
            case GREEN_STAINED_GLASS_PANE:
            case GREEN_TERRACOTTA:
            case GREEN_WALL_BANNER:
            case GREEN_WOOL:
            case HAY_BLOCK:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case HOPPER:
            case HORN_CORAL_BLOCK:
            case ICE:
            case INFESTED_CHISELED_STONE_BRICKS:
            case INFESTED_COBBLESTONE:
            case INFESTED_CRACKED_STONE_BRICKS:
            case INFESTED_MOSSY_STONE_BRICKS:
            case INFESTED_STONE:
            case INFESTED_STONE_BRICKS:
            case IRON_BARS:
            case IRON_BLOCK:
            case IRON_DOOR:
            case IRON_ORE:
            case IRON_TRAPDOOR:
            case JACK_O_LANTERN:
            case JUKEBOX:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LEAVES:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case JUNGLE_WOOD:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_BED:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_BED:
            case LIGHT_GRAY_CONCRETE:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case LIGHT_GRAY_TERRACOTTA:
            case LIGHT_GRAY_WALL_BANNER:
            case LIGHT_GRAY_WOOL:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case LIME_BANNER:
            case LIME_BED:
            case LIME_CONCRETE:
            case LIME_CONCRETE_POWDER:
            case LIME_GLAZED_TERRACOTTA:
            case LIME_SHULKER_BOX:
            case LIME_STAINED_GLASS:
            case LIME_STAINED_GLASS_PANE:
            case LIME_TERRACOTTA:
            case LIME_WALL_BANNER:
            case LIME_WOOL:
            case MAGENTA_BANNER:
            case MAGENTA_BED:
            case MAGENTA_CONCRETE:
            case MAGENTA_CONCRETE_POWDER:
            case MAGENTA_GLAZED_TERRACOTTA:
            case MAGENTA_SHULKER_BOX:
            case MAGENTA_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS_PANE:
            case MAGENTA_TERRACOTTA:
            case MAGENTA_WALL_BANNER:
            case MAGENTA_WOOL:
            case MAGMA_BLOCK:
            case MELON:
            case MOSSY_COBBLESTONE:
            case MOSSY_COBBLESTONE_WALL:
            case MOSSY_STONE_BRICKS:
            case MOVING_PISTON:
            case MUSHROOM_STEM:
            case MYCELIUM:
            case NETHERRACK:
            case NETHER_BRICKS:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_SLAB:
            case NETHER_BRICK_STAIRS:
            case NETHER_QUARTZ_ORE:
            case NETHER_WART_BLOCK:
            case NOTE_BLOCK:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LEAVES:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OAK_WOOD:
            case OBSERVER:
            case OBSIDIAN:
            case ORANGE_BANNER:
            case ORANGE_BED:
            case ORANGE_CONCRETE:
            case ORANGE_CONCRETE_POWDER:
            case ORANGE_GLAZED_TERRACOTTA:
            case ORANGE_SHULKER_BOX:
            case ORANGE_STAINED_GLASS:
            case ORANGE_STAINED_GLASS_PANE:
            case ORANGE_TERRACOTTA:
            case ORANGE_WALL_BANNER:
            case ORANGE_WOOL:
            case PACKED_ICE:
            case PETRIFIED_OAK_SLAB:
            case PINK_BANNER:
            case PINK_BED:
            case PINK_CONCRETE:
            case PINK_CONCRETE_POWDER:
            case PINK_GLAZED_TERRACOTTA:
            case PINK_SHULKER_BOX:
            case PINK_STAINED_GLASS:
            case PINK_STAINED_GLASS_PANE:
            case PINK_TERRACOTTA:
            case PINK_WALL_BANNER:
            case PINK_WOOL:
            case PISTON:
            case PISTON_HEAD:
            case PODZOL:
            case POLISHED_ANDESITE:
            case POLISHED_DIORITE:
            case POLISHED_GRANITE:
            case PRISMARINE:
            case PRISMARINE_BRICKS:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_SLAB:
            case PRISMARINE_STAIRS:
            case PUMPKIN:
            case PURPLE_BANNER:
            case PURPLE_BED:
            case PURPLE_CONCRETE:
            case PURPLE_CONCRETE_POWDER:
            case PURPLE_GLAZED_TERRACOTTA:
            case PURPLE_SHULKER_BOX:
            case PURPLE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS_PANE:
            case PURPLE_TERRACOTTA:
            case PURPLE_WALL_BANNER:
            case PURPLE_WOOL:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_SLAB:
            case PURPUR_STAIRS:
            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case QUARTZ_SLAB:
            case QUARTZ_STAIRS:
            case REDSTONE_BLOCK:
            case REDSTONE_LAMP:
            case REDSTONE_ORE:
            case RED_BANNER:
            case RED_BED:
            case RED_CONCRETE:
            case RED_CONCRETE_POWDER:
            case RED_GLAZED_TERRACOTTA:
            case RED_MUSHROOM_BLOCK:
            case RED_NETHER_BRICKS:
            case RED_SAND:
            case RED_SANDSTONE:
            case RED_SANDSTONE_SLAB:
            case RED_SANDSTONE_STAIRS:
            case RED_SHULKER_BOX:
            case RED_STAINED_GLASS:
            case RED_STAINED_GLASS_PANE:
            case RED_TERRACOTTA:
            case RED_WALL_BANNER:
            case RED_WOOL:
            case REPEATING_COMMAND_BLOCK:
            case SAND:
            case SANDSTONE:
            case SANDSTONE_SLAB:
            case SANDSTONE_STAIRS:
            case SEA_LANTERN:
            case SHULKER_BOX:
            case SIGN:
            case SLIME_BLOCK:
            case SMOOTH_QUARTZ:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_SANDSTONE:
            case SMOOTH_STONE:
            case SNOW_BLOCK:
            case SOUL_SAND:
            case SPAWNER:
            case SPONGE:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LEAVES:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case SPRUCE_WOOD:
            case STICKY_PISTON:
            case STONE:
            case STONE_BRICKS:
            case STONE_BRICK_SLAB:
            case STONE_BRICK_STAIRS:
            case STONE_PRESSURE_PLATE:
            case STONE_SLAB:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case STRUCTURE_BLOCK:
            case TERRACOTTA:
            case TNT:
            case TRAPPED_CHEST:
            case TUBE_CORAL_BLOCK:
            case TURTLE_EGG:
            case WALL_SIGN:
            case WET_SPONGE:
            case WHITE_BANNER:
            case WHITE_BED:
            case WHITE_CONCRETE:
            case WHITE_CONCRETE_POWDER:
            case WHITE_GLAZED_TERRACOTTA:
            case WHITE_SHULKER_BOX:
            case WHITE_STAINED_GLASS:
            case WHITE_STAINED_GLASS_PANE:
            case WHITE_TERRACOTTA:
            case WHITE_WALL_BANNER:
            case WHITE_WOOL:
            case YELLOW_BANNER:
            case YELLOW_BED:
            case YELLOW_CONCRETE:
            case YELLOW_CONCRETE_POWDER:
            case YELLOW_GLAZED_TERRACOTTA:
            case YELLOW_SHULKER_BOX:
            case YELLOW_STAINED_GLASS:
            case YELLOW_STAINED_GLASS_PANE:
            case YELLOW_TERRACOTTA:
            case YELLOW_WALL_BANNER:
            case YELLOW_WOOL:
            // ----- Legacy Separator -----
            case LEGACY_STONE:
            case LEGACY_GRASS:
            case LEGACY_DIRT:
            case LEGACY_COBBLESTONE:
            case LEGACY_WOOD:
            case LEGACY_BEDROCK:
            case LEGACY_SAND:
            case LEGACY_GRAVEL:
            case LEGACY_GOLD_ORE:
            case LEGACY_IRON_ORE:
            case LEGACY_COAL_ORE:
            case LEGACY_LOG:
            case LEGACY_LEAVES:
            case LEGACY_SPONGE:
            case LEGACY_GLASS:
            case LEGACY_LAPIS_ORE:
            case LEGACY_LAPIS_BLOCK:
            case LEGACY_DISPENSER:
            case LEGACY_SANDSTONE:
            case LEGACY_NOTE_BLOCK:
            case LEGACY_BED_BLOCK:
            case LEGACY_PISTON_STICKY_BASE:
            case LEGACY_PISTON_BASE:
            case LEGACY_PISTON_EXTENSION:
            case LEGACY_WOOL:
            case LEGACY_PISTON_MOVING_PIECE:
            case LEGACY_GOLD_BLOCK:
            case LEGACY_IRON_BLOCK:
            case LEGACY_DOUBLE_STEP:
            case LEGACY_STEP:
            case LEGACY_BRICK:
            case LEGACY_TNT:
            case LEGACY_BOOKSHELF:
            case LEGACY_MOSSY_COBBLESTONE:
            case LEGACY_OBSIDIAN:
            case LEGACY_MOB_SPAWNER:
            case LEGACY_WOOD_STAIRS:
            case LEGACY_CHEST:
            case LEGACY_DIAMOND_ORE:
            case LEGACY_DIAMOND_BLOCK:
            case LEGACY_WORKBENCH:
            case LEGACY_SOIL:
            case LEGACY_FURNACE:
            case LEGACY_BURNING_FURNACE:
            case LEGACY_SIGN_POST:
            case LEGACY_WOODEN_DOOR:
            case LEGACY_COBBLESTONE_STAIRS:
            case LEGACY_WALL_SIGN:
            case LEGACY_STONE_PLATE:
            case LEGACY_IRON_DOOR_BLOCK:
            case LEGACY_WOOD_PLATE:
            case LEGACY_REDSTONE_ORE:
            case LEGACY_GLOWING_REDSTONE_ORE:
            case LEGACY_ICE:
            case LEGACY_SNOW_BLOCK:
            case LEGACY_CACTUS:
            case LEGACY_CLAY:
            case LEGACY_JUKEBOX:
            case LEGACY_FENCE:
            case LEGACY_PUMPKIN:
            case LEGACY_NETHERRACK:
            case LEGACY_SOUL_SAND:
            case LEGACY_GLOWSTONE:
            case LEGACY_JACK_O_LANTERN:
            case LEGACY_CAKE_BLOCK:
            case LEGACY_STAINED_GLASS:
            case LEGACY_TRAP_DOOR:
            case LEGACY_MONSTER_EGGS:
            case LEGACY_SMOOTH_BRICK:
            case LEGACY_HUGE_MUSHROOM_1:
            case LEGACY_HUGE_MUSHROOM_2:
            case LEGACY_IRON_FENCE:
            case LEGACY_THIN_GLASS:
            case LEGACY_MELON_BLOCK:
            case LEGACY_FENCE_GATE:
            case LEGACY_BRICK_STAIRS:
            case LEGACY_SMOOTH_STAIRS:
            case LEGACY_MYCEL:
            case LEGACY_NETHER_BRICK:
            case LEGACY_NETHER_FENCE:
            case LEGACY_NETHER_BRICK_STAIRS:
            case LEGACY_ENCHANTMENT_TABLE:
            case LEGACY_BREWING_STAND:
            case LEGACY_CAULDRON:
            case LEGACY_ENDER_PORTAL_FRAME:
            case LEGACY_ENDER_STONE:
            case LEGACY_DRAGON_EGG:
            case LEGACY_REDSTONE_LAMP_OFF:
            case LEGACY_REDSTONE_LAMP_ON:
            case LEGACY_WOOD_DOUBLE_STEP:
            case LEGACY_WOOD_STEP:
            case LEGACY_SANDSTONE_STAIRS:
            case LEGACY_EMERALD_ORE:
            case LEGACY_ENDER_CHEST:
            case LEGACY_EMERALD_BLOCK:
            case LEGACY_SPRUCE_WOOD_STAIRS:
            case LEGACY_BIRCH_WOOD_STAIRS:
            case LEGACY_JUNGLE_WOOD_STAIRS:
            case LEGACY_COMMAND:
            case LEGACY_BEACON:
            case LEGACY_COBBLE_WALL:
            case LEGACY_ANVIL:
            case LEGACY_TRAPPED_CHEST:
            case LEGACY_GOLD_PLATE:
            case LEGACY_IRON_PLATE:
            case LEGACY_DAYLIGHT_DETECTOR:
            case LEGACY_REDSTONE_BLOCK:
            case LEGACY_QUARTZ_ORE:
            case LEGACY_HOPPER:
            case LEGACY_QUARTZ_BLOCK:
            case LEGACY_QUARTZ_STAIRS:
            case LEGACY_DROPPER:
            case LEGACY_STAINED_CLAY:
            case LEGACY_HAY_BLOCK:
            case LEGACY_HARD_CLAY:
            case LEGACY_COAL_BLOCK:
            case LEGACY_STAINED_GLASS_PANE:
            case LEGACY_LEAVES_2:
            case LEGACY_LOG_2:
            case LEGACY_ACACIA_STAIRS:
            case LEGACY_DARK_OAK_STAIRS:
            case LEGACY_PACKED_ICE:
            case LEGACY_RED_SANDSTONE:
            case LEGACY_SLIME_BLOCK:
            case LEGACY_BARRIER:
            case LEGACY_IRON_TRAPDOOR:
            case LEGACY_PRISMARINE:
            case LEGACY_SEA_LANTERN:
            case LEGACY_DOUBLE_STONE_SLAB2:
            case LEGACY_RED_SANDSTONE_STAIRS:
            case LEGACY_STONE_SLAB2:
            case LEGACY_SPRUCE_FENCE_GATE:
            case LEGACY_BIRCH_FENCE_GATE:
            case LEGACY_JUNGLE_FENCE_GATE:
            case LEGACY_DARK_OAK_FENCE_GATE:
            case LEGACY_ACACIA_FENCE_GATE:
            case LEGACY_SPRUCE_FENCE:
            case LEGACY_BIRCH_FENCE:
            case LEGACY_JUNGLE_FENCE:
            case LEGACY_DARK_OAK_FENCE:
            case LEGACY_ACACIA_FENCE:
            case LEGACY_STANDING_BANNER:
            case LEGACY_WALL_BANNER:
            case LEGACY_DAYLIGHT_DETECTOR_INVERTED:
            case LEGACY_SPRUCE_DOOR:
            case LEGACY_BIRCH_DOOR:
            case LEGACY_JUNGLE_DOOR:
            case LEGACY_ACACIA_DOOR:
            case LEGACY_DARK_OAK_DOOR:
            case LEGACY_PURPUR_BLOCK:
            case LEGACY_PURPUR_PILLAR:
            case LEGACY_PURPUR_STAIRS:
            case LEGACY_PURPUR_DOUBLE_SLAB:
            case LEGACY_PURPUR_SLAB:
            case LEGACY_END_BRICKS:
            case LEGACY_GRASS_PATH:
            case LEGACY_STRUCTURE_BLOCK:
            case LEGACY_COMMAND_REPEATING:
            case LEGACY_COMMAND_CHAIN:
            case LEGACY_FROSTED_ICE:
            case LEGACY_MAGMA:
            case LEGACY_NETHER_WART_BLOCK:
            case LEGACY_RED_NETHER_BRICK:
            case LEGACY_BONE_BLOCK:
            case LEGACY_OBSERVER:
            case LEGACY_WHITE_SHULKER_BOX:
            case LEGACY_ORANGE_SHULKER_BOX:
            case LEGACY_MAGENTA_SHULKER_BOX:
            case LEGACY_LIGHT_BLUE_SHULKER_BOX:
            case LEGACY_YELLOW_SHULKER_BOX:
            case LEGACY_LIME_SHULKER_BOX:
            case LEGACY_PINK_SHULKER_BOX:
            case LEGACY_GRAY_SHULKER_BOX:
            case LEGACY_SILVER_SHULKER_BOX:
            case LEGACY_CYAN_SHULKER_BOX:
            case LEGACY_PURPLE_SHULKER_BOX:
            case LEGACY_BLUE_SHULKER_BOX:
            case LEGACY_BROWN_SHULKER_BOX:
            case LEGACY_GREEN_SHULKER_BOX:
            case LEGACY_RED_SHULKER_BOX:
            case LEGACY_BLACK_SHULKER_BOX:
            case LEGACY_WHITE_GLAZED_TERRACOTTA:
            case LEGACY_ORANGE_GLAZED_TERRACOTTA:
            case LEGACY_MAGENTA_GLAZED_TERRACOTTA:
            case LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LEGACY_YELLOW_GLAZED_TERRACOTTA:
            case LEGACY_LIME_GLAZED_TERRACOTTA:
            case LEGACY_PINK_GLAZED_TERRACOTTA:
            case LEGACY_GRAY_GLAZED_TERRACOTTA:
            case LEGACY_SILVER_GLAZED_TERRACOTTA:
            case LEGACY_CYAN_GLAZED_TERRACOTTA:
            case LEGACY_PURPLE_GLAZED_TERRACOTTA:
            case LEGACY_BLUE_GLAZED_TERRACOTTA:
            case LEGACY_BROWN_GLAZED_TERRACOTTA:
            case LEGACY_GREEN_GLAZED_TERRACOTTA:
            case LEGACY_RED_GLAZED_TERRACOTTA:
            case LEGACY_BLACK_GLAZED_TERRACOTTA:
            case LEGACY_CONCRETE:
            case LEGACY_CONCRETE_POWDER:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and does not block any light
     *
     * @return True if this material is a block and does not block any light
     * @deprecated currently does not have an implementation which is well
     * linked to the underlying server. Contributions welcome.
     */
    @Deprecated
    public boolean isTransparent() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isTransparent">
            case ACACIA_BUTTON:
            case ACACIA_SAPLING:
            case ACTIVATOR_RAIL:
            case AIR:
            case ALLIUM:
            case ATTACHED_MELON_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case AZURE_BLUET:
            case BARRIER:
            case BEETROOTS:
            case BIRCH_BUTTON:
            case BIRCH_SAPLING:
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BLUE_ORCHID:
            case BROWN_CARPET:
            case BROWN_MUSHROOM:
            case CARROTS:
            case CAVE_AIR:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
            case COCOA:
            case COMPARATOR:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case CYAN_CARPET:
            case DANDELION:
            case DARK_OAK_BUTTON:
            case DARK_OAK_SAPLING:
            case DEAD_BUSH:
            case DETECTOR_RAIL:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case END_GATEWAY:
            case END_PORTAL:
            case END_ROD:
            case FERN:
            case FIRE:
            case FLOWER_POT:
            case GRASS:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case JUNGLE_BUTTON:
            case JUNGLE_SAPLING:
            case LADDER:
            case LARGE_FERN:
            case LEVER:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LILAC:
            case LILY_PAD:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case MELON_STEM:
            case NETHER_PORTAL:
            case NETHER_WART:
            case OAK_BUTTON:
            case OAK_SAPLING:
            case ORANGE_CARPET:
            case ORANGE_TULIP:
            case OXEYE_DAISY:
            case PEONY:
            case PINK_CARPET:
            case PINK_TULIP:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case POPPY:
            case POTATOES:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case POWERED_RAIL:
            case PUMPKIN_STEM:
            case PURPLE_CARPET:
            case RAIL:
            case REDSTONE_TORCH:
            case REDSTONE_WALL_TORCH:
            case REDSTONE_WIRE:
            case RED_CARPET:
            case RED_MUSHROOM:
            case RED_TULIP:
            case REPEATER:
            case ROSE_BUSH:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case SNOW:
            case SPRUCE_BUTTON:
            case SPRUCE_SAPLING:
            case STONE_BUTTON:
            case STRUCTURE_VOID:
            case SUGAR_CANE:
            case SUNFLOWER:
            case TALL_GRASS:
            case TORCH:
            case TRIPWIRE:
            case TRIPWIRE_HOOK:
            case VINE:
            case VOID_AIR:
            case WALL_TORCH:
            case WHEAT:
            case WHITE_CARPET:
            case WHITE_TULIP:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_CARPET:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
            // ----- Legacy Separator -----
            case LEGACY_AIR:
            case LEGACY_SAPLING:
            case LEGACY_POWERED_RAIL:
            case LEGACY_DETECTOR_RAIL:
            case LEGACY_LONG_GRASS:
            case LEGACY_DEAD_BUSH:
            case LEGACY_YELLOW_FLOWER:
            case LEGACY_RED_ROSE:
            case LEGACY_BROWN_MUSHROOM:
            case LEGACY_RED_MUSHROOM:
            case LEGACY_TORCH:
            case LEGACY_FIRE:
            case LEGACY_REDSTONE_WIRE:
            case LEGACY_CROPS:
            case LEGACY_LADDER:
            case LEGACY_RAILS:
            case LEGACY_LEVER:
            case LEGACY_REDSTONE_TORCH_OFF:
            case LEGACY_REDSTONE_TORCH_ON:
            case LEGACY_STONE_BUTTON:
            case LEGACY_SNOW:
            case LEGACY_SUGAR_CANE_BLOCK:
            case LEGACY_PORTAL:
            case LEGACY_DIODE_BLOCK_OFF:
            case LEGACY_DIODE_BLOCK_ON:
            case LEGACY_PUMPKIN_STEM:
            case LEGACY_MELON_STEM:
            case LEGACY_VINE:
            case LEGACY_WATER_LILY:
            case LEGACY_NETHER_WARTS:
            case LEGACY_ENDER_PORTAL:
            case LEGACY_COCOA:
            case LEGACY_TRIPWIRE_HOOK:
            case LEGACY_TRIPWIRE:
            case LEGACY_FLOWER_POT:
            case LEGACY_CARROT:
            case LEGACY_POTATO:
            case LEGACY_WOOD_BUTTON:
            case LEGACY_SKULL:
            case LEGACY_REDSTONE_COMPARATOR_OFF:
            case LEGACY_REDSTONE_COMPARATOR_ON:
            case LEGACY_ACTIVATOR_RAIL:
            case LEGACY_CARPET:
            case LEGACY_DOUBLE_PLANT:
            case LEGACY_END_ROD:
            case LEGACY_CHORUS_PLANT:
            case LEGACY_CHORUS_FLOWER:
            case LEGACY_BEETROOT_BLOCK:
            case LEGACY_END_GATEWAY:
            case LEGACY_STRUCTURE_VOID:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and can catch fire
     *
     * @return True if this material is a block and can catch fire
     */
    public boolean isFlammable() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isFlammable">
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LEAVES:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ACACIA_WOOD:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LEAVES:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BIRCH_WOOD:
            case BLACK_BANNER:
            case BLACK_BED:
            case BLACK_CARPET:
            case BLACK_WALL_BANNER:
            case BLACK_WOOL:
            case BLUE_BANNER:
            case BLUE_BED:
            case BLUE_CARPET:
            case BLUE_WALL_BANNER:
            case BLUE_WOOL:
            case BOOKSHELF:
            case BROWN_BANNER:
            case BROWN_BED:
            case BROWN_CARPET:
            case BROWN_MUSHROOM_BLOCK:
            case BROWN_WALL_BANNER:
            case BROWN_WOOL:
            case CHEST:
            case CRAFTING_TABLE:
            case CYAN_BANNER:
            case CYAN_BED:
            case CYAN_CARPET:
            case CYAN_WALL_BANNER:
            case CYAN_WOOL:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LEAVES:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_OAK_WOOD:
            case DAYLIGHT_DETECTOR:
            case DEAD_BUSH:
            case FERN:
            case GRASS:
            case GRAY_BANNER:
            case GRAY_BED:
            case GRAY_CARPET:
            case GRAY_WALL_BANNER:
            case GRAY_WOOL:
            case GREEN_BANNER:
            case GREEN_BED:
            case GREEN_CARPET:
            case GREEN_WALL_BANNER:
            case GREEN_WOOL:
            case JUKEBOX:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LEAVES:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case JUNGLE_WOOD:
            case LARGE_FERN:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_BED:
            case LIGHT_BLUE_CARPET:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_BED:
            case LIGHT_GRAY_CARPET:
            case LIGHT_GRAY_WALL_BANNER:
            case LIGHT_GRAY_WOOL:
            case LILAC:
            case LIME_BANNER:
            case LIME_BED:
            case LIME_CARPET:
            case LIME_WALL_BANNER:
            case LIME_WOOL:
            case MAGENTA_BANNER:
            case MAGENTA_BED:
            case MAGENTA_CARPET:
            case MAGENTA_WALL_BANNER:
            case MAGENTA_WOOL:
            case MUSHROOM_STEM:
            case NOTE_BLOCK:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LEAVES:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OAK_WOOD:
            case ORANGE_BANNER:
            case ORANGE_BED:
            case ORANGE_CARPET:
            case ORANGE_WALL_BANNER:
            case ORANGE_WOOL:
            case PEONY:
            case PINK_BANNER:
            case PINK_BED:
            case PINK_CARPET:
            case PINK_WALL_BANNER:
            case PINK_WOOL:
            case PURPLE_BANNER:
            case PURPLE_BED:
            case PURPLE_CARPET:
            case PURPLE_WALL_BANNER:
            case PURPLE_WOOL:
            case RED_BANNER:
            case RED_BED:
            case RED_CARPET:
            case RED_MUSHROOM_BLOCK:
            case RED_WALL_BANNER:
            case RED_WOOL:
            case ROSE_BUSH:
            case SIGN:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LEAVES:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case SPRUCE_WOOD:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case SUNFLOWER:
            case TALL_GRASS:
            case TNT:
            case TRAPPED_CHEST:
            case VINE:
            case WALL_SIGN:
            case WHITE_BANNER:
            case WHITE_BED:
            case WHITE_CARPET:
            case WHITE_WALL_BANNER:
            case WHITE_WOOL:
            case YELLOW_BANNER:
            case YELLOW_BED:
            case YELLOW_CARPET:
            case YELLOW_WALL_BANNER:
            case YELLOW_WOOL:
            // ----- Legacy Separator -----
            case LEGACY_WOOD:
            case LEGACY_LOG:
            case LEGACY_LEAVES:
            case LEGACY_NOTE_BLOCK:
            case LEGACY_BED_BLOCK:
            case LEGACY_LONG_GRASS:
            case LEGACY_DEAD_BUSH:
            case LEGACY_WOOL:
            case LEGACY_TNT:
            case LEGACY_BOOKSHELF:
            case LEGACY_WOOD_STAIRS:
            case LEGACY_CHEST:
            case LEGACY_WORKBENCH:
            case LEGACY_SIGN_POST:
            case LEGACY_WOODEN_DOOR:
            case LEGACY_WALL_SIGN:
            case LEGACY_WOOD_PLATE:
            case LEGACY_JUKEBOX:
            case LEGACY_FENCE:
            case LEGACY_TRAP_DOOR:
            case LEGACY_HUGE_MUSHROOM_1:
            case LEGACY_HUGE_MUSHROOM_2:
            case LEGACY_VINE:
            case LEGACY_FENCE_GATE:
            case LEGACY_WOOD_DOUBLE_STEP:
            case LEGACY_WOOD_STEP:
            case LEGACY_SPRUCE_WOOD_STAIRS:
            case LEGACY_BIRCH_WOOD_STAIRS:
            case LEGACY_JUNGLE_WOOD_STAIRS:
            case LEGACY_TRAPPED_CHEST:
            case LEGACY_DAYLIGHT_DETECTOR:
            case LEGACY_CARPET:
            case LEGACY_LEAVES_2:
            case LEGACY_LOG_2:
            case LEGACY_ACACIA_STAIRS:
            case LEGACY_DARK_OAK_STAIRS:
            case LEGACY_DOUBLE_PLANT:
            case LEGACY_SPRUCE_FENCE_GATE:
            case LEGACY_BIRCH_FENCE_GATE:
            case LEGACY_JUNGLE_FENCE_GATE:
            case LEGACY_DARK_OAK_FENCE_GATE:
            case LEGACY_ACACIA_FENCE_GATE:
            case LEGACY_SPRUCE_FENCE:
            case LEGACY_BIRCH_FENCE:
            case LEGACY_JUNGLE_FENCE:
            case LEGACY_DARK_OAK_FENCE:
            case LEGACY_ACACIA_FENCE:
            case LEGACY_STANDING_BANNER:
            case LEGACY_WALL_BANNER:
            case LEGACY_DAYLIGHT_DETECTOR_INVERTED:
            case LEGACY_SPRUCE_DOOR:
            case LEGACY_BIRCH_DOOR:
            case LEGACY_JUNGLE_DOOR:
            case LEGACY_ACACIA_DOOR:
            case LEGACY_DARK_OAK_DOOR:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and can burn away
     *
     * @return True if this material is a block and can burn away
     */
    public boolean isBurnable() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isBurnable">
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LEAVES:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_WOOD:
            case ALLIUM:
            case AZURE_BLUET:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LEAVES:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_WOOD:
            case BLACK_CARPET:
            case BLACK_WOOL:
            case BLUE_CARPET:
            case BLUE_ORCHID:
            case BLUE_WOOL:
            case BOOKSHELF:
            case BROWN_CARPET:
            case BROWN_WOOL:
            case COAL_BLOCK:
            case CYAN_CARPET:
            case CYAN_WOOL:
            case DANDELION:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LEAVES:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_WOOD:
            case DEAD_BUSH:
            case DRIED_KELP_BLOCK:
            case FERN:
            case GRASS:
            case GRAY_CARPET:
            case GRAY_WOOL:
            case GREEN_CARPET:
            case GREEN_WOOL:
            case HAY_BLOCK:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LEAVES:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_WOOD:
            case LARGE_FERN:
            case LIGHT_BLUE_CARPET:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_CARPET:
            case LIGHT_GRAY_WOOL:
            case LILAC:
            case LIME_CARPET:
            case LIME_WOOL:
            case MAGENTA_CARPET:
            case MAGENTA_WOOL:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LEAVES:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_WOOD:
            case ORANGE_CARPET:
            case ORANGE_TULIP:
            case ORANGE_WOOL:
            case OXEYE_DAISY:
            case PEONY:
            case PINK_CARPET:
            case PINK_TULIP:
            case PINK_WOOL:
            case POPPY:
            case PURPLE_CARPET:
            case PURPLE_WOOL:
            case RED_CARPET:
            case RED_TULIP:
            case RED_WOOL:
            case ROSE_BUSH:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LEAVES:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_WOOD:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case SUNFLOWER:
            case TALL_GRASS:
            case TNT:
            case VINE:
            case WHITE_CARPET:
            case WHITE_TULIP:
            case WHITE_WOOL:
            case YELLOW_CARPET:
            case YELLOW_WOOL:
            // ----- Legacy Separator -----
            case LEGACY_WOOD:
            case LEGACY_LOG:
            case LEGACY_LEAVES:
            case LEGACY_LONG_GRASS:
            case LEGACY_WOOL:
            case LEGACY_YELLOW_FLOWER:
            case LEGACY_RED_ROSE:
            case LEGACY_TNT:
            case LEGACY_BOOKSHELF:
            case LEGACY_WOOD_STAIRS:
            case LEGACY_FENCE:
            case LEGACY_VINE:
            case LEGACY_WOOD_DOUBLE_STEP:
            case LEGACY_WOOD_STEP:
            case LEGACY_SPRUCE_WOOD_STAIRS:
            case LEGACY_BIRCH_WOOD_STAIRS:
            case LEGACY_JUNGLE_WOOD_STAIRS:
            case LEGACY_HAY_BLOCK:
            case LEGACY_COAL_BLOCK:
            case LEGACY_LEAVES_2:
            case LEGACY_LOG_2:
            case LEGACY_CARPET:
            case LEGACY_DOUBLE_PLANT:
            case LEGACY_DEAD_BUSH:
            case LEGACY_FENCE_GATE:
            case LEGACY_SPRUCE_FENCE_GATE:
            case LEGACY_BIRCH_FENCE_GATE:
            case LEGACY_JUNGLE_FENCE_GATE:
            case LEGACY_DARK_OAK_FENCE_GATE:
            case LEGACY_ACACIA_FENCE_GATE:
            case LEGACY_SPRUCE_FENCE:
            case LEGACY_BIRCH_FENCE:
            case LEGACY_JUNGLE_FENCE:
            case LEGACY_DARK_OAK_FENCE:
            case LEGACY_ACACIA_FENCE:
            case LEGACY_ACACIA_STAIRS:
            case LEGACY_DARK_OAK_STAIRS:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this Material can be used as fuel in a Furnace
     *
     * @return true if this Material can be used as fuel.
     */
    public boolean isFuel() {
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isFuel">
            case ACACIA_BOAT:
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SAPLING:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ACACIA_WOOD:
            case BIRCH_BOAT:
            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SAPLING:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BIRCH_WOOD:
            case BLACK_BANNER:
            case BLACK_CARPET:
            case BLACK_WOOL:
            case BLAZE_ROD:
            case BLUE_BANNER:
            case BLUE_CARPET:
            case BLUE_WOOL:
            case BOOKSHELF:
            case BOW:
            case BOWL:
            case BROWN_BANNER:
            case BROWN_CARPET:
            case BROWN_WOOL:
            case CHARCOAL:
            case CHEST:
            case COAL:
            case COAL_BLOCK:
            case CRAFTING_TABLE:
            case CYAN_BANNER:
            case CYAN_CARPET:
            case CYAN_WOOL:
            case DARK_OAK_BOAT:
            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SAPLING:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_OAK_WOOD:
            case DAYLIGHT_DETECTOR:
            case DRIED_KELP_BLOCK:
            case FISHING_ROD:
            case GRAY_BANNER:
            case GRAY_CARPET:
            case GRAY_WOOL:
            case GREEN_BANNER:
            case GREEN_CARPET:
            case GREEN_WOOL:
            case JUKEBOX:
            case JUNGLE_BOAT:
            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SAPLING:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case JUNGLE_WOOD:
            case LADDER:
            case LAVA_BUCKET:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_CARPET:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_CARPET:
            case LIGHT_GRAY_WOOL:
            case LIME_BANNER:
            case LIME_CARPET:
            case LIME_WOOL:
            case MAGENTA_BANNER:
            case MAGENTA_CARPET:
            case MAGENTA_WOOL:
            case NOTE_BLOCK:
            case OAK_BOAT:
            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SAPLING:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OAK_WOOD:
            case ORANGE_BANNER:
            case ORANGE_CARPET:
            case ORANGE_WOOL:
            case PINK_BANNER:
            case PINK_CARPET:
            case PINK_WOOL:
            case PURPLE_BANNER:
            case PURPLE_CARPET:
            case PURPLE_WOOL:
            case RED_BANNER:
            case RED_CARPET:
            case RED_WOOL:
            case SIGN:
            case SPRUCE_BOAT:
            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SAPLING:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case SPRUCE_WOOD:
            case STICK:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case TRAPPED_CHEST:
            case WHITE_BANNER:
            case WHITE_CARPET:
            case WHITE_WOOL:
            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
            case WOODEN_SWORD:
            case YELLOW_BANNER:
            case YELLOW_CARPET:
            case YELLOW_WOOL:
            // ----- Legacy Separator -----
            case LEGACY_LAVA_BUCKET:
            case LEGACY_COAL_BLOCK:
            case LEGACY_BLAZE_ROD:
            case LEGACY_COAL:
            case LEGACY_BOAT:
            case LEGACY_BOAT_ACACIA:
            case LEGACY_BOAT_BIRCH:
            case LEGACY_BOAT_DARK_OAK:
            case LEGACY_BOAT_JUNGLE:
            case LEGACY_BOAT_SPRUCE:
            case LEGACY_LOG:
            case LEGACY_LOG_2:
            case LEGACY_WOOD:
            case LEGACY_WOOD_PLATE:
            case LEGACY_FENCE:
            case LEGACY_ACACIA_FENCE:
            case LEGACY_BIRCH_FENCE:
            case LEGACY_DARK_OAK_FENCE:
            case LEGACY_JUNGLE_FENCE:
            case LEGACY_SPRUCE_FENCE:
            case LEGACY_FENCE_GATE:
            case LEGACY_ACACIA_FENCE_GATE:
            case LEGACY_BIRCH_FENCE_GATE:
            case LEGACY_DARK_OAK_FENCE_GATE:
            case LEGACY_JUNGLE_FENCE_GATE:
            case LEGACY_SPRUCE_FENCE_GATE:
            case LEGACY_WOOD_STAIRS:
            case LEGACY_ACACIA_STAIRS:
            case LEGACY_BIRCH_WOOD_STAIRS:
            case LEGACY_DARK_OAK_STAIRS:
            case LEGACY_JUNGLE_WOOD_STAIRS:
            case LEGACY_SPRUCE_WOOD_STAIRS:
            case LEGACY_TRAP_DOOR:
            case LEGACY_WORKBENCH:
            case LEGACY_BOOKSHELF:
            case LEGACY_CHEST:
            case LEGACY_TRAPPED_CHEST:
            case LEGACY_DAYLIGHT_DETECTOR:
            case LEGACY_JUKEBOX:
            case LEGACY_NOTE_BLOCK:
            case LEGACY_BANNER:
            case LEGACY_FISHING_ROD:
            case LEGACY_LADDER:
            case LEGACY_WOOD_SWORD:
            case LEGACY_WOOD_PICKAXE:
            case LEGACY_WOOD_AXE:
            case LEGACY_WOOD_SPADE:
            case LEGACY_WOOD_HOE:
            case LEGACY_BOW:
            case LEGACY_SIGN:
            case LEGACY_WOOD_DOOR:
            case LEGACY_ACACIA_DOOR_ITEM:
            case LEGACY_BIRCH_DOOR_ITEM:
            case LEGACY_DARK_OAK_DOOR_ITEM:
            case LEGACY_JUNGLE_DOOR_ITEM:
            case LEGACY_SPRUCE_DOOR_ITEM:
            case LEGACY_WOOD_STEP:
            case LEGACY_SAPLING:
            case LEGACY_STICK:
            case LEGACY_WOOD_BUTTON:
            case LEGACY_WOOL:
            case LEGACY_CARPET:
            case LEGACY_BOWL:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and completely blocks vision
     *
     * @return True if this material is a block and completely blocks vision
     */
    public boolean isOccluding() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isOccluding">
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_WOOD:
            case ANDESITE:
            case BARRIER:
            case BEDROCK:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_WOOD:
            case BLACK_CONCRETE:
            case BLACK_CONCRETE_POWDER:
            case BLACK_GLAZED_TERRACOTTA:
            case BLACK_TERRACOTTA:
            case BLACK_WOOL:
            case BLUE_CONCRETE:
            case BLUE_CONCRETE_POWDER:
            case BLUE_GLAZED_TERRACOTTA:
            case BLUE_ICE:
            case BLUE_TERRACOTTA:
            case BLUE_WOOL:
            case BONE_BLOCK:
            case BOOKSHELF:
            case BRAIN_CORAL_BLOCK:
            case BRICKS:
            case BROWN_CONCRETE:
            case BROWN_CONCRETE_POWDER:
            case BROWN_GLAZED_TERRACOTTA:
            case BROWN_MUSHROOM_BLOCK:
            case BROWN_TERRACOTTA:
            case BROWN_WOOL:
            case BUBBLE_CORAL_BLOCK:
            case CARVED_PUMPKIN:
            case CHAIN_COMMAND_BLOCK:
            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CHISELED_STONE_BRICKS:
            case CLAY:
            case COAL_BLOCK:
            case COAL_ORE:
            case COARSE_DIRT:
            case COBBLESTONE:
            case COMMAND_BLOCK:
            case CRACKED_STONE_BRICKS:
            case CRAFTING_TABLE:
            case CUT_RED_SANDSTONE:
            case CUT_SANDSTONE:
            case CYAN_CONCRETE:
            case CYAN_CONCRETE_POWDER:
            case CYAN_GLAZED_TERRACOTTA:
            case CYAN_TERRACOTTA:
            case CYAN_WOOL:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_WOOD:
            case DARK_PRISMARINE:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_HORN_CORAL_BLOCK:
            case DEAD_TUBE_CORAL_BLOCK:
            case DIAMOND_BLOCK:
            case DIAMOND_ORE:
            case DIORITE:
            case DIRT:
            case DISPENSER:
            case DRIED_KELP_BLOCK:
            case DROPPER:
            case EMERALD_BLOCK:
            case EMERALD_ORE:
            case END_STONE:
            case END_STONE_BRICKS:
            case FIRE_CORAL_BLOCK:
            case FURNACE:
            case GOLD_BLOCK:
            case GOLD_ORE:
            case GRANITE:
            case GRASS_BLOCK:
            case GRAVEL:
            case GRAY_CONCRETE:
            case GRAY_CONCRETE_POWDER:
            case GRAY_GLAZED_TERRACOTTA:
            case GRAY_TERRACOTTA:
            case GRAY_WOOL:
            case GREEN_CONCRETE:
            case GREEN_CONCRETE_POWDER:
            case GREEN_GLAZED_TERRACOTTA:
            case GREEN_TERRACOTTA:
            case GREEN_WOOL:
            case HAY_BLOCK:
            case HORN_CORAL_BLOCK:
            case INFESTED_CHISELED_STONE_BRICKS:
            case INFESTED_COBBLESTONE:
            case INFESTED_CRACKED_STONE_BRICKS:
            case INFESTED_MOSSY_STONE_BRICKS:
            case INFESTED_STONE:
            case INFESTED_STONE_BRICKS:
            case IRON_BLOCK:
            case IRON_ORE:
            case JACK_O_LANTERN:
            case JUKEBOX:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_WOOD:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_CONCRETE:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_TERRACOTTA:
            case LIGHT_GRAY_WOOL:
            case LIME_CONCRETE:
            case LIME_CONCRETE_POWDER:
            case LIME_GLAZED_TERRACOTTA:
            case LIME_TERRACOTTA:
            case LIME_WOOL:
            case MAGENTA_CONCRETE:
            case MAGENTA_CONCRETE_POWDER:
            case MAGENTA_GLAZED_TERRACOTTA:
            case MAGENTA_TERRACOTTA:
            case MAGENTA_WOOL:
            case MAGMA_BLOCK:
            case MELON:
            case MOSSY_COBBLESTONE:
            case MOSSY_STONE_BRICKS:
            case MUSHROOM_STEM:
            case MYCELIUM:
            case NETHERRACK:
            case NETHER_BRICKS:
            case NETHER_QUARTZ_ORE:
            case NETHER_WART_BLOCK:
            case NOTE_BLOCK:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_WOOD:
            case OBSIDIAN:
            case ORANGE_CONCRETE:
            case ORANGE_CONCRETE_POWDER:
            case ORANGE_GLAZED_TERRACOTTA:
            case ORANGE_TERRACOTTA:
            case ORANGE_WOOL:
            case PACKED_ICE:
            case PINK_CONCRETE:
            case PINK_CONCRETE_POWDER:
            case PINK_GLAZED_TERRACOTTA:
            case PINK_TERRACOTTA:
            case PINK_WOOL:
            case PODZOL:
            case POLISHED_ANDESITE:
            case POLISHED_DIORITE:
            case POLISHED_GRANITE:
            case PRISMARINE:
            case PRISMARINE_BRICKS:
            case PUMPKIN:
            case PURPLE_CONCRETE:
            case PURPLE_CONCRETE_POWDER:
            case PURPLE_GLAZED_TERRACOTTA:
            case PURPLE_TERRACOTTA:
            case PURPLE_WOOL:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case REDSTONE_LAMP:
            case REDSTONE_ORE:
            case RED_CONCRETE:
            case RED_CONCRETE_POWDER:
            case RED_GLAZED_TERRACOTTA:
            case RED_MUSHROOM_BLOCK:
            case RED_NETHER_BRICKS:
            case RED_SAND:
            case RED_SANDSTONE:
            case RED_TERRACOTTA:
            case RED_WOOL:
            case REPEATING_COMMAND_BLOCK:
            case SAND:
            case SANDSTONE:
            case SLIME_BLOCK:
            case SMOOTH_QUARTZ:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_SANDSTONE:
            case SMOOTH_STONE:
            case SNOW_BLOCK:
            case SOUL_SAND:
            case SPAWNER:
            case SPONGE:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_WOOD:
            case STONE:
            case STONE_BRICKS:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case STRUCTURE_BLOCK:
            case TERRACOTTA:
            case TUBE_CORAL_BLOCK:
            case WET_SPONGE:
            case WHITE_CONCRETE:
            case WHITE_CONCRETE_POWDER:
            case WHITE_GLAZED_TERRACOTTA:
            case WHITE_TERRACOTTA:
            case WHITE_WOOL:
            case YELLOW_CONCRETE:
            case YELLOW_CONCRETE_POWDER:
            case YELLOW_GLAZED_TERRACOTTA:
            case YELLOW_TERRACOTTA:
            case YELLOW_WOOL:
            // ----- Legacy Separator -----
            case LEGACY_STONE:
            case LEGACY_GRASS:
            case LEGACY_DIRT:
            case LEGACY_COBBLESTONE:
            case LEGACY_WOOD:
            case LEGACY_BEDROCK:
            case LEGACY_SAND:
            case LEGACY_GRAVEL:
            case LEGACY_GOLD_ORE:
            case LEGACY_IRON_ORE:
            case LEGACY_COAL_ORE:
            case LEGACY_LOG:
            case LEGACY_SPONGE:
            case LEGACY_LAPIS_ORE:
            case LEGACY_LAPIS_BLOCK:
            case LEGACY_DISPENSER:
            case LEGACY_SANDSTONE:
            case LEGACY_NOTE_BLOCK:
            case LEGACY_WOOL:
            case LEGACY_GOLD_BLOCK:
            case LEGACY_IRON_BLOCK:
            case LEGACY_DOUBLE_STEP:
            case LEGACY_BRICK:
            case LEGACY_BOOKSHELF:
            case LEGACY_MOSSY_COBBLESTONE:
            case LEGACY_OBSIDIAN:
            case LEGACY_MOB_SPAWNER:
            case LEGACY_DIAMOND_ORE:
            case LEGACY_DIAMOND_BLOCK:
            case LEGACY_WORKBENCH:
            case LEGACY_FURNACE:
            case LEGACY_BURNING_FURNACE:
            case LEGACY_REDSTONE_ORE:
            case LEGACY_GLOWING_REDSTONE_ORE:
            case LEGACY_SNOW_BLOCK:
            case LEGACY_CLAY:
            case LEGACY_JUKEBOX:
            case LEGACY_PUMPKIN:
            case LEGACY_NETHERRACK:
            case LEGACY_SOUL_SAND:
            case LEGACY_JACK_O_LANTERN:
            case LEGACY_MONSTER_EGGS:
            case LEGACY_SMOOTH_BRICK:
            case LEGACY_HUGE_MUSHROOM_1:
            case LEGACY_HUGE_MUSHROOM_2:
            case LEGACY_MELON_BLOCK:
            case LEGACY_MYCEL:
            case LEGACY_NETHER_BRICK:
            case LEGACY_ENDER_STONE:
            case LEGACY_REDSTONE_LAMP_OFF:
            case LEGACY_REDSTONE_LAMP_ON:
            case LEGACY_WOOD_DOUBLE_STEP:
            case LEGACY_EMERALD_ORE:
            case LEGACY_EMERALD_BLOCK:
            case LEGACY_COMMAND:
            case LEGACY_QUARTZ_ORE:
            case LEGACY_QUARTZ_BLOCK:
            case LEGACY_DROPPER:
            case LEGACY_STAINED_CLAY:
            case LEGACY_HAY_BLOCK:
            case LEGACY_HARD_CLAY:
            case LEGACY_COAL_BLOCK:
            case LEGACY_LOG_2:
            case LEGACY_PACKED_ICE:
            case LEGACY_SLIME_BLOCK:
            case LEGACY_BARRIER:
            case LEGACY_PRISMARINE:
            case LEGACY_RED_SANDSTONE:
            case LEGACY_DOUBLE_STONE_SLAB2:
            case LEGACY_PURPUR_BLOCK:
            case LEGACY_PURPUR_PILLAR:
            case LEGACY_PURPUR_DOUBLE_SLAB:
            case LEGACY_END_BRICKS:
            case LEGACY_STRUCTURE_BLOCK:
            case LEGACY_COMMAND_REPEATING:
            case LEGACY_COMMAND_CHAIN:
            case LEGACY_MAGMA:
            case LEGACY_NETHER_WART_BLOCK:
            case LEGACY_RED_NETHER_BRICK:
            case LEGACY_BONE_BLOCK:
            case LEGACY_WHITE_GLAZED_TERRACOTTA:
            case LEGACY_ORANGE_GLAZED_TERRACOTTA:
            case LEGACY_MAGENTA_GLAZED_TERRACOTTA:
            case LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LEGACY_YELLOW_GLAZED_TERRACOTTA:
            case LEGACY_LIME_GLAZED_TERRACOTTA:
            case LEGACY_PINK_GLAZED_TERRACOTTA:
            case LEGACY_GRAY_GLAZED_TERRACOTTA:
            case LEGACY_SILVER_GLAZED_TERRACOTTA:
            case LEGACY_CYAN_GLAZED_TERRACOTTA:
            case LEGACY_PURPLE_GLAZED_TERRACOTTA:
            case LEGACY_BLUE_GLAZED_TERRACOTTA:
            case LEGACY_BROWN_GLAZED_TERRACOTTA:
            case LEGACY_GREEN_GLAZED_TERRACOTTA:
            case LEGACY_RED_GLAZED_TERRACOTTA:
            case LEGACY_BLACK_GLAZED_TERRACOTTA:
            case LEGACY_CONCRETE:
            case LEGACY_CONCRETE_POWDER:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * @return True if this material is affected by gravity.
     */
    public boolean hasGravity() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="hasGravity">
            case ANVIL:
            case BLACK_CONCRETE_POWDER:
            case BLUE_CONCRETE_POWDER:
            case BROWN_CONCRETE_POWDER:
            case CHIPPED_ANVIL:
            case CYAN_CONCRETE_POWDER:
            case DAMAGED_ANVIL:
            case DRAGON_EGG:
            case GRAVEL:
            case GRAY_CONCRETE_POWDER:
            case GREEN_CONCRETE_POWDER:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIME_CONCRETE_POWDER:
            case MAGENTA_CONCRETE_POWDER:
            case ORANGE_CONCRETE_POWDER:
            case PINK_CONCRETE_POWDER:
            case PURPLE_CONCRETE_POWDER:
            case RED_CONCRETE_POWDER:
            case RED_SAND:
            case SAND:
            case WHITE_CONCRETE_POWDER:
            case YELLOW_CONCRETE_POWDER:
            // ----- Legacy Separator -----
            case LEGACY_SAND:
            case LEGACY_GRAVEL:
            case LEGACY_ANVIL:
            case LEGACY_CONCRETE_POWDER:
            //</editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this Material is an obtainable item.
     *
     * @return true if this material is an item
     */
    public boolean isItem() {
        switch (this) {
            //<editor-fold defaultstate="collapsed" desc="isItem">
            case ATTACHED_MELON_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case BEETROOTS:
            case BLACK_WALL_BANNER:
            case BLUE_WALL_BANNER:
            case BRAIN_CORAL_WALL_FAN:
            case BROWN_WALL_BANNER:
            case BUBBLE_COLUMN:
            case BUBBLE_CORAL_WALL_FAN:
            case CARROTS:
            case CAVE_AIR:
            case COCOA:
            case CREEPER_WALL_HEAD:
            case CYAN_WALL_BANNER:
            case DEAD_BRAIN_CORAL_WALL_FAN:
            case DEAD_BUBBLE_CORAL_WALL_FAN:
            case DEAD_FIRE_CORAL_WALL_FAN:
            case DEAD_HORN_CORAL_WALL_FAN:
            case DEAD_TUBE_CORAL_WALL_FAN:
            case DRAGON_WALL_HEAD:
            case END_GATEWAY:
            case END_PORTAL:
            case FIRE:
            case FIRE_CORAL_WALL_FAN:
            case FROSTED_ICE:
            case GRAY_WALL_BANNER:
            case GREEN_WALL_BANNER:
            case HORN_CORAL_WALL_FAN:
            case KELP_PLANT:
            case LAVA:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_WALL_BANNER:
            case MELON_STEM:
            case MOVING_PISTON:
            case NETHER_PORTAL:
            case ORANGE_WALL_BANNER:
            case PINK_WALL_BANNER:
            case PISTON_HEAD:
            case PLAYER_WALL_HEAD:
            case POTATOES:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case PUMPKIN_STEM:
            case PURPLE_WALL_BANNER:
            case REDSTONE_WALL_TORCH:
            case REDSTONE_WIRE:
            case RED_WALL_BANNER:
            case SKELETON_WALL_SKULL:
            case TALL_SEAGRASS:
            case TRIPWIRE:
            case TUBE_CORAL_WALL_FAN:
            case VOID_AIR:
            case WALL_SIGN:
            case WALL_TORCH:
            case WATER:
            case WHITE_WALL_BANNER:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_WALL_BANNER:
            case ZOMBIE_WALL_HEAD:
            // ----- Legacy Separator -----
            case LEGACY_ACACIA_DOOR:
            case LEGACY_BED_BLOCK:
            case LEGACY_BEETROOT_BLOCK:
            case LEGACY_BIRCH_DOOR:
            case LEGACY_BREWING_STAND:
            case LEGACY_BURNING_FURNACE:
            case LEGACY_CAKE_BLOCK:
            case LEGACY_CARROT:
            case LEGACY_CAULDRON:
            case LEGACY_COCOA:
            case LEGACY_CROPS:
            case LEGACY_DARK_OAK_DOOR:
            case LEGACY_DAYLIGHT_DETECTOR_INVERTED:
            case LEGACY_DIODE_BLOCK_OFF:
            case LEGACY_DIODE_BLOCK_ON:
            case LEGACY_DOUBLE_STEP:
            case LEGACY_DOUBLE_STONE_SLAB2:
            case LEGACY_ENDER_PORTAL:
            case LEGACY_END_GATEWAY:
            case LEGACY_FIRE:
            case LEGACY_FLOWER_POT:
            case LEGACY_FROSTED_ICE:
            case LEGACY_GLOWING_REDSTONE_ORE:
            case LEGACY_IRON_DOOR_BLOCK:
            case LEGACY_JUNGLE_DOOR:
            case LEGACY_LAVA:
            case LEGACY_MELON_STEM:
            case LEGACY_NETHER_WARTS:
            case LEGACY_PISTON_EXTENSION:
            case LEGACY_PISTON_MOVING_PIECE:
            case LEGACY_PORTAL:
            case LEGACY_POTATO:
            case LEGACY_PUMPKIN_STEM:
            case LEGACY_PURPUR_DOUBLE_SLAB:
            case LEGACY_REDSTONE_COMPARATOR_OFF:
            case LEGACY_REDSTONE_COMPARATOR_ON:
            case LEGACY_REDSTONE_LAMP_ON:
            case LEGACY_REDSTONE_TORCH_OFF:
            case LEGACY_REDSTONE_WIRE:
            case LEGACY_SIGN_POST:
            case LEGACY_SKULL:
            case LEGACY_SPRUCE_DOOR:
            case LEGACY_STANDING_BANNER:
            case LEGACY_STATIONARY_LAVA:
            case LEGACY_STATIONARY_WATER:
            case LEGACY_SUGAR_CANE_BLOCK:
            case LEGACY_TRIPWIRE:
            case LEGACY_WALL_BANNER:
            case LEGACY_WALL_SIGN:
            case LEGACY_WATER:
            case LEGACY_WOODEN_DOOR:
            case LEGACY_WOOD_DOUBLE_STEP:
            //</editor-fold>
                return false;
            default:
                return true;
        }
    }

    /**
     * Checks if this Material can be interacted with.
     *
     * Interactable materials include those with functionality when they are
     * interacted with by a player such as chests, furnaces, etc.
     *
     * Some blocks such as piston heads and stairs are considered interactable
     * though may not perform any additional functionality.
     *
     * Note that the interactability of some materials may be dependant on their
     * state as well. This method will return true if there is at least one
     * state in which additional interact handling is performed for the
     * material.
     *
     * @return true if this material can be interacted with.
     */
    public boolean isInteractable() {
        switch (this) {
            // <editor-fold defaultstate="collapsed" desc="isInteractable">
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ANVIL:
            case BEACON:
            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BLACK_BED:
            case BLACK_SHULKER_BOX:
            case BLUE_BED:
            case BLUE_SHULKER_BOX:
            case BREWING_STAND:
            case BRICK_STAIRS:
            case BROWN_BED:
            case BROWN_SHULKER_BOX:
            case CAKE:
            case CAULDRON:
            case CHAIN_COMMAND_BLOCK:
            case CHEST:
            case CHIPPED_ANVIL:
            case COBBLESTONE_STAIRS:
            case COMMAND_BLOCK:
            case COMPARATOR:
            case CRAFTING_TABLE:
            case CYAN_BED:
            case CYAN_SHULKER_BOX:
            case DAMAGED_ANVIL:
            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_PRISMARINE_STAIRS:
            case DAYLIGHT_DETECTOR:
            case DISPENSER:
            case DRAGON_EGG:
            case DROPPER:
            case ENCHANTING_TABLE:
            case ENDER_CHEST:
            case FLOWER_POT:
            case FURNACE:
            case GRAY_BED:
            case GRAY_SHULKER_BOX:
            case GREEN_BED:
            case GREEN_SHULKER_BOX:
            case HOPPER:
            case IRON_DOOR:
            case IRON_TRAPDOOR:
            case JUKEBOX:
            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case LEVER:
            case LIGHT_BLUE_BED:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_BED:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_BED:
            case LIME_SHULKER_BOX:
            case MAGENTA_BED:
            case MAGENTA_SHULKER_BOX:
            case MOVING_PISTON:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_STAIRS:
            case NOTE_BLOCK:
            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case ORANGE_BED:
            case ORANGE_SHULKER_BOX:
            case PINK_BED:
            case PINK_SHULKER_BOX:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_STAIRS:
            case PUMPKIN:
            case PURPLE_BED:
            case PURPLE_SHULKER_BOX:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case REDSTONE_ORE:
            case RED_BED:
            case RED_SANDSTONE_STAIRS:
            case RED_SHULKER_BOX:
            case REPEATER:
            case REPEATING_COMMAND_BLOCK:
            case SANDSTONE_STAIRS:
            case SHULKER_BOX:
            case SIGN:
            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case STONE_BRICK_STAIRS:
            case STONE_BUTTON:
            case STRUCTURE_BLOCK:
            case TNT:
            case TRAPPED_CHEST:
            case WALL_SIGN:
            case WHITE_BED:
            case WHITE_SHULKER_BOX:
            case YELLOW_BED:
            case YELLOW_SHULKER_BOX:
                // </editor-fold>
                return true;
            default:
                return false;
        }
    }

    /**
     * Obtains the block's hardness level (also known as "strength").
     * <br>
     * This number is used to calculate the time required to break each block.
     * <br>
     * Only available when {@link #isBlock()} is true.
     *
     * @return the hardness of that material.
     */
    public float getHardness() {
        Validate.isTrue(isBlock(), "The Material is not a block!");
        switch (this) {
            // <editor-fold defaultstate="collapsed" desc="getBlockHardness">
            case BARRIER:
            case BEDROCK:
            case CHAIN_COMMAND_BLOCK:
            case COMMAND_BLOCK:
            case END_GATEWAY:
            case END_PORTAL:
            case END_PORTAL_FRAME:
            case MOVING_PISTON:
            case NETHER_PORTAL:
            case REPEATING_COMMAND_BLOCK:
            case STRUCTURE_BLOCK:
                return -1.0F;
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BROWN_CARPET:
            case CYAN_CARPET:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case ORANGE_CARPET:
            case PINK_CARPET:
            case PURPLE_CARPET:
            case RED_CARPET:
            case SNOW:
            case WHITE_CARPET:
            case YELLOW_CARPET:
                return 0.1F;
            case ACACIA_LEAVES:
            case BIRCH_LEAVES:
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case BROWN_MUSHROOM_BLOCK:
            case COCOA:
            case CYAN_BED:
            case DARK_OAK_LEAVES:
            case DAYLIGHT_DETECTOR:
            case GRAY_BED:
            case GREEN_BED:
            case JUNGLE_LEAVES:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case MUSHROOM_STEM:
            case OAK_LEAVES:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case RED_MUSHROOM_BLOCK:
            case SNOW_BLOCK:
            case SPRUCE_LEAVES:
            case VINE:
            case WHITE_BED:
            case YELLOW_BED:
                return 0.2F;
            case BLACK_STAINED_GLASS:
            case BLACK_STAINED_GLASS_PANE:
            case BLUE_STAINED_GLASS:
            case BLUE_STAINED_GLASS_PANE:
            case BROWN_STAINED_GLASS:
            case BROWN_STAINED_GLASS_PANE:
            case CYAN_STAINED_GLASS:
            case CYAN_STAINED_GLASS_PANE:
            case GLASS:
            case GLASS_PANE:
            case GLOWSTONE:
            case GRAY_STAINED_GLASS:
            case GRAY_STAINED_GLASS_PANE:
            case GREEN_STAINED_GLASS:
            case GREEN_STAINED_GLASS_PANE:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case LIME_STAINED_GLASS:
            case LIME_STAINED_GLASS_PANE:
            case MAGENTA_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS_PANE:
            case ORANGE_STAINED_GLASS:
            case ORANGE_STAINED_GLASS_PANE:
            case PINK_STAINED_GLASS:
            case PINK_STAINED_GLASS_PANE:
            case PURPLE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS_PANE:
            case REDSTONE_LAMP:
            case RED_STAINED_GLASS:
            case RED_STAINED_GLASS_PANE:
            case SEA_LANTERN:
            case WHITE_STAINED_GLASS:
            case WHITE_STAINED_GLASS_PANE:
            case YELLOW_STAINED_GLASS:
            case YELLOW_STAINED_GLASS_PANE:
                return 0.3F;
            case CACTUS:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
            case LADDER:
            case NETHERRACK:
                return 0.4F;
            case ACACIA_BUTTON:
            case ACACIA_PRESSURE_PLATE:
            case BIRCH_BUTTON:
            case BIRCH_PRESSURE_PLATE:
            case BLACK_CONCRETE_POWDER:
            case BLUE_CONCRETE_POWDER:
            case BREWING_STAND:
            case BROWN_CONCRETE_POWDER:
            case CAKE:
            case COARSE_DIRT:
            case CYAN_CONCRETE_POWDER:
            case DARK_OAK_BUTTON:
            case DARK_OAK_PRESSURE_PLATE:
            case DIRT:
            case DRIED_KELP_BLOCK:
            case FROSTED_ICE:
            case GRAY_CONCRETE_POWDER:
            case GREEN_CONCRETE_POWDER:
            case HAY_BLOCK:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case ICE:
            case JUNGLE_BUTTON:
            case JUNGLE_PRESSURE_PLATE:
            case LEVER:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case LIME_CONCRETE_POWDER:
            case MAGENTA_CONCRETE_POWDER:
            case MAGMA_BLOCK:
            case OAK_BUTTON:
            case OAK_PRESSURE_PLATE:
            case ORANGE_CONCRETE_POWDER:
            case PACKED_ICE:
            case PINK_CONCRETE_POWDER:
            case PISTON:
            case PISTON_HEAD:
            case PODZOL:
            case PURPLE_CONCRETE_POWDER:
            case RED_CONCRETE_POWDER:
            case RED_SAND:
            case SAND:
            case SOUL_SAND:
            case SPRUCE_BUTTON:
            case SPRUCE_PRESSURE_PLATE:
            case STICKY_PISTON:
            case STONE_BUTTON:
            case STONE_PRESSURE_PLATE:
            case TURTLE_EGG:
            case WHITE_CONCRETE_POWDER:
            case YELLOW_CONCRETE_POWDER:
                return 0.5F;
            case CLAY:
            case FARMLAND:
            case GRASS_BLOCK:
            case GRAVEL:
            case MYCELIUM:
            case SPONGE:
            case WET_SPONGE:
                return 0.6F;
            case GRASS_PATH:
                return 0.65F;
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case POWERED_RAIL:
            case RAIL:
                return 0.7F;
            case BLACK_WOOL:
            case BLUE_WOOL:
            case BROWN_WOOL:
            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CUT_RED_SANDSTONE:
            case CUT_SANDSTONE:
            case CYAN_WOOL:
            case END_STONE_BRICKS:
            case GRAY_WOOL:
            case GREEN_WOOL:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_WOOL:
            case LIME_WOOL:
            case MAGENTA_WOOL:
            case NOTE_BLOCK:
            case ORANGE_WOOL:
            case PINK_WOOL:
            case PURPLE_WOOL:
            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE:
            case RED_SANDSTONE_STAIRS:
            case RED_WOOL:
            case SANDSTONE:
            case SANDSTONE_STAIRS:
            case WHITE_WOOL:
            case YELLOW_WOOL:
                return 0.8F;
            case BLACK_BANNER:
            case BLACK_WALL_BANNER:
            case BLUE_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_BANNER:
            case BROWN_WALL_BANNER:
            case CARVED_PUMPKIN:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case CYAN_BANNER:
            case CYAN_WALL_BANNER:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case GRAY_BANNER:
            case GRAY_WALL_BANNER:
            case GREEN_BANNER:
            case GREEN_WALL_BANNER:
            case JACK_O_LANTERN:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_BANNER:
            case MAGENTA_WALL_BANNER:
            case MELON:
            case NETHER_WART_BLOCK:
            case ORANGE_BANNER:
            case ORANGE_WALL_BANNER:
            case PINK_BANNER:
            case PINK_WALL_BANNER:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case PUMPKIN:
            case PURPLE_BANNER:
            case PURPLE_WALL_BANNER:
            case RED_BANNER:
            case RED_WALL_BANNER:
            case SIGN:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WALL_SIGN:
            case WHITE_BANNER:
            case WHITE_WALL_BANNER:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_BANNER:
            case YELLOW_WALL_BANNER:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return 1.0F;
            case BLACK_TERRACOTTA:
            case BLUE_TERRACOTTA:
            case BROWN_TERRACOTTA:
            case CYAN_TERRACOTTA:
            case GRAY_TERRACOTTA:
            case GREEN_TERRACOTTA:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_GRAY_TERRACOTTA:
            case LIME_TERRACOTTA:
            case MAGENTA_TERRACOTTA:
            case ORANGE_TERRACOTTA:
            case PINK_TERRACOTTA:
            case PURPLE_TERRACOTTA:
            case RED_TERRACOTTA:
            case TERRACOTTA:
            case WHITE_TERRACOTTA:
            case YELLOW_TERRACOTTA:
                return 1.25F;
            case BLACK_GLAZED_TERRACOTTA:
            case BLUE_GLAZED_TERRACOTTA:
            case BROWN_GLAZED_TERRACOTTA:
            case CYAN_GLAZED_TERRACOTTA:
            case GRAY_GLAZED_TERRACOTTA:
            case GREEN_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIME_GLAZED_TERRACOTTA:
            case MAGENTA_GLAZED_TERRACOTTA:
            case ORANGE_GLAZED_TERRACOTTA:
            case PINK_GLAZED_TERRACOTTA:
            case PURPLE_GLAZED_TERRACOTTA:
            case RED_GLAZED_TERRACOTTA:
            case WHITE_GLAZED_TERRACOTTA:
            case YELLOW_GLAZED_TERRACOTTA:
                return 1.4F;
            case ANDESITE:
            case BOOKSHELF:
            case BRAIN_CORAL_BLOCK:
            case BUBBLE_CORAL_BLOCK:
            case CHISELED_STONE_BRICKS:
            case CRACKED_STONE_BRICKS:
            case DARK_PRISMARINE:
            case DARK_PRISMARINE_SLAB:
            case DARK_PRISMARINE_STAIRS:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_HORN_CORAL_BLOCK:
            case DEAD_TUBE_CORAL_BLOCK:
            case DIORITE:
            case FIRE_CORAL_BLOCK:
            case GRANITE:
            case HORN_CORAL_BLOCK:
            case MOSSY_STONE_BRICKS:
            case POLISHED_ANDESITE:
            case POLISHED_DIORITE:
            case POLISHED_GRANITE:
            case PRISMARINE:
            case PRISMARINE_BRICKS:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_SLAB:
            case PRISMARINE_STAIRS:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_STAIRS:
            case STONE:
            case STONE_BRICKS:
            case STONE_BRICK_STAIRS:
            case TUBE_CORAL_BLOCK:
                return 1.5F;
            case BLACK_CONCRETE:
            case BLUE_CONCRETE:
            case BROWN_CONCRETE:
            case CYAN_CONCRETE:
            case GRAY_CONCRETE:
            case GREEN_CONCRETE:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_GRAY_CONCRETE:
            case LIME_CONCRETE:
            case MAGENTA_CONCRETE:
            case ORANGE_CONCRETE:
            case PINK_CONCRETE:
            case PURPLE_CONCRETE:
            case RED_CONCRETE:
            case WHITE_CONCRETE:
            case YELLOW_CONCRETE:
                return 1.8F;
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_WOOD:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_WOOD:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BONE_BLOCK:
            case BRICKS:
            case BRICK_SLAB:
            case BRICK_STAIRS:
            case BROWN_SHULKER_BOX:
            case CAULDRON:
            case COBBLESTONE:
            case COBBLESTONE_SLAB:
            case COBBLESTONE_STAIRS:
            case COBBLESTONE_WALL:
            case CYAN_SHULKER_BOX:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_WOOD:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case JUKEBOX:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_WOOD:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case MOSSY_COBBLESTONE:
            case MOSSY_COBBLESTONE_WALL:
            case NETHER_BRICKS:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_SLAB:
            case NETHER_BRICK_STAIRS:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_WOOD:
            case ORANGE_SHULKER_BOX:
            case PETRIFIED_OAK_SLAB:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case PURPUR_SLAB:
            case QUARTZ_SLAB:
            case RED_NETHER_BRICKS:
            case RED_SANDSTONE_SLAB:
            case RED_SHULKER_BOX:
            case SANDSTONE_SLAB:
            case SHULKER_BOX:
            case SMOOTH_QUARTZ:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_SANDSTONE:
            case SMOOTH_STONE:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_WOOD:
            case STONE_BRICK_SLAB:
            case STONE_SLAB:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                return 2.0F;
            case CHEST:
            case CRAFTING_TABLE:
            case TRAPPED_CHEST:
                return 2.5F;
            case BLUE_ICE:
                return 2.8F;
            case ACACIA_DOOR:
            case ACACIA_TRAPDOOR:
            case BEACON:
            case BIRCH_DOOR:
            case BIRCH_TRAPDOOR:
            case COAL_ORE:
            case CONDUIT:
            case DARK_OAK_DOOR:
            case DARK_OAK_TRAPDOOR:
            case DIAMOND_ORE:
            case DRAGON_EGG:
            case EMERALD_ORE:
            case END_STONE:
            case GOLD_BLOCK:
            case GOLD_ORE:
            case HOPPER:
            case IRON_ORE:
            case JUNGLE_DOOR:
            case JUNGLE_TRAPDOOR:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
            case NETHER_QUARTZ_ORE:
            case OAK_DOOR:
            case OAK_TRAPDOOR:
            case OBSERVER:
            case REDSTONE_ORE:
            case SPRUCE_DOOR:
            case SPRUCE_TRAPDOOR:
                return 3.0F;
            case DISPENSER:
            case DROPPER:
            case FURNACE:
                return 3.5F;
            case COBWEB:
                return 4.0F;
            case ANVIL:
            case CHIPPED_ANVIL:
            case COAL_BLOCK:
            case DAMAGED_ANVIL:
            case DIAMOND_BLOCK:
            case EMERALD_BLOCK:
            case ENCHANTING_TABLE:
            case IRON_BARS:
            case IRON_BLOCK:
            case IRON_DOOR:
            case IRON_TRAPDOOR:
            case REDSTONE_BLOCK:
            case SPAWNER:
                return 5.0F;
            case ENDER_CHEST:
                return 22.5F;
            case OBSIDIAN:
                return 50.0F;
            case LAVA:
            case WATER:
                return 100.0F;
            default:
                return 0F;
            // </editor-fold>
        }
    }

    /**
     * Obtains the blast resistance value (also known as block "durability").
     * <br>
     * This value is used in explosions to calculate whether a block should be
     * broken or not.
     * <br>
     * Only available when {@link #isBlock()} is true.
     *
     * @return the blast resistance of that material.
     */
    public float getBlastResistance() {
        Validate.isTrue(isBlock(), "The Material is not a block!");
        switch (this) {
            // <editor-fold defaultstate="collapsed" desc="getBlastResistance">
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BROWN_CARPET:
            case CYAN_CARPET:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case ORANGE_CARPET:
            case PINK_CARPET:
            case PURPLE_CARPET:
            case RED_CARPET:
            case SNOW:
            case WHITE_CARPET:
            case YELLOW_CARPET:
                return 0.1F;
            case ACACIA_LEAVES:
            case BIRCH_LEAVES:
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case BROWN_MUSHROOM_BLOCK:
            case CYAN_BED:
            case DARK_OAK_LEAVES:
            case DAYLIGHT_DETECTOR:
            case GRAY_BED:
            case GREEN_BED:
            case JUNGLE_LEAVES:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case MUSHROOM_STEM:
            case OAK_LEAVES:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case RED_MUSHROOM_BLOCK:
            case SNOW_BLOCK:
            case SPRUCE_LEAVES:
            case VINE:
            case WHITE_BED:
            case YELLOW_BED:
                return 0.2F;
            case BLACK_STAINED_GLASS:
            case BLACK_STAINED_GLASS_PANE:
            case BLUE_STAINED_GLASS:
            case BLUE_STAINED_GLASS_PANE:
            case BROWN_STAINED_GLASS:
            case BROWN_STAINED_GLASS_PANE:
            case CYAN_STAINED_GLASS:
            case CYAN_STAINED_GLASS_PANE:
            case GLASS:
            case GLASS_PANE:
            case GLOWSTONE:
            case GRAY_STAINED_GLASS:
            case GRAY_STAINED_GLASS_PANE:
            case GREEN_STAINED_GLASS:
            case GREEN_STAINED_GLASS_PANE:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case LIME_STAINED_GLASS:
            case LIME_STAINED_GLASS_PANE:
            case MAGENTA_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS_PANE:
            case ORANGE_STAINED_GLASS:
            case ORANGE_STAINED_GLASS_PANE:
            case PINK_STAINED_GLASS:
            case PINK_STAINED_GLASS_PANE:
            case PURPLE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS_PANE:
            case REDSTONE_LAMP:
            case RED_STAINED_GLASS:
            case RED_STAINED_GLASS_PANE:
            case SEA_LANTERN:
            case WHITE_STAINED_GLASS:
            case WHITE_STAINED_GLASS_PANE:
            case YELLOW_STAINED_GLASS:
            case YELLOW_STAINED_GLASS_PANE:
                return 0.3F;
            case CACTUS:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
            case LADDER:
            case NETHERRACK:
                return 0.4F;
            case ACACIA_BUTTON:
            case ACACIA_PRESSURE_PLATE:
            case BIRCH_BUTTON:
            case BIRCH_PRESSURE_PLATE:
            case BLACK_CONCRETE_POWDER:
            case BLUE_CONCRETE_POWDER:
            case BREWING_STAND:
            case BROWN_CONCRETE_POWDER:
            case CAKE:
            case COARSE_DIRT:
            case CYAN_CONCRETE_POWDER:
            case DARK_OAK_BUTTON:
            case DARK_OAK_PRESSURE_PLATE:
            case DIRT:
            case FROSTED_ICE:
            case GRAY_CONCRETE_POWDER:
            case GREEN_CONCRETE_POWDER:
            case HAY_BLOCK:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case ICE:
            case JUNGLE_BUTTON:
            case JUNGLE_PRESSURE_PLATE:
            case LEVER:
            case LIGHT_BLUE_CONCRETE_POWDER:
            case LIGHT_GRAY_CONCRETE_POWDER:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case LIME_CONCRETE_POWDER:
            case MAGENTA_CONCRETE_POWDER:
            case MAGMA_BLOCK:
            case OAK_BUTTON:
            case OAK_PRESSURE_PLATE:
            case ORANGE_CONCRETE_POWDER:
            case PACKED_ICE:
            case PINK_CONCRETE_POWDER:
            case PISTON:
            case PISTON_HEAD:
            case PODZOL:
            case PURPLE_CONCRETE_POWDER:
            case RED_CONCRETE_POWDER:
            case RED_SAND:
            case SAND:
            case SOUL_SAND:
            case SPRUCE_BUTTON:
            case SPRUCE_PRESSURE_PLATE:
            case STICKY_PISTON:
            case STONE_BUTTON:
            case STONE_PRESSURE_PLATE:
            case TURTLE_EGG:
            case WHITE_CONCRETE_POWDER:
            case YELLOW_CONCRETE_POWDER:
                return 0.5F;
            case CLAY:
            case FARMLAND:
            case GRASS_BLOCK:
            case GRAVEL:
            case MYCELIUM:
            case SPONGE:
            case WET_SPONGE:
                return 0.6F;
            case GRASS_PATH:
                return 0.65F;
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case POWERED_RAIL:
            case RAIL:
                return 0.7F;
            case INFESTED_CHISELED_STONE_BRICKS:
            case INFESTED_COBBLESTONE:
            case INFESTED_CRACKED_STONE_BRICKS:
            case INFESTED_MOSSY_STONE_BRICKS:
            case INFESTED_STONE:
            case INFESTED_STONE_BRICKS:
                return 0.75F;
            case BLACK_WOOL:
            case BLUE_WOOL:
            case BROWN_WOOL:
            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CUT_RED_SANDSTONE:
            case CUT_SANDSTONE:
            case CYAN_WOOL:
            case END_STONE_BRICKS:
            case GRAY_WOOL:
            case GREEN_WOOL:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_WOOL:
            case LIME_WOOL:
            case MAGENTA_WOOL:
            case NOTE_BLOCK:
            case ORANGE_WOOL:
            case PINK_WOOL:
            case PURPLE_WOOL:
            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE:
            case RED_SANDSTONE_STAIRS:
            case RED_WOOL:
            case SANDSTONE:
            case SANDSTONE_STAIRS:
            case WHITE_WOOL:
            case YELLOW_WOOL:
                return 0.8F;
            case BLACK_BANNER:
            case BLACK_WALL_BANNER:
            case BLUE_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_BANNER:
            case BROWN_WALL_BANNER:
            case CARVED_PUMPKIN:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case CYAN_BANNER:
            case CYAN_WALL_BANNER:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case GRAY_BANNER:
            case GRAY_WALL_BANNER:
            case GREEN_BANNER:
            case GREEN_WALL_BANNER:
            case JACK_O_LANTERN:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_BANNER:
            case MAGENTA_WALL_BANNER:
            case MELON:
            case NETHER_WART_BLOCK:
            case ORANGE_BANNER:
            case ORANGE_WALL_BANNER:
            case PINK_BANNER:
            case PINK_WALL_BANNER:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case PUMPKIN:
            case PURPLE_BANNER:
            case PURPLE_WALL_BANNER:
            case RED_BANNER:
            case RED_WALL_BANNER:
            case SIGN:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WALL_SIGN:
            case WHITE_BANNER:
            case WHITE_WALL_BANNER:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_BANNER:
            case YELLOW_WALL_BANNER:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return 1.0F;
            case BLACK_GLAZED_TERRACOTTA:
            case BLUE_GLAZED_TERRACOTTA:
            case BROWN_GLAZED_TERRACOTTA:
            case CYAN_GLAZED_TERRACOTTA:
            case GRAY_GLAZED_TERRACOTTA:
            case GREEN_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case LIGHT_GRAY_GLAZED_TERRACOTTA:
            case LIME_GLAZED_TERRACOTTA:
            case MAGENTA_GLAZED_TERRACOTTA:
            case ORANGE_GLAZED_TERRACOTTA:
            case PINK_GLAZED_TERRACOTTA:
            case PURPLE_GLAZED_TERRACOTTA:
            case RED_GLAZED_TERRACOTTA:
            case WHITE_GLAZED_TERRACOTTA:
            case YELLOW_GLAZED_TERRACOTTA:
                return 1.4F;
            case BOOKSHELF:
                return 1.5F;
            case BLACK_CONCRETE:
            case BLUE_CONCRETE:
            case BROWN_CONCRETE:
            case CYAN_CONCRETE:
            case GRAY_CONCRETE:
            case GREEN_CONCRETE:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_GRAY_CONCRETE:
            case LIME_CONCRETE:
            case MAGENTA_CONCRETE:
            case ORANGE_CONCRETE:
            case PINK_CONCRETE:
            case PURPLE_CONCRETE:
            case RED_CONCRETE:
            case WHITE_CONCRETE:
            case YELLOW_CONCRETE:
                return 1.8F;
            case ACACIA_LOG:
            case ACACIA_WOOD:
            case BIRCH_LOG:
            case BIRCH_WOOD:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BONE_BLOCK:
            case BROWN_SHULKER_BOX:
            case CAULDRON:
            case CYAN_SHULKER_BOX:
            case DARK_OAK_LOG:
            case DARK_OAK_WOOD:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case JUNGLE_LOG:
            case JUNGLE_WOOD:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case OAK_LOG:
            case OAK_WOOD:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case SHULKER_BOX:
            case SPRUCE_LOG:
            case SPRUCE_WOOD:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                return 2.0F;
            case CHEST:
            case CRAFTING_TABLE:
            case DRIED_KELP_BLOCK:
            case TRAPPED_CHEST:
                return 2.5F;
            case BLUE_ICE:
                return 2.8F;
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_PLANKS:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case BEACON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_PLANKS:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case COAL_ORE:
            case COCOA:
            case CONDUIT:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_PLANKS:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case GOLD_ORE:
            case IRON_ORE:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_PLANKS:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
            case NETHER_QUARTZ_ORE:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_PLANKS:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OBSERVER:
            case REDSTONE_ORE:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_PLANKS:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
                return 3.0F;
            case DISPENSER:
            case DROPPER:
            case FURNACE:
                return 3.5F;
            case COBWEB:
                return 4.0F;
            case BLACK_TERRACOTTA:
            case BLUE_TERRACOTTA:
            case BROWN_TERRACOTTA:
            case CYAN_TERRACOTTA:
            case GRAY_TERRACOTTA:
            case GREEN_TERRACOTTA:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_GRAY_TERRACOTTA:
            case LIME_TERRACOTTA:
            case MAGENTA_TERRACOTTA:
            case ORANGE_TERRACOTTA:
            case PINK_TERRACOTTA:
            case PURPLE_TERRACOTTA:
            case RED_TERRACOTTA:
            case TERRACOTTA:
            case WHITE_TERRACOTTA:
            case YELLOW_TERRACOTTA:
                return 4.2F;
            case HOPPER:
                return 4.8F;
            case IRON_DOOR:
            case IRON_TRAPDOOR:
            case SPAWNER:
                return 5.0F;
            case ANDESITE:
            case BRAIN_CORAL_BLOCK:
            case BRICKS:
            case BRICK_SLAB:
            case BRICK_STAIRS:
            case BUBBLE_CORAL_BLOCK:
            case CHISELED_STONE_BRICKS:
            case COAL_BLOCK:
            case COBBLESTONE:
            case COBBLESTONE_SLAB:
            case COBBLESTONE_STAIRS:
            case COBBLESTONE_WALL:
            case CRACKED_STONE_BRICKS:
            case DARK_PRISMARINE:
            case DARK_PRISMARINE_SLAB:
            case DARK_PRISMARINE_STAIRS:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_HORN_CORAL_BLOCK:
            case DEAD_TUBE_CORAL_BLOCK:
            case DIAMOND_BLOCK:
            case DIORITE:
            case EMERALD_BLOCK:
            case FIRE_CORAL_BLOCK:
            case GOLD_BLOCK:
            case GRANITE:
            case HORN_CORAL_BLOCK:
            case IRON_BARS:
            case IRON_BLOCK:
            case JUKEBOX:
            case MOSSY_COBBLESTONE:
            case MOSSY_COBBLESTONE_WALL:
            case MOSSY_STONE_BRICKS:
            case NETHER_BRICKS:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_SLAB:
            case NETHER_BRICK_STAIRS:
            case PETRIFIED_OAK_SLAB:
            case POLISHED_ANDESITE:
            case POLISHED_DIORITE:
            case POLISHED_GRANITE:
            case PRISMARINE:
            case PRISMARINE_BRICKS:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_SLAB:
            case PRISMARINE_STAIRS:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_SLAB:
            case PURPUR_STAIRS:
            case QUARTZ_SLAB:
            case REDSTONE_BLOCK:
            case RED_NETHER_BRICKS:
            case RED_SANDSTONE_SLAB:
            case SANDSTONE_SLAB:
            case SMOOTH_QUARTZ:
            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_SANDSTONE:
            case SMOOTH_STONE:
            case STONE:
            case STONE_BRICKS:
            case STONE_BRICK_SLAB:
            case STONE_BRICK_STAIRS:
            case STONE_SLAB:
            case TUBE_CORAL_BLOCK:
                return 6.0F;
            case DRAGON_EGG:
            case END_STONE:
                return 9.0F;
            case LAVA:
            case WATER:
                return 100.0F;
            case ENDER_CHEST:
                return 600.0F;
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:
            case ENCHANTING_TABLE:
            case OBSIDIAN:
                return 1200.0F;
            case BEDROCK:
            case CHAIN_COMMAND_BLOCK:
            case COMMAND_BLOCK:
            case END_GATEWAY:
            case END_PORTAL:
            case END_PORTAL_FRAME:
            case REPEATING_COMMAND_BLOCK:
            case STRUCTURE_BLOCK:
                return 3600000.0F;
            case BARRIER:
                return 3600000.8F;
            default:
                return 0;
            // </editor-fold>
        }
    }
}
