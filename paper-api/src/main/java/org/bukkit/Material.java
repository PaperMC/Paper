package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Brushable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Hatchable;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.AmethystCluster;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.CalibratedSculkSensor;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.CaveVines;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.Chain;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.ChiseledBookshelf;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CopperBulb;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.Crafter;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.DecoratedPot;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Dripleaf;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Fire;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.block.data.type.GlowLichen;
import org.bukkit.block.data.type.Grindstone;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.Lectern;
import org.bukkit.block.data.type.Light;
import org.bukkit.block.data.type.LightningRod;
import org.bukkit.block.data.type.MangrovePropagule;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Observer;
import org.bukkit.block.data.type.PinkPetals;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.PitcherCrop;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.block.data.type.SculkCatalyst;
import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.block.data.type.SculkVein;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.SmallDripleaf;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.block.data.type.TripwireHook;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.block.data.type.Vault;
import org.bukkit.block.data.type.Wall;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An enum of all material IDs accepted by the official server and client
 */
public enum Material implements Keyed, Translatable {
    //<editor-fold desc="Materials" defaultstate="collapsed">
    AIR(9648, 0),
    STONE(22948),
    GRANITE(21091),
    POLISHED_GRANITE(5477),
    DIORITE(24688),
    POLISHED_DIORITE(31615),
    ANDESITE(25975),
    POLISHED_ANDESITE(8335),
    /**
     * BlockData: {@link Orientable}
     */
    DEEPSLATE(26842, Orientable.class),
    COBBLED_DEEPSLATE(8021),
    POLISHED_DEEPSLATE(31772),
    CALCITE(20311),
    TUFF(24364),
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_SLAB(19305, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_STAIRS(11268, Stairs.class),
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_WALL(24395, Wall.class),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    CHISELED_TUFF(15831),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    POLISHED_TUFF(17801),
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    POLISHED_TUFF_SLAB(31096, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    POLISHED_TUFF_STAIRS(7964, Stairs.class),
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    POLISHED_TUFF_WALL(28886, Wall.class),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_BRICKS(26276),
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_BRICK_SLAB(11843, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_BRICK_STAIRS(30753, Stairs.class),
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TUFF_BRICK_WALL(11761, Wall.class),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    CHISELED_TUFF_BRICKS(8601),
    DRIPSTONE_BLOCK(26227),
    /**
     * BlockData: {@link Snowable}
     */
    GRASS_BLOCK(28346, Snowable.class),
    DIRT(10580),
    COARSE_DIRT(15411),
    /**
     * BlockData: {@link Snowable}
     */
    PODZOL(24068, Snowable.class),
    ROOTED_DIRT(11410),
    MUD(32418),
    CRIMSON_NYLIUM(18139),
    WARPED_NYLIUM(26396),
    COBBLESTONE(32147),
    OAK_PLANKS(14905),
    SPRUCE_PLANKS(14593),
    BIRCH_PLANKS(29322),
    JUNGLE_PLANKS(26445),
    ACACIA_PLANKS(31312),
    CHERRY_PLANKS(8354),
    DARK_OAK_PLANKS(20869),
    MANGROVE_PLANKS(7078),
    BAMBOO_PLANKS(8520),
    CRIMSON_PLANKS(18812),
    WARPED_PLANKS(16045),
    BAMBOO_MOSAIC(10715),
    /**
     * BlockData: {@link Sapling}
     */
    OAK_SAPLING(9636, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    SPRUCE_SAPLING(19874, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    BIRCH_SAPLING(31533, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    JUNGLE_SAPLING(17951, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    ACACIA_SAPLING(20806, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    CHERRY_SAPLING(25204, Sapling.class),
    /**
     * BlockData: {@link Sapling}
     */
    DARK_OAK_SAPLING(14933, Sapling.class),
    /**
     * BlockData: {@link MangrovePropagule}
     */
    MANGROVE_PROPAGULE(18688, MangrovePropagule.class),
    BEDROCK(23130),
    SAND(11542),
    /**
     * BlockData: {@link Brushable}
     */
    SUSPICIOUS_SAND(18410, Brushable.class),
    /**
     * BlockData: {@link Brushable}
     */
    SUSPICIOUS_GRAVEL(7353, Brushable.class),
    RED_SAND(16279),
    GRAVEL(7804),
    COAL_ORE(30965),
    DEEPSLATE_COAL_ORE(16823),
    IRON_ORE(19834),
    DEEPSLATE_IRON_ORE(26021),
    COPPER_ORE(32666),
    DEEPSLATE_COPPER_ORE(6588),
    GOLD_ORE(32625),
    DEEPSLATE_GOLD_ORE(13582),
    /**
     * BlockData: {@link Lightable}
     */
    REDSTONE_ORE(10887, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    DEEPSLATE_REDSTONE_ORE(6331, Lightable.class),
    EMERALD_ORE(16630),
    DEEPSLATE_EMERALD_ORE(5299),
    LAPIS_ORE(22934),
    DEEPSLATE_LAPIS_ORE(13598),
    DIAMOND_ORE(9292),
    DEEPSLATE_DIAMOND_ORE(17792),
    NETHER_GOLD_ORE(4185),
    NETHER_QUARTZ_ORE(4807),
    ANCIENT_DEBRIS(18198),
    COAL_BLOCK(27968),
    RAW_IRON_BLOCK(32210),
    RAW_COPPER_BLOCK(17504),
    RAW_GOLD_BLOCK(23246),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    HEAVY_CORE(15788, Waterlogged.class),
    AMETHYST_BLOCK(18919),
    BUDDING_AMETHYST(13963),
    IRON_BLOCK(24754),
    COPPER_BLOCK(12880),
    GOLD_BLOCK(27392),
    DIAMOND_BLOCK(5944),
    NETHERITE_BLOCK(6527),
    EXPOSED_COPPER(28488),
    WEATHERED_COPPER(19699),
    OXIDIZED_COPPER(19490),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    CHISELED_COPPER(12143),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    EXPOSED_CHISELED_COPPER(4570),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WEATHERED_CHISELED_COPPER(30876),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OXIDIZED_CHISELED_COPPER(27719),
    CUT_COPPER(32519),
    EXPOSED_CUT_COPPER(18000),
    WEATHERED_CUT_COPPER(21158),
    OXIDIZED_CUT_COPPER(5382),
    /**
     * BlockData: {@link Stairs}
     */
    CUT_COPPER_STAIRS(25925, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    EXPOSED_CUT_COPPER_STAIRS(31621, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    WEATHERED_CUT_COPPER_STAIRS(5851, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    OXIDIZED_CUT_COPPER_STAIRS(25379, Stairs.class),
    /**
     * BlockData: {@link Slab}
     */
    CUT_COPPER_SLAB(28988, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    EXPOSED_CUT_COPPER_SLAB(26694, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    WEATHERED_CUT_COPPER_SLAB(4602, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    OXIDIZED_CUT_COPPER_SLAB(29642, Slab.class),
    WAXED_COPPER_BLOCK(14638),
    WAXED_EXPOSED_COPPER(27989),
    WAXED_WEATHERED_COPPER(5960),
    WAXED_OXIDIZED_COPPER(25626),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_CHISELED_COPPER(7500),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_EXPOSED_CHISELED_COPPER(30658),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_WEATHERED_CHISELED_COPPER(5970),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_OXIDIZED_CHISELED_COPPER(7735),
    WAXED_CUT_COPPER(11030),
    WAXED_EXPOSED_CUT_COPPER(30043),
    WAXED_WEATHERED_CUT_COPPER(13823),
    WAXED_OXIDIZED_CUT_COPPER(22582),
    /**
     * BlockData: {@link Stairs}
     */
    WAXED_CUT_COPPER_STAIRS(23125, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    WAXED_EXPOSED_CUT_COPPER_STAIRS(15532, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    WAXED_WEATHERED_CUT_COPPER_STAIRS(29701, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    WAXED_OXIDIZED_CUT_COPPER_STAIRS(9842, Stairs.class),
    /**
     * BlockData: {@link Slab}
     */
    WAXED_CUT_COPPER_SLAB(6271, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    WAXED_EXPOSED_CUT_COPPER_SLAB(22091, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    WAXED_WEATHERED_CUT_COPPER_SLAB(20035, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    WAXED_OXIDIZED_CUT_COPPER_SLAB(11202, Slab.class),
    /**
     * BlockData: {@link Orientable}
     */
    OAK_LOG(26723, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    SPRUCE_LOG(9726, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    BIRCH_LOG(26727, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    JUNGLE_LOG(20721, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    ACACIA_LOG(8385, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    CHERRY_LOG(20847, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    DARK_OAK_LOG(14831, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    MANGROVE_LOG(23890, Orientable.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    MANGROVE_ROOTS(22124, Waterlogged.class),
    /**
     * BlockData: {@link Orientable}
     */
    MUDDY_MANGROVE_ROOTS(23244, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    CRIMSON_STEM(27920, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    WARPED_STEM(28920, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    BAMBOO_BLOCK(20770, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_OAK_LOG(20523, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_SPRUCE_LOG(6140, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_BIRCH_LOG(8838, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_JUNGLE_LOG(15476, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_ACACIA_LOG(18167, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_CHERRY_LOG(18061, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_DARK_OAK_LOG(6492, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_MANGROVE_LOG(15197, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_CRIMSON_STEM(16882, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_WARPED_STEM(15627, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_OAK_WOOD(31455, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_SPRUCE_WOOD(6467, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_BIRCH_WOOD(22350, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_JUNGLE_WOOD(30315, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_ACACIA_WOOD(27193, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_CHERRY_WOOD(19647, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_DARK_OAK_WOOD(16000, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_MANGROVE_WOOD(4828, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_CRIMSON_HYPHAE(27488, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_WARPED_HYPHAE(7422, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    STRIPPED_BAMBOO_BLOCK(14799, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    OAK_WOOD(7378, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    SPRUCE_WOOD(32328, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    BIRCH_WOOD(20913, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    JUNGLE_WOOD(10341, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    ACACIA_WOOD(9541, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    CHERRY_WOOD(9826, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    DARK_OAK_WOOD(16995, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    MANGROVE_WOOD(25484, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    CRIMSON_HYPHAE(6550, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    WARPED_HYPHAE(18439, Orientable.class),
    /**
     * BlockData: {@link Leaves}
     */
    OAK_LEAVES(4385, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    SPRUCE_LEAVES(20039, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    BIRCH_LEAVES(12601, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    JUNGLE_LEAVES(5133, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    ACACIA_LEAVES(16606, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    CHERRY_LEAVES(20856, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    DARK_OAK_LEAVES(22254, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    MANGROVE_LEAVES(15310, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    AZALEA_LEAVES(23001, Leaves.class),
    /**
     * BlockData: {@link Leaves}
     */
    FLOWERING_AZALEA_LEAVES(7139, Leaves.class),
    SPONGE(15860),
    WET_SPONGE(9043),
    GLASS(6195),
    TINTED_GLASS(19154),
    LAPIS_BLOCK(14485),
    SANDSTONE(13141),
    CHISELED_SANDSTONE(31763),
    CUT_SANDSTONE(6118),
    COBWEB(9469),
    SHORT_GRASS(16335),
    FERN(15794),
    AZALEA(29386),
    FLOWERING_AZALEA(28270),
    DEAD_BUSH(22888),
    SEAGRASS(23942),
    /**
     * BlockData: {@link SeaPickle}
     */
    SEA_PICKLE(19562, SeaPickle.class),
    WHITE_WOOL(8624),
    ORANGE_WOOL(23957),
    MAGENTA_WOOL(11853),
    LIGHT_BLUE_WOOL(21073),
    YELLOW_WOOL(29507),
    LIME_WOOL(10443),
    PINK_WOOL(7611),
    GRAY_WOOL(27209),
    LIGHT_GRAY_WOOL(22936),
    CYAN_WOOL(12221),
    PURPLE_WOOL(11922),
    BLUE_WOOL(15738),
    BROWN_WOOL(32638),
    GREEN_WOOL(25085),
    RED_WOOL(11621),
    BLACK_WOOL(16693),
    DANDELION(30558),
    POPPY(12851),
    BLUE_ORCHID(13432),
    ALLIUM(6871),
    AZURE_BLUET(17608),
    RED_TULIP(16781),
    ORANGE_TULIP(26038),
    WHITE_TULIP(31495),
    PINK_TULIP(27319),
    OXEYE_DAISY(11709),
    CORNFLOWER(15405),
    LILY_OF_THE_VALLEY(7185),
    WITHER_ROSE(8619),
    TORCHFLOWER(4501),
    /**
     * BlockData: {@link Bisected}
     */
    PITCHER_PLANT(28172, Bisected.class),
    SPORE_BLOSSOM(20627),
    BROWN_MUSHROOM(9665),
    RED_MUSHROOM(19728),
    CRIMSON_FUNGUS(26268),
    WARPED_FUNGUS(19799),
    CRIMSON_ROOTS(14064),
    WARPED_ROOTS(13932),
    NETHER_SPROUTS(10431),
    /**
     * BlockData: {@link Ageable}
     */
    WEEPING_VINES(29267, Ageable.class),
    /**
     * BlockData: {@link Ageable}
     */
    TWISTING_VINES(27283, Ageable.class),
    /**
     * BlockData: {@link Ageable}
     */
    SUGAR_CANE(7726, Ageable.class),
    /**
     * BlockData: {@link Ageable}
     */
    KELP(21916, Ageable.class),
    MOSS_CARPET(8221),
    /**
     * BlockData: {@link PinkPetals}
     */
    PINK_PETALS(10420, PinkPetals.class),
    MOSS_BLOCK(9175),
    /**
     * BlockData: {@link Waterlogged}
     */
    HANGING_ROOTS(15498, Waterlogged.class),
    /**
     * BlockData: {@link BigDripleaf}
     */
    BIG_DRIPLEAF(26173, BigDripleaf.class),
    /**
     * BlockData: {@link SmallDripleaf}
     */
    SMALL_DRIPLEAF(17540, SmallDripleaf.class),
    /**
     * BlockData: {@link Bamboo}
     */
    BAMBOO(18728, Bamboo.class),
    /**
     * BlockData: {@link Slab}
     */
    OAK_SLAB(12002, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SPRUCE_SLAB(28798, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    BIRCH_SLAB(13807, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    JUNGLE_SLAB(19117, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    ACACIA_SLAB(23730, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    CHERRY_SLAB(16673, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    DARK_OAK_SLAB(28852, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    MANGROVE_SLAB(13704, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    BAMBOO_SLAB(17798, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    BAMBOO_MOSAIC_SLAB(22118, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    CRIMSON_SLAB(4691, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    WARPED_SLAB(27150, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    STONE_SLAB(19838, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SMOOTH_STONE_SLAB(24129, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SANDSTONE_SLAB(29830, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    CUT_SANDSTONE_SLAB(30944, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    PETRIFIED_OAK_SLAB(18658, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    COBBLESTONE_SLAB(6340, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    BRICK_SLAB(26333, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    STONE_BRICK_SLAB(19676, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    MUD_BRICK_SLAB(10611, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    NETHER_BRICK_SLAB(26586, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    QUARTZ_SLAB(4423, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    RED_SANDSTONE_SLAB(17550, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    CUT_RED_SANDSTONE_SLAB(7220, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    PURPUR_SLAB(11487, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    PRISMARINE_SLAB(31323, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    PRISMARINE_BRICK_SLAB(25624, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    DARK_PRISMARINE_SLAB(7577, Slab.class),
    SMOOTH_QUARTZ(14415),
    SMOOTH_RED_SANDSTONE(25180),
    SMOOTH_SANDSTONE(30039),
    SMOOTH_STONE(21910),
    BRICKS(14165),
    BOOKSHELF(10069),
    /**
     * BlockData: {@link ChiseledBookshelf}
     */
    CHISELED_BOOKSHELF(8099, ChiseledBookshelf.class),
    /**
     * BlockData: {@link DecoratedPot}
     */
    DECORATED_POT(8720, DecoratedPot.class),
    MOSSY_COBBLESTONE(21900),
    OBSIDIAN(32723),
    TORCH(6063),
    /**
     * BlockData: {@link Directional}
     */
    END_ROD(24832, Directional.class),
    /**
     * BlockData: {@link MultipleFacing}
     */
    CHORUS_PLANT(28243, MultipleFacing.class),
    /**
     * BlockData: {@link Ageable}
     */
    CHORUS_FLOWER(28542, Ageable.class),
    PURPUR_BLOCK(7538),
    /**
     * BlockData: {@link Orientable}
     */
    PURPUR_PILLAR(26718, Orientable.class),
    /**
     * BlockData: {@link Stairs}
     */
    PURPUR_STAIRS(8921, Stairs.class),
    SPAWNER(7018),
    /**
     * BlockData: {@link Chest}
     */
    CHEST(22969, Chest.class),
    CRAFTING_TABLE(20706),
    /**
     * BlockData: {@link Farmland}
     */
    FARMLAND(31166, Farmland.class),
    /**
     * BlockData: {@link Furnace}
     */
    FURNACE(8133, Furnace.class),
    /**
     * BlockData: {@link Ladder}
     */
    LADDER(23599, Ladder.class),
    /**
     * BlockData: {@link Stairs}
     */
    COBBLESTONE_STAIRS(24715, Stairs.class),
    /**
     * BlockData: {@link Snow}
     */
    SNOW(14146, Snow.class),
    ICE(30428),
    SNOW_BLOCK(19913),
    /**
     * BlockData: {@link Ageable}
     */
    CACTUS(12191, Ageable.class),
    CLAY(27880),
    /**
     * BlockData: {@link Jukebox}
     */
    JUKEBOX(19264, Jukebox.class),
    /**
     * BlockData: {@link Fence}
     */
    OAK_FENCE(6442, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    SPRUCE_FENCE(25416, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    BIRCH_FENCE(17347, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    JUNGLE_FENCE(14358, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    ACACIA_FENCE(4569, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    CHERRY_FENCE(32047, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    DARK_OAK_FENCE(21767, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    MANGROVE_FENCE(15021, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    BAMBOO_FENCE(17207, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    CRIMSON_FENCE(21075, Fence.class),
    /**
     * BlockData: {@link Fence}
     */
    WARPED_FENCE(18438, Fence.class),
    PUMPKIN(19170),
    /**
     * BlockData: {@link Directional}
     */
    CARVED_PUMPKIN(25833, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    JACK_O_LANTERN(13758, Directional.class),
    NETHERRACK(23425),
    SOUL_SAND(16841),
    SOUL_SOIL(31140),
    /**
     * BlockData: {@link Orientable}
     */
    BASALT(28478, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    POLISHED_BASALT(11659, Orientable.class),
    SMOOTH_BASALT(13617),
    SOUL_TORCH(14292),
    GLOWSTONE(32713),
    INFESTED_STONE(18440),
    INFESTED_COBBLESTONE(4348),
    INFESTED_STONE_BRICKS(19749),
    INFESTED_MOSSY_STONE_BRICKS(9850),
    INFESTED_CRACKED_STONE_BRICKS(7476),
    INFESTED_CHISELED_STONE_BRICKS(4728),
    /**
     * BlockData: {@link Orientable}
     */
    INFESTED_DEEPSLATE(9472, Orientable.class),
    STONE_BRICKS(6962),
    MOSSY_STONE_BRICKS(16415),
    CRACKED_STONE_BRICKS(27869),
    CHISELED_STONE_BRICKS(9087),
    PACKED_MUD(7472),
    MUD_BRICKS(29168),
    DEEPSLATE_BRICKS(13193),
    CRACKED_DEEPSLATE_BRICKS(17105),
    DEEPSLATE_TILES(11250),
    CRACKED_DEEPSLATE_TILES(26249),
    CHISELED_DEEPSLATE(23825),
    REINFORCED_DEEPSLATE(10949),
    /**
     * BlockData: {@link MultipleFacing}
     */
    BROWN_MUSHROOM_BLOCK(6291, MultipleFacing.class),
    /**
     * BlockData: {@link MultipleFacing}
     */
    RED_MUSHROOM_BLOCK(20766, MultipleFacing.class),
    /**
     * BlockData: {@link MultipleFacing}
     */
    MUSHROOM_STEM(16543, MultipleFacing.class),
    /**
     * BlockData: {@link Fence}
     */
    IRON_BARS(9378, Fence.class),
    /**
     * BlockData: {@link Chain}
     */
    CHAIN(28265, Chain.class),
    /**
     * BlockData: {@link Fence}
     */
    GLASS_PANE(5709, Fence.class),
    MELON(25172),
    /**
     * BlockData: {@link MultipleFacing}
     */
    VINE(14564, MultipleFacing.class),
    /**
     * BlockData: {@link GlowLichen}
     */
    GLOW_LICHEN(19165, GlowLichen.class),
    /**
     * BlockData: {@link Stairs}
     */
    BRICK_STAIRS(21534, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    STONE_BRICK_STAIRS(27032, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    MUD_BRICK_STAIRS(13620, Stairs.class),
    /**
     * BlockData: {@link Snowable}
     */
    MYCELIUM(9913, Snowable.class),
    LILY_PAD(19271),
    NETHER_BRICKS(27802),
    CRACKED_NETHER_BRICKS(10888),
    CHISELED_NETHER_BRICKS(21613),
    /**
     * BlockData: {@link Fence}
     */
    NETHER_BRICK_FENCE(5286, Fence.class),
    /**
     * BlockData: {@link Stairs}
     */
    NETHER_BRICK_STAIRS(12085, Stairs.class),
    SCULK(17870),
    /**
     * BlockData: {@link SculkVein}
     */
    SCULK_VEIN(11615, SculkVein.class),
    /**
     * BlockData: {@link SculkCatalyst}
     */
    SCULK_CATALYST(12017, SculkCatalyst.class),
    /**
     * BlockData: {@link SculkShrieker}
     */
    SCULK_SHRIEKER(20985, SculkShrieker.class),
    ENCHANTING_TABLE(16255),
    /**
     * BlockData: {@link EndPortalFrame}
     */
    END_PORTAL_FRAME(15480, EndPortalFrame.class),
    END_STONE(29686),
    END_STONE_BRICKS(20314),
    DRAGON_EGG(29946),
    /**
     * BlockData: {@link Stairs}
     */
    SANDSTONE_STAIRS(18474, Stairs.class),
    /**
     * BlockData: {@link EnderChest}
     */
    ENDER_CHEST(32349, EnderChest.class),
    EMERALD_BLOCK(9914),
    /**
     * BlockData: {@link Stairs}
     */
    OAK_STAIRS(5449, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    SPRUCE_STAIRS(11192, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    BIRCH_STAIRS(7657, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    JUNGLE_STAIRS(20636, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    ACACIA_STAIRS(17453, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    CHERRY_STAIRS(18380, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    DARK_OAK_STAIRS(22921, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    MANGROVE_STAIRS(27641, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    BAMBOO_STAIRS(25674, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    BAMBOO_MOSAIC_STAIRS(20977, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    CRIMSON_STAIRS(32442, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    WARPED_STAIRS(17721, Stairs.class),
    /**
     * BlockData: {@link CommandBlock}
     */
    COMMAND_BLOCK(4355, CommandBlock.class),
    BEACON(6608),
    /**
     * BlockData: {@link Wall}
     */
    COBBLESTONE_WALL(12616, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    MOSSY_COBBLESTONE_WALL(11536, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    BRICK_WALL(18995, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    PRISMARINE_WALL(18184, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    RED_SANDSTONE_WALL(4753, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    MOSSY_STONE_BRICK_WALL(18259, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    GRANITE_WALL(23279, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    STONE_BRICK_WALL(29073, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    MUD_BRICK_WALL(18292, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    NETHER_BRICK_WALL(10398, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    ANDESITE_WALL(14938, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    RED_NETHER_BRICK_WALL(4580, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    SANDSTONE_WALL(18470, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    END_STONE_BRICK_WALL(27225, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    DIORITE_WALL(17412, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    BLACKSTONE_WALL(17327, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    POLISHED_BLACKSTONE_WALL(15119, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    POLISHED_BLACKSTONE_BRICK_WALL(9540, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    COBBLED_DEEPSLATE_WALL(21893, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    POLISHED_DEEPSLATE_WALL(6574, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    DEEPSLATE_BRICK_WALL(13304, Wall.class),
    /**
     * BlockData: {@link Wall}
     */
    DEEPSLATE_TILE_WALL(17077, Wall.class),
    /**
     * BlockData: {@link Directional}
     */
    ANVIL(18718, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    CHIPPED_ANVIL(10623, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    DAMAGED_ANVIL(10274, Directional.class),
    CHISELED_QUARTZ_BLOCK(30964),
    QUARTZ_BLOCK(11987),
    QUARTZ_BRICKS(23358),
    /**
     * BlockData: {@link Orientable}
     */
    QUARTZ_PILLAR(16452, Orientable.class),
    /**
     * BlockData: {@link Stairs}
     */
    QUARTZ_STAIRS(24079, Stairs.class),
    WHITE_TERRACOTTA(20975),
    ORANGE_TERRACOTTA(18684),
    MAGENTA_TERRACOTTA(25900),
    LIGHT_BLUE_TERRACOTTA(31779),
    YELLOW_TERRACOTTA(32129),
    LIME_TERRACOTTA(24013),
    PINK_TERRACOTTA(23727),
    GRAY_TERRACOTTA(18004),
    LIGHT_GRAY_TERRACOTTA(26388),
    CYAN_TERRACOTTA(25940),
    PURPLE_TERRACOTTA(10387),
    BLUE_TERRACOTTA(5236),
    BROWN_TERRACOTTA(23664),
    GREEN_TERRACOTTA(4105),
    RED_TERRACOTTA(5086),
    BLACK_TERRACOTTA(26691),
    /**
     * BlockData: {@link Waterlogged}
     */
    BARRIER(26453, Waterlogged.class),
    /**
     * BlockData: {@link Light}
     */
    LIGHT(17829, Light.class),
    /**
     * BlockData: {@link Orientable}
     */
    HAY_BLOCK(17461, Orientable.class),
    WHITE_CARPET(15117),
    ORANGE_CARPET(24752),
    MAGENTA_CARPET(6180),
    LIGHT_BLUE_CARPET(21194),
    YELLOW_CARPET(18149),
    LIME_CARPET(15443),
    PINK_CARPET(27381),
    GRAY_CARPET(26991),
    LIGHT_GRAY_CARPET(11317),
    CYAN_CARPET(9742),
    PURPLE_CARPET(5574),
    BLUE_CARPET(13292),
    BROWN_CARPET(23352),
    GREEN_CARPET(7780),
    RED_CARPET(5424),
    BLACK_CARPET(6056),
    TERRACOTTA(16544),
    PACKED_ICE(28993),
    DIRT_PATH(10846),
    /**
     * BlockData: {@link Bisected}
     */
    SUNFLOWER(7408, Bisected.class),
    /**
     * BlockData: {@link Bisected}
     */
    LILAC(22837, Bisected.class),
    /**
     * BlockData: {@link Bisected}
     */
    ROSE_BUSH(6080, Bisected.class),
    /**
     * BlockData: {@link Bisected}
     */
    PEONY(21155, Bisected.class),
    /**
     * BlockData: {@link Bisected}
     */
    TALL_GRASS(21559, Bisected.class),
    /**
     * BlockData: {@link Bisected}
     */
    LARGE_FERN(30177, Bisected.class),
    WHITE_STAINED_GLASS(31190),
    ORANGE_STAINED_GLASS(25142),
    MAGENTA_STAINED_GLASS(26814),
    LIGHT_BLUE_STAINED_GLASS(17162),
    YELLOW_STAINED_GLASS(12182),
    LIME_STAINED_GLASS(24266),
    PINK_STAINED_GLASS(16164),
    GRAY_STAINED_GLASS(29979),
    LIGHT_GRAY_STAINED_GLASS(5843),
    CYAN_STAINED_GLASS(30604),
    PURPLE_STAINED_GLASS(21845),
    BLUE_STAINED_GLASS(7107),
    BROWN_STAINED_GLASS(20945),
    GREEN_STAINED_GLASS(22503),
    RED_STAINED_GLASS(9717),
    BLACK_STAINED_GLASS(13941),
    /**
     * BlockData: {@link GlassPane}
     */
    WHITE_STAINED_GLASS_PANE(10557, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    ORANGE_STAINED_GLASS_PANE(21089, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    MAGENTA_STAINED_GLASS_PANE(14082, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    LIGHT_BLUE_STAINED_GLASS_PANE(18721, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    YELLOW_STAINED_GLASS_PANE(20298, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    LIME_STAINED_GLASS_PANE(10610, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    PINK_STAINED_GLASS_PANE(24637, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    GRAY_STAINED_GLASS_PANE(25272, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    LIGHT_GRAY_STAINED_GLASS_PANE(19008, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    CYAN_STAINED_GLASS_PANE(11784, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    PURPLE_STAINED_GLASS_PANE(10948, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    BLUE_STAINED_GLASS_PANE(28484, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    BROWN_STAINED_GLASS_PANE(17557, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    GREEN_STAINED_GLASS_PANE(4767, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    RED_STAINED_GLASS_PANE(8630, GlassPane.class),
    /**
     * BlockData: {@link GlassPane}
     */
    BLACK_STAINED_GLASS_PANE(13201, GlassPane.class),
    PRISMARINE(7539),
    PRISMARINE_BRICKS(29118),
    DARK_PRISMARINE(19940),
    /**
     * BlockData: {@link Stairs}
     */
    PRISMARINE_STAIRS(19217, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    PRISMARINE_BRICK_STAIRS(15445, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    DARK_PRISMARINE_STAIRS(26511, Stairs.class),
    SEA_LANTERN(20780),
    RED_SANDSTONE(9092),
    CHISELED_RED_SANDSTONE(15529),
    CUT_RED_SANDSTONE(29108),
    /**
     * BlockData: {@link Stairs}
     */
    RED_SANDSTONE_STAIRS(25466, Stairs.class),
    /**
     * BlockData: {@link CommandBlock}
     */
    REPEATING_COMMAND_BLOCK(12405, CommandBlock.class),
    /**
     * BlockData: {@link CommandBlock}
     */
    CHAIN_COMMAND_BLOCK(26798, CommandBlock.class),
    MAGMA_BLOCK(25927),
    NETHER_WART_BLOCK(15486),
    WARPED_WART_BLOCK(15463),
    RED_NETHER_BRICKS(18056),
    /**
     * BlockData: {@link Orientable}
     */
    BONE_BLOCK(17312, Orientable.class),
    STRUCTURE_VOID(30806),
    /**
     * BlockData: {@link Directional}
     */
    SHULKER_BOX(7776, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    WHITE_SHULKER_BOX(31750, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    ORANGE_SHULKER_BOX(21673, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    MAGENTA_SHULKER_BOX(21566, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_BLUE_SHULKER_BOX(18226, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    YELLOW_SHULKER_BOX(28700, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIME_SHULKER_BOX(28360, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PINK_SHULKER_BOX(24968, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GRAY_SHULKER_BOX(12754, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_GRAY_SHULKER_BOX(21345, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    CYAN_SHULKER_BOX(28123, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PURPLE_SHULKER_BOX(10373, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLUE_SHULKER_BOX(11476, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BROWN_SHULKER_BOX(24230, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GREEN_SHULKER_BOX(9377, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    RED_SHULKER_BOX(32448, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLACK_SHULKER_BOX(24076, 1, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    WHITE_GLAZED_TERRACOTTA(11326, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    ORANGE_GLAZED_TERRACOTTA(27451, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    MAGENTA_GLAZED_TERRACOTTA(8067, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_BLUE_GLAZED_TERRACOTTA(4336, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    YELLOW_GLAZED_TERRACOTTA(10914, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIME_GLAZED_TERRACOTTA(13861, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PINK_GLAZED_TERRACOTTA(10260, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GRAY_GLAZED_TERRACOTTA(6256, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_GRAY_GLAZED_TERRACOTTA(10707, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    CYAN_GLAZED_TERRACOTTA(9550, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PURPLE_GLAZED_TERRACOTTA(4818, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLUE_GLAZED_TERRACOTTA(23823, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BROWN_GLAZED_TERRACOTTA(5655, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GREEN_GLAZED_TERRACOTTA(6958, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    RED_GLAZED_TERRACOTTA(24989, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLACK_GLAZED_TERRACOTTA(29678, Directional.class),
    WHITE_CONCRETE(6281),
    ORANGE_CONCRETE(19914),
    MAGENTA_CONCRETE(20591),
    LIGHT_BLUE_CONCRETE(29481),
    YELLOW_CONCRETE(15722),
    LIME_CONCRETE(5863),
    PINK_CONCRETE(5227),
    GRAY_CONCRETE(13959),
    LIGHT_GRAY_CONCRETE(14453),
    CYAN_CONCRETE(26522),
    PURPLE_CONCRETE(20623),
    BLUE_CONCRETE(18756),
    BROWN_CONCRETE(19006),
    GREEN_CONCRETE(17949),
    RED_CONCRETE(8032),
    BLACK_CONCRETE(13338),
    WHITE_CONCRETE_POWDER(10363),
    ORANGE_CONCRETE_POWDER(30159),
    MAGENTA_CONCRETE_POWDER(8272),
    LIGHT_BLUE_CONCRETE_POWDER(31206),
    YELLOW_CONCRETE_POWDER(10655),
    LIME_CONCRETE_POWDER(28859),
    PINK_CONCRETE_POWDER(6421),
    GRAY_CONCRETE_POWDER(13031),
    LIGHT_GRAY_CONCRETE_POWDER(21589),
    CYAN_CONCRETE_POWDER(15734),
    PURPLE_CONCRETE_POWDER(26808),
    BLUE_CONCRETE_POWDER(17773),
    BROWN_CONCRETE_POWDER(21485),
    GREEN_CONCRETE_POWDER(6904),
    RED_CONCRETE_POWDER(13286),
    BLACK_CONCRETE_POWDER(16150),
    /**
     * BlockData: {@link TurtleEgg}
     */
    TURTLE_EGG(32101, TurtleEgg.class),
    /**
     * BlockData: {@link Hatchable}
     */
    SNIFFER_EGG(12980, Hatchable.class),
    DEAD_TUBE_CORAL_BLOCK(28350),
    DEAD_BRAIN_CORAL_BLOCK(12979),
    DEAD_BUBBLE_CORAL_BLOCK(28220),
    DEAD_FIRE_CORAL_BLOCK(5307),
    DEAD_HORN_CORAL_BLOCK(15103),
    TUBE_CORAL_BLOCK(23723),
    BRAIN_CORAL_BLOCK(30618),
    BUBBLE_CORAL_BLOCK(15437),
    FIRE_CORAL_BLOCK(12119),
    HORN_CORAL_BLOCK(19958),
    /**
     * BlockData: {@link Waterlogged}
     */
    TUBE_CORAL(23048, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    BRAIN_CORAL(31316, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    BUBBLE_CORAL(12464, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    FIRE_CORAL(29151, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    HORN_CORAL(19511, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_BRAIN_CORAL(9116, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_BUBBLE_CORAL(30583, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_FIRE_CORAL(8365, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_HORN_CORAL(5755, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_TUBE_CORAL(18028, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    TUBE_CORAL_FAN(19929, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    BRAIN_CORAL_FAN(13849, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    BUBBLE_CORAL_FAN(10795, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    FIRE_CORAL_FAN(11112, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    HORN_CORAL_FAN(13610, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_TUBE_CORAL_FAN(17628, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_BRAIN_CORAL_FAN(26150, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_BUBBLE_CORAL_FAN(17322, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_FIRE_CORAL_FAN(27073, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    DEAD_HORN_CORAL_FAN(11387, Waterlogged.class),
    BLUE_ICE(22449),
    /**
     * BlockData: {@link Waterlogged}
     */
    CONDUIT(5148, Waterlogged.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_GRANITE_STAIRS(29588, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    SMOOTH_RED_SANDSTONE_STAIRS(17561, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    MOSSY_STONE_BRICK_STAIRS(27578, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_DIORITE_STAIRS(4625, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    MOSSY_COBBLESTONE_STAIRS(29210, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    END_STONE_BRICK_STAIRS(28831, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    STONE_STAIRS(23784, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    SMOOTH_SANDSTONE_STAIRS(21183, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    SMOOTH_QUARTZ_STAIRS(19560, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    GRANITE_STAIRS(21840, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    ANDESITE_STAIRS(17747, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    RED_NETHER_BRICK_STAIRS(26374, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_ANDESITE_STAIRS(7573, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    DIORITE_STAIRS(13134, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    COBBLED_DEEPSLATE_STAIRS(20699, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_DEEPSLATE_STAIRS(19513, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    DEEPSLATE_BRICK_STAIRS(29624, Stairs.class),
    /**
     * BlockData: {@link Stairs}
     */
    DEEPSLATE_TILE_STAIRS(6361, Stairs.class),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_GRANITE_SLAB(4521, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SMOOTH_RED_SANDSTONE_SLAB(16304, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    MOSSY_STONE_BRICK_SLAB(14002, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_DIORITE_SLAB(18303, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    MOSSY_COBBLESTONE_SLAB(12139, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    END_STONE_BRICK_SLAB(23239, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SMOOTH_SANDSTONE_SLAB(9030, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    SMOOTH_QUARTZ_SLAB(26543, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    GRANITE_SLAB(10901, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    ANDESITE_SLAB(32124, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    RED_NETHER_BRICK_SLAB(12462, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_ANDESITE_SLAB(24573, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    DIORITE_SLAB(25526, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    COBBLED_DEEPSLATE_SLAB(17388, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_DEEPSLATE_SLAB(32201, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    DEEPSLATE_BRICK_SLAB(23910, Slab.class),
    /**
     * BlockData: {@link Slab}
     */
    DEEPSLATE_TILE_SLAB(13315, Slab.class),
    /**
     * BlockData: {@link Scaffolding}
     */
    SCAFFOLDING(15757, Scaffolding.class),
    REDSTONE(11233),
    /**
     * BlockData: {@link Lightable}
     */
    REDSTONE_TORCH(22547, Lightable.class),
    REDSTONE_BLOCK(19496),
    /**
     * BlockData: {@link Repeater}
     */
    REPEATER(28823, Repeater.class),
    /**
     * BlockData: {@link Comparator}
     */
    COMPARATOR(18911, Comparator.class),
    /**
     * BlockData: {@link Piston}
     */
    PISTON(21130, Piston.class),
    /**
     * BlockData: {@link Piston}
     */
    STICKY_PISTON(18127, Piston.class),
    SLIME_BLOCK(31892),
    HONEY_BLOCK(30615),
    /**
     * BlockData: {@link Observer}
     */
    OBSERVER(10726, Observer.class),
    /**
     * BlockData: {@link Hopper}
     */
    HOPPER(31974, Hopper.class),
    /**
     * BlockData: {@link Dispenser}
     */
    DISPENSER(20871, Dispenser.class),
    /**
     * BlockData: {@link Dispenser}
     */
    DROPPER(31273, Dispenser.class),
    /**
     * BlockData: {@link Lectern}
     */
    LECTERN(23490, Lectern.class),
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    TARGET(22637, AnaloguePowerable.class),
    /**
     * BlockData: {@link Switch}
     */
    LEVER(15319, Switch.class),
    /**
     * BlockData: {@link LightningRod}
     */
    LIGHTNING_ROD(30770, LightningRod.class),
    /**
     * BlockData: {@link DaylightDetector}
     */
    DAYLIGHT_DETECTOR(8864, DaylightDetector.class),
    /**
     * BlockData: {@link SculkSensor}
     */
    SCULK_SENSOR(5598, SculkSensor.class),
    /**
     * BlockData: {@link CalibratedSculkSensor}
     */
    CALIBRATED_SCULK_SENSOR(21034, CalibratedSculkSensor.class),
    /**
     * BlockData: {@link TripwireHook}
     */
    TRIPWIRE_HOOK(8130, TripwireHook.class),
    /**
     * BlockData: {@link Chest}
     */
    TRAPPED_CHEST(18970, Chest.class),
    /**
     * BlockData: {@link TNT}
     */
    TNT(7896, TNT.class),
    /**
     * BlockData: {@link Lightable}
     */
    REDSTONE_LAMP(8217, Lightable.class),
    /**
     * BlockData: {@link NoteBlock}
     */
    NOTE_BLOCK(20979, NoteBlock.class),
    /**
     * BlockData: {@link Switch}
     */
    STONE_BUTTON(12279, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    POLISHED_BLACKSTONE_BUTTON(20760, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    OAK_BUTTON(13510, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    SPRUCE_BUTTON(23281, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    BIRCH_BUTTON(26934, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    JUNGLE_BUTTON(25317, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    ACACIA_BUTTON(13993, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    CHERRY_BUTTON(9058, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    DARK_OAK_BUTTON(6214, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    MANGROVE_BUTTON(9838, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    BAMBOO_BUTTON(21810, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    CRIMSON_BUTTON(26799, Switch.class),
    /**
     * BlockData: {@link Switch}
     */
    WARPED_BUTTON(25264, Switch.class),
    /**
     * BlockData: {@link Powerable}
     */
    STONE_PRESSURE_PLATE(22591, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    POLISHED_BLACKSTONE_PRESSURE_PLATE(32340, Powerable.class),
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    LIGHT_WEIGHTED_PRESSURE_PLATE(14875, AnaloguePowerable.class),
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    HEAVY_WEIGHTED_PRESSURE_PLATE(16970, AnaloguePowerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    OAK_PRESSURE_PLATE(20108, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    SPRUCE_PRESSURE_PLATE(15932, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    BIRCH_PRESSURE_PLATE(9664, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    JUNGLE_PRESSURE_PLATE(11376, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    ACACIA_PRESSURE_PLATE(17586, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    CHERRY_PRESSURE_PLATE(8651, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    DARK_OAK_PRESSURE_PLATE(31375, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    MANGROVE_PRESSURE_PLATE(9748, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    BAMBOO_PRESSURE_PLATE(26740, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    CRIMSON_PRESSURE_PLATE(18316, Powerable.class),
    /**
     * BlockData: {@link Powerable}
     */
    WARPED_PRESSURE_PLATE(29516, Powerable.class),
    /**
     * BlockData: {@link Door}
     */
    IRON_DOOR(4788, Door.class),
    /**
     * BlockData: {@link Door}
     */
    OAK_DOOR(20341, Door.class),
    /**
     * BlockData: {@link Door}
     */
    SPRUCE_DOOR(10642, Door.class),
    /**
     * BlockData: {@link Door}
     */
    BIRCH_DOOR(14759, Door.class),
    /**
     * BlockData: {@link Door}
     */
    JUNGLE_DOOR(28163, Door.class),
    /**
     * BlockData: {@link Door}
     */
    ACACIA_DOOR(23797, Door.class),
    /**
     * BlockData: {@link Door}
     */
    CHERRY_DOOR(12684, Door.class),
    /**
     * BlockData: {@link Door}
     */
    DARK_OAK_DOOR(10669, Door.class),
    /**
     * BlockData: {@link Door}
     */
    MANGROVE_DOOR(18964, Door.class),
    /**
     * BlockData: {@link Door}
     */
    BAMBOO_DOOR(19971, Door.class),
    /**
     * BlockData: {@link Door}
     */
    CRIMSON_DOOR(19544, Door.class),
    /**
     * BlockData: {@link Door}
     */
    WARPED_DOOR(15062, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    COPPER_DOOR(26809, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    EXPOSED_COPPER_DOOR(13236, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WEATHERED_COPPER_DOOR(10208, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OXIDIZED_COPPER_DOOR(5348, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_COPPER_DOOR(9954, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_EXPOSED_COPPER_DOOR(20748, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_WEATHERED_COPPER_DOOR(25073, Door.class),
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_OXIDIZED_COPPER_DOOR(23888, Door.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    IRON_TRAPDOOR(17095, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    OAK_TRAPDOOR(16927, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    SPRUCE_TRAPDOOR(10289, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    BIRCH_TRAPDOOR(32585, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    JUNGLE_TRAPDOOR(8626, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    ACACIA_TRAPDOOR(18343, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    CHERRY_TRAPDOOR(6293, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    DARK_OAK_TRAPDOOR(10355, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    MANGROVE_TRAPDOOR(17066, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    BAMBOO_TRAPDOOR(9174, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    CRIMSON_TRAPDOOR(25056, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    WARPED_TRAPDOOR(7708, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    COPPER_TRAPDOOR(12110, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    EXPOSED_COPPER_TRAPDOOR(19219, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WEATHERED_COPPER_TRAPDOOR(28254, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OXIDIZED_COPPER_TRAPDOOR(26518, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_COPPER_TRAPDOOR(12626, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_EXPOSED_COPPER_TRAPDOOR(11010, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_WEATHERED_COPPER_TRAPDOOR(30709, TrapDoor.class),
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_OXIDIZED_COPPER_TRAPDOOR(21450, TrapDoor.class),
    /**
     * BlockData: {@link Gate}
     */
    OAK_FENCE_GATE(16689, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    SPRUCE_FENCE_GATE(26423, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    BIRCH_FENCE_GATE(6322, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    JUNGLE_FENCE_GATE(21360, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    ACACIA_FENCE_GATE(14145, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    CHERRY_FENCE_GATE(28222, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    DARK_OAK_FENCE_GATE(10679, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    MANGROVE_FENCE_GATE(28476, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    BAMBOO_FENCE_GATE(14290, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    CRIMSON_FENCE_GATE(15602, Gate.class),
    /**
     * BlockData: {@link Gate}
     */
    WARPED_FENCE_GATE(11115, Gate.class),
    /**
     * BlockData: {@link RedstoneRail}
     */
    POWERED_RAIL(11064, RedstoneRail.class),
    /**
     * BlockData: {@link RedstoneRail}
     */
    DETECTOR_RAIL(13475, RedstoneRail.class),
    /**
     * BlockData: {@link Rail}
     */
    RAIL(13285, Rail.class),
    /**
     * BlockData: {@link RedstoneRail}
     */
    ACTIVATOR_RAIL(5834, RedstoneRail.class),
    SADDLE(30206, 1),
    MINECART(14352, 1),
    CHEST_MINECART(4497, 1),
    FURNACE_MINECART(14196, 1),
    TNT_MINECART(4277, 1),
    HOPPER_MINECART(19024, 1),
    CARROT_ON_A_STICK(27809, 1, 25),
    WARPED_FUNGUS_ON_A_STICK(11706, 1, 100),
    ELYTRA(23829, 1, 432),
    OAK_BOAT(17570, 1),
    OAK_CHEST_BOAT(7765, 1),
    SPRUCE_BOAT(31427, 1),
    SPRUCE_CHEST_BOAT(30841, 1),
    BIRCH_BOAT(28104, 1),
    BIRCH_CHEST_BOAT(18546, 1),
    JUNGLE_BOAT(4495, 1),
    JUNGLE_CHEST_BOAT(20133, 1),
    ACACIA_BOAT(27326, 1),
    ACACIA_CHEST_BOAT(28455, 1),
    CHERRY_BOAT(13628, 1),
    CHERRY_CHEST_BOAT(7165, 1),
    DARK_OAK_BOAT(28618, 1),
    DARK_OAK_CHEST_BOAT(8733, 1),
    MANGROVE_BOAT(20792, 1),
    MANGROVE_CHEST_BOAT(18572, 1),
    BAMBOO_RAFT(25901, 1),
    BAMBOO_CHEST_RAFT(20056, 1),
    /**
     * BlockData: {@link StructureBlock}
     */
    STRUCTURE_BLOCK(26831, StructureBlock.class),
    /**
     * BlockData: {@link Jigsaw}
     */
    JIGSAW(17398, Jigsaw.class),
    TURTLE_HELMET(30120, 1, 275),
    TURTLE_SCUTE(6766),
    ARMADILLO_SCUTE(11497),
    WOLF_ARMOR(17138, 1, 64),
    FLINT_AND_STEEL(28620, 1, 64),
    APPLE(7720),
    BOW(8745, 1, 384),
    ARROW(31091),
    COAL(29067),
    CHARCOAL(5390),
    DIAMOND(20865),
    EMERALD(5654),
    LAPIS_LAZULI(11075),
    QUARTZ(23608),
    AMETHYST_SHARD(7613),
    RAW_IRON(5329),
    IRON_INGOT(24895),
    RAW_COPPER(6162),
    COPPER_INGOT(12611),
    RAW_GOLD(19564),
    GOLD_INGOT(28927),
    NETHERITE_INGOT(32457),
    NETHERITE_SCRAP(29331),
    WOODEN_SWORD(7175, 1, 59),
    WOODEN_SHOVEL(28432, 1, 59),
    WOODEN_PICKAXE(12792, 1, 59),
    WOODEN_AXE(6292, 1, 59),
    WOODEN_HOE(16043, 1, 59),
    STONE_SWORD(25084, 1, 131),
    STONE_SHOVEL(9520, 1, 131),
    STONE_PICKAXE(14611, 1, 131),
    STONE_AXE(6338, 1, 131),
    STONE_HOE(22855, 1, 131),
    GOLDEN_SWORD(10505, 1, 32),
    GOLDEN_SHOVEL(15597, 1, 32),
    GOLDEN_PICKAXE(25898, 1, 32),
    GOLDEN_AXE(4878, 1, 32),
    GOLDEN_HOE(19337, 1, 32),
    IRON_SWORD(10904, 1, 250),
    IRON_SHOVEL(30045, 1, 250),
    IRON_PICKAXE(8842, 1, 250),
    IRON_AXE(15894, 1, 250),
    IRON_HOE(11339, 1, 250),
    DIAMOND_SWORD(27707, 1, 1561),
    DIAMOND_SHOVEL(25415, 1, 1561),
    DIAMOND_PICKAXE(24291, 1, 1561),
    DIAMOND_AXE(27277, 1, 1561),
    DIAMOND_HOE(24050, 1, 1561),
    NETHERITE_SWORD(23871, 1, 2031),
    NETHERITE_SHOVEL(29728, 1, 2031),
    NETHERITE_PICKAXE(9930, 1, 2031),
    NETHERITE_AXE(29533, 1, 2031),
    NETHERITE_HOE(27385, 1, 2031),
    STICK(9773),
    BOWL(32661),
    MUSHROOM_STEW(16336, 1),
    STRING(12806),
    FEATHER(30548),
    GUNPOWDER(29974),
    WHEAT_SEEDS(28742),
    /**
     * BlockData: {@link Ageable}
     */
    WHEAT(27709, Ageable.class),
    BREAD(32049),
    LEATHER_HELMET(11624, 1, 55),
    LEATHER_CHESTPLATE(29275, 1, 80),
    LEATHER_LEGGINGS(28210, 1, 75),
    LEATHER_BOOTS(15282, 1, 65),
    CHAINMAIL_HELMET(26114, 1, 165),
    CHAINMAIL_CHESTPLATE(23602, 1, 240),
    CHAINMAIL_LEGGINGS(19087, 1, 225),
    CHAINMAIL_BOOTS(17953, 1, 195),
    IRON_HELMET(12025, 1, 165),
    IRON_CHESTPLATE(28112, 1, 240),
    IRON_LEGGINGS(18951, 1, 225),
    IRON_BOOTS(8531, 1, 195),
    DIAMOND_HELMET(10755, 1, 363),
    DIAMOND_CHESTPLATE(32099, 1, 528),
    DIAMOND_LEGGINGS(26500, 1, 495),
    DIAMOND_BOOTS(16522, 1, 429),
    GOLDEN_HELMET(7945, 1, 77),
    GOLDEN_CHESTPLATE(4507, 1, 112),
    GOLDEN_LEGGINGS(21002, 1, 105),
    GOLDEN_BOOTS(7859, 1, 91),
    NETHERITE_HELMET(15907, 1, 407),
    NETHERITE_CHESTPLATE(6106, 1, 592),
    NETHERITE_LEGGINGS(25605, 1, 555),
    NETHERITE_BOOTS(8923, 1, 481),
    FLINT(23596),
    PORKCHOP(30896),
    COOKED_PORKCHOP(27231),
    PAINTING(23945),
    GOLDEN_APPLE(27732),
    ENCHANTED_GOLDEN_APPLE(8280),
    /**
     * BlockData: {@link Sign}
     */
    OAK_SIGN(8192, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    SPRUCE_SIGN(21502, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    BIRCH_SIGN(11351, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    JUNGLE_SIGN(24717, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    ACACIA_SIGN(29808, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    CHERRY_SIGN(16520, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    DARK_OAK_SIGN(15127, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    MANGROVE_SIGN(21975, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    BAMBOO_SIGN(26139, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    CRIMSON_SIGN(12162, 16, Sign.class),
    /**
     * BlockData: {@link Sign}
     */
    WARPED_SIGN(10407, 16, Sign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    OAK_HANGING_SIGN(20116, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    SPRUCE_HANGING_SIGN(24371, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    BIRCH_HANGING_SIGN(17938, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    JUNGLE_HANGING_SIGN(27671, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    ACACIA_HANGING_SIGN(30257, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    CHERRY_HANGING_SIGN(5088, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    DARK_OAK_HANGING_SIGN(23360, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    MANGROVE_HANGING_SIGN(25106, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    BAMBOO_HANGING_SIGN(4726, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    CRIMSON_HANGING_SIGN(20696, 16, HangingSign.class),
    /**
     * BlockData: {@link HangingSign}
     */
    WARPED_HANGING_SIGN(8195, 16, HangingSign.class),
    BUCKET(15215, 16),
    WATER_BUCKET(8802, 1),
    LAVA_BUCKET(9228, 1),
    POWDER_SNOW_BUCKET(31101, 1),
    SNOWBALL(19487, 16),
    LEATHER(16414),
    MILK_BUCKET(9680, 1),
    PUFFERFISH_BUCKET(8861, 1),
    SALMON_BUCKET(9606, 1),
    COD_BUCKET(28601, 1),
    TROPICAL_FISH_BUCKET(29995, 1),
    AXOLOTL_BUCKET(20669, 1),
    TADPOLE_BUCKET(9731, 1),
    BRICK(6820),
    CLAY_BALL(24603),
    DRIED_KELP_BLOCK(12966),
    PAPER(9923),
    BOOK(23097),
    SLIME_BALL(5242),
    EGG(21603, 16),
    COMPASS(24139),
    RECOVERY_COMPASS(12710),
    BUNDLE(16835, 1),
    FISHING_ROD(4167, 1, 64),
    CLOCK(14980),
    SPYGLASS(27490, 1),
    GLOWSTONE_DUST(6665),
    COD(24691),
    SALMON(18516),
    TROPICAL_FISH(24879),
    PUFFERFISH(8115),
    COOKED_COD(9681),
    COOKED_SALMON(5615),
    INK_SAC(7184),
    GLOW_INK_SAC(9686),
    COCOA_BEANS(30186),
    WHITE_DYE(10758),
    ORANGE_DYE(13866),
    MAGENTA_DYE(11788),
    LIGHT_BLUE_DYE(28738),
    YELLOW_DYE(5952),
    LIME_DYE(6147),
    PINK_DYE(31151),
    GRAY_DYE(9184),
    LIGHT_GRAY_DYE(27643),
    CYAN_DYE(8043),
    PURPLE_DYE(6347),
    BLUE_DYE(11588),
    BROWN_DYE(7648),
    GREEN_DYE(23215),
    RED_DYE(5728),
    BLACK_DYE(6202),
    BONE_MEAL(32458),
    BONE(5686),
    SUGAR(30638),
    /**
     * BlockData: {@link Cake}
     */
    CAKE(27048, 1, Cake.class),
    /**
     * BlockData: {@link Bed}
     */
    WHITE_BED(8185, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    ORANGE_BED(11194, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    MAGENTA_BED(20061, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    LIGHT_BLUE_BED(20957, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    YELLOW_BED(30410, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    LIME_BED(27860, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    PINK_BED(13795, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    GRAY_BED(15745, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    LIGHT_GRAY_BED(5090, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    CYAN_BED(16746, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    PURPLE_BED(29755, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    BLUE_BED(12714, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    BROWN_BED(26672, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    GREEN_BED(13797, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    RED_BED(30910, 1, Bed.class),
    /**
     * BlockData: {@link Bed}
     */
    BLACK_BED(20490, 1, Bed.class),
    COOKIE(27431),
    /**
     * BlockData: {@link Crafter}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    CRAFTER(25243, Crafter.class),
    FILLED_MAP(23504),
    SHEARS(27971, 1, 238),
    MELON_SLICE(5347),
    DRIED_KELP(21042),
    PUMPKIN_SEEDS(28985),
    MELON_SEEDS(18340),
    BEEF(4803),
    COOKED_BEEF(21595),
    CHICKEN(17281),
    COOKED_CHICKEN(16984),
    ROTTEN_FLESH(21591),
    ENDER_PEARL(5259, 16),
    BLAZE_ROD(8289),
    GHAST_TEAR(18222),
    GOLD_NUGGET(28814),
    /**
     * BlockData: {@link Ageable}
     */
    NETHER_WART(29227, Ageable.class),
    POTION(24020, 1),
    GLASS_BOTTLE(6116),
    SPIDER_EYE(9318),
    FERMENTED_SPIDER_EYE(19386),
    BLAZE_POWDER(18941),
    MAGMA_CREAM(25097),
    /**
     * BlockData: {@link BrewingStand}
     */
    BREWING_STAND(14539, BrewingStand.class),
    CAULDRON(26531),
    ENDER_EYE(24860),
    GLISTERING_MELON_SLICE(20158),
    ARMADILLO_SPAWN_EGG(22098),
    ALLAY_SPAWN_EGG(7909),
    AXOLOTL_SPAWN_EGG(30381),
    BAT_SPAWN_EGG(14607),
    BEE_SPAWN_EGG(22924),
    BLAZE_SPAWN_EGG(4759),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    BOGGED_SPAWN_EGG(12042),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    BREEZE_SPAWN_EGG(7580),
    CAT_SPAWN_EGG(29583),
    CAMEL_SPAWN_EGG(14760),
    CAVE_SPIDER_SPAWN_EGG(23341),
    CHICKEN_SPAWN_EGG(5462),
    COD_SPAWN_EGG(27248),
    COW_SPAWN_EGG(14761),
    CREEPER_SPAWN_EGG(9653),
    DOLPHIN_SPAWN_EGG(20787),
    DONKEY_SPAWN_EGG(14513),
    DROWNED_SPAWN_EGG(19368),
    ELDER_GUARDIAN_SPAWN_EGG(11418),
    ENDER_DRAGON_SPAWN_EGG(28092),
    ENDERMAN_SPAWN_EGG(29488),
    ENDERMITE_SPAWN_EGG(16617),
    EVOKER_SPAWN_EGG(21271),
    FOX_SPAWN_EGG(22376),
    FROG_SPAWN_EGG(26682),
    GHAST_SPAWN_EGG(9970),
    GLOW_SQUID_SPAWN_EGG(31578),
    GOAT_SPAWN_EGG(30639),
    GUARDIAN_SPAWN_EGG(20113),
    HOGLIN_SPAWN_EGG(14088),
    HORSE_SPAWN_EGG(25981),
    HUSK_SPAWN_EGG(20178),
    IRON_GOLEM_SPAWN_EGG(12781),
    LLAMA_SPAWN_EGG(23640),
    MAGMA_CUBE_SPAWN_EGG(26638),
    MOOSHROOM_SPAWN_EGG(22125),
    MULE_SPAWN_EGG(11229),
    OCELOT_SPAWN_EGG(30080),
    PANDA_SPAWN_EGG(23759),
    PARROT_SPAWN_EGG(23614),
    PHANTOM_SPAWN_EGG(24648),
    PIG_SPAWN_EGG(22584),
    PIGLIN_SPAWN_EGG(16193),
    PIGLIN_BRUTE_SPAWN_EGG(30230),
    PILLAGER_SPAWN_EGG(28659),
    POLAR_BEAR_SPAWN_EGG(17015),
    PUFFERFISH_SPAWN_EGG(24570),
    RABBIT_SPAWN_EGG(26496),
    RAVAGER_SPAWN_EGG(8726),
    SALMON_SPAWN_EGG(18739),
    SHEEP_SPAWN_EGG(24488),
    SHULKER_SPAWN_EGG(31848),
    SILVERFISH_SPAWN_EGG(14537),
    SKELETON_SPAWN_EGG(15261),
    SKELETON_HORSE_SPAWN_EGG(21356),
    SLIME_SPAWN_EGG(17196),
    SNIFFER_SPAWN_EGG(27473),
    SNOW_GOLEM_SPAWN_EGG(24732),
    SPIDER_SPAWN_EGG(14984),
    SQUID_SPAWN_EGG(10682),
    STRAY_SPAWN_EGG(30153),
    STRIDER_SPAWN_EGG(6203),
    TADPOLE_SPAWN_EGG(32467),
    TRADER_LLAMA_SPAWN_EGG(8439),
    TROPICAL_FISH_SPAWN_EGG(19713),
    TURTLE_SPAWN_EGG(17324),
    VEX_SPAWN_EGG(27751),
    VILLAGER_SPAWN_EGG(30348),
    VINDICATOR_SPAWN_EGG(25324),
    WANDERING_TRADER_SPAWN_EGG(17904),
    WARDEN_SPAWN_EGG(27553),
    WITCH_SPAWN_EGG(11837),
    WITHER_SPAWN_EGG(8024),
    WITHER_SKELETON_SPAWN_EGG(10073),
    WOLF_SPAWN_EGG(21692),
    ZOGLIN_SPAWN_EGG(7442),
    ZOMBIE_SPAWN_EGG(5814),
    ZOMBIE_HORSE_SPAWN_EGG(4275),
    ZOMBIE_VILLAGER_SPAWN_EGG(10311),
    ZOMBIFIED_PIGLIN_SPAWN_EGG(6626),
    EXPERIENCE_BOTTLE(12858),
    FIRE_CHARGE(4842),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WIND_CHARGE(23928),
    WRITABLE_BOOK(13393, 1),
    WRITTEN_BOOK(24164, 16),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    MACE(4771, 1, 250),
    ITEM_FRAME(27318),
    GLOW_ITEM_FRAME(26473),
    FLOWER_POT(30567),
    CARROT(22824),
    POTATO(21088),
    BAKED_POTATO(14624),
    POISONOUS_POTATO(32640),
    MAP(21655),
    GOLDEN_CARROT(5300),
    /**
     * BlockData: {@link Rotatable}
     */
    SKELETON_SKULL(13270, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    WITHER_SKELETON_SKULL(31487, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    PLAYER_HEAD(21174, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    ZOMBIE_HEAD(9304, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    CREEPER_HEAD(29146, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    DRAGON_HEAD(20084, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    PIGLIN_HEAD(5512, Rotatable.class),
    NETHER_STAR(12469),
    PUMPKIN_PIE(28725),
    FIREWORK_ROCKET(23841),
    FIREWORK_STAR(12190),
    ENCHANTED_BOOK(11741, 1),
    NETHER_BRICK(19996),
    PRISMARINE_SHARD(10993),
    PRISMARINE_CRYSTALS(31546),
    RABBIT(23068),
    COOKED_RABBIT(4454),
    RABBIT_STEW(25318, 1),
    RABBIT_FOOT(13864),
    RABBIT_HIDE(12467),
    ARMOR_STAND(12852, 16),
    IRON_HORSE_ARMOR(30108, 1),
    GOLDEN_HORSE_ARMOR(7996, 1),
    DIAMOND_HORSE_ARMOR(10321, 1),
    LEATHER_HORSE_ARMOR(30667, 1),
    LEAD(29539),
    NAME_TAG(30731),
    COMMAND_BLOCK_MINECART(7992, 1),
    MUTTON(4792),
    COOKED_MUTTON(31447),
    /**
     * BlockData: {@link Rotatable}
     */
    WHITE_BANNER(17562, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    ORANGE_BANNER(4839, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    MAGENTA_BANNER(15591, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    LIGHT_BLUE_BANNER(18060, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    YELLOW_BANNER(30382, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    LIME_BANNER(18887, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    PINK_BANNER(19439, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    GRAY_BANNER(12053, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    LIGHT_GRAY_BANNER(11417, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    CYAN_BANNER(9839, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    PURPLE_BANNER(29027, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    BLUE_BANNER(18481, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    BROWN_BANNER(11481, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    GREEN_BANNER(10698, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    RED_BANNER(26961, 16, Rotatable.class),
    /**
     * BlockData: {@link Rotatable}
     */
    BLACK_BANNER(9365, 16, Rotatable.class),
    END_CRYSTAL(19090),
    CHORUS_FRUIT(7652),
    POPPED_CHORUS_FRUIT(27844),
    TORCHFLOWER_SEEDS(18153),
    PITCHER_POD(7977),
    BEETROOT(23305),
    BEETROOT_SEEDS(21282),
    BEETROOT_SOUP(16036, 1),
    DRAGON_BREATH(20154),
    SPLASH_POTION(30248, 1),
    SPECTRAL_ARROW(4568),
    TIPPED_ARROW(25164),
    LINGERING_POTION(25857, 1),
    SHIELD(29943, 1, 336),
    TOTEM_OF_UNDYING(10139, 1),
    SHULKER_SHELL(27848),
    IRON_NUGGET(13715),
    KNOWLEDGE_BOOK(12646, 1),
    DEBUG_STICK(24562, 1),
    MUSIC_DISC_13(16359, 1),
    MUSIC_DISC_CAT(16246, 1),
    MUSIC_DISC_BLOCKS(26667, 1),
    MUSIC_DISC_CHIRP(19436, 1),
    MUSIC_DISC_FAR(31742, 1),
    MUSIC_DISC_MALL(11517, 1),
    MUSIC_DISC_MELLOHI(26117, 1),
    MUSIC_DISC_STAL(14989, 1),
    MUSIC_DISC_STRAD(16785, 1),
    MUSIC_DISC_WARD(24026, 1),
    MUSIC_DISC_11(27426, 1),
    MUSIC_DISC_WAIT(26499, 1),
    MUSIC_DISC_OTHERSIDE(12974, 1),
    MUSIC_DISC_RELIC(8200, 1),
    MUSIC_DISC_5(9212, 1),
    MUSIC_DISC_PIGSTEP(21323, 1),
    DISC_FRAGMENT_5(29729),
    TRIDENT(7534, 1, 250),
    PHANTOM_MEMBRANE(18398),
    NAUTILUS_SHELL(19989),
    HEART_OF_THE_SEA(11807),
    CROSSBOW(4340, 1, 465),
    SUSPICIOUS_STEW(8173, 1),
    /**
     * BlockData: {@link Directional}
     */
    LOOM(14276, Directional.class),
    FLOWER_BANNER_PATTERN(5762, 1),
    CREEPER_BANNER_PATTERN(15774, 1),
    SKULL_BANNER_PATTERN(7680, 1),
    MOJANG_BANNER_PATTERN(11903, 1),
    GLOBE_BANNER_PATTERN(27753, 1),
    PIGLIN_BANNER_PATTERN(22028, 1),
    FLOW_BANNER_PATTERN(32683, 1),
    GUSTER_BANNER_PATTERN(27267, 1),
    GOAT_HORN(28237, 1),
    /**
     * BlockData: {@link Levelled}
     */
    COMPOSTER(31247, Levelled.class),
    /**
     * BlockData: {@link Barrel}
     */
    BARREL(22396, Barrel.class),
    /**
     * BlockData: {@link Furnace}
     */
    SMOKER(24781, Furnace.class),
    /**
     * BlockData: {@link Furnace}
     */
    BLAST_FURNACE(31157, Furnace.class),
    CARTOGRAPHY_TABLE(28529),
    FLETCHING_TABLE(30838),
    /**
     * BlockData: {@link Grindstone}
     */
    GRINDSTONE(26260, Grindstone.class),
    SMITHING_TABLE(9082),
    /**
     * BlockData: {@link Directional}
     */
    STONECUTTER(25170, Directional.class),
    /**
     * BlockData: {@link Bell}
     */
    BELL(20000, Bell.class),
    /**
     * BlockData: {@link Lantern}
     */
    LANTERN(5992, Lantern.class),
    /**
     * BlockData: {@link Lantern}
     */
    SOUL_LANTERN(27778, Lantern.class),
    SWEET_BERRIES(19747),
    GLOW_BERRIES(11584),
    /**
     * BlockData: {@link Campfire}
     */
    CAMPFIRE(8488, Campfire.class),
    /**
     * BlockData: {@link Campfire}
     */
    SOUL_CAMPFIRE(4238, Campfire.class),
    SHROOMLIGHT(20424),
    HONEYCOMB(9482),
    /**
     * BlockData: {@link Beehive}
     */
    BEE_NEST(8825, Beehive.class),
    /**
     * BlockData: {@link Beehive}
     */
    BEEHIVE(11830, Beehive.class),
    HONEY_BOTTLE(22927, 16),
    HONEYCOMB_BLOCK(28780),
    LODESTONE(23127),
    CRYING_OBSIDIAN(31545),
    BLACKSTONE(7354),
    /**
     * BlockData: {@link Slab}
     */
    BLACKSTONE_SLAB(11948, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    BLACKSTONE_STAIRS(14646, Stairs.class),
    GILDED_BLACKSTONE(8498),
    POLISHED_BLACKSTONE(18144),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_BLACKSTONE_SLAB(23430, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_BLACKSTONE_STAIRS(8653, Stairs.class),
    CHISELED_POLISHED_BLACKSTONE(21942),
    POLISHED_BLACKSTONE_BRICKS(19844),
    /**
     * BlockData: {@link Slab}
     */
    POLISHED_BLACKSTONE_BRICK_SLAB(12219, Slab.class),
    /**
     * BlockData: {@link Stairs}
     */
    POLISHED_BLACKSTONE_BRICK_STAIRS(17983, Stairs.class),
    CRACKED_POLISHED_BLACKSTONE_BRICKS(16846),
    /**
     * BlockData: {@link RespawnAnchor}
     */
    RESPAWN_ANCHOR(4099, RespawnAnchor.class),
    /**
     * BlockData: {@link Candle}
     */
    CANDLE(16122, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    WHITE_CANDLE(26410, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    ORANGE_CANDLE(22668, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    MAGENTA_CANDLE(25467, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    LIGHT_BLUE_CANDLE(28681, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    YELLOW_CANDLE(14351, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    LIME_CANDLE(21778, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    PINK_CANDLE(28259, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    GRAY_CANDLE(10721, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    LIGHT_GRAY_CANDLE(10031, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    CYAN_CANDLE(24765, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    PURPLE_CANDLE(19606, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    BLUE_CANDLE(29047, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    BROWN_CANDLE(26145, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    GREEN_CANDLE(29756, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    RED_CANDLE(4214, Candle.class),
    /**
     * BlockData: {@link Candle}
     */
    BLACK_CANDLE(12617, Candle.class),
    /**
     * BlockData: {@link AmethystCluster}
     */
    SMALL_AMETHYST_BUD(14958, AmethystCluster.class),
    /**
     * BlockData: {@link AmethystCluster}
     */
    MEDIUM_AMETHYST_BUD(8429, AmethystCluster.class),
    /**
     * BlockData: {@link AmethystCluster}
     */
    LARGE_AMETHYST_BUD(7279, AmethystCluster.class),
    /**
     * BlockData: {@link AmethystCluster}
     */
    AMETHYST_CLUSTER(13142, AmethystCluster.class),
    /**
     * BlockData: {@link PointedDripstone}
     */
    POINTED_DRIPSTONE(18755, PointedDripstone.class),
    /**
     * BlockData: {@link Orientable}
     */
    OCHRE_FROGLIGHT(25330, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    VERDANT_FROGLIGHT(22793, Orientable.class),
    /**
     * BlockData: {@link Orientable}
     */
    PEARLESCENT_FROGLIGHT(21441, Orientable.class),
    FROGSPAWN(8350),
    ECHO_SHARD(12529),
    BRUSH(30569, 1, 64),
    NETHERITE_UPGRADE_SMITHING_TEMPLATE(7615),
    SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE(16124),
    DUNE_ARMOR_TRIM_SMITHING_TEMPLATE(30925),
    COAST_ARMOR_TRIM_SMITHING_TEMPLATE(25501),
    WILD_ARMOR_TRIM_SMITHING_TEMPLATE(5870),
    WARD_ARMOR_TRIM_SMITHING_TEMPLATE(24534),
    EYE_ARMOR_TRIM_SMITHING_TEMPLATE(14663),
    VEX_ARMOR_TRIM_SMITHING_TEMPLATE(25818),
    TIDE_ARMOR_TRIM_SMITHING_TEMPLATE(20420),
    SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE(14386),
    RIB_ARMOR_TRIM_SMITHING_TEMPLATE(6010),
    SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE(29143),
    WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE(4957),
    SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE(20537),
    SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE(7070),
    RAISER_ARMOR_TRIM_SMITHING_TEMPLATE(29116),
    HOST_ARMOR_TRIM_SMITHING_TEMPLATE(12165),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    FLOW_ARMOR_TRIM_SMITHING_TEMPLATE(29175),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    BOLT_ARMOR_TRIM_SMITHING_TEMPLATE(9698),
    ANGLER_POTTERY_SHERD(9952),
    ARCHER_POTTERY_SHERD(21629),
    ARMS_UP_POTTERY_SHERD(5484),
    BLADE_POTTERY_SHERD(25079),
    BREWER_POTTERY_SHERD(23429),
    BURN_POTTERY_SHERD(21259),
    DANGER_POTTERY_SHERD(30506),
    EXPLORER_POTTERY_SHERD(5124),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    FLOW_POTTERY_SHERD(4896),
    FRIEND_POTTERY_SHERD(18221),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    GUSTER_POTTERY_SHERD(28193),
    HEART_POTTERY_SHERD(17607),
    HEARTBREAK_POTTERY_SHERD(21108),
    HOWL_POTTERY_SHERD(24900),
    MINER_POTTERY_SHERD(30602),
    MOURNER_POTTERY_SHERD(23993),
    PLENTY_POTTERY_SHERD(28236),
    PRIZE_POTTERY_SHERD(4341),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    SCRAPE_POTTERY_SHERD(30034),
    SHEAF_POTTERY_SHERD(23652),
    SHELTER_POTTERY_SHERD(28390),
    SKULL_POTTERY_SHERD(16980),
    SNORT_POTTERY_SHERD(15921),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    COPPER_GRATE(16221, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    EXPOSED_COPPER_GRATE(7783, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WEATHERED_COPPER_GRATE(24954, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OXIDIZED_COPPER_GRATE(14122, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_COPPER_GRATE(11230, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_EXPOSED_COPPER_GRATE(20520, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_WEATHERED_COPPER_GRATE(16533, Waterlogged.class),
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_OXIDIZED_COPPER_GRATE(32010, Waterlogged.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    COPPER_BULB(21370, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    EXPOSED_COPPER_BULB(11944, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WEATHERED_COPPER_BULB(10800, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OXIDIZED_COPPER_BULB(22421, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_COPPER_BULB(23756, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_EXPOSED_COPPER_BULB(5530, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_WEATHERED_COPPER_BULB(13239, CopperBulb.class),
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    WAXED_OXIDIZED_COPPER_BULB(26892, CopperBulb.class),
    /**
     * BlockData: {@link TrialSpawner}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TRIAL_SPAWNER(19902, TrialSpawner.class),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    TRIAL_KEY(12725),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OMINOUS_TRIAL_KEY(4986),
    /**
     * BlockData: {@link Vault}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    VAULT(6288, Vault.class),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    OMINOUS_BOTTLE(26321),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    BREEZE_ROD(14281),
    /**
     * BlockData: {@link Levelled}
     */
    WATER(24998, Levelled.class),
    /**
     * BlockData: {@link Levelled}
     */
    LAVA(8415, Levelled.class),
    /**
     * BlockData: {@link Bisected}
     */
    TALL_SEAGRASS(27189, Bisected.class),
    /**
     * BlockData: {@link PistonHead}
     */
    PISTON_HEAD(30226, PistonHead.class),
    /**
     * BlockData: {@link TechnicalPiston}
     */
    MOVING_PISTON(13831, TechnicalPiston.class),
    /**
     * BlockData: {@link Directional}
     */
    WALL_TORCH(25890, Directional.class),
    /**
     * BlockData: {@link Fire}
     */
    FIRE(16396, Fire.class),
    SOUL_FIRE(30163),
    /**
     * BlockData: {@link RedstoneWire}
     */
    REDSTONE_WIRE(25984, RedstoneWire.class),
    /**
     * BlockData: {@link WallSign}
     */
    OAK_WALL_SIGN(12984, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    SPRUCE_WALL_SIGN(7352, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    BIRCH_WALL_SIGN(9887, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    ACACIA_WALL_SIGN(20316, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    CHERRY_WALL_SIGN(20188, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    JUNGLE_WALL_SIGN(29629, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    DARK_OAK_WALL_SIGN(9508, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    MANGROVE_WALL_SIGN(27203, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    BAMBOO_WALL_SIGN(18857, 16, WallSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    OAK_WALL_HANGING_SIGN(15637, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    SPRUCE_WALL_HANGING_SIGN(18833, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    BIRCH_WALL_HANGING_SIGN(15937, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    ACACIA_WALL_HANGING_SIGN(22477, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    CHERRY_WALL_HANGING_SIGN(10953, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    JUNGLE_WALL_HANGING_SIGN(16691, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    DARK_OAK_WALL_HANGING_SIGN(14296, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    MANGROVE_WALL_HANGING_SIGN(16974, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    CRIMSON_WALL_HANGING_SIGN(28982, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    WARPED_WALL_HANGING_SIGN(20605, WallHangingSign.class),
    /**
     * BlockData: {@link WallHangingSign}
     */
    BAMBOO_WALL_HANGING_SIGN(6669, WallHangingSign.class),
    /**
     * BlockData: {@link RedstoneWallTorch}
     */
    REDSTONE_WALL_TORCH(7595, RedstoneWallTorch.class),
    /**
     * BlockData: {@link Directional}
     */
    SOUL_WALL_TORCH(27500, Directional.class),
    /**
     * BlockData: {@link Orientable}
     */
    NETHER_PORTAL(19469, Orientable.class),
    /**
     * BlockData: {@link Directional}
     */
    ATTACHED_PUMPKIN_STEM(12724, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    ATTACHED_MELON_STEM(30882, Directional.class),
    /**
     * BlockData: {@link Ageable}
     */
    PUMPKIN_STEM(19021, Ageable.class),
    /**
     * BlockData: {@link Ageable}
     */
    MELON_STEM(8247, Ageable.class),
    /**
     * BlockData: {@link Levelled}
     */
    WATER_CAULDRON(32008, Levelled.class),
    LAVA_CAULDRON(4514),
    /**
     * BlockData: {@link Levelled}
     */
    POWDER_SNOW_CAULDRON(31571, Levelled.class),
    END_PORTAL(16782),
    /**
     * BlockData: {@link Cocoa}
     */
    COCOA(29709, Cocoa.class),
    /**
     * BlockData: {@link Tripwire}
     */
    TRIPWIRE(8810, Tripwire.class),
    POTTED_TORCHFLOWER(21278),
    POTTED_OAK_SAPLING(11905),
    POTTED_SPRUCE_SAPLING(29498),
    POTTED_BIRCH_SAPLING(32484),
    POTTED_JUNGLE_SAPLING(7525),
    POTTED_ACACIA_SAPLING(14096),
    POTTED_CHERRY_SAPLING(30785),
    POTTED_DARK_OAK_SAPLING(6486),
    POTTED_MANGROVE_PROPAGULE(22003),
    POTTED_FERN(23315),
    POTTED_DANDELION(9727),
    POTTED_POPPY(7457),
    POTTED_BLUE_ORCHID(6599),
    POTTED_ALLIUM(13184),
    POTTED_AZURE_BLUET(8754),
    POTTED_RED_TULIP(28594),
    POTTED_ORANGE_TULIP(28807),
    POTTED_WHITE_TULIP(24330),
    POTTED_PINK_TULIP(10089),
    POTTED_OXEYE_DAISY(19707),
    POTTED_CORNFLOWER(28917),
    POTTED_LILY_OF_THE_VALLEY(9364),
    POTTED_WITHER_ROSE(26876),
    POTTED_RED_MUSHROOM(22881),
    POTTED_BROWN_MUSHROOM(14481),
    POTTED_DEAD_BUSH(13020),
    POTTED_CACTUS(8777),
    /**
     * BlockData: {@link Ageable}
     */
    CARROTS(17258, Ageable.class),
    /**
     * BlockData: {@link Ageable}
     */
    POTATOES(10879, Ageable.class),
    /**
     * BlockData: {@link Directional}
     */
    SKELETON_WALL_SKULL(31650, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    WITHER_SKELETON_WALL_SKULL(9326, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    ZOMBIE_WALL_HEAD(16296, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PLAYER_WALL_HEAD(13164, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    CREEPER_WALL_HEAD(30123, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    DRAGON_WALL_HEAD(19818, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PIGLIN_WALL_HEAD(4446, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    WHITE_WALL_BANNER(15967, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    ORANGE_WALL_BANNER(9936, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    MAGENTA_WALL_BANNER(23291, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_BLUE_WALL_BANNER(12011, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    YELLOW_WALL_BANNER(32004, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIME_WALL_BANNER(21422, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PINK_WALL_BANNER(9421, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GRAY_WALL_BANNER(24275, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    LIGHT_GRAY_WALL_BANNER(31088, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    CYAN_WALL_BANNER(10889, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    PURPLE_WALL_BANNER(14298, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLUE_WALL_BANNER(17757, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BROWN_WALL_BANNER(14731, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    GREEN_WALL_BANNER(15046, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    RED_WALL_BANNER(4378, Directional.class),
    /**
     * BlockData: {@link Directional}
     */
    BLACK_WALL_BANNER(4919, Directional.class),
    /**
     * BlockData: {@link Ageable}
     */
    TORCHFLOWER_CROP(28460, Ageable.class),
    /**
     * BlockData: {@link PitcherCrop}
     */
    PITCHER_CROP(15420, PitcherCrop.class),
    /**
     * BlockData: {@link Ageable}
     */
    BEETROOTS(22075, Ageable.class),
    END_GATEWAY(26605),
    /**
     * BlockData: {@link Ageable}
     */
    FROSTED_ICE(21814, Ageable.class),
    KELP_PLANT(29697),
    /**
     * BlockData: {@link CoralWallFan}
     */
    DEAD_TUBE_CORAL_WALL_FAN(5128, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    DEAD_BRAIN_CORAL_WALL_FAN(23718, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    DEAD_BUBBLE_CORAL_WALL_FAN(18453, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    DEAD_FIRE_CORAL_WALL_FAN(23375, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    DEAD_HORN_CORAL_WALL_FAN(27550, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    TUBE_CORAL_WALL_FAN(25282, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    BRAIN_CORAL_WALL_FAN(22685, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    BUBBLE_CORAL_WALL_FAN(20382, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    FIRE_CORAL_WALL_FAN(20100, CoralWallFan.class),
    /**
     * BlockData: {@link CoralWallFan}
     */
    HORN_CORAL_WALL_FAN(28883, CoralWallFan.class),
    BAMBOO_SAPLING(8478),
    POTTED_BAMBOO(22542),
    VOID_AIR(13668),
    CAVE_AIR(17422),
    /**
     * BlockData: {@link BubbleColumn}
     */
    BUBBLE_COLUMN(31612, BubbleColumn.class),
    /**
     * BlockData: {@link Ageable}
     */
    SWEET_BERRY_BUSH(11958, Ageable.class),
    WEEPING_VINES_PLANT(19437),
    TWISTING_VINES_PLANT(25338),
    /**
     * BlockData: {@link WallSign}
     */
    CRIMSON_WALL_SIGN(19242, 16, WallSign.class),
    /**
     * BlockData: {@link WallSign}
     */
    WARPED_WALL_SIGN(13534, 16, WallSign.class),
    POTTED_CRIMSON_FUNGUS(5548),
    POTTED_WARPED_FUNGUS(30800),
    POTTED_CRIMSON_ROOTS(13852),
    POTTED_WARPED_ROOTS(6403),
    /**
     * BlockData: {@link Lightable}
     */
    CANDLE_CAKE(25423, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    WHITE_CANDLE_CAKE(12674, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    ORANGE_CANDLE_CAKE(24982, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    MAGENTA_CANDLE_CAKE(11022, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    LIGHT_BLUE_CANDLE_CAKE(7787, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    YELLOW_CANDLE_CAKE(17157, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    LIME_CANDLE_CAKE(14309, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    PINK_CANDLE_CAKE(20405, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    GRAY_CANDLE_CAKE(6777, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    LIGHT_GRAY_CANDLE_CAKE(11318, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    CYAN_CANDLE_CAKE(21202, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    PURPLE_CANDLE_CAKE(22663, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    BLUE_CANDLE_CAKE(26425, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    BROWN_CANDLE_CAKE(26024, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    GREEN_CANDLE_CAKE(16334, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    RED_CANDLE_CAKE(24151, Lightable.class),
    /**
     * BlockData: {@link Lightable}
     */
    BLACK_CANDLE_CAKE(15191, Lightable.class),
    POWDER_SNOW(24077),
    /**
     * BlockData: {@link CaveVines}
     */
    CAVE_VINES(7339, CaveVines.class),
    /**
     * BlockData: {@link CaveVinesPlant}
     */
    CAVE_VINES_PLANT(30645, CaveVinesPlant.class),
    /**
     * BlockData: {@link Dripleaf}
     */
    BIG_DRIPLEAF_STEM(13167, Dripleaf.class),
    POTTED_AZALEA_BUSH(20430),
    POTTED_FLOWERING_AZALEA_BUSH(10609),
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
    private static final Map<String, Material> BY_NAME = Maps.newHashMap();
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

    private Material(final int id, /*@NotNull*/ final Class<?> data) {
        this(id, 64, data);
    }

    private Material(final int id, final int stack, /*@NotNull*/ final Class<?> data) {
        this(id, stack, 0, data);
    }

    private Material(final int id, final int stack, final int durability, /*@NotNull*/ final Class<?> data) {
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
        Preconditions.checkArgument(legacy, "Cannot get ID of Modern Material");
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

    @NotNull
    @Override
    public NamespacedKey getKey() {
        Preconditions.checkArgument(!legacy, "Cannot get key of Legacy Material");
        return key;
    }

    /**
     * Gets the maximum amount of this material that can be held in a stack.
     * <p>
     * Note that this is the <strong>default</strong> maximum size for this Material.
     * {@link ItemStack ItemStacks} are able to change their maximum stack size per
     * stack with {@link ItemMeta#setMaxStackSize(Integer)}. If an ItemStack instance
     * is available, {@link ItemStack#getMaxStackSize()} may be preferred.
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
    @NotNull
    public BlockData createBlockData() {
        return Bukkit.createBlockData(this);
    }

    /**
     * Creates a new {@link BlockData} instance for this Material, with
     * all properties initialized to unspecified defaults.
     *
     * @param consumer consumer to run on new instance before returning
     * @return new data instance
     */
    @NotNull
    public BlockData createBlockData(@Nullable Consumer<? super BlockData> consumer) {
        return Bukkit.createBlockData(this, consumer);
    }

    /**
     * Creates a new {@link BlockData} instance for this Material, with all
     * properties initialized to unspecified defaults, except for those provided
     * in data.
     *
     * @param data data string
     * @return new data instance
     * @throws IllegalArgumentException if the specified data is not valid
     */
    @NotNull
    public BlockData createBlockData(@Nullable String data) throws IllegalArgumentException {
        return Bukkit.createBlockData(this, data);
    }

    /**
     * Gets the MaterialData class associated with this Material
     *
     * @return MaterialData associated with this Material
     */
    @NotNull
    public Class<? extends MaterialData> getData() {
        Preconditions.checkArgument(legacy, "Cannot get data class of Modern Material");
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
    @NotNull
    public MaterialData getNewData(final byte raw) {
        Preconditions.checkArgument(legacy, "Cannot get new data of Modern Material");
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
        return asBlockType() != null;
    }

    /**
     * Checks if this Material is edible.
     *
     * @return true if this Material is edible.
     */
    public boolean isEdible() {
        ItemType type = asItemType();
        return type == null ? false : type.isEdible();
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
    @Nullable
    public static Material getMaterial(@NotNull final String name) {
        return getMaterial(name, false);
    }

    /**
     * Attempts to get the Material with the given name.
     * <p>
     * This is a normal lookup, names must be the precise name they are given in
     * the enum (but optionally including the LEGACY_PREFIX if legacyName is
     * true).
     * <p>
     * If legacyName is true, then the lookup will be against legacy materials,
     * but the returned Material will be a modern material (ie this method is
     * useful for updating stored data).
     *
     * @param name Name of the material to get
     * @param legacyName whether this is a legacy name lookup
     * @return Material if found, or null
     */
    @Nullable
    public static Material getMaterial(@NotNull String name, boolean legacyName) {
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
     * This is a match lookup; names will be stripped of the "minecraft:"
     * namespace, converted to uppercase, then stripped of special characters in
     * an attempt to format it like the enum.
     *
     * @param name Name of the material to get
     * @return Material if found, or null
     */
    @Nullable
    public static Material matchMaterial(@NotNull final String name) {
        return matchMaterial(name, false);
    }

    /**
     * Attempts to match the Material with the given name.
     * <p>
     * This is a match lookup; names will be stripped of the "minecraft:"
     * namespace, converted to uppercase, then stripped of special characters in
     * an attempt to format it like the enum.
     *
     * @param name Name of the material to get
     * @param legacyName whether this is a legacy name (see
     * {@link #getMaterial(java.lang.String, boolean)}
     * @return Material if found, or null
     */
    @Nullable
    public static Material matchMaterial(@NotNull final String name, boolean legacyName) {
        Preconditions.checkArgument(name != null, "Name cannot be null");

        String filtered = name;
        if (filtered.startsWith(NamespacedKey.MINECRAFT + ":")) {
            filtered = filtered.substring((NamespacedKey.MINECRAFT + ":").length());
        }

        filtered = filtered.toUpperCase(java.util.Locale.ENGLISH);

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
        ItemType type = asItemType();
        return type != null && type.isRecord();
    }

    /**
     * Check if the material is a block and solid (can be built upon)
     *
     * @return True if this material is a block and solid
     */
    public boolean isSolid() {
        BlockType type = asBlockType();
        return type != null && type.isSolid();
    }

    /**
     * Check if the material is an air block.
     *
     * @return True if this material is an air block.
     */
    public boolean isAir() {
        BlockType type = asBlockType();
        return type != null && type.isAir();
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
            case POTTED_AZALEA_BUSH:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_FLOWERING_AZALEA_BUSH:
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
            case SHORT_GRASS:
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
        BlockType type = asBlockType();
        return type != null && type.isFlammable();
    }

    /**
     * Check if the material is a block and can burn away
     *
     * @return True if this material is a block and can burn away
     */
    public boolean isBurnable() {
        BlockType type = asBlockType();
        return type != null && type.isBurnable();
    }

    /**
     * Checks if this Material can be used as fuel in a Furnace
     *
     * @return true if this Material can be used as fuel.
     */
    public boolean isFuel() {
        ItemType type = asItemType();
        return type != null && type.isFuel();
    }

    /**
     * Check if the material is a block and occludes light in the lighting engine.
     * <p>
     * Generally speaking, most full blocks will occlude light. Non-full blocks are
     * not occluding (e.g. anvils, chests, tall grass, stairs, etc.), nor are specific
     * full blocks such as barriers or spawners which block light despite their texture.
     * <p>
     * An occluding block will have the following effects:
     * <ul>
     *   <li>Chests cannot be opened if an occluding block is above it.
     *   <li>Mobs cannot spawn inside of occluding blocks.
     *   <li>Only occluding blocks can be "powered" ({@link Block#isBlockPowered()}).
     * </ul>
     * This list may be inconclusive. For a full list of the side effects of an occluding
     * block, see the <a href="https://minecraft.wiki/w/Opacity">Minecraft Wiki</a>.
     *
     * @return True if this material is a block and occludes light
     */
    public boolean isOccluding() {
        BlockType type = asBlockType();
        return type != null && type.isOccluding();
    }

    /**
     * @return True if this material is affected by gravity.
     */
    public boolean hasGravity() {
        BlockType type = asBlockType();
        return type != null && type.hasGravity();
    }

    /**
     * Checks if this Material is an obtainable item.
     *
     * @return true if this material is an item
     */
    public boolean isItem() {
        return asItemType() != null;
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
        BlockType type = asBlockType();
        return type != null && type.isInteractable();
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
        BlockType type = asBlockType();
        Preconditions.checkArgument(type != null, "The Material is not a block!");
        return type.getHardness();

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
        BlockType type = asBlockType();
        Preconditions.checkArgument(type != null, "The Material is not a block!");
        return type.getBlastResistance();
    }

    /**
     * Returns a value that represents how 'slippery' the block is.
     *
     * Blocks with higher slipperiness, like {@link Material#ICE} can be slid on
     * further by the player and other entities.
     *
     * Most blocks have a default slipperiness of {@code 0.6f}.
     *
     * Only available when {@link #isBlock()} is true.
     *
     * @return the slipperiness of this block
     */
    public float getSlipperiness() {
        BlockType type = asBlockType();
        Preconditions.checkArgument(type != null, "The Material is not a block!");
        return type.getSlipperiness();
    }

    /**
     * Determines the remaining item in a crafting grid after crafting with this
     * ingredient.
     * <br>
     * Only available when {@link #isItem()} is true.
     *
     * @return the item left behind when crafting, or null if nothing is.
     */
    @Nullable
    public Material getCraftingRemainingItem() {
        ItemType type = asItemType();
        Preconditions.checkArgument(type != null, "The Material is not an item!");
        return type.getCraftingRemainingItem() == null ? null : type.getCraftingRemainingItem().asMaterial();
    }

    /**
     * Get the best suitable slot for this Material.
     *
     * For most items this will be {@link EquipmentSlot#HAND}.
     *
     * @return the best EquipmentSlot for this Material
     */
    @NotNull
    public EquipmentSlot getEquipmentSlot() {
        Preconditions.checkArgument(isItem(), "The Material is not an item!");
        switch (this) {
            // <editor-fold defaultstate="collapsed" desc="getEquipmentSlot">
            case CARVED_PUMPKIN:
            case CHAINMAIL_HELMET:
            case CREEPER_HEAD:
            case DIAMOND_HELMET:
            case DRAGON_HEAD:
            case GOLDEN_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
            case NETHERITE_HELMET:
            case PLAYER_HEAD:
            case PIGLIN_HEAD:
            case SKELETON_SKULL:
            case TURTLE_HELMET:
            case WITHER_SKELETON_SKULL:
            case ZOMBIE_HEAD:
                return EquipmentSlot.HEAD;
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case ELYTRA:
            case GOLDEN_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
            case NETHERITE_CHESTPLATE:
                return EquipmentSlot.CHEST;
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLDEN_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
            case NETHERITE_LEGGINGS:
                return EquipmentSlot.LEGS;
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLDEN_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
            case NETHERITE_BOOTS:
                return EquipmentSlot.FEET;
            case SHIELD:
                return EquipmentSlot.OFF_HAND;
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BROWN_CARPET:
            case CYAN_CARPET:
            case DIAMOND_HORSE_ARMOR:
            case GOLDEN_HORSE_ARMOR:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case IRON_HORSE_ARMOR:
            case LEATHER_HORSE_ARMOR:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case ORANGE_CARPET:
            case PINK_CARPET:
            case PURPLE_CARPET:
            case RED_CARPET:
            case WHITE_CARPET:
            case WOLF_ARMOR:
            case YELLOW_CARPET:
                return EquipmentSlot.BODY;
            default:
                return EquipmentSlot.HAND;
            // </editor-fold>
        }
    }

    /**
     * Return an immutable copy of all default {@link Attribute}s and their
     * {@link AttributeModifier}s for a given {@link EquipmentSlot}.
     *
     * Default attributes are those that are always preset on some items, such
     * as the attack damage on weapons or the armor value on armor.
     *
     * Only available when {@link #isItem()} is true.
     *
     * @param slot the {@link EquipmentSlot} to check
     * @return the immutable {@link Multimap} with the respective default
     * Attributes and modifiers, or an empty map if no attributes are set.
     */
    @NotNull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        ItemType type = asItemType();
        Preconditions.checkArgument(type != null, "The Material is not an item!");
        return type.getDefaultAttributeModifiers(slot);
    }

    /**
     * Get the {@link CreativeCategory} to which this material belongs.
     *
     * @return the creative category. null if does not belong to a category
     */
    @Nullable
    public CreativeCategory getCreativeCategory() {
        ItemType type = asItemType();
        return type == null ? null : type.getCreativeCategory();
    }

    /**
     * Get the translation key of the item or block associated with this
     * material.
     *
     * If this material has both an item and a block form, the item form is
     * used.
     *
     * @return the translation key of the item or block associated with this
     * material
     * @see #getBlockTranslationKey()
     * @see #getItemTranslationKey()
     */
    @Override
    @NotNull
    public String getTranslationKey() {
        if (this.isItem()) {
            return asItemType().getTranslationKey();
        } else {
            return asBlockType().getTranslationKey();
        }
    }

    /**
     * Get the translation key of the block associated with this material, or
     * null if this material does not have an associated block.
     *
     * @return the translation key of the block associated with this material,
     * or null if this material does not have an associated block
     */
    @Nullable
    public String getBlockTranslationKey() {
        BlockType type = asBlockType();
        return type == null ? null : type.getTranslationKey();
    }

    /**
     * Get the translation key of the item associated with this material, or
     * null if this material does not have an associated item.
     *
     * @return the translation key of the item associated with this material, or
     * null if this material does not have an associated item.
     */
    @Nullable
    public String getItemTranslationKey() {
        ItemType type = asItemType();
        return type == null ? null : type.getTranslationKey();
    }

    /**
     * Gets if the Material is enabled by the features in a world.
     *
     * @param world the world to check
     * @return true if this material can be used in this World.
     */
    public boolean isEnabledByFeature(@NotNull World world) {
        if (isItem()) {
            return Bukkit.getDataPackManager().isEnabledByFeature(asItemType(), world);
        }

        return Bukkit.getDataPackManager().isEnabledByFeature(asBlockType(), world);
    }

    /**
     * Tries to convert this Material to an item type
     *
     * @return the converted item type or null
     * @apiNote only for internal use
     */
    @ApiStatus.Internal
    @Nullable
    public ItemType asItemType() {
        Material material = this;
        if (isLegacy()) {
            material = Bukkit.getUnsafe().fromLegacy(this);
        }
        return Registry.ITEM.get(material.key);
    }

    /**
     * Tries to convert this Material to a block type
     *
     * @return the converted block type or null
     * @apiNote only for internal use
     */
    @ApiStatus.Internal
    @Nullable
    public BlockType asBlockType() {
        Material material = this;
        if (isLegacy()) {
            material = Bukkit.getUnsafe().fromLegacy(this);
        }
        return Registry.BLOCK.get(material.key);
    }
}
