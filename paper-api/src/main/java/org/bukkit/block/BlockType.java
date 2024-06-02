package org.bukkit.block;

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.World;
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
import org.bukkit.block.data.type.Wall;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * While this API is in a public interface, it is not intended for use by
 * plugins until further notice. The purpose of these types is to make
 * {@link Material} more maintenance friendly, but will in due time be the
 * official replacement for the aforementioned enum. Entirely incompatible
 * changes may occur. Do not use this API in plugins.
 */
@ApiStatus.Internal
public interface BlockType extends Keyed, Translatable {

    /**
     * Typed represents a subtype of {@link BlockType}s that have a known block
     * data type at compile time.
     *
     * @param <B> the generic type of the block data that represents the block
     * type.
     */
    interface Typed<B extends BlockData> extends BlockType {

        /**
         * Gets the BlockData class of this BlockType
         *
         * @return the BlockData class of this BlockType
         */
        @NotNull
        @Override
        Class<B> getBlockDataClass();

        /**
         * Creates a new {@link BlockData} instance for this block type, with
         * all properties initialized to unspecified defaults.
         *
         * @param consumer consumer to run on new instance before returning
         * @return new data instance
         */
        @NotNull
        B createBlockData(@Nullable Consumer<? super B> consumer);

        /**
         * Creates a new {@link BlockData} instance for this block type, with all
         * properties initialized to unspecified defaults.
         *
         * @return new data instance
         */
        @NotNull
        @Override
        B createBlockData();

        /**
         * Creates a new {@link BlockData} instance for this block type, with all
         * properties initialized to unspecified defaults, except for those provided
         * in data.
         *
         * @param data data string
         * @return new data instance
         * @throws IllegalArgumentException if the specified data is not valid
         */
        @NotNull
        B createBlockData(@Nullable String data);
    }

    //<editor-fold desc="BlockTypes" defaultstate="collapsed">
    BlockType.Typed<BlockData> AIR = getBlockType("air");
    BlockType.Typed<BlockData> STONE = getBlockType("stone");
    BlockType.Typed<BlockData> GRANITE = getBlockType("granite");
    BlockType.Typed<BlockData> POLISHED_GRANITE = getBlockType("polished_granite");
    BlockType.Typed<BlockData> DIORITE = getBlockType("diorite");
    BlockType.Typed<BlockData> POLISHED_DIORITE = getBlockType("polished_diorite");
    BlockType.Typed<BlockData> ANDESITE = getBlockType("andesite");
    BlockType.Typed<BlockData> POLISHED_ANDESITE = getBlockType("polished_andesite");
    /**
     * BlockData: {@link Snowable}
     */
    BlockType.Typed<Snowable> GRASS_BLOCK = getBlockType("grass_block");
    BlockType.Typed<BlockData> DIRT = getBlockType("dirt");
    BlockType.Typed<BlockData> COARSE_DIRT = getBlockType("coarse_dirt");
    /**
     * BlockData: {@link Snowable}
     */
    BlockType.Typed<Snowable> PODZOL = getBlockType("podzol");
    BlockType.Typed<BlockData> COBBLESTONE = getBlockType("cobblestone");
    BlockType.Typed<BlockData> OAK_PLANKS = getBlockType("oak_planks");
    BlockType.Typed<BlockData> SPRUCE_PLANKS = getBlockType("spruce_planks");
    BlockType.Typed<BlockData> BIRCH_PLANKS = getBlockType("birch_planks");
    BlockType.Typed<BlockData> JUNGLE_PLANKS = getBlockType("jungle_planks");
    BlockType.Typed<BlockData> ACACIA_PLANKS = getBlockType("acacia_planks");
    BlockType.Typed<BlockData> CHERRY_PLANKS = getBlockType("cherry_planks");
    BlockType.Typed<BlockData> DARK_OAK_PLANKS = getBlockType("dark_oak_planks");
    BlockType.Typed<BlockData> MANGROVE_PLANKS = getBlockType("mangrove_planks");
    BlockType.Typed<BlockData> BAMBOO_PLANKS = getBlockType("bamboo_planks");
    BlockType.Typed<BlockData> BAMBOO_MOSAIC = getBlockType("bamboo_mosaic");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> OAK_SAPLING = getBlockType("oak_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> SPRUCE_SAPLING = getBlockType("spruce_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> BIRCH_SAPLING = getBlockType("birch_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> JUNGLE_SAPLING = getBlockType("jungle_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> ACACIA_SAPLING = getBlockType("acacia_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> CHERRY_SAPLING = getBlockType("cherry_sapling");
    /**
     * BlockData: {@link Sapling}
     */
    BlockType.Typed<Sapling> DARK_OAK_SAPLING = getBlockType("dark_oak_sapling");
    /**
     * BlockData: {@link MangrovePropagule}
     */
    BlockType.Typed<MangrovePropagule> MANGROVE_PROPAGULE = getBlockType("mangrove_propagule");
    BlockType.Typed<BlockData> BEDROCK = getBlockType("bedrock");
    /**
     * BlockData: {@link Levelled}
     */
    BlockType.Typed<Levelled> WATER = getBlockType("water");
    /**
     * BlockData: {@link Levelled}
     */
    BlockType.Typed<Levelled> LAVA = getBlockType("lava");
    BlockType.Typed<BlockData> SAND = getBlockType("sand");
    /**
     * BlockData: {@link Brushable}
     */
    BlockType.Typed<Brushable> SUSPICIOUS_SAND = getBlockType("suspicious_sand");
    BlockType.Typed<BlockData> RED_SAND = getBlockType("red_sand");
    BlockType.Typed<BlockData> GRAVEL = getBlockType("gravel");
    /**
     * BlockData: {@link Brushable}
     */
    BlockType.Typed<Brushable> SUSPICIOUS_GRAVEL = getBlockType("suspicious_gravel");
    BlockType.Typed<BlockData> GOLD_ORE = getBlockType("gold_ore");
    BlockType.Typed<BlockData> DEEPSLATE_GOLD_ORE = getBlockType("deepslate_gold_ore");
    BlockType.Typed<BlockData> IRON_ORE = getBlockType("iron_ore");
    BlockType.Typed<BlockData> DEEPSLATE_IRON_ORE = getBlockType("deepslate_iron_ore");
    BlockType.Typed<BlockData> COAL_ORE = getBlockType("coal_ore");
    BlockType.Typed<BlockData> DEEPSLATE_COAL_ORE = getBlockType("deepslate_coal_ore");
    BlockType.Typed<BlockData> NETHER_GOLD_ORE = getBlockType("nether_gold_ore");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> OAK_LOG = getBlockType("oak_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> SPRUCE_LOG = getBlockType("spruce_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> BIRCH_LOG = getBlockType("birch_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> JUNGLE_LOG = getBlockType("jungle_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> ACACIA_LOG = getBlockType("acacia_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> CHERRY_LOG = getBlockType("cherry_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> DARK_OAK_LOG = getBlockType("dark_oak_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> MANGROVE_LOG = getBlockType("mangrove_log");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> MANGROVE_ROOTS = getBlockType("mangrove_roots");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> MUDDY_MANGROVE_ROOTS = getBlockType("muddy_mangrove_roots");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> BAMBOO_BLOCK = getBlockType("bamboo_block");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_SPRUCE_LOG = getBlockType("stripped_spruce_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_BIRCH_LOG = getBlockType("stripped_birch_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_JUNGLE_LOG = getBlockType("stripped_jungle_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_ACACIA_LOG = getBlockType("stripped_acacia_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_CHERRY_LOG = getBlockType("stripped_cherry_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_DARK_OAK_LOG = getBlockType("stripped_dark_oak_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_OAK_LOG = getBlockType("stripped_oak_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_MANGROVE_LOG = getBlockType("stripped_mangrove_log");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_BAMBOO_BLOCK = getBlockType("stripped_bamboo_block");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> OAK_WOOD = getBlockType("oak_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> SPRUCE_WOOD = getBlockType("spruce_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> BIRCH_WOOD = getBlockType("birch_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> JUNGLE_WOOD = getBlockType("jungle_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> ACACIA_WOOD = getBlockType("acacia_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> CHERRY_WOOD = getBlockType("cherry_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> DARK_OAK_WOOD = getBlockType("dark_oak_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> MANGROVE_WOOD = getBlockType("mangrove_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_OAK_WOOD = getBlockType("stripped_oak_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_SPRUCE_WOOD = getBlockType("stripped_spruce_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_BIRCH_WOOD = getBlockType("stripped_birch_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_JUNGLE_WOOD = getBlockType("stripped_jungle_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_ACACIA_WOOD = getBlockType("stripped_acacia_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_CHERRY_WOOD = getBlockType("stripped_cherry_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_DARK_OAK_WOOD = getBlockType("stripped_dark_oak_wood");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_MANGROVE_WOOD = getBlockType("stripped_mangrove_wood");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> OAK_LEAVES = getBlockType("oak_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> SPRUCE_LEAVES = getBlockType("spruce_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> BIRCH_LEAVES = getBlockType("birch_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> JUNGLE_LEAVES = getBlockType("jungle_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> ACACIA_LEAVES = getBlockType("acacia_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> CHERRY_LEAVES = getBlockType("cherry_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> DARK_OAK_LEAVES = getBlockType("dark_oak_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> MANGROVE_LEAVES = getBlockType("mangrove_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> AZALEA_LEAVES = getBlockType("azalea_leaves");
    /**
     * BlockData: {@link Leaves}
     */
    BlockType.Typed<Leaves> FLOWERING_AZALEA_LEAVES = getBlockType("flowering_azalea_leaves");
    BlockType.Typed<BlockData> SPONGE = getBlockType("sponge");
    BlockType.Typed<BlockData> WET_SPONGE = getBlockType("wet_sponge");
    BlockType.Typed<BlockData> GLASS = getBlockType("glass");
    BlockType.Typed<BlockData> LAPIS_ORE = getBlockType("lapis_ore");
    BlockType.Typed<BlockData> DEEPSLATE_LAPIS_ORE = getBlockType("deepslate_lapis_ore");
    BlockType.Typed<BlockData> LAPIS_BLOCK = getBlockType("lapis_block");
    /**
     * BlockData: {@link Dispenser}
     */
    BlockType.Typed<Dispenser> DISPENSER = getBlockType("dispenser");
    BlockType.Typed<BlockData> SANDSTONE = getBlockType("sandstone");
    BlockType.Typed<BlockData> CHISELED_SANDSTONE = getBlockType("chiseled_sandstone");
    BlockType.Typed<BlockData> CUT_SANDSTONE = getBlockType("cut_sandstone");
    /**
     * BlockData: {@link NoteBlock}
     */
    BlockType.Typed<NoteBlock> NOTE_BLOCK = getBlockType("note_block");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> WHITE_BED = getBlockType("white_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> ORANGE_BED = getBlockType("orange_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> MAGENTA_BED = getBlockType("magenta_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> LIGHT_BLUE_BED = getBlockType("light_blue_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> YELLOW_BED = getBlockType("yellow_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> LIME_BED = getBlockType("lime_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> PINK_BED = getBlockType("pink_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> GRAY_BED = getBlockType("gray_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> LIGHT_GRAY_BED = getBlockType("light_gray_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> CYAN_BED = getBlockType("cyan_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> PURPLE_BED = getBlockType("purple_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> BLUE_BED = getBlockType("blue_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> BROWN_BED = getBlockType("brown_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> GREEN_BED = getBlockType("green_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> RED_BED = getBlockType("red_bed");
    /**
     * BlockData: {@link Bed}
     */
    BlockType.Typed<Bed> BLACK_BED = getBlockType("black_bed");
    /**
     * BlockData: {@link RedstoneRail}
     */
    BlockType.Typed<RedstoneRail> POWERED_RAIL = getBlockType("powered_rail");
    /**
     * BlockData: {@link RedstoneRail}
     */
    BlockType.Typed<RedstoneRail> DETECTOR_RAIL = getBlockType("detector_rail");
    /**
     * BlockData: {@link Piston}
     */
    BlockType.Typed<Piston> STICKY_PISTON = getBlockType("sticky_piston");
    BlockType.Typed<BlockData> COBWEB = getBlockType("cobweb");
    BlockType.Typed<BlockData> SHORT_GRASS = getBlockType("short_grass");
    BlockType.Typed<BlockData> FERN = getBlockType("fern");
    BlockType.Typed<BlockData> DEAD_BUSH = getBlockType("dead_bush");
    BlockType.Typed<BlockData> SEAGRASS = getBlockType("seagrass");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> TALL_SEAGRASS = getBlockType("tall_seagrass");
    /**
     * BlockData: {@link Piston}
     */
    BlockType.Typed<Piston> PISTON = getBlockType("piston");
    /**
     * BlockData: {@link PistonHead}
     */
    BlockType.Typed<PistonHead> PISTON_HEAD = getBlockType("piston_head");
    BlockType.Typed<BlockData> WHITE_WOOL = getBlockType("white_wool");
    BlockType.Typed<BlockData> ORANGE_WOOL = getBlockType("orange_wool");
    BlockType.Typed<BlockData> MAGENTA_WOOL = getBlockType("magenta_wool");
    BlockType.Typed<BlockData> LIGHT_BLUE_WOOL = getBlockType("light_blue_wool");
    BlockType.Typed<BlockData> YELLOW_WOOL = getBlockType("yellow_wool");
    BlockType.Typed<BlockData> LIME_WOOL = getBlockType("lime_wool");
    BlockType.Typed<BlockData> PINK_WOOL = getBlockType("pink_wool");
    BlockType.Typed<BlockData> GRAY_WOOL = getBlockType("gray_wool");
    BlockType.Typed<BlockData> LIGHT_GRAY_WOOL = getBlockType("light_gray_wool");
    BlockType.Typed<BlockData> CYAN_WOOL = getBlockType("cyan_wool");
    BlockType.Typed<BlockData> PURPLE_WOOL = getBlockType("purple_wool");
    BlockType.Typed<BlockData> BLUE_WOOL = getBlockType("blue_wool");
    BlockType.Typed<BlockData> BROWN_WOOL = getBlockType("brown_wool");
    BlockType.Typed<BlockData> GREEN_WOOL = getBlockType("green_wool");
    BlockType.Typed<BlockData> RED_WOOL = getBlockType("red_wool");
    BlockType.Typed<BlockData> BLACK_WOOL = getBlockType("black_wool");
    /**
     * BlockData: {@link TechnicalPiston}
     */
    BlockType.Typed<TechnicalPiston> MOVING_PISTON = getBlockType("moving_piston");
    BlockType.Typed<BlockData> DANDELION = getBlockType("dandelion");
    BlockType.Typed<BlockData> TORCHFLOWER = getBlockType("torchflower");
    BlockType.Typed<BlockData> POPPY = getBlockType("poppy");
    BlockType.Typed<BlockData> BLUE_ORCHID = getBlockType("blue_orchid");
    BlockType.Typed<BlockData> ALLIUM = getBlockType("allium");
    BlockType.Typed<BlockData> AZURE_BLUET = getBlockType("azure_bluet");
    BlockType.Typed<BlockData> RED_TULIP = getBlockType("red_tulip");
    BlockType.Typed<BlockData> ORANGE_TULIP = getBlockType("orange_tulip");
    BlockType.Typed<BlockData> WHITE_TULIP = getBlockType("white_tulip");
    BlockType.Typed<BlockData> PINK_TULIP = getBlockType("pink_tulip");
    BlockType.Typed<BlockData> OXEYE_DAISY = getBlockType("oxeye_daisy");
    BlockType.Typed<BlockData> CORNFLOWER = getBlockType("cornflower");
    BlockType.Typed<BlockData> WITHER_ROSE = getBlockType("wither_rose");
    BlockType.Typed<BlockData> LILY_OF_THE_VALLEY = getBlockType("lily_of_the_valley");
    BlockType.Typed<BlockData> BROWN_MUSHROOM = getBlockType("brown_mushroom");
    BlockType.Typed<BlockData> RED_MUSHROOM = getBlockType("red_mushroom");
    BlockType.Typed<BlockData> GOLD_BLOCK = getBlockType("gold_block");
    BlockType.Typed<BlockData> IRON_BLOCK = getBlockType("iron_block");
    BlockType.Typed<BlockData> BRICKS = getBlockType("bricks");
    /**
     * BlockData: {@link TNT}
     */
    BlockType.Typed<TNT> TNT = getBlockType("tnt");
    BlockType.Typed<BlockData> BOOKSHELF = getBlockType("bookshelf");
    /**
     * BlockData: {@link ChiseledBookshelf}
     */
    BlockType.Typed<ChiseledBookshelf> CHISELED_BOOKSHELF = getBlockType("chiseled_bookshelf");
    BlockType.Typed<BlockData> MOSSY_COBBLESTONE = getBlockType("mossy_cobblestone");
    BlockType.Typed<BlockData> OBSIDIAN = getBlockType("obsidian");
    BlockType.Typed<BlockData> TORCH = getBlockType("torch");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> WALL_TORCH = getBlockType("wall_torch");
    /**
     * BlockData: {@link Fire}
     */
    BlockType.Typed<Fire> FIRE = getBlockType("fire");
    BlockType.Typed<BlockData> SOUL_FIRE = getBlockType("soul_fire");
    BlockType.Typed<BlockData> SPAWNER = getBlockType("spawner");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> OAK_STAIRS = getBlockType("oak_stairs");
    /**
     * BlockData: {@link Chest}
     */
    BlockType.Typed<Chest> CHEST = getBlockType("chest");
    /**
     * BlockData: {@link RedstoneWire}
     */
    BlockType.Typed<RedstoneWire> REDSTONE_WIRE = getBlockType("redstone_wire");
    BlockType.Typed<BlockData> DIAMOND_ORE = getBlockType("diamond_ore");
    BlockType.Typed<BlockData> DEEPSLATE_DIAMOND_ORE = getBlockType("deepslate_diamond_ore");
    BlockType.Typed<BlockData> DIAMOND_BLOCK = getBlockType("diamond_block");
    BlockType.Typed<BlockData> CRAFTING_TABLE = getBlockType("crafting_table");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> WHEAT = getBlockType("wheat");
    /**
     * BlockData: {@link Farmland}
     */
    BlockType.Typed<Farmland> FARMLAND = getBlockType("farmland");
    /**
     * BlockData: {@link Furnace}
     */
    BlockType.Typed<Furnace> FURNACE = getBlockType("furnace");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> OAK_SIGN = getBlockType("oak_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> SPRUCE_SIGN = getBlockType("spruce_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> BIRCH_SIGN = getBlockType("birch_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> ACACIA_SIGN = getBlockType("acacia_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> CHERRY_SIGN = getBlockType("cherry_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> JUNGLE_SIGN = getBlockType("jungle_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> DARK_OAK_SIGN = getBlockType("dark_oak_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> MANGROVE_SIGN = getBlockType("mangrove_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> BAMBOO_SIGN = getBlockType("bamboo_sign");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> OAK_DOOR = getBlockType("oak_door");
    /**
     * BlockData: {@link Ladder}
     */
    BlockType.Typed<Ladder> LADDER = getBlockType("ladder");
    /**
     * BlockData: {@link Rail}
     */
    BlockType.Typed<Rail> RAIL = getBlockType("rail");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> COBBLESTONE_STAIRS = getBlockType("cobblestone_stairs");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> OAK_WALL_SIGN = getBlockType("oak_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> SPRUCE_WALL_SIGN = getBlockType("spruce_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> BIRCH_WALL_SIGN = getBlockType("birch_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> ACACIA_WALL_SIGN = getBlockType("acacia_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> CHERRY_WALL_SIGN = getBlockType("cherry_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> JUNGLE_WALL_SIGN = getBlockType("jungle_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> DARK_OAK_WALL_SIGN = getBlockType("dark_oak_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> MANGROVE_WALL_SIGN = getBlockType("mangrove_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> BAMBOO_WALL_SIGN = getBlockType("bamboo_wall_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> OAK_HANGING_SIGN = getBlockType("oak_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> SPRUCE_HANGING_SIGN = getBlockType("spruce_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> BIRCH_HANGING_SIGN = getBlockType("birch_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> ACACIA_HANGING_SIGN = getBlockType("acacia_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> CHERRY_HANGING_SIGN = getBlockType("cherry_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> JUNGLE_HANGING_SIGN = getBlockType("jungle_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> DARK_OAK_HANGING_SIGN = getBlockType("dark_oak_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> CRIMSON_HANGING_SIGN = getBlockType("crimson_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> WARPED_HANGING_SIGN = getBlockType("warped_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> MANGROVE_HANGING_SIGN = getBlockType("mangrove_hanging_sign");
    /**
     * BlockData: {@link HangingSign}
     */
    BlockType.Typed<HangingSign> BAMBOO_HANGING_SIGN = getBlockType("bamboo_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> OAK_WALL_HANGING_SIGN = getBlockType("oak_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> SPRUCE_WALL_HANGING_SIGN = getBlockType("spruce_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> BIRCH_WALL_HANGING_SIGN = getBlockType("birch_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> ACACIA_WALL_HANGING_SIGN = getBlockType("acacia_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> CHERRY_WALL_HANGING_SIGN = getBlockType("cherry_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> JUNGLE_WALL_HANGING_SIGN = getBlockType("jungle_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> DARK_OAK_WALL_HANGING_SIGN = getBlockType("dark_oak_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> MANGROVE_WALL_HANGING_SIGN = getBlockType("mangrove_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> CRIMSON_WALL_HANGING_SIGN = getBlockType("crimson_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> WARPED_WALL_HANGING_SIGN = getBlockType("warped_wall_hanging_sign");
    /**
     * BlockData: {@link WallHangingSign}
     */
    BlockType.Typed<WallHangingSign> BAMBOO_WALL_HANGING_SIGN = getBlockType("bamboo_wall_hanging_sign");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> LEVER = getBlockType("lever");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> STONE_PRESSURE_PLATE = getBlockType("stone_pressure_plate");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> IRON_DOOR = getBlockType("iron_door");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> OAK_PRESSURE_PLATE = getBlockType("oak_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> SPRUCE_PRESSURE_PLATE = getBlockType("spruce_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> BIRCH_PRESSURE_PLATE = getBlockType("birch_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> JUNGLE_PRESSURE_PLATE = getBlockType("jungle_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> ACACIA_PRESSURE_PLATE = getBlockType("acacia_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> CHERRY_PRESSURE_PLATE = getBlockType("cherry_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> DARK_OAK_PRESSURE_PLATE = getBlockType("dark_oak_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> MANGROVE_PRESSURE_PLATE = getBlockType("mangrove_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> BAMBOO_PRESSURE_PLATE = getBlockType("bamboo_pressure_plate");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> REDSTONE_ORE = getBlockType("redstone_ore");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> DEEPSLATE_REDSTONE_ORE = getBlockType("deepslate_redstone_ore");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> REDSTONE_TORCH = getBlockType("redstone_torch");
    /**
     * BlockData: {@link RedstoneWallTorch}
     */
    BlockType.Typed<RedstoneWallTorch> REDSTONE_WALL_TORCH = getBlockType("redstone_wall_torch");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> STONE_BUTTON = getBlockType("stone_button");
    /**
     * BlockData: {@link Snow}
     */
    BlockType.Typed<Snow> SNOW = getBlockType("snow");
    BlockType.Typed<BlockData> ICE = getBlockType("ice");
    BlockType.Typed<BlockData> SNOW_BLOCK = getBlockType("snow_block");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> CACTUS = getBlockType("cactus");
    BlockType.Typed<BlockData> CLAY = getBlockType("clay");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> SUGAR_CANE = getBlockType("sugar_cane");
    /**
     * BlockData: {@link Jukebox}
     */
    BlockType.Typed<Jukebox> JUKEBOX = getBlockType("jukebox");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> OAK_FENCE = getBlockType("oak_fence");
    BlockType.Typed<BlockData> NETHERRACK = getBlockType("netherrack");
    BlockType.Typed<BlockData> SOUL_SAND = getBlockType("soul_sand");
    BlockType.Typed<BlockData> SOUL_SOIL = getBlockType("soul_soil");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> BASALT = getBlockType("basalt");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> POLISHED_BASALT = getBlockType("polished_basalt");
    BlockType.Typed<BlockData> SOUL_TORCH = getBlockType("soul_torch");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> SOUL_WALL_TORCH = getBlockType("soul_wall_torch");
    BlockType.Typed<BlockData> GLOWSTONE = getBlockType("glowstone");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> NETHER_PORTAL = getBlockType("nether_portal");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CARVED_PUMPKIN = getBlockType("carved_pumpkin");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> JACK_O_LANTERN = getBlockType("jack_o_lantern");
    /**
     * BlockData: {@link Cake}
     */
    BlockType.Typed<Cake> CAKE = getBlockType("cake");
    /**
     * BlockData: {@link Repeater}
     */
    BlockType.Typed<Repeater> REPEATER = getBlockType("repeater");
    BlockType.Typed<BlockData> WHITE_STAINED_GLASS = getBlockType("white_stained_glass");
    BlockType.Typed<BlockData> ORANGE_STAINED_GLASS = getBlockType("orange_stained_glass");
    BlockType.Typed<BlockData> MAGENTA_STAINED_GLASS = getBlockType("magenta_stained_glass");
    BlockType.Typed<BlockData> LIGHT_BLUE_STAINED_GLASS = getBlockType("light_blue_stained_glass");
    BlockType.Typed<BlockData> YELLOW_STAINED_GLASS = getBlockType("yellow_stained_glass");
    BlockType.Typed<BlockData> LIME_STAINED_GLASS = getBlockType("lime_stained_glass");
    BlockType.Typed<BlockData> PINK_STAINED_GLASS = getBlockType("pink_stained_glass");
    BlockType.Typed<BlockData> GRAY_STAINED_GLASS = getBlockType("gray_stained_glass");
    BlockType.Typed<BlockData> LIGHT_GRAY_STAINED_GLASS = getBlockType("light_gray_stained_glass");
    BlockType.Typed<BlockData> CYAN_STAINED_GLASS = getBlockType("cyan_stained_glass");
    BlockType.Typed<BlockData> PURPLE_STAINED_GLASS = getBlockType("purple_stained_glass");
    BlockType.Typed<BlockData> BLUE_STAINED_GLASS = getBlockType("blue_stained_glass");
    BlockType.Typed<BlockData> BROWN_STAINED_GLASS = getBlockType("brown_stained_glass");
    BlockType.Typed<BlockData> GREEN_STAINED_GLASS = getBlockType("green_stained_glass");
    BlockType.Typed<BlockData> RED_STAINED_GLASS = getBlockType("red_stained_glass");
    BlockType.Typed<BlockData> BLACK_STAINED_GLASS = getBlockType("black_stained_glass");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> OAK_TRAPDOOR = getBlockType("oak_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> SPRUCE_TRAPDOOR = getBlockType("spruce_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> BIRCH_TRAPDOOR = getBlockType("birch_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> JUNGLE_TRAPDOOR = getBlockType("jungle_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> ACACIA_TRAPDOOR = getBlockType("acacia_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> CHERRY_TRAPDOOR = getBlockType("cherry_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> DARK_OAK_TRAPDOOR = getBlockType("dark_oak_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> MANGROVE_TRAPDOOR = getBlockType("mangrove_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> BAMBOO_TRAPDOOR = getBlockType("bamboo_trapdoor");
    BlockType.Typed<BlockData> STONE_BRICKS = getBlockType("stone_bricks");
    BlockType.Typed<BlockData> MOSSY_STONE_BRICKS = getBlockType("mossy_stone_bricks");
    BlockType.Typed<BlockData> CRACKED_STONE_BRICKS = getBlockType("cracked_stone_bricks");
    BlockType.Typed<BlockData> CHISELED_STONE_BRICKS = getBlockType("chiseled_stone_bricks");
    BlockType.Typed<BlockData> PACKED_MUD = getBlockType("packed_mud");
    BlockType.Typed<BlockData> MUD_BRICKS = getBlockType("mud_bricks");
    BlockType.Typed<BlockData> INFESTED_STONE = getBlockType("infested_stone");
    BlockType.Typed<BlockData> INFESTED_COBBLESTONE = getBlockType("infested_cobblestone");
    BlockType.Typed<BlockData> INFESTED_STONE_BRICKS = getBlockType("infested_stone_bricks");
    BlockType.Typed<BlockData> INFESTED_MOSSY_STONE_BRICKS = getBlockType("infested_mossy_stone_bricks");
    BlockType.Typed<BlockData> INFESTED_CRACKED_STONE_BRICKS = getBlockType("infested_cracked_stone_bricks");
    BlockType.Typed<BlockData> INFESTED_CHISELED_STONE_BRICKS = getBlockType("infested_chiseled_stone_bricks");
    /**
     * BlockData: {@link MultipleFacing}
     */
    BlockType.Typed<MultipleFacing> BROWN_MUSHROOM_BLOCK = getBlockType("brown_mushroom_block");
    /**
     * BlockData: {@link MultipleFacing}
     */
    BlockType.Typed<MultipleFacing> RED_MUSHROOM_BLOCK = getBlockType("red_mushroom_block");
    /**
     * BlockData: {@link MultipleFacing}
     */
    BlockType.Typed<MultipleFacing> MUSHROOM_STEM = getBlockType("mushroom_stem");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> IRON_BARS = getBlockType("iron_bars");
    /**
     * BlockData: {@link Chain}
     */
    BlockType.Typed<Chain> CHAIN = getBlockType("chain");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> GLASS_PANE = getBlockType("glass_pane");
    BlockType.Typed<BlockData> PUMPKIN = getBlockType("pumpkin");
    BlockType.Typed<BlockData> MELON = getBlockType("melon");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ATTACHED_PUMPKIN_STEM = getBlockType("attached_pumpkin_stem");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ATTACHED_MELON_STEM = getBlockType("attached_melon_stem");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> PUMPKIN_STEM = getBlockType("pumpkin_stem");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> MELON_STEM = getBlockType("melon_stem");
    /**
     * BlockData: {@link MultipleFacing}
     */
    BlockType.Typed<MultipleFacing> VINE = getBlockType("vine");
    /**
     * BlockData: {@link GlowLichen}
     */
    BlockType.Typed<GlowLichen> GLOW_LICHEN = getBlockType("glow_lichen");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> OAK_FENCE_GATE = getBlockType("oak_fence_gate");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> BRICK_STAIRS = getBlockType("brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> STONE_BRICK_STAIRS = getBlockType("stone_brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> MUD_BRICK_STAIRS = getBlockType("mud_brick_stairs");
    /**
     * BlockData: {@link Snowable}
     */
    BlockType.Typed<Snowable> MYCELIUM = getBlockType("mycelium");
    BlockType.Typed<BlockData> LILY_PAD = getBlockType("lily_pad");
    BlockType.Typed<BlockData> NETHER_BRICKS = getBlockType("nether_bricks");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> NETHER_BRICK_FENCE = getBlockType("nether_brick_fence");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> NETHER_BRICK_STAIRS = getBlockType("nether_brick_stairs");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> NETHER_WART = getBlockType("nether_wart");
    BlockType.Typed<BlockData> ENCHANTING_TABLE = getBlockType("enchanting_table");
    /**
     * BlockData: {@link BrewingStand}
     */
    BlockType.Typed<BrewingStand> BREWING_STAND = getBlockType("brewing_stand");
    BlockType.Typed<BlockData> CAULDRON = getBlockType("cauldron");
    /**
     * BlockData: {@link Levelled}
     */
    BlockType.Typed<Levelled> WATER_CAULDRON = getBlockType("water_cauldron");
    BlockType.Typed<BlockData> LAVA_CAULDRON = getBlockType("lava_cauldron");
    /**
     * BlockData: {@link Levelled}
     */
    BlockType.Typed<Levelled> POWDER_SNOW_CAULDRON = getBlockType("powder_snow_cauldron");
    BlockType.Typed<BlockData> END_PORTAL = getBlockType("end_portal");
    /**
     * BlockData: {@link EndPortalFrame}
     */
    BlockType.Typed<EndPortalFrame> END_PORTAL_FRAME = getBlockType("end_portal_frame");
    BlockType.Typed<BlockData> END_STONE = getBlockType("end_stone");
    BlockType.Typed<BlockData> DRAGON_EGG = getBlockType("dragon_egg");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> REDSTONE_LAMP = getBlockType("redstone_lamp");
    /**
     * BlockData: {@link Cocoa}
     */
    BlockType.Typed<Cocoa> COCOA = getBlockType("cocoa");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> SANDSTONE_STAIRS = getBlockType("sandstone_stairs");
    BlockType.Typed<BlockData> EMERALD_ORE = getBlockType("emerald_ore");
    BlockType.Typed<BlockData> DEEPSLATE_EMERALD_ORE = getBlockType("deepslate_emerald_ore");
    /**
     * BlockData: {@link EnderChest}
     */
    BlockType.Typed<EnderChest> ENDER_CHEST = getBlockType("ender_chest");
    /**
     * BlockData: {@link TripwireHook}
     */
    BlockType.Typed<TripwireHook> TRIPWIRE_HOOK = getBlockType("tripwire_hook");
    /**
     * BlockData: {@link Tripwire}
     */
    BlockType.Typed<Tripwire> TRIPWIRE = getBlockType("tripwire");
    BlockType.Typed<BlockData> EMERALD_BLOCK = getBlockType("emerald_block");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> SPRUCE_STAIRS = getBlockType("spruce_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> BIRCH_STAIRS = getBlockType("birch_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> JUNGLE_STAIRS = getBlockType("jungle_stairs");
    /**
     * BlockData: {@link CommandBlock}
     */
    BlockType.Typed<CommandBlock> COMMAND_BLOCK = getBlockType("command_block");
    BlockType.Typed<BlockData> BEACON = getBlockType("beacon");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> COBBLESTONE_WALL = getBlockType("cobblestone_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> MOSSY_COBBLESTONE_WALL = getBlockType("mossy_cobblestone_wall");
    BlockType.Typed<BlockData> FLOWER_POT = getBlockType("flower_pot");
    BlockType.Typed<BlockData> POTTED_TORCHFLOWER = getBlockType("potted_torchflower");
    BlockType.Typed<BlockData> POTTED_OAK_SAPLING = getBlockType("potted_oak_sapling");
    BlockType.Typed<BlockData> POTTED_SPRUCE_SAPLING = getBlockType("potted_spruce_sapling");
    BlockType.Typed<BlockData> POTTED_BIRCH_SAPLING = getBlockType("potted_birch_sapling");
    BlockType.Typed<BlockData> POTTED_JUNGLE_SAPLING = getBlockType("potted_jungle_sapling");
    BlockType.Typed<BlockData> POTTED_ACACIA_SAPLING = getBlockType("potted_acacia_sapling");
    BlockType.Typed<BlockData> POTTED_CHERRY_SAPLING = getBlockType("potted_cherry_sapling");
    BlockType.Typed<BlockData> POTTED_DARK_OAK_SAPLING = getBlockType("potted_dark_oak_sapling");
    BlockType.Typed<BlockData> POTTED_MANGROVE_PROPAGULE = getBlockType("potted_mangrove_propagule");
    BlockType.Typed<BlockData> POTTED_FERN = getBlockType("potted_fern");
    BlockType.Typed<BlockData> POTTED_DANDELION = getBlockType("potted_dandelion");
    BlockType.Typed<BlockData> POTTED_POPPY = getBlockType("potted_poppy");
    BlockType.Typed<BlockData> POTTED_BLUE_ORCHID = getBlockType("potted_blue_orchid");
    BlockType.Typed<BlockData> POTTED_ALLIUM = getBlockType("potted_allium");
    BlockType.Typed<BlockData> POTTED_AZURE_BLUET = getBlockType("potted_azure_bluet");
    BlockType.Typed<BlockData> POTTED_RED_TULIP = getBlockType("potted_red_tulip");
    BlockType.Typed<BlockData> POTTED_ORANGE_TULIP = getBlockType("potted_orange_tulip");
    BlockType.Typed<BlockData> POTTED_WHITE_TULIP = getBlockType("potted_white_tulip");
    BlockType.Typed<BlockData> POTTED_PINK_TULIP = getBlockType("potted_pink_tulip");
    BlockType.Typed<BlockData> POTTED_OXEYE_DAISY = getBlockType("potted_oxeye_daisy");
    BlockType.Typed<BlockData> POTTED_CORNFLOWER = getBlockType("potted_cornflower");
    BlockType.Typed<BlockData> POTTED_LILY_OF_THE_VALLEY = getBlockType("potted_lily_of_the_valley");
    BlockType.Typed<BlockData> POTTED_WITHER_ROSE = getBlockType("potted_wither_rose");
    BlockType.Typed<BlockData> POTTED_RED_MUSHROOM = getBlockType("potted_red_mushroom");
    BlockType.Typed<BlockData> POTTED_BROWN_MUSHROOM = getBlockType("potted_brown_mushroom");
    BlockType.Typed<BlockData> POTTED_DEAD_BUSH = getBlockType("potted_dead_bush");
    BlockType.Typed<BlockData> POTTED_CACTUS = getBlockType("potted_cactus");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> CARROTS = getBlockType("carrots");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> POTATOES = getBlockType("potatoes");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> OAK_BUTTON = getBlockType("oak_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> SPRUCE_BUTTON = getBlockType("spruce_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> BIRCH_BUTTON = getBlockType("birch_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> JUNGLE_BUTTON = getBlockType("jungle_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> ACACIA_BUTTON = getBlockType("acacia_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> CHERRY_BUTTON = getBlockType("cherry_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> DARK_OAK_BUTTON = getBlockType("dark_oak_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> MANGROVE_BUTTON = getBlockType("mangrove_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> BAMBOO_BUTTON = getBlockType("bamboo_button");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> SKELETON_SKULL = getBlockType("skeleton_skull");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> SKELETON_WALL_SKULL = getBlockType("skeleton_wall_skull");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> WITHER_SKELETON_SKULL = getBlockType("wither_skeleton_skull");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> WITHER_SKELETON_WALL_SKULL = getBlockType("wither_skeleton_wall_skull");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> ZOMBIE_HEAD = getBlockType("zombie_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ZOMBIE_WALL_HEAD = getBlockType("zombie_wall_head");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> PLAYER_HEAD = getBlockType("player_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PLAYER_WALL_HEAD = getBlockType("player_wall_head");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> CREEPER_HEAD = getBlockType("creeper_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CREEPER_WALL_HEAD = getBlockType("creeper_wall_head");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> DRAGON_HEAD = getBlockType("dragon_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> DRAGON_WALL_HEAD = getBlockType("dragon_wall_head");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> PIGLIN_HEAD = getBlockType("piglin_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PIGLIN_WALL_HEAD = getBlockType("piglin_wall_head");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ANVIL = getBlockType("anvil");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CHIPPED_ANVIL = getBlockType("chipped_anvil");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> DAMAGED_ANVIL = getBlockType("damaged_anvil");
    /**
     * BlockData: {@link Chest}
     */
    BlockType.Typed<Chest> TRAPPED_CHEST = getBlockType("trapped_chest");
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    BlockType.Typed<AnaloguePowerable> LIGHT_WEIGHTED_PRESSURE_PLATE = getBlockType("light_weighted_pressure_plate");
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    BlockType.Typed<AnaloguePowerable> HEAVY_WEIGHTED_PRESSURE_PLATE = getBlockType("heavy_weighted_pressure_plate");
    /**
     * BlockData: {@link Comparator}
     */
    BlockType.Typed<Comparator> COMPARATOR = getBlockType("comparator");
    /**
     * BlockData: {@link DaylightDetector}
     */
    BlockType.Typed<DaylightDetector> DAYLIGHT_DETECTOR = getBlockType("daylight_detector");
    BlockType.Typed<BlockData> REDSTONE_BLOCK = getBlockType("redstone_block");
    BlockType.Typed<BlockData> NETHER_QUARTZ_ORE = getBlockType("nether_quartz_ore");
    /**
     * BlockData: {@link Hopper}
     */
    BlockType.Typed<Hopper> HOPPER = getBlockType("hopper");
    BlockType.Typed<BlockData> QUARTZ_BLOCK = getBlockType("quartz_block");
    BlockType.Typed<BlockData> CHISELED_QUARTZ_BLOCK = getBlockType("chiseled_quartz_block");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> QUARTZ_PILLAR = getBlockType("quartz_pillar");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> QUARTZ_STAIRS = getBlockType("quartz_stairs");
    /**
     * BlockData: {@link RedstoneRail}
     */
    BlockType.Typed<RedstoneRail> ACTIVATOR_RAIL = getBlockType("activator_rail");
    /**
     * BlockData: {@link Dispenser}
     */
    BlockType.Typed<Dispenser> DROPPER = getBlockType("dropper");
    BlockType.Typed<BlockData> WHITE_TERRACOTTA = getBlockType("white_terracotta");
    BlockType.Typed<BlockData> ORANGE_TERRACOTTA = getBlockType("orange_terracotta");
    BlockType.Typed<BlockData> MAGENTA_TERRACOTTA = getBlockType("magenta_terracotta");
    BlockType.Typed<BlockData> LIGHT_BLUE_TERRACOTTA = getBlockType("light_blue_terracotta");
    BlockType.Typed<BlockData> YELLOW_TERRACOTTA = getBlockType("yellow_terracotta");
    BlockType.Typed<BlockData> LIME_TERRACOTTA = getBlockType("lime_terracotta");
    BlockType.Typed<BlockData> PINK_TERRACOTTA = getBlockType("pink_terracotta");
    BlockType.Typed<BlockData> GRAY_TERRACOTTA = getBlockType("gray_terracotta");
    BlockType.Typed<BlockData> LIGHT_GRAY_TERRACOTTA = getBlockType("light_gray_terracotta");
    BlockType.Typed<BlockData> CYAN_TERRACOTTA = getBlockType("cyan_terracotta");
    BlockType.Typed<BlockData> PURPLE_TERRACOTTA = getBlockType("purple_terracotta");
    BlockType.Typed<BlockData> BLUE_TERRACOTTA = getBlockType("blue_terracotta");
    BlockType.Typed<BlockData> BROWN_TERRACOTTA = getBlockType("brown_terracotta");
    BlockType.Typed<BlockData> GREEN_TERRACOTTA = getBlockType("green_terracotta");
    BlockType.Typed<BlockData> RED_TERRACOTTA = getBlockType("red_terracotta");
    BlockType.Typed<BlockData> BLACK_TERRACOTTA = getBlockType("black_terracotta");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> WHITE_STAINED_GLASS_PANE = getBlockType("white_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> ORANGE_STAINED_GLASS_PANE = getBlockType("orange_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> MAGENTA_STAINED_GLASS_PANE = getBlockType("magenta_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> LIGHT_BLUE_STAINED_GLASS_PANE = getBlockType("light_blue_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> YELLOW_STAINED_GLASS_PANE = getBlockType("yellow_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> LIME_STAINED_GLASS_PANE = getBlockType("lime_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> PINK_STAINED_GLASS_PANE = getBlockType("pink_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> GRAY_STAINED_GLASS_PANE = getBlockType("gray_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> LIGHT_GRAY_STAINED_GLASS_PANE = getBlockType("light_gray_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> CYAN_STAINED_GLASS_PANE = getBlockType("cyan_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> PURPLE_STAINED_GLASS_PANE = getBlockType("purple_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> BLUE_STAINED_GLASS_PANE = getBlockType("blue_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> BROWN_STAINED_GLASS_PANE = getBlockType("brown_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> GREEN_STAINED_GLASS_PANE = getBlockType("green_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> RED_STAINED_GLASS_PANE = getBlockType("red_stained_glass_pane");
    /**
     * BlockData: {@link GlassPane}
     */
    BlockType.Typed<GlassPane> BLACK_STAINED_GLASS_PANE = getBlockType("black_stained_glass_pane");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> ACACIA_STAIRS = getBlockType("acacia_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> CHERRY_STAIRS = getBlockType("cherry_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> DARK_OAK_STAIRS = getBlockType("dark_oak_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> MANGROVE_STAIRS = getBlockType("mangrove_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> BAMBOO_STAIRS = getBlockType("bamboo_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> BAMBOO_MOSAIC_STAIRS = getBlockType("bamboo_mosaic_stairs");
    BlockType.Typed<BlockData> SLIME_BLOCK = getBlockType("slime_block");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> BARRIER = getBlockType("barrier");
    /**
     * BlockData: {@link Light}
     */
    BlockType.Typed<Light> LIGHT = getBlockType("light");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> IRON_TRAPDOOR = getBlockType("iron_trapdoor");
    BlockType.Typed<BlockData> PRISMARINE = getBlockType("prismarine");
    BlockType.Typed<BlockData> PRISMARINE_BRICKS = getBlockType("prismarine_bricks");
    BlockType.Typed<BlockData> DARK_PRISMARINE = getBlockType("dark_prismarine");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> PRISMARINE_STAIRS = getBlockType("prismarine_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> PRISMARINE_BRICK_STAIRS = getBlockType("prismarine_brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> DARK_PRISMARINE_STAIRS = getBlockType("dark_prismarine_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> PRISMARINE_SLAB = getBlockType("prismarine_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> PRISMARINE_BRICK_SLAB = getBlockType("prismarine_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> DARK_PRISMARINE_SLAB = getBlockType("dark_prismarine_slab");
    BlockType.Typed<BlockData> SEA_LANTERN = getBlockType("sea_lantern");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> HAY_BLOCK = getBlockType("hay_block");
    BlockType.Typed<BlockData> WHITE_CARPET = getBlockType("white_carpet");
    BlockType.Typed<BlockData> ORANGE_CARPET = getBlockType("orange_carpet");
    BlockType.Typed<BlockData> MAGENTA_CARPET = getBlockType("magenta_carpet");
    BlockType.Typed<BlockData> LIGHT_BLUE_CARPET = getBlockType("light_blue_carpet");
    BlockType.Typed<BlockData> YELLOW_CARPET = getBlockType("yellow_carpet");
    BlockType.Typed<BlockData> LIME_CARPET = getBlockType("lime_carpet");
    BlockType.Typed<BlockData> PINK_CARPET = getBlockType("pink_carpet");
    BlockType.Typed<BlockData> GRAY_CARPET = getBlockType("gray_carpet");
    BlockType.Typed<BlockData> LIGHT_GRAY_CARPET = getBlockType("light_gray_carpet");
    BlockType.Typed<BlockData> CYAN_CARPET = getBlockType("cyan_carpet");
    BlockType.Typed<BlockData> PURPLE_CARPET = getBlockType("purple_carpet");
    BlockType.Typed<BlockData> BLUE_CARPET = getBlockType("blue_carpet");
    BlockType.Typed<BlockData> BROWN_CARPET = getBlockType("brown_carpet");
    BlockType.Typed<BlockData> GREEN_CARPET = getBlockType("green_carpet");
    BlockType.Typed<BlockData> RED_CARPET = getBlockType("red_carpet");
    BlockType.Typed<BlockData> BLACK_CARPET = getBlockType("black_carpet");
    BlockType.Typed<BlockData> TERRACOTTA = getBlockType("terracotta");
    BlockType.Typed<BlockData> COAL_BLOCK = getBlockType("coal_block");
    BlockType.Typed<BlockData> PACKED_ICE = getBlockType("packed_ice");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> SUNFLOWER = getBlockType("sunflower");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> LILAC = getBlockType("lilac");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> ROSE_BUSH = getBlockType("rose_bush");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> PEONY = getBlockType("peony");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> TALL_GRASS = getBlockType("tall_grass");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> LARGE_FERN = getBlockType("large_fern");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> WHITE_BANNER = getBlockType("white_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> ORANGE_BANNER = getBlockType("orange_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> MAGENTA_BANNER = getBlockType("magenta_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> LIGHT_BLUE_BANNER = getBlockType("light_blue_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> YELLOW_BANNER = getBlockType("yellow_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> LIME_BANNER = getBlockType("lime_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> PINK_BANNER = getBlockType("pink_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> GRAY_BANNER = getBlockType("gray_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> LIGHT_GRAY_BANNER = getBlockType("light_gray_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> CYAN_BANNER = getBlockType("cyan_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> PURPLE_BANNER = getBlockType("purple_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> BLUE_BANNER = getBlockType("blue_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> BROWN_BANNER = getBlockType("brown_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> GREEN_BANNER = getBlockType("green_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> RED_BANNER = getBlockType("red_banner");
    /**
     * BlockData: {@link Rotatable}
     */
    BlockType.Typed<Rotatable> BLACK_BANNER = getBlockType("black_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> WHITE_WALL_BANNER = getBlockType("white_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ORANGE_WALL_BANNER = getBlockType("orange_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> MAGENTA_WALL_BANNER = getBlockType("magenta_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_BLUE_WALL_BANNER = getBlockType("light_blue_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> YELLOW_WALL_BANNER = getBlockType("yellow_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIME_WALL_BANNER = getBlockType("lime_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PINK_WALL_BANNER = getBlockType("pink_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GRAY_WALL_BANNER = getBlockType("gray_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_GRAY_WALL_BANNER = getBlockType("light_gray_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CYAN_WALL_BANNER = getBlockType("cyan_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PURPLE_WALL_BANNER = getBlockType("purple_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLUE_WALL_BANNER = getBlockType("blue_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BROWN_WALL_BANNER = getBlockType("brown_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GREEN_WALL_BANNER = getBlockType("green_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> RED_WALL_BANNER = getBlockType("red_wall_banner");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLACK_WALL_BANNER = getBlockType("black_wall_banner");
    BlockType.Typed<BlockData> RED_SANDSTONE = getBlockType("red_sandstone");
    BlockType.Typed<BlockData> CHISELED_RED_SANDSTONE = getBlockType("chiseled_red_sandstone");
    BlockType.Typed<BlockData> CUT_RED_SANDSTONE = getBlockType("cut_red_sandstone");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> RED_SANDSTONE_STAIRS = getBlockType("red_sandstone_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> OAK_SLAB = getBlockType("oak_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SPRUCE_SLAB = getBlockType("spruce_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> BIRCH_SLAB = getBlockType("birch_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> JUNGLE_SLAB = getBlockType("jungle_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> ACACIA_SLAB = getBlockType("acacia_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> CHERRY_SLAB = getBlockType("cherry_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> DARK_OAK_SLAB = getBlockType("dark_oak_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> MANGROVE_SLAB = getBlockType("mangrove_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> BAMBOO_SLAB = getBlockType("bamboo_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> BAMBOO_MOSAIC_SLAB = getBlockType("bamboo_mosaic_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> STONE_SLAB = getBlockType("stone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SMOOTH_STONE_SLAB = getBlockType("smooth_stone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SANDSTONE_SLAB = getBlockType("sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> CUT_SANDSTONE_SLAB = getBlockType("cut_sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> PETRIFIED_OAK_SLAB = getBlockType("petrified_oak_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> COBBLESTONE_SLAB = getBlockType("cobblestone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> BRICK_SLAB = getBlockType("brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> STONE_BRICK_SLAB = getBlockType("stone_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> MUD_BRICK_SLAB = getBlockType("mud_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> NETHER_BRICK_SLAB = getBlockType("nether_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> QUARTZ_SLAB = getBlockType("quartz_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> RED_SANDSTONE_SLAB = getBlockType("red_sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> CUT_RED_SANDSTONE_SLAB = getBlockType("cut_red_sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> PURPUR_SLAB = getBlockType("purpur_slab");
    BlockType.Typed<BlockData> SMOOTH_STONE = getBlockType("smooth_stone");
    BlockType.Typed<BlockData> SMOOTH_SANDSTONE = getBlockType("smooth_sandstone");
    BlockType.Typed<BlockData> SMOOTH_QUARTZ = getBlockType("smooth_quartz");
    BlockType.Typed<BlockData> SMOOTH_RED_SANDSTONE = getBlockType("smooth_red_sandstone");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> SPRUCE_FENCE_GATE = getBlockType("spruce_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> BIRCH_FENCE_GATE = getBlockType("birch_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> JUNGLE_FENCE_GATE = getBlockType("jungle_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> ACACIA_FENCE_GATE = getBlockType("acacia_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> CHERRY_FENCE_GATE = getBlockType("cherry_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> DARK_OAK_FENCE_GATE = getBlockType("dark_oak_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> MANGROVE_FENCE_GATE = getBlockType("mangrove_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> BAMBOO_FENCE_GATE = getBlockType("bamboo_fence_gate");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> SPRUCE_FENCE = getBlockType("spruce_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> BIRCH_FENCE = getBlockType("birch_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> JUNGLE_FENCE = getBlockType("jungle_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> ACACIA_FENCE = getBlockType("acacia_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> CHERRY_FENCE = getBlockType("cherry_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> DARK_OAK_FENCE = getBlockType("dark_oak_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> MANGROVE_FENCE = getBlockType("mangrove_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> BAMBOO_FENCE = getBlockType("bamboo_fence");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> SPRUCE_DOOR = getBlockType("spruce_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> BIRCH_DOOR = getBlockType("birch_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> JUNGLE_DOOR = getBlockType("jungle_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> ACACIA_DOOR = getBlockType("acacia_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> CHERRY_DOOR = getBlockType("cherry_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> DARK_OAK_DOOR = getBlockType("dark_oak_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> MANGROVE_DOOR = getBlockType("mangrove_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> BAMBOO_DOOR = getBlockType("bamboo_door");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> END_ROD = getBlockType("end_rod");
    /**
     * BlockData: {@link MultipleFacing}
     */
    BlockType.Typed<MultipleFacing> CHORUS_PLANT = getBlockType("chorus_plant");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> CHORUS_FLOWER = getBlockType("chorus_flower");
    BlockType.Typed<BlockData> PURPUR_BLOCK = getBlockType("purpur_block");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> PURPUR_PILLAR = getBlockType("purpur_pillar");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> PURPUR_STAIRS = getBlockType("purpur_stairs");
    BlockType.Typed<BlockData> END_STONE_BRICKS = getBlockType("end_stone_bricks");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> TORCHFLOWER_CROP = getBlockType("torchflower_crop");
    /**
     * BlockData: {@link PitcherCrop}
     */
    BlockType.Typed<PitcherCrop> PITCHER_CROP = getBlockType("pitcher_crop");
    /**
     * BlockData: {@link Bisected}
     */
    BlockType.Typed<Bisected> PITCHER_PLANT = getBlockType("pitcher_plant");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> BEETROOTS = getBlockType("beetroots");
    BlockType.Typed<BlockData> DIRT_PATH = getBlockType("dirt_path");
    BlockType.Typed<BlockData> END_GATEWAY = getBlockType("end_gateway");
    /**
     * BlockData: {@link CommandBlock}
     */
    BlockType.Typed<CommandBlock> REPEATING_COMMAND_BLOCK = getBlockType("repeating_command_block");
    /**
     * BlockData: {@link CommandBlock}
     */
    BlockType.Typed<CommandBlock> CHAIN_COMMAND_BLOCK = getBlockType("chain_command_block");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> FROSTED_ICE = getBlockType("frosted_ice");
    BlockType.Typed<BlockData> MAGMA_BLOCK = getBlockType("magma_block");
    BlockType.Typed<BlockData> NETHER_WART_BLOCK = getBlockType("nether_wart_block");
    BlockType.Typed<BlockData> RED_NETHER_BRICKS = getBlockType("red_nether_bricks");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> BONE_BLOCK = getBlockType("bone_block");
    BlockType.Typed<BlockData> STRUCTURE_VOID = getBlockType("structure_void");
    /**
     * BlockData: {@link Observer}
     */
    BlockType.Typed<Observer> OBSERVER = getBlockType("observer");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> SHULKER_BOX = getBlockType("shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> WHITE_SHULKER_BOX = getBlockType("white_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ORANGE_SHULKER_BOX = getBlockType("orange_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> MAGENTA_SHULKER_BOX = getBlockType("magenta_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_BLUE_SHULKER_BOX = getBlockType("light_blue_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> YELLOW_SHULKER_BOX = getBlockType("yellow_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIME_SHULKER_BOX = getBlockType("lime_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PINK_SHULKER_BOX = getBlockType("pink_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GRAY_SHULKER_BOX = getBlockType("gray_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_GRAY_SHULKER_BOX = getBlockType("light_gray_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CYAN_SHULKER_BOX = getBlockType("cyan_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PURPLE_SHULKER_BOX = getBlockType("purple_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLUE_SHULKER_BOX = getBlockType("blue_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BROWN_SHULKER_BOX = getBlockType("brown_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GREEN_SHULKER_BOX = getBlockType("green_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> RED_SHULKER_BOX = getBlockType("red_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLACK_SHULKER_BOX = getBlockType("black_shulker_box");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> WHITE_GLAZED_TERRACOTTA = getBlockType("white_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> ORANGE_GLAZED_TERRACOTTA = getBlockType("orange_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> MAGENTA_GLAZED_TERRACOTTA = getBlockType("magenta_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_BLUE_GLAZED_TERRACOTTA = getBlockType("light_blue_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> YELLOW_GLAZED_TERRACOTTA = getBlockType("yellow_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIME_GLAZED_TERRACOTTA = getBlockType("lime_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PINK_GLAZED_TERRACOTTA = getBlockType("pink_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GRAY_GLAZED_TERRACOTTA = getBlockType("gray_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LIGHT_GRAY_GLAZED_TERRACOTTA = getBlockType("light_gray_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> CYAN_GLAZED_TERRACOTTA = getBlockType("cyan_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> PURPLE_GLAZED_TERRACOTTA = getBlockType("purple_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLUE_GLAZED_TERRACOTTA = getBlockType("blue_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BROWN_GLAZED_TERRACOTTA = getBlockType("brown_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> GREEN_GLAZED_TERRACOTTA = getBlockType("green_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> RED_GLAZED_TERRACOTTA = getBlockType("red_glazed_terracotta");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> BLACK_GLAZED_TERRACOTTA = getBlockType("black_glazed_terracotta");
    BlockType.Typed<BlockData> WHITE_CONCRETE = getBlockType("white_concrete");
    BlockType.Typed<BlockData> ORANGE_CONCRETE = getBlockType("orange_concrete");
    BlockType.Typed<BlockData> MAGENTA_CONCRETE = getBlockType("magenta_concrete");
    BlockType.Typed<BlockData> LIGHT_BLUE_CONCRETE = getBlockType("light_blue_concrete");
    BlockType.Typed<BlockData> YELLOW_CONCRETE = getBlockType("yellow_concrete");
    BlockType.Typed<BlockData> LIME_CONCRETE = getBlockType("lime_concrete");
    BlockType.Typed<BlockData> PINK_CONCRETE = getBlockType("pink_concrete");
    BlockType.Typed<BlockData> GRAY_CONCRETE = getBlockType("gray_concrete");
    BlockType.Typed<BlockData> LIGHT_GRAY_CONCRETE = getBlockType("light_gray_concrete");
    BlockType.Typed<BlockData> CYAN_CONCRETE = getBlockType("cyan_concrete");
    BlockType.Typed<BlockData> PURPLE_CONCRETE = getBlockType("purple_concrete");
    BlockType.Typed<BlockData> BLUE_CONCRETE = getBlockType("blue_concrete");
    BlockType.Typed<BlockData> BROWN_CONCRETE = getBlockType("brown_concrete");
    BlockType.Typed<BlockData> GREEN_CONCRETE = getBlockType("green_concrete");
    BlockType.Typed<BlockData> RED_CONCRETE = getBlockType("red_concrete");
    BlockType.Typed<BlockData> BLACK_CONCRETE = getBlockType("black_concrete");
    BlockType.Typed<BlockData> WHITE_CONCRETE_POWDER = getBlockType("white_concrete_powder");
    BlockType.Typed<BlockData> ORANGE_CONCRETE_POWDER = getBlockType("orange_concrete_powder");
    BlockType.Typed<BlockData> MAGENTA_CONCRETE_POWDER = getBlockType("magenta_concrete_powder");
    BlockType.Typed<BlockData> LIGHT_BLUE_CONCRETE_POWDER = getBlockType("light_blue_concrete_powder");
    BlockType.Typed<BlockData> YELLOW_CONCRETE_POWDER = getBlockType("yellow_concrete_powder");
    BlockType.Typed<BlockData> LIME_CONCRETE_POWDER = getBlockType("lime_concrete_powder");
    BlockType.Typed<BlockData> PINK_CONCRETE_POWDER = getBlockType("pink_concrete_powder");
    BlockType.Typed<BlockData> GRAY_CONCRETE_POWDER = getBlockType("gray_concrete_powder");
    BlockType.Typed<BlockData> LIGHT_GRAY_CONCRETE_POWDER = getBlockType("light_gray_concrete_powder");
    BlockType.Typed<BlockData> CYAN_CONCRETE_POWDER = getBlockType("cyan_concrete_powder");
    BlockType.Typed<BlockData> PURPLE_CONCRETE_POWDER = getBlockType("purple_concrete_powder");
    BlockType.Typed<BlockData> BLUE_CONCRETE_POWDER = getBlockType("blue_concrete_powder");
    BlockType.Typed<BlockData> BROWN_CONCRETE_POWDER = getBlockType("brown_concrete_powder");
    BlockType.Typed<BlockData> GREEN_CONCRETE_POWDER = getBlockType("green_concrete_powder");
    BlockType.Typed<BlockData> RED_CONCRETE_POWDER = getBlockType("red_concrete_powder");
    BlockType.Typed<BlockData> BLACK_CONCRETE_POWDER = getBlockType("black_concrete_powder");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> KELP = getBlockType("kelp");
    BlockType.Typed<BlockData> KELP_PLANT = getBlockType("kelp_plant");
    BlockType.Typed<BlockData> DRIED_KELP_BLOCK = getBlockType("dried_kelp_block");
    /**
     * BlockData: {@link TurtleEgg}
     */
    BlockType.Typed<TurtleEgg> TURTLE_EGG = getBlockType("turtle_egg");
    /**
     * BlockData: {@link Hatchable}
     */
    BlockType.Typed<Hatchable> SNIFFER_EGG = getBlockType("sniffer_egg");
    BlockType.Typed<BlockData> DEAD_TUBE_CORAL_BLOCK = getBlockType("dead_tube_coral_block");
    BlockType.Typed<BlockData> DEAD_BRAIN_CORAL_BLOCK = getBlockType("dead_brain_coral_block");
    BlockType.Typed<BlockData> DEAD_BUBBLE_CORAL_BLOCK = getBlockType("dead_bubble_coral_block");
    BlockType.Typed<BlockData> DEAD_FIRE_CORAL_BLOCK = getBlockType("dead_fire_coral_block");
    BlockType.Typed<BlockData> DEAD_HORN_CORAL_BLOCK = getBlockType("dead_horn_coral_block");
    BlockType.Typed<BlockData> TUBE_CORAL_BLOCK = getBlockType("tube_coral_block");
    BlockType.Typed<BlockData> BRAIN_CORAL_BLOCK = getBlockType("brain_coral_block");
    BlockType.Typed<BlockData> BUBBLE_CORAL_BLOCK = getBlockType("bubble_coral_block");
    BlockType.Typed<BlockData> FIRE_CORAL_BLOCK = getBlockType("fire_coral_block");
    BlockType.Typed<BlockData> HORN_CORAL_BLOCK = getBlockType("horn_coral_block");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_TUBE_CORAL = getBlockType("dead_tube_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_BRAIN_CORAL = getBlockType("dead_brain_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_BUBBLE_CORAL = getBlockType("dead_bubble_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_FIRE_CORAL = getBlockType("dead_fire_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_HORN_CORAL = getBlockType("dead_horn_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> TUBE_CORAL = getBlockType("tube_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> BRAIN_CORAL = getBlockType("brain_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> BUBBLE_CORAL = getBlockType("bubble_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> FIRE_CORAL = getBlockType("fire_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> HORN_CORAL = getBlockType("horn_coral");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_TUBE_CORAL_FAN = getBlockType("dead_tube_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_BRAIN_CORAL_FAN = getBlockType("dead_brain_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_BUBBLE_CORAL_FAN = getBlockType("dead_bubble_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_FIRE_CORAL_FAN = getBlockType("dead_fire_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> DEAD_HORN_CORAL_FAN = getBlockType("dead_horn_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> TUBE_CORAL_FAN = getBlockType("tube_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> BRAIN_CORAL_FAN = getBlockType("brain_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> BUBBLE_CORAL_FAN = getBlockType("bubble_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> FIRE_CORAL_FAN = getBlockType("fire_coral_fan");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> HORN_CORAL_FAN = getBlockType("horn_coral_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> DEAD_TUBE_CORAL_WALL_FAN = getBlockType("dead_tube_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> DEAD_BRAIN_CORAL_WALL_FAN = getBlockType("dead_brain_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> DEAD_BUBBLE_CORAL_WALL_FAN = getBlockType("dead_bubble_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> DEAD_FIRE_CORAL_WALL_FAN = getBlockType("dead_fire_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> DEAD_HORN_CORAL_WALL_FAN = getBlockType("dead_horn_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> TUBE_CORAL_WALL_FAN = getBlockType("tube_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> BRAIN_CORAL_WALL_FAN = getBlockType("brain_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> BUBBLE_CORAL_WALL_FAN = getBlockType("bubble_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> FIRE_CORAL_WALL_FAN = getBlockType("fire_coral_wall_fan");
    /**
     * BlockData: {@link CoralWallFan}
     */
    BlockType.Typed<CoralWallFan> HORN_CORAL_WALL_FAN = getBlockType("horn_coral_wall_fan");
    /**
     * BlockData: {@link SeaPickle}
     */
    BlockType.Typed<SeaPickle> SEA_PICKLE = getBlockType("sea_pickle");
    BlockType.Typed<BlockData> BLUE_ICE = getBlockType("blue_ice");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> CONDUIT = getBlockType("conduit");
    BlockType.Typed<BlockData> BAMBOO_SAPLING = getBlockType("bamboo_sapling");
    /**
     * BlockData: {@link Bamboo}
     */
    BlockType.Typed<Bamboo> BAMBOO = getBlockType("bamboo");
    BlockType.Typed<BlockData> POTTED_BAMBOO = getBlockType("potted_bamboo");
    BlockType.Typed<BlockData> VOID_AIR = getBlockType("void_air");
    BlockType.Typed<BlockData> CAVE_AIR = getBlockType("cave_air");
    /**
     * BlockData: {@link BubbleColumn}
     */
    BlockType.Typed<BubbleColumn> BUBBLE_COLUMN = getBlockType("bubble_column");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_GRANITE_STAIRS = getBlockType("polished_granite_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> SMOOTH_RED_SANDSTONE_STAIRS = getBlockType("smooth_red_sandstone_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> MOSSY_STONE_BRICK_STAIRS = getBlockType("mossy_stone_brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_DIORITE_STAIRS = getBlockType("polished_diorite_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> MOSSY_COBBLESTONE_STAIRS = getBlockType("mossy_cobblestone_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> END_STONE_BRICK_STAIRS = getBlockType("end_stone_brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> STONE_STAIRS = getBlockType("stone_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> SMOOTH_SANDSTONE_STAIRS = getBlockType("smooth_sandstone_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> SMOOTH_QUARTZ_STAIRS = getBlockType("smooth_quartz_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> GRANITE_STAIRS = getBlockType("granite_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> ANDESITE_STAIRS = getBlockType("andesite_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> RED_NETHER_BRICK_STAIRS = getBlockType("red_nether_brick_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_ANDESITE_STAIRS = getBlockType("polished_andesite_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> DIORITE_STAIRS = getBlockType("diorite_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_GRANITE_SLAB = getBlockType("polished_granite_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SMOOTH_RED_SANDSTONE_SLAB = getBlockType("smooth_red_sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> MOSSY_STONE_BRICK_SLAB = getBlockType("mossy_stone_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_DIORITE_SLAB = getBlockType("polished_diorite_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> MOSSY_COBBLESTONE_SLAB = getBlockType("mossy_cobblestone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> END_STONE_BRICK_SLAB = getBlockType("end_stone_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SMOOTH_SANDSTONE_SLAB = getBlockType("smooth_sandstone_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> SMOOTH_QUARTZ_SLAB = getBlockType("smooth_quartz_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> GRANITE_SLAB = getBlockType("granite_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> ANDESITE_SLAB = getBlockType("andesite_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> RED_NETHER_BRICK_SLAB = getBlockType("red_nether_brick_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_ANDESITE_SLAB = getBlockType("polished_andesite_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> DIORITE_SLAB = getBlockType("diorite_slab");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> BRICK_WALL = getBlockType("brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> PRISMARINE_WALL = getBlockType("prismarine_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> RED_SANDSTONE_WALL = getBlockType("red_sandstone_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> MOSSY_STONE_BRICK_WALL = getBlockType("mossy_stone_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> GRANITE_WALL = getBlockType("granite_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> STONE_BRICK_WALL = getBlockType("stone_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> MUD_BRICK_WALL = getBlockType("mud_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> NETHER_BRICK_WALL = getBlockType("nether_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> ANDESITE_WALL = getBlockType("andesite_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> RED_NETHER_BRICK_WALL = getBlockType("red_nether_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> SANDSTONE_WALL = getBlockType("sandstone_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> END_STONE_BRICK_WALL = getBlockType("end_stone_brick_wall");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> DIORITE_WALL = getBlockType("diorite_wall");
    /**
     * BlockData: {@link Scaffolding}
     */
    BlockType.Typed<Scaffolding> SCAFFOLDING = getBlockType("scaffolding");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> LOOM = getBlockType("loom");
    /**
     * BlockData: {@link Barrel}
     */
    BlockType.Typed<Barrel> BARREL = getBlockType("barrel");
    /**
     * BlockData: {@link Furnace}
     */
    BlockType.Typed<Furnace> SMOKER = getBlockType("smoker");
    /**
     * BlockData: {@link Furnace}
     */
    BlockType.Typed<Furnace> BLAST_FURNACE = getBlockType("blast_furnace");
    BlockType.Typed<BlockData> CARTOGRAPHY_TABLE = getBlockType("cartography_table");
    BlockType.Typed<BlockData> FLETCHING_TABLE = getBlockType("fletching_table");
    /**
     * BlockData: {@link Grindstone}
     */
    BlockType.Typed<Grindstone> GRINDSTONE = getBlockType("grindstone");
    /**
     * BlockData: {@link Lectern}
     */
    BlockType.Typed<Lectern> LECTERN = getBlockType("lectern");
    BlockType.Typed<BlockData> SMITHING_TABLE = getBlockType("smithing_table");
    /**
     * BlockData: {@link Directional}
     */
    BlockType.Typed<Directional> STONECUTTER = getBlockType("stonecutter");
    /**
     * BlockData: {@link Bell}
     */
    BlockType.Typed<Bell> BELL = getBlockType("bell");
    /**
     * BlockData: {@link Lantern}
     */
    BlockType.Typed<Lantern> LANTERN = getBlockType("lantern");
    /**
     * BlockData: {@link Lantern}
     */
    BlockType.Typed<Lantern> SOUL_LANTERN = getBlockType("soul_lantern");
    /**
     * BlockData: {@link Campfire}
     */
    BlockType.Typed<Campfire> CAMPFIRE = getBlockType("campfire");
    /**
     * BlockData: {@link Campfire}
     */
    BlockType.Typed<Campfire> SOUL_CAMPFIRE = getBlockType("soul_campfire");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> SWEET_BERRY_BUSH = getBlockType("sweet_berry_bush");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> WARPED_STEM = getBlockType("warped_stem");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_WARPED_STEM = getBlockType("stripped_warped_stem");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> WARPED_HYPHAE = getBlockType("warped_hyphae");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_WARPED_HYPHAE = getBlockType("stripped_warped_hyphae");
    BlockType.Typed<BlockData> WARPED_NYLIUM = getBlockType("warped_nylium");
    BlockType.Typed<BlockData> WARPED_FUNGUS = getBlockType("warped_fungus");
    BlockType.Typed<BlockData> WARPED_WART_BLOCK = getBlockType("warped_wart_block");
    BlockType.Typed<BlockData> WARPED_ROOTS = getBlockType("warped_roots");
    BlockType.Typed<BlockData> NETHER_SPROUTS = getBlockType("nether_sprouts");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> CRIMSON_STEM = getBlockType("crimson_stem");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_CRIMSON_STEM = getBlockType("stripped_crimson_stem");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> CRIMSON_HYPHAE = getBlockType("crimson_hyphae");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> STRIPPED_CRIMSON_HYPHAE = getBlockType("stripped_crimson_hyphae");
    BlockType.Typed<BlockData> CRIMSON_NYLIUM = getBlockType("crimson_nylium");
    BlockType.Typed<BlockData> CRIMSON_FUNGUS = getBlockType("crimson_fungus");
    BlockType.Typed<BlockData> SHROOMLIGHT = getBlockType("shroomlight");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> WEEPING_VINES = getBlockType("weeping_vines");
    BlockType.Typed<BlockData> WEEPING_VINES_PLANT = getBlockType("weeping_vines_plant");
    /**
     * BlockData: {@link Ageable}
     */
    BlockType.Typed<Ageable> TWISTING_VINES = getBlockType("twisting_vines");
    BlockType.Typed<BlockData> TWISTING_VINES_PLANT = getBlockType("twisting_vines_plant");
    BlockType.Typed<BlockData> CRIMSON_ROOTS = getBlockType("crimson_roots");
    BlockType.Typed<BlockData> CRIMSON_PLANKS = getBlockType("crimson_planks");
    BlockType.Typed<BlockData> WARPED_PLANKS = getBlockType("warped_planks");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> CRIMSON_SLAB = getBlockType("crimson_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WARPED_SLAB = getBlockType("warped_slab");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> CRIMSON_PRESSURE_PLATE = getBlockType("crimson_pressure_plate");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> WARPED_PRESSURE_PLATE = getBlockType("warped_pressure_plate");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> CRIMSON_FENCE = getBlockType("crimson_fence");
    /**
     * BlockData: {@link Fence}
     */
    BlockType.Typed<Fence> WARPED_FENCE = getBlockType("warped_fence");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> CRIMSON_TRAPDOOR = getBlockType("crimson_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    BlockType.Typed<TrapDoor> WARPED_TRAPDOOR = getBlockType("warped_trapdoor");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> CRIMSON_FENCE_GATE = getBlockType("crimson_fence_gate");
    /**
     * BlockData: {@link Gate}
     */
    BlockType.Typed<Gate> WARPED_FENCE_GATE = getBlockType("warped_fence_gate");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> CRIMSON_STAIRS = getBlockType("crimson_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WARPED_STAIRS = getBlockType("warped_stairs");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> CRIMSON_BUTTON = getBlockType("crimson_button");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> WARPED_BUTTON = getBlockType("warped_button");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> CRIMSON_DOOR = getBlockType("crimson_door");
    /**
     * BlockData: {@link Door}
     */
    BlockType.Typed<Door> WARPED_DOOR = getBlockType("warped_door");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> CRIMSON_SIGN = getBlockType("crimson_sign");
    /**
     * BlockData: {@link Sign}
     */
    BlockType.Typed<Sign> WARPED_SIGN = getBlockType("warped_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> CRIMSON_WALL_SIGN = getBlockType("crimson_wall_sign");
    /**
     * BlockData: {@link WallSign}
     */
    BlockType.Typed<WallSign> WARPED_WALL_SIGN = getBlockType("warped_wall_sign");
    /**
     * BlockData: {@link StructureBlock}
     */
    BlockType.Typed<StructureBlock> STRUCTURE_BLOCK = getBlockType("structure_block");
    /**
     * BlockData: {@link Jigsaw}
     */
    BlockType.Typed<Jigsaw> JIGSAW = getBlockType("jigsaw");
    /**
     * BlockData: {@link Levelled}
     */
    BlockType.Typed<Levelled> COMPOSTER = getBlockType("composter");
    /**
     * BlockData: {@link AnaloguePowerable}
     */
    BlockType.Typed<AnaloguePowerable> TARGET = getBlockType("target");
    /**
     * BlockData: {@link Beehive}
     */
    BlockType.Typed<Beehive> BEE_NEST = getBlockType("bee_nest");
    /**
     * BlockData: {@link Beehive}
     */
    BlockType.Typed<Beehive> BEEHIVE = getBlockType("beehive");
    BlockType.Typed<BlockData> HONEY_BLOCK = getBlockType("honey_block");
    BlockType.Typed<BlockData> HONEYCOMB_BLOCK = getBlockType("honeycomb_block");
    BlockType.Typed<BlockData> NETHERITE_BLOCK = getBlockType("netherite_block");
    BlockType.Typed<BlockData> ANCIENT_DEBRIS = getBlockType("ancient_debris");
    BlockType.Typed<BlockData> CRYING_OBSIDIAN = getBlockType("crying_obsidian");
    /**
     * BlockData: {@link RespawnAnchor}
     */
    BlockType.Typed<RespawnAnchor> RESPAWN_ANCHOR = getBlockType("respawn_anchor");
    BlockType.Typed<BlockData> POTTED_CRIMSON_FUNGUS = getBlockType("potted_crimson_fungus");
    BlockType.Typed<BlockData> POTTED_WARPED_FUNGUS = getBlockType("potted_warped_fungus");
    BlockType.Typed<BlockData> POTTED_CRIMSON_ROOTS = getBlockType("potted_crimson_roots");
    BlockType.Typed<BlockData> POTTED_WARPED_ROOTS = getBlockType("potted_warped_roots");
    BlockType.Typed<BlockData> LODESTONE = getBlockType("lodestone");
    BlockType.Typed<BlockData> BLACKSTONE = getBlockType("blackstone");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> BLACKSTONE_STAIRS = getBlockType("blackstone_stairs");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> BLACKSTONE_WALL = getBlockType("blackstone_wall");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> BLACKSTONE_SLAB = getBlockType("blackstone_slab");
    BlockType.Typed<BlockData> POLISHED_BLACKSTONE = getBlockType("polished_blackstone");
    BlockType.Typed<BlockData> POLISHED_BLACKSTONE_BRICKS = getBlockType("polished_blackstone_bricks");
    BlockType.Typed<BlockData> CRACKED_POLISHED_BLACKSTONE_BRICKS = getBlockType("cracked_polished_blackstone_bricks");
    BlockType.Typed<BlockData> CHISELED_POLISHED_BLACKSTONE = getBlockType("chiseled_polished_blackstone");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_BLACKSTONE_BRICK_SLAB = getBlockType("polished_blackstone_brick_slab");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_BLACKSTONE_BRICK_STAIRS = getBlockType("polished_blackstone_brick_stairs");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> POLISHED_BLACKSTONE_BRICK_WALL = getBlockType("polished_blackstone_brick_wall");
    BlockType.Typed<BlockData> GILDED_BLACKSTONE = getBlockType("gilded_blackstone");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_BLACKSTONE_STAIRS = getBlockType("polished_blackstone_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_BLACKSTONE_SLAB = getBlockType("polished_blackstone_slab");
    /**
     * BlockData: {@link Powerable}
     */
    BlockType.Typed<Powerable> POLISHED_BLACKSTONE_PRESSURE_PLATE = getBlockType("polished_blackstone_pressure_plate");
    /**
     * BlockData: {@link Switch}
     */
    BlockType.Typed<Switch> POLISHED_BLACKSTONE_BUTTON = getBlockType("polished_blackstone_button");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> POLISHED_BLACKSTONE_WALL = getBlockType("polished_blackstone_wall");
    BlockType.Typed<BlockData> CHISELED_NETHER_BRICKS = getBlockType("chiseled_nether_bricks");
    BlockType.Typed<BlockData> CRACKED_NETHER_BRICKS = getBlockType("cracked_nether_bricks");
    BlockType.Typed<BlockData> QUARTZ_BRICKS = getBlockType("quartz_bricks");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> CANDLE = getBlockType("candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> WHITE_CANDLE = getBlockType("white_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> ORANGE_CANDLE = getBlockType("orange_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> MAGENTA_CANDLE = getBlockType("magenta_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> LIGHT_BLUE_CANDLE = getBlockType("light_blue_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> YELLOW_CANDLE = getBlockType("yellow_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> LIME_CANDLE = getBlockType("lime_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> PINK_CANDLE = getBlockType("pink_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> GRAY_CANDLE = getBlockType("gray_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> LIGHT_GRAY_CANDLE = getBlockType("light_gray_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> CYAN_CANDLE = getBlockType("cyan_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> PURPLE_CANDLE = getBlockType("purple_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> BLUE_CANDLE = getBlockType("blue_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> BROWN_CANDLE = getBlockType("brown_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> GREEN_CANDLE = getBlockType("green_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> RED_CANDLE = getBlockType("red_candle");
    /**
     * BlockData: {@link Candle}
     */
    BlockType.Typed<Candle> BLACK_CANDLE = getBlockType("black_candle");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> CANDLE_CAKE = getBlockType("candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> WHITE_CANDLE_CAKE = getBlockType("white_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> ORANGE_CANDLE_CAKE = getBlockType("orange_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> MAGENTA_CANDLE_CAKE = getBlockType("magenta_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> LIGHT_BLUE_CANDLE_CAKE = getBlockType("light_blue_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> YELLOW_CANDLE_CAKE = getBlockType("yellow_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> LIME_CANDLE_CAKE = getBlockType("lime_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> PINK_CANDLE_CAKE = getBlockType("pink_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> GRAY_CANDLE_CAKE = getBlockType("gray_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> LIGHT_GRAY_CANDLE_CAKE = getBlockType("light_gray_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> CYAN_CANDLE_CAKE = getBlockType("cyan_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> PURPLE_CANDLE_CAKE = getBlockType("purple_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> BLUE_CANDLE_CAKE = getBlockType("blue_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> BROWN_CANDLE_CAKE = getBlockType("brown_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> GREEN_CANDLE_CAKE = getBlockType("green_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> RED_CANDLE_CAKE = getBlockType("red_candle_cake");
    /**
     * BlockData: {@link Lightable}
     */
    BlockType.Typed<Lightable> BLACK_CANDLE_CAKE = getBlockType("black_candle_cake");
    BlockType.Typed<BlockData> AMETHYST_BLOCK = getBlockType("amethyst_block");
    BlockType.Typed<BlockData> BUDDING_AMETHYST = getBlockType("budding_amethyst");
    /**
     * BlockData: {@link AmethystCluster}
     */
    BlockType.Typed<AmethystCluster> AMETHYST_CLUSTER = getBlockType("amethyst_cluster");
    /**
     * BlockData: {@link AmethystCluster}
     */
    BlockType.Typed<AmethystCluster> LARGE_AMETHYST_BUD = getBlockType("large_amethyst_bud");
    /**
     * BlockData: {@link AmethystCluster}
     */
    BlockType.Typed<AmethystCluster> MEDIUM_AMETHYST_BUD = getBlockType("medium_amethyst_bud");
    /**
     * BlockData: {@link AmethystCluster}
     */
    BlockType.Typed<AmethystCluster> SMALL_AMETHYST_BUD = getBlockType("small_amethyst_bud");
    BlockType.Typed<BlockData> TUFF = getBlockType("tuff");
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Slab> TUFF_SLAB = getBlockType("tuff_slab");
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Stairs> TUFF_STAIRS = getBlockType("tuff_stairs");
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Wall> TUFF_WALL = getBlockType("tuff_wall");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> POLISHED_TUFF = getBlockType("polished_tuff");
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Slab> POLISHED_TUFF_SLAB = getBlockType("polished_tuff_slab");
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Stairs> POLISHED_TUFF_STAIRS = getBlockType("polished_tuff_stairs");
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Wall> POLISHED_TUFF_WALL = getBlockType("polished_tuff_wall");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> CHISELED_TUFF = getBlockType("chiseled_tuff");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> TUFF_BRICKS = getBlockType("tuff_bricks");
    /**
     * BlockData: {@link Slab}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Slab> TUFF_BRICK_SLAB = getBlockType("tuff_brick_slab");
    /**
     * BlockData: {@link Stairs}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Stairs> TUFF_BRICK_STAIRS = getBlockType("tuff_brick_stairs");
    /**
     * BlockData: {@link Wall}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Wall> TUFF_BRICK_WALL = getBlockType("tuff_brick_wall");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> CHISELED_TUFF_BRICKS = getBlockType("chiseled_tuff_bricks");
    BlockType.Typed<BlockData> CALCITE = getBlockType("calcite");
    BlockType.Typed<BlockData> TINTED_GLASS = getBlockType("tinted_glass");
    BlockType.Typed<BlockData> POWDER_SNOW = getBlockType("powder_snow");
    /**
     * BlockData: {@link SculkSensor}
     */
    BlockType.Typed<SculkSensor> SCULK_SENSOR = getBlockType("sculk_sensor");
    /**
     * BlockData: {@link CalibratedSculkSensor}
     */
    BlockType.Typed<CalibratedSculkSensor> CALIBRATED_SCULK_SENSOR = getBlockType("calibrated_sculk_sensor");
    BlockType.Typed<BlockData> SCULK = getBlockType("sculk");
    /**
     * BlockData: {@link SculkVein}
     */
    BlockType.Typed<SculkVein> SCULK_VEIN = getBlockType("sculk_vein");
    /**
     * BlockData: {@link SculkCatalyst}
     */
    BlockType.Typed<SculkCatalyst> SCULK_CATALYST = getBlockType("sculk_catalyst");
    /**
     * BlockData: {@link SculkShrieker}
     */
    BlockType.Typed<SculkShrieker> SCULK_SHRIEKER = getBlockType("sculk_shrieker");
    BlockType.Typed<BlockData> COPPER_BLOCK = getBlockType("copper_block");
    BlockType.Typed<BlockData> EXPOSED_COPPER = getBlockType("exposed_copper");
    BlockType.Typed<BlockData> WEATHERED_COPPER = getBlockType("weathered_copper");
    BlockType.Typed<BlockData> OXIDIZED_COPPER = getBlockType("oxidized_copper");
    BlockType.Typed<BlockData> COPPER_ORE = getBlockType("copper_ore");
    BlockType.Typed<BlockData> DEEPSLATE_COPPER_ORE = getBlockType("deepslate_copper_ore");
    BlockType.Typed<BlockData> OXIDIZED_CUT_COPPER = getBlockType("oxidized_cut_copper");
    BlockType.Typed<BlockData> WEATHERED_CUT_COPPER = getBlockType("weathered_cut_copper");
    BlockType.Typed<BlockData> EXPOSED_CUT_COPPER = getBlockType("exposed_cut_copper");
    BlockType.Typed<BlockData> CUT_COPPER = getBlockType("cut_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> OXIDIZED_CHISELED_COPPER = getBlockType("oxidized_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> WEATHERED_CHISELED_COPPER = getBlockType("weathered_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> EXPOSED_CHISELED_COPPER = getBlockType("exposed_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> CHISELED_COPPER = getBlockType("chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> WAXED_OXIDIZED_CHISELED_COPPER = getBlockType("waxed_oxidized_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> WAXED_WEATHERED_CHISELED_COPPER = getBlockType("waxed_weathered_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> WAXED_EXPOSED_CHISELED_COPPER = getBlockType("waxed_exposed_chiseled_copper");
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<BlockData> WAXED_CHISELED_COPPER = getBlockType("waxed_chiseled_copper");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> OXIDIZED_CUT_COPPER_STAIRS = getBlockType("oxidized_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WEATHERED_CUT_COPPER_STAIRS = getBlockType("weathered_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> EXPOSED_CUT_COPPER_STAIRS = getBlockType("exposed_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> CUT_COPPER_STAIRS = getBlockType("cut_copper_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> OXIDIZED_CUT_COPPER_SLAB = getBlockType("oxidized_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WEATHERED_CUT_COPPER_SLAB = getBlockType("weathered_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> EXPOSED_CUT_COPPER_SLAB = getBlockType("exposed_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> CUT_COPPER_SLAB = getBlockType("cut_copper_slab");
    BlockType.Typed<BlockData> WAXED_COPPER_BLOCK = getBlockType("waxed_copper_block");
    BlockType.Typed<BlockData> WAXED_WEATHERED_COPPER = getBlockType("waxed_weathered_copper");
    BlockType.Typed<BlockData> WAXED_EXPOSED_COPPER = getBlockType("waxed_exposed_copper");
    BlockType.Typed<BlockData> WAXED_OXIDIZED_COPPER = getBlockType("waxed_oxidized_copper");
    BlockType.Typed<BlockData> WAXED_OXIDIZED_CUT_COPPER = getBlockType("waxed_oxidized_cut_copper");
    BlockType.Typed<BlockData> WAXED_WEATHERED_CUT_COPPER = getBlockType("waxed_weathered_cut_copper");
    BlockType.Typed<BlockData> WAXED_EXPOSED_CUT_COPPER = getBlockType("waxed_exposed_cut_copper");
    BlockType.Typed<BlockData> WAXED_CUT_COPPER = getBlockType("waxed_cut_copper");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WAXED_OXIDIZED_CUT_COPPER_STAIRS = getBlockType("waxed_oxidized_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WAXED_WEATHERED_CUT_COPPER_STAIRS = getBlockType("waxed_weathered_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WAXED_EXPOSED_CUT_COPPER_STAIRS = getBlockType("waxed_exposed_cut_copper_stairs");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> WAXED_CUT_COPPER_STAIRS = getBlockType("waxed_cut_copper_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WAXED_OXIDIZED_CUT_COPPER_SLAB = getBlockType("waxed_oxidized_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WAXED_WEATHERED_CUT_COPPER_SLAB = getBlockType("waxed_weathered_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WAXED_EXPOSED_CUT_COPPER_SLAB = getBlockType("waxed_exposed_cut_copper_slab");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> WAXED_CUT_COPPER_SLAB = getBlockType("waxed_cut_copper_slab");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> COPPER_DOOR = getBlockType("copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> EXPOSED_COPPER_DOOR = getBlockType("exposed_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> OXIDIZED_COPPER_DOOR = getBlockType("oxidized_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> WEATHERED_COPPER_DOOR = getBlockType("weathered_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> WAXED_COPPER_DOOR = getBlockType("waxed_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> WAXED_EXPOSED_COPPER_DOOR = getBlockType("waxed_exposed_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> WAXED_OXIDIZED_COPPER_DOOR = getBlockType("waxed_oxidized_copper_door");
    /**
     * BlockData: {@link Door}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Door> WAXED_WEATHERED_COPPER_DOOR = getBlockType("waxed_weathered_copper_door");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> COPPER_TRAPDOOR = getBlockType("copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> EXPOSED_COPPER_TRAPDOOR = getBlockType("exposed_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> OXIDIZED_COPPER_TRAPDOOR = getBlockType("oxidized_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> WEATHERED_COPPER_TRAPDOOR = getBlockType("weathered_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> WAXED_COPPER_TRAPDOOR = getBlockType("waxed_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> WAXED_EXPOSED_COPPER_TRAPDOOR = getBlockType("waxed_exposed_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> WAXED_OXIDIZED_COPPER_TRAPDOOR = getBlockType("waxed_oxidized_copper_trapdoor");
    /**
     * BlockData: {@link TrapDoor}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrapDoor> WAXED_WEATHERED_COPPER_TRAPDOOR = getBlockType("waxed_weathered_copper_trapdoor");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> COPPER_GRATE = getBlockType("copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> EXPOSED_COPPER_GRATE = getBlockType("exposed_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> WEATHERED_COPPER_GRATE = getBlockType("weathered_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> OXIDIZED_COPPER_GRATE = getBlockType("oxidized_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> WAXED_COPPER_GRATE = getBlockType("waxed_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> WAXED_EXPOSED_COPPER_GRATE = getBlockType("waxed_exposed_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> WAXED_WEATHERED_COPPER_GRATE = getBlockType("waxed_weathered_copper_grate");
    /**
     * BlockData: {@link Waterlogged}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Waterlogged> WAXED_OXIDIZED_COPPER_GRATE = getBlockType("waxed_oxidized_copper_grate");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> COPPER_BULB = getBlockType("copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> EXPOSED_COPPER_BULB = getBlockType("exposed_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> WEATHERED_COPPER_BULB = getBlockType("weathered_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> OXIDIZED_COPPER_BULB = getBlockType("oxidized_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> WAXED_COPPER_BULB = getBlockType("waxed_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> WAXED_EXPOSED_COPPER_BULB = getBlockType("waxed_exposed_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> WAXED_WEATHERED_COPPER_BULB = getBlockType("waxed_weathered_copper_bulb");
    /**
     * BlockData: {@link CopperBulb}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<CopperBulb> WAXED_OXIDIZED_COPPER_BULB = getBlockType("waxed_oxidized_copper_bulb");
    /**
     * BlockData: {@link LightningRod}
     */
    BlockType.Typed<LightningRod> LIGHTNING_ROD = getBlockType("lightning_rod");
    /**
     * BlockData: {@link PointedDripstone}
     */
    BlockType.Typed<PointedDripstone> POINTED_DRIPSTONE = getBlockType("pointed_dripstone");
    BlockType.Typed<BlockData> DRIPSTONE_BLOCK = getBlockType("dripstone_block");
    /**
     * BlockData: {@link CaveVines}
     */
    BlockType.Typed<CaveVines> CAVE_VINES = getBlockType("cave_vines");
    /**
     * BlockData: {@link CaveVinesPlant}
     */
    BlockType.Typed<CaveVinesPlant> CAVE_VINES_PLANT = getBlockType("cave_vines_plant");
    BlockType.Typed<BlockData> SPORE_BLOSSOM = getBlockType("spore_blossom");
    BlockType.Typed<BlockData> AZALEA = getBlockType("azalea");
    BlockType.Typed<BlockData> FLOWERING_AZALEA = getBlockType("flowering_azalea");
    BlockType.Typed<BlockData> MOSS_CARPET = getBlockType("moss_carpet");
    /**
     * BlockData: {@link PinkPetals}
     */
    BlockType.Typed<PinkPetals> PINK_PETALS = getBlockType("pink_petals");
    BlockType.Typed<BlockData> MOSS_BLOCK = getBlockType("moss_block");
    /**
     * BlockData: {@link BigDripleaf}
     */
    BlockType.Typed<BigDripleaf> BIG_DRIPLEAF = getBlockType("big_dripleaf");
    /**
     * BlockData: {@link Dripleaf}
     */
    BlockType.Typed<Dripleaf> BIG_DRIPLEAF_STEM = getBlockType("big_dripleaf_stem");
    /**
     * BlockData: {@link SmallDripleaf}
     */
    BlockType.Typed<SmallDripleaf> SMALL_DRIPLEAF = getBlockType("small_dripleaf");
    /**
     * BlockData: {@link Waterlogged}
     */
    BlockType.Typed<Waterlogged> HANGING_ROOTS = getBlockType("hanging_roots");
    BlockType.Typed<BlockData> ROOTED_DIRT = getBlockType("rooted_dirt");
    BlockType.Typed<BlockData> MUD = getBlockType("mud");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> DEEPSLATE = getBlockType("deepslate");
    BlockType.Typed<BlockData> COBBLED_DEEPSLATE = getBlockType("cobbled_deepslate");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> COBBLED_DEEPSLATE_STAIRS = getBlockType("cobbled_deepslate_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> COBBLED_DEEPSLATE_SLAB = getBlockType("cobbled_deepslate_slab");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> COBBLED_DEEPSLATE_WALL = getBlockType("cobbled_deepslate_wall");
    BlockType.Typed<BlockData> POLISHED_DEEPSLATE = getBlockType("polished_deepslate");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> POLISHED_DEEPSLATE_STAIRS = getBlockType("polished_deepslate_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> POLISHED_DEEPSLATE_SLAB = getBlockType("polished_deepslate_slab");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> POLISHED_DEEPSLATE_WALL = getBlockType("polished_deepslate_wall");
    BlockType.Typed<BlockData> DEEPSLATE_TILES = getBlockType("deepslate_tiles");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> DEEPSLATE_TILE_STAIRS = getBlockType("deepslate_tile_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> DEEPSLATE_TILE_SLAB = getBlockType("deepslate_tile_slab");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> DEEPSLATE_TILE_WALL = getBlockType("deepslate_tile_wall");
    BlockType.Typed<BlockData> DEEPSLATE_BRICKS = getBlockType("deepslate_bricks");
    /**
     * BlockData: {@link Stairs}
     */
    BlockType.Typed<Stairs> DEEPSLATE_BRICK_STAIRS = getBlockType("deepslate_brick_stairs");
    /**
     * BlockData: {@link Slab}
     */
    BlockType.Typed<Slab> DEEPSLATE_BRICK_SLAB = getBlockType("deepslate_brick_slab");
    /**
     * BlockData: {@link Wall}
     */
    BlockType.Typed<Wall> DEEPSLATE_BRICK_WALL = getBlockType("deepslate_brick_wall");
    BlockType.Typed<BlockData> CHISELED_DEEPSLATE = getBlockType("chiseled_deepslate");
    BlockType.Typed<BlockData> CRACKED_DEEPSLATE_BRICKS = getBlockType("cracked_deepslate_bricks");
    BlockType.Typed<BlockData> CRACKED_DEEPSLATE_TILES = getBlockType("cracked_deepslate_tiles");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> INFESTED_DEEPSLATE = getBlockType("infested_deepslate");
    BlockType.Typed<BlockData> SMOOTH_BASALT = getBlockType("smooth_basalt");
    BlockType.Typed<BlockData> RAW_IRON_BLOCK = getBlockType("raw_iron_block");
    BlockType.Typed<BlockData> RAW_COPPER_BLOCK = getBlockType("raw_copper_block");
    BlockType.Typed<BlockData> RAW_GOLD_BLOCK = getBlockType("raw_gold_block");
    BlockType.Typed<BlockData> POTTED_AZALEA_BUSH = getBlockType("potted_azalea_bush");
    BlockType.Typed<BlockData> POTTED_FLOWERING_AZALEA_BUSH = getBlockType("potted_flowering_azalea_bush");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> OCHRE_FROGLIGHT = getBlockType("ochre_froglight");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> VERDANT_FROGLIGHT = getBlockType("verdant_froglight");
    /**
     * BlockData: {@link Orientable}
     */
    BlockType.Typed<Orientable> PEARLESCENT_FROGLIGHT = getBlockType("pearlescent_froglight");
    BlockType.Typed<BlockData> FROGSPAWN = getBlockType("frogspawn");
    BlockType.Typed<BlockData> REINFORCED_DEEPSLATE = getBlockType("reinforced_deepslate");
    /**
     * BlockData: {@link DecoratedPot}
     */
    BlockType.Typed<DecoratedPot> DECORATED_POT = getBlockType("decorated_pot");
    /**
     * BlockData: {@link Crafter}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<Crafter> CRAFTER = getBlockType("crafter");
    /**
     * BlockData: {@link TrialSpawner}
     */
    @MinecraftExperimental(Requires.UPDATE_1_21)
    @ApiStatus.Experimental
    BlockType.Typed<TrialSpawner> TRIAL_SPAWNER = getBlockType("trial_spawner");

    //</editor-fold>

    @NotNull
    private static <B extends BlockType> B getBlockType(@NotNull String key) {
        NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
        BlockType blockType = Registry.BLOCK.get(namespacedKey);
        Preconditions.checkNotNull(blockType, "No BlockType found for %s. This is a bug.", namespacedKey);
        // Cast instead of using BlockType#typed, since block type can be a mock during testing and would return null
        return (B) blockType;
    }

    /**
     * Yields this block type as a typed version of itself with a plain {@link BlockData} representing it.
     *
     * @return the typed block type.
     */
    @NotNull
    BlockType.Typed<BlockData> typed();

    /**
     * Yields this block type as a typed version of itself with a specific {@link BlockData} representing it.
     *
     * @param blockDataType the class type of the {@link BlockData} to type this {@link BlockType} with.
     * @param <B>          the generic type of the block data to type this block type with.
     * @return the typed block type.
     */
    @NotNull
    <B extends BlockData> BlockType.Typed<B> typed(@NotNull Class<B> blockDataType);

    /**
     * Returns true if this BlockType has a corresponding {@link ItemType}.
     *
     * @return true if there is a corresponding ItemType, otherwise false
     * @see #getItemType()
     */
    boolean hasItemType();

    /**
     * Returns the corresponding {@link ItemType} for the given BlockType.
     * <p>
     * If there is no corresponding {@link ItemType} an error will be thrown.
     *
     * @return the corresponding ItemType
     * @see #hasItemType()
     * @see BlockData#getPlacementMaterial()
     */
    @NotNull
    ItemType getItemType();

    /**
     * Gets the BlockData class of this BlockType
     *
     * @return the BlockData class of this BlockType
     */
    @NotNull
    Class<? extends BlockData> getBlockDataClass();

    /**
     * Creates a new {@link BlockData} instance for this block type, with all
     * properties initialized to unspecified defaults.
     *
     * @return new data instance
     */
    @NotNull
    BlockData createBlockData();

    /**
     * Creates a new {@link BlockData} instance for this block type, with all
     * properties initialized to unspecified defaults, except for those provided
     * in data.
     *
     * @param data data string
     * @return new data instance
     * @throws IllegalArgumentException if the specified data is not valid
     */
    @NotNull
    BlockData createBlockData(@Nullable String data);

    /**
     * Check if the blockt type is solid (can be built upon)
     *
     * @return True if this block type is solid
     */
    boolean isSolid();

    /**
     * Check if the block type can catch fire
     *
     * @return True if this block type can catch fire
     */
    boolean isFlammable();

    /**
     * Check if the block type can burn away
     *
     * @return True if this block type can burn away
     */
    boolean isBurnable();

    /**
     * Check if the block type is occludes light in the lighting engine.
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
     * block, see the <a href="https://minecraft.fandom.com/wiki/Opacity">Minecraft Wiki</a>.
     *
     * @return True if this block type occludes light
     */
    boolean isOccluding();

    /**
     * @return True if this block type is affected by gravity.
     */
    boolean hasGravity();

    /**
     * Checks if this block type can be interacted with.
     * <p>
     * Interactable block types include those with functionality when they are
     * interacted with by a player such as chests, furnaces, etc.
     * <p>
     * Some blocks such as piston heads and stairs are considered interactable
     * though may not perform any additional functionality.
     * <p>
     * Note that the interactability of some block types may be dependant on their
     * state as well. This method will return true if there is at least one
     * state in which additional interact handling is performed for the
     * block type.
     *
     * @return true if this block type can be interacted with.
     */
    boolean isInteractable();

    /**
     * Obtains the block's hardness level (also known as "strength").
     * <br>
     * This number is used to calculate the time required to break each block.
     *
     * @return the hardness of that block type.
     */
    float getHardness();

    /**
     * Obtains the blast resistance value (also known as block "durability").
     * <br>
     * This value is used in explosions to calculate whether a block should be
     * broken or not.
     *
     * @return the blast resistance of that block type.
     */
    float getBlastResistance();

    /**
     * Returns a value that represents how 'slippery' the block is.
     * <p>
     * Blocks with higher slipperiness, like {@link BlockType#ICE} can be slid on
     * further by the player and other entities.
     * <p>
     * Most blocks have a default slipperiness of {@code 0.6f}.
     *
     * @return the slipperiness of this block
     */
    float getSlipperiness();

    /**
     * Check if the block type is an air block.
     *
     * @return True if this block type is an air block.
     */
    boolean isAir();

    /**
     * Gets if the BlockType is enabled by the features in a world.
     *
     * @param world the world to check
     * @return true if this BlockType can be used in this World.
     */
    boolean isEnabledByFeature(@NotNull World world);

    /**
     * Tries to convert this BlockType into a Material
     *
     * @return the converted Material or null
     * @deprecated only for internal use
     */
    @Nullable
    @Deprecated
    Material asMaterial();
}
