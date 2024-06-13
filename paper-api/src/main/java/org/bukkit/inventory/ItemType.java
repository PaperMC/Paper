package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import java.util.function.Consumer;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
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
public interface ItemType extends Keyed, Translatable {

    /**
     * Typed represents a subtype of {@link ItemType}s that have a known item meta type
     * at compile time.
     *
     * @param <M> the generic type of the item meta that represents the item type.
     */
    interface Typed<M extends ItemMeta> extends ItemType {

        /**
         * Gets the ItemMeta class of this ItemType
         *
         * @return the ItemMeta class of this ItemType
         */
        @Override
        @NotNull
        Class<M> getItemMetaClass();

        /**
         * Constructs a new item stack with this item type with the amount 1.
         *
         * @param metaConfigurator an optional consumer of the items {@link ItemMeta} that is called.
         *                         May be null if no intent exists to mutate the item meta at this point.
         * @return the created and configured item stack.
         */
        @NotNull
        ItemStack createItemStack(@Nullable Consumer<? super M> metaConfigurator);

        /**
         * Constructs a new item stack with this item type.
         *
         * @param amount           the amount of itemstack.
         * @param metaConfigurator an optional consumer of the items {@link ItemMeta} that is called.
         *                         May be null if no intent exists to mutate the item meta at this point.
         * @return the created and configured item stack.
         */
        @NotNull
        ItemStack createItemStack(int amount, @Nullable Consumer<? super M> metaConfigurator);
    }

    //<editor-fold desc="ItemTypes" defaultstate="collapsed">
    /**
     * Air does not have any ItemMeta
     */
    ItemType AIR = getItemType("air");
    ItemType.Typed<ItemMeta> STONE = getItemType("stone");
    ItemType.Typed<ItemMeta> GRANITE = getItemType("granite");
    ItemType.Typed<ItemMeta> POLISHED_GRANITE = getItemType("polished_granite");
    ItemType.Typed<ItemMeta> DIORITE = getItemType("diorite");
    ItemType.Typed<ItemMeta> POLISHED_DIORITE = getItemType("polished_diorite");
    ItemType.Typed<ItemMeta> ANDESITE = getItemType("andesite");
    ItemType.Typed<ItemMeta> POLISHED_ANDESITE = getItemType("polished_andesite");
    ItemType.Typed<ItemMeta> DEEPSLATE = getItemType("deepslate");
    ItemType.Typed<ItemMeta> COBBLED_DEEPSLATE = getItemType("cobbled_deepslate");
    ItemType.Typed<ItemMeta> POLISHED_DEEPSLATE = getItemType("polished_deepslate");
    ItemType.Typed<ItemMeta> CALCITE = getItemType("calcite");
    ItemType.Typed<ItemMeta> TUFF = getItemType("tuff");
    ItemType.Typed<ItemMeta> TUFF_SLAB = getItemType("tuff_slab");
    ItemType.Typed<ItemMeta> TUFF_STAIRS = getItemType("tuff_stairs");
    ItemType.Typed<ItemMeta> TUFF_WALL = getItemType("tuff_wall");
    ItemType.Typed<ItemMeta> CHISELED_TUFF = getItemType("chiseled_tuff");
    ItemType.Typed<ItemMeta> POLISHED_TUFF = getItemType("polished_tuff");
    ItemType.Typed<ItemMeta> POLISHED_TUFF_SLAB = getItemType("polished_tuff_slab");
    ItemType.Typed<ItemMeta> POLISHED_TUFF_STAIRS = getItemType("polished_tuff_stairs");
    ItemType.Typed<ItemMeta> POLISHED_TUFF_WALL = getItemType("polished_tuff_wall");
    ItemType.Typed<ItemMeta> TUFF_BRICKS = getItemType("tuff_bricks");
    ItemType.Typed<ItemMeta> TUFF_BRICK_SLAB = getItemType("tuff_brick_slab");
    ItemType.Typed<ItemMeta> TUFF_BRICK_STAIRS = getItemType("tuff_brick_stairs");
    ItemType.Typed<ItemMeta> TUFF_BRICK_WALL = getItemType("tuff_brick_wall");
    ItemType.Typed<ItemMeta> CHISELED_TUFF_BRICKS = getItemType("chiseled_tuff_bricks");
    ItemType.Typed<ItemMeta> DRIPSTONE_BLOCK = getItemType("dripstone_block");
    ItemType.Typed<ItemMeta> GRASS_BLOCK = getItemType("grass_block");
    ItemType.Typed<ItemMeta> DIRT = getItemType("dirt");
    ItemType.Typed<ItemMeta> COARSE_DIRT = getItemType("coarse_dirt");
    ItemType.Typed<ItemMeta> PODZOL = getItemType("podzol");
    ItemType.Typed<ItemMeta> ROOTED_DIRT = getItemType("rooted_dirt");
    ItemType.Typed<ItemMeta> MUD = getItemType("mud");
    ItemType.Typed<ItemMeta> CRIMSON_NYLIUM = getItemType("crimson_nylium");
    ItemType.Typed<ItemMeta> WARPED_NYLIUM = getItemType("warped_nylium");
    ItemType.Typed<ItemMeta> COBBLESTONE = getItemType("cobblestone");
    ItemType.Typed<ItemMeta> OAK_PLANKS = getItemType("oak_planks");
    ItemType.Typed<ItemMeta> SPRUCE_PLANKS = getItemType("spruce_planks");
    ItemType.Typed<ItemMeta> BIRCH_PLANKS = getItemType("birch_planks");
    ItemType.Typed<ItemMeta> JUNGLE_PLANKS = getItemType("jungle_planks");
    ItemType.Typed<ItemMeta> ACACIA_PLANKS = getItemType("acacia_planks");
    ItemType.Typed<ItemMeta> CHERRY_PLANKS = getItemType("cherry_planks");
    ItemType.Typed<ItemMeta> DARK_OAK_PLANKS = getItemType("dark_oak_planks");
    ItemType.Typed<ItemMeta> MANGROVE_PLANKS = getItemType("mangrove_planks");
    ItemType.Typed<ItemMeta> BAMBOO_PLANKS = getItemType("bamboo_planks");
    ItemType.Typed<ItemMeta> CRIMSON_PLANKS = getItemType("crimson_planks");
    ItemType.Typed<ItemMeta> WARPED_PLANKS = getItemType("warped_planks");
    ItemType.Typed<ItemMeta> BAMBOO_MOSAIC = getItemType("bamboo_mosaic");
    ItemType.Typed<ItemMeta> OAK_SAPLING = getItemType("oak_sapling");
    ItemType.Typed<ItemMeta> SPRUCE_SAPLING = getItemType("spruce_sapling");
    ItemType.Typed<ItemMeta> BIRCH_SAPLING = getItemType("birch_sapling");
    ItemType.Typed<ItemMeta> JUNGLE_SAPLING = getItemType("jungle_sapling");
    ItemType.Typed<ItemMeta> ACACIA_SAPLING = getItemType("acacia_sapling");
    ItemType.Typed<ItemMeta> CHERRY_SAPLING = getItemType("cherry_sapling");
    ItemType.Typed<ItemMeta> DARK_OAK_SAPLING = getItemType("dark_oak_sapling");
    ItemType.Typed<ItemMeta> MANGROVE_PROPAGULE = getItemType("mangrove_propagule");
    ItemType.Typed<ItemMeta> BEDROCK = getItemType("bedrock");
    ItemType.Typed<ItemMeta> SAND = getItemType("sand");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SUSPICIOUS_SAND = getItemType("suspicious_sand");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SUSPICIOUS_GRAVEL = getItemType("suspicious_gravel");
    ItemType.Typed<ItemMeta> RED_SAND = getItemType("red_sand");
    ItemType.Typed<ItemMeta> GRAVEL = getItemType("gravel");
    ItemType.Typed<ItemMeta> COAL_ORE = getItemType("coal_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_COAL_ORE = getItemType("deepslate_coal_ore");
    ItemType.Typed<ItemMeta> IRON_ORE = getItemType("iron_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_IRON_ORE = getItemType("deepslate_iron_ore");
    ItemType.Typed<ItemMeta> COPPER_ORE = getItemType("copper_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_COPPER_ORE = getItemType("deepslate_copper_ore");
    ItemType.Typed<ItemMeta> GOLD_ORE = getItemType("gold_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_GOLD_ORE = getItemType("deepslate_gold_ore");
    ItemType.Typed<ItemMeta> REDSTONE_ORE = getItemType("redstone_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_REDSTONE_ORE = getItemType("deepslate_redstone_ore");
    ItemType.Typed<ItemMeta> EMERALD_ORE = getItemType("emerald_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_EMERALD_ORE = getItemType("deepslate_emerald_ore");
    ItemType.Typed<ItemMeta> LAPIS_ORE = getItemType("lapis_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_LAPIS_ORE = getItemType("deepslate_lapis_ore");
    ItemType.Typed<ItemMeta> DIAMOND_ORE = getItemType("diamond_ore");
    ItemType.Typed<ItemMeta> DEEPSLATE_DIAMOND_ORE = getItemType("deepslate_diamond_ore");
    ItemType.Typed<ItemMeta> NETHER_GOLD_ORE = getItemType("nether_gold_ore");
    ItemType.Typed<ItemMeta> NETHER_QUARTZ_ORE = getItemType("nether_quartz_ore");
    ItemType.Typed<ItemMeta> ANCIENT_DEBRIS = getItemType("ancient_debris");
    ItemType.Typed<ItemMeta> COAL_BLOCK = getItemType("coal_block");
    ItemType.Typed<ItemMeta> RAW_IRON_BLOCK = getItemType("raw_iron_block");
    ItemType.Typed<ItemMeta> RAW_COPPER_BLOCK = getItemType("raw_copper_block");
    ItemType.Typed<ItemMeta> RAW_GOLD_BLOCK = getItemType("raw_gold_block");
    ItemType.Typed<ItemMeta> HEAVY_CORE = getItemType("heavy_core");
    ItemType.Typed<ItemMeta> AMETHYST_BLOCK = getItemType("amethyst_block");
    ItemType.Typed<ItemMeta> BUDDING_AMETHYST = getItemType("budding_amethyst");
    ItemType.Typed<ItemMeta> IRON_BLOCK = getItemType("iron_block");
    ItemType.Typed<ItemMeta> COPPER_BLOCK = getItemType("copper_block");
    ItemType.Typed<ItemMeta> GOLD_BLOCK = getItemType("gold_block");
    ItemType.Typed<ItemMeta> DIAMOND_BLOCK = getItemType("diamond_block");
    ItemType.Typed<ItemMeta> NETHERITE_BLOCK = getItemType("netherite_block");
    ItemType.Typed<ItemMeta> EXPOSED_COPPER = getItemType("exposed_copper");
    ItemType.Typed<ItemMeta> WEATHERED_COPPER = getItemType("weathered_copper");
    ItemType.Typed<ItemMeta> OXIDIZED_COPPER = getItemType("oxidized_copper");
    ItemType.Typed<ItemMeta> CHISELED_COPPER = getItemType("chiseled_copper");
    ItemType.Typed<ItemMeta> EXPOSED_CHISELED_COPPER = getItemType("exposed_chiseled_copper");
    ItemType.Typed<ItemMeta> WEATHERED_CHISELED_COPPER = getItemType("weathered_chiseled_copper");
    ItemType.Typed<ItemMeta> OXIDIZED_CHISELED_COPPER = getItemType("oxidized_chiseled_copper");
    ItemType.Typed<ItemMeta> CUT_COPPER = getItemType("cut_copper");
    ItemType.Typed<ItemMeta> EXPOSED_CUT_COPPER = getItemType("exposed_cut_copper");
    ItemType.Typed<ItemMeta> WEATHERED_CUT_COPPER = getItemType("weathered_cut_copper");
    ItemType.Typed<ItemMeta> OXIDIZED_CUT_COPPER = getItemType("oxidized_cut_copper");
    ItemType.Typed<ItemMeta> CUT_COPPER_STAIRS = getItemType("cut_copper_stairs");
    ItemType.Typed<ItemMeta> EXPOSED_CUT_COPPER_STAIRS = getItemType("exposed_cut_copper_stairs");
    ItemType.Typed<ItemMeta> WEATHERED_CUT_COPPER_STAIRS = getItemType("weathered_cut_copper_stairs");
    ItemType.Typed<ItemMeta> OXIDIZED_CUT_COPPER_STAIRS = getItemType("oxidized_cut_copper_stairs");
    ItemType.Typed<ItemMeta> CUT_COPPER_SLAB = getItemType("cut_copper_slab");
    ItemType.Typed<ItemMeta> EXPOSED_CUT_COPPER_SLAB = getItemType("exposed_cut_copper_slab");
    ItemType.Typed<ItemMeta> WEATHERED_CUT_COPPER_SLAB = getItemType("weathered_cut_copper_slab");
    ItemType.Typed<ItemMeta> OXIDIZED_CUT_COPPER_SLAB = getItemType("oxidized_cut_copper_slab");
    ItemType.Typed<ItemMeta> WAXED_COPPER_BLOCK = getItemType("waxed_copper_block");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_COPPER = getItemType("waxed_exposed_copper");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_COPPER = getItemType("waxed_weathered_copper");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_COPPER = getItemType("waxed_oxidized_copper");
    ItemType.Typed<ItemMeta> WAXED_CHISELED_COPPER = getItemType("waxed_chiseled_copper");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_CHISELED_COPPER = getItemType("waxed_exposed_chiseled_copper");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_CHISELED_COPPER = getItemType("waxed_weathered_chiseled_copper");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_CHISELED_COPPER = getItemType("waxed_oxidized_chiseled_copper");
    ItemType.Typed<ItemMeta> WAXED_CUT_COPPER = getItemType("waxed_cut_copper");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_CUT_COPPER = getItemType("waxed_exposed_cut_copper");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_CUT_COPPER = getItemType("waxed_weathered_cut_copper");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_CUT_COPPER = getItemType("waxed_oxidized_cut_copper");
    ItemType.Typed<ItemMeta> WAXED_CUT_COPPER_STAIRS = getItemType("waxed_cut_copper_stairs");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_CUT_COPPER_STAIRS = getItemType("waxed_exposed_cut_copper_stairs");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_CUT_COPPER_STAIRS = getItemType("waxed_weathered_cut_copper_stairs");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_CUT_COPPER_STAIRS = getItemType("waxed_oxidized_cut_copper_stairs");
    ItemType.Typed<ItemMeta> WAXED_CUT_COPPER_SLAB = getItemType("waxed_cut_copper_slab");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_CUT_COPPER_SLAB = getItemType("waxed_exposed_cut_copper_slab");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_CUT_COPPER_SLAB = getItemType("waxed_weathered_cut_copper_slab");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_CUT_COPPER_SLAB = getItemType("waxed_oxidized_cut_copper_slab");
    ItemType.Typed<ItemMeta> OAK_LOG = getItemType("oak_log");
    ItemType.Typed<ItemMeta> SPRUCE_LOG = getItemType("spruce_log");
    ItemType.Typed<ItemMeta> BIRCH_LOG = getItemType("birch_log");
    ItemType.Typed<ItemMeta> JUNGLE_LOG = getItemType("jungle_log");
    ItemType.Typed<ItemMeta> ACACIA_LOG = getItemType("acacia_log");
    ItemType.Typed<ItemMeta> CHERRY_LOG = getItemType("cherry_log");
    ItemType.Typed<ItemMeta> DARK_OAK_LOG = getItemType("dark_oak_log");
    ItemType.Typed<ItemMeta> MANGROVE_LOG = getItemType("mangrove_log");
    ItemType.Typed<ItemMeta> MANGROVE_ROOTS = getItemType("mangrove_roots");
    ItemType.Typed<ItemMeta> MUDDY_MANGROVE_ROOTS = getItemType("muddy_mangrove_roots");
    ItemType.Typed<ItemMeta> CRIMSON_STEM = getItemType("crimson_stem");
    ItemType.Typed<ItemMeta> WARPED_STEM = getItemType("warped_stem");
    ItemType.Typed<ItemMeta> BAMBOO_BLOCK = getItemType("bamboo_block");
    ItemType.Typed<ItemMeta> STRIPPED_OAK_LOG = getItemType("stripped_oak_log");
    ItemType.Typed<ItemMeta> STRIPPED_SPRUCE_LOG = getItemType("stripped_spruce_log");
    ItemType.Typed<ItemMeta> STRIPPED_BIRCH_LOG = getItemType("stripped_birch_log");
    ItemType.Typed<ItemMeta> STRIPPED_JUNGLE_LOG = getItemType("stripped_jungle_log");
    ItemType.Typed<ItemMeta> STRIPPED_ACACIA_LOG = getItemType("stripped_acacia_log");
    ItemType.Typed<ItemMeta> STRIPPED_CHERRY_LOG = getItemType("stripped_cherry_log");
    ItemType.Typed<ItemMeta> STRIPPED_DARK_OAK_LOG = getItemType("stripped_dark_oak_log");
    ItemType.Typed<ItemMeta> STRIPPED_MANGROVE_LOG = getItemType("stripped_mangrove_log");
    ItemType.Typed<ItemMeta> STRIPPED_CRIMSON_STEM = getItemType("stripped_crimson_stem");
    ItemType.Typed<ItemMeta> STRIPPED_WARPED_STEM = getItemType("stripped_warped_stem");
    ItemType.Typed<ItemMeta> STRIPPED_OAK_WOOD = getItemType("stripped_oak_wood");
    ItemType.Typed<ItemMeta> STRIPPED_SPRUCE_WOOD = getItemType("stripped_spruce_wood");
    ItemType.Typed<ItemMeta> STRIPPED_BIRCH_WOOD = getItemType("stripped_birch_wood");
    ItemType.Typed<ItemMeta> STRIPPED_JUNGLE_WOOD = getItemType("stripped_jungle_wood");
    ItemType.Typed<ItemMeta> STRIPPED_ACACIA_WOOD = getItemType("stripped_acacia_wood");
    ItemType.Typed<ItemMeta> STRIPPED_CHERRY_WOOD = getItemType("stripped_cherry_wood");
    ItemType.Typed<ItemMeta> STRIPPED_DARK_OAK_WOOD = getItemType("stripped_dark_oak_wood");
    ItemType.Typed<ItemMeta> STRIPPED_MANGROVE_WOOD = getItemType("stripped_mangrove_wood");
    ItemType.Typed<ItemMeta> STRIPPED_CRIMSON_HYPHAE = getItemType("stripped_crimson_hyphae");
    ItemType.Typed<ItemMeta> STRIPPED_WARPED_HYPHAE = getItemType("stripped_warped_hyphae");
    ItemType.Typed<ItemMeta> STRIPPED_BAMBOO_BLOCK = getItemType("stripped_bamboo_block");
    ItemType.Typed<ItemMeta> OAK_WOOD = getItemType("oak_wood");
    ItemType.Typed<ItemMeta> SPRUCE_WOOD = getItemType("spruce_wood");
    ItemType.Typed<ItemMeta> BIRCH_WOOD = getItemType("birch_wood");
    ItemType.Typed<ItemMeta> JUNGLE_WOOD = getItemType("jungle_wood");
    ItemType.Typed<ItemMeta> ACACIA_WOOD = getItemType("acacia_wood");
    ItemType.Typed<ItemMeta> CHERRY_WOOD = getItemType("cherry_wood");
    ItemType.Typed<ItemMeta> DARK_OAK_WOOD = getItemType("dark_oak_wood");
    ItemType.Typed<ItemMeta> MANGROVE_WOOD = getItemType("mangrove_wood");
    ItemType.Typed<ItemMeta> CRIMSON_HYPHAE = getItemType("crimson_hyphae");
    ItemType.Typed<ItemMeta> WARPED_HYPHAE = getItemType("warped_hyphae");
    ItemType.Typed<ItemMeta> OAK_LEAVES = getItemType("oak_leaves");
    ItemType.Typed<ItemMeta> SPRUCE_LEAVES = getItemType("spruce_leaves");
    ItemType.Typed<ItemMeta> BIRCH_LEAVES = getItemType("birch_leaves");
    ItemType.Typed<ItemMeta> JUNGLE_LEAVES = getItemType("jungle_leaves");
    ItemType.Typed<ItemMeta> ACACIA_LEAVES = getItemType("acacia_leaves");
    ItemType.Typed<ItemMeta> CHERRY_LEAVES = getItemType("cherry_leaves");
    ItemType.Typed<ItemMeta> DARK_OAK_LEAVES = getItemType("dark_oak_leaves");
    ItemType.Typed<ItemMeta> MANGROVE_LEAVES = getItemType("mangrove_leaves");
    ItemType.Typed<ItemMeta> AZALEA_LEAVES = getItemType("azalea_leaves");
    ItemType.Typed<ItemMeta> FLOWERING_AZALEA_LEAVES = getItemType("flowering_azalea_leaves");
    ItemType.Typed<ItemMeta> SPONGE = getItemType("sponge");
    ItemType.Typed<ItemMeta> WET_SPONGE = getItemType("wet_sponge");
    ItemType.Typed<ItemMeta> GLASS = getItemType("glass");
    ItemType.Typed<ItemMeta> TINTED_GLASS = getItemType("tinted_glass");
    ItemType.Typed<ItemMeta> LAPIS_BLOCK = getItemType("lapis_block");
    ItemType.Typed<ItemMeta> SANDSTONE = getItemType("sandstone");
    ItemType.Typed<ItemMeta> CHISELED_SANDSTONE = getItemType("chiseled_sandstone");
    ItemType.Typed<ItemMeta> CUT_SANDSTONE = getItemType("cut_sandstone");
    ItemType.Typed<ItemMeta> COBWEB = getItemType("cobweb");
    ItemType.Typed<ItemMeta> SHORT_GRASS = getItemType("short_grass");
    ItemType.Typed<ItemMeta> FERN = getItemType("fern");
    ItemType.Typed<ItemMeta> AZALEA = getItemType("azalea");
    ItemType.Typed<ItemMeta> FLOWERING_AZALEA = getItemType("flowering_azalea");
    ItemType.Typed<ItemMeta> DEAD_BUSH = getItemType("dead_bush");
    ItemType.Typed<ItemMeta> SEAGRASS = getItemType("seagrass");
    ItemType.Typed<ItemMeta> SEA_PICKLE = getItemType("sea_pickle");
    ItemType.Typed<ItemMeta> WHITE_WOOL = getItemType("white_wool");
    ItemType.Typed<ItemMeta> ORANGE_WOOL = getItemType("orange_wool");
    ItemType.Typed<ItemMeta> MAGENTA_WOOL = getItemType("magenta_wool");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_WOOL = getItemType("light_blue_wool");
    ItemType.Typed<ItemMeta> YELLOW_WOOL = getItemType("yellow_wool");
    ItemType.Typed<ItemMeta> LIME_WOOL = getItemType("lime_wool");
    ItemType.Typed<ItemMeta> PINK_WOOL = getItemType("pink_wool");
    ItemType.Typed<ItemMeta> GRAY_WOOL = getItemType("gray_wool");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_WOOL = getItemType("light_gray_wool");
    ItemType.Typed<ItemMeta> CYAN_WOOL = getItemType("cyan_wool");
    ItemType.Typed<ItemMeta> PURPLE_WOOL = getItemType("purple_wool");
    ItemType.Typed<ItemMeta> BLUE_WOOL = getItemType("blue_wool");
    ItemType.Typed<ItemMeta> BROWN_WOOL = getItemType("brown_wool");
    ItemType.Typed<ItemMeta> GREEN_WOOL = getItemType("green_wool");
    ItemType.Typed<ItemMeta> RED_WOOL = getItemType("red_wool");
    ItemType.Typed<ItemMeta> BLACK_WOOL = getItemType("black_wool");
    ItemType.Typed<ItemMeta> DANDELION = getItemType("dandelion");
    ItemType.Typed<ItemMeta> POPPY = getItemType("poppy");
    ItemType.Typed<ItemMeta> BLUE_ORCHID = getItemType("blue_orchid");
    ItemType.Typed<ItemMeta> ALLIUM = getItemType("allium");
    ItemType.Typed<ItemMeta> AZURE_BLUET = getItemType("azure_bluet");
    ItemType.Typed<ItemMeta> RED_TULIP = getItemType("red_tulip");
    ItemType.Typed<ItemMeta> ORANGE_TULIP = getItemType("orange_tulip");
    ItemType.Typed<ItemMeta> WHITE_TULIP = getItemType("white_tulip");
    ItemType.Typed<ItemMeta> PINK_TULIP = getItemType("pink_tulip");
    ItemType.Typed<ItemMeta> OXEYE_DAISY = getItemType("oxeye_daisy");
    ItemType.Typed<ItemMeta> CORNFLOWER = getItemType("cornflower");
    ItemType.Typed<ItemMeta> LILY_OF_THE_VALLEY = getItemType("lily_of_the_valley");
    ItemType.Typed<ItemMeta> WITHER_ROSE = getItemType("wither_rose");
    ItemType.Typed<ItemMeta> TORCHFLOWER = getItemType("torchflower");
    ItemType.Typed<ItemMeta> PITCHER_PLANT = getItemType("pitcher_plant");
    ItemType.Typed<ItemMeta> SPORE_BLOSSOM = getItemType("spore_blossom");
    ItemType.Typed<ItemMeta> BROWN_MUSHROOM = getItemType("brown_mushroom");
    ItemType.Typed<ItemMeta> RED_MUSHROOM = getItemType("red_mushroom");
    ItemType.Typed<ItemMeta> CRIMSON_FUNGUS = getItemType("crimson_fungus");
    ItemType.Typed<ItemMeta> WARPED_FUNGUS = getItemType("warped_fungus");
    ItemType.Typed<ItemMeta> CRIMSON_ROOTS = getItemType("crimson_roots");
    ItemType.Typed<ItemMeta> WARPED_ROOTS = getItemType("warped_roots");
    ItemType.Typed<ItemMeta> NETHER_SPROUTS = getItemType("nether_sprouts");
    ItemType.Typed<ItemMeta> WEEPING_VINES = getItemType("weeping_vines");
    ItemType.Typed<ItemMeta> TWISTING_VINES = getItemType("twisting_vines");
    ItemType.Typed<ItemMeta> SUGAR_CANE = getItemType("sugar_cane");
    ItemType.Typed<ItemMeta> KELP = getItemType("kelp");
    ItemType.Typed<ItemMeta> MOSS_CARPET = getItemType("moss_carpet");
    ItemType.Typed<ItemMeta> PINK_PETALS = getItemType("pink_petals");
    ItemType.Typed<ItemMeta> MOSS_BLOCK = getItemType("moss_block");
    ItemType.Typed<ItemMeta> HANGING_ROOTS = getItemType("hanging_roots");
    ItemType.Typed<ItemMeta> BIG_DRIPLEAF = getItemType("big_dripleaf");
    ItemType.Typed<ItemMeta> SMALL_DRIPLEAF = getItemType("small_dripleaf");
    ItemType.Typed<ItemMeta> BAMBOO = getItemType("bamboo");
    ItemType.Typed<ItemMeta> OAK_SLAB = getItemType("oak_slab");
    ItemType.Typed<ItemMeta> SPRUCE_SLAB = getItemType("spruce_slab");
    ItemType.Typed<ItemMeta> BIRCH_SLAB = getItemType("birch_slab");
    ItemType.Typed<ItemMeta> JUNGLE_SLAB = getItemType("jungle_slab");
    ItemType.Typed<ItemMeta> ACACIA_SLAB = getItemType("acacia_slab");
    ItemType.Typed<ItemMeta> CHERRY_SLAB = getItemType("cherry_slab");
    ItemType.Typed<ItemMeta> DARK_OAK_SLAB = getItemType("dark_oak_slab");
    ItemType.Typed<ItemMeta> MANGROVE_SLAB = getItemType("mangrove_slab");
    ItemType.Typed<ItemMeta> BAMBOO_SLAB = getItemType("bamboo_slab");
    ItemType.Typed<ItemMeta> BAMBOO_MOSAIC_SLAB = getItemType("bamboo_mosaic_slab");
    ItemType.Typed<ItemMeta> CRIMSON_SLAB = getItemType("crimson_slab");
    ItemType.Typed<ItemMeta> WARPED_SLAB = getItemType("warped_slab");
    ItemType.Typed<ItemMeta> STONE_SLAB = getItemType("stone_slab");
    ItemType.Typed<ItemMeta> SMOOTH_STONE_SLAB = getItemType("smooth_stone_slab");
    ItemType.Typed<ItemMeta> SANDSTONE_SLAB = getItemType("sandstone_slab");
    ItemType.Typed<ItemMeta> CUT_SANDSTONE_SLAB = getItemType("cut_sandstone_slab");
    ItemType.Typed<ItemMeta> PETRIFIED_OAK_SLAB = getItemType("petrified_oak_slab");
    ItemType.Typed<ItemMeta> COBBLESTONE_SLAB = getItemType("cobblestone_slab");
    ItemType.Typed<ItemMeta> BRICK_SLAB = getItemType("brick_slab");
    ItemType.Typed<ItemMeta> STONE_BRICK_SLAB = getItemType("stone_brick_slab");
    ItemType.Typed<ItemMeta> MUD_BRICK_SLAB = getItemType("mud_brick_slab");
    ItemType.Typed<ItemMeta> NETHER_BRICK_SLAB = getItemType("nether_brick_slab");
    ItemType.Typed<ItemMeta> QUARTZ_SLAB = getItemType("quartz_slab");
    ItemType.Typed<ItemMeta> RED_SANDSTONE_SLAB = getItemType("red_sandstone_slab");
    ItemType.Typed<ItemMeta> CUT_RED_SANDSTONE_SLAB = getItemType("cut_red_sandstone_slab");
    ItemType.Typed<ItemMeta> PURPUR_SLAB = getItemType("purpur_slab");
    ItemType.Typed<ItemMeta> PRISMARINE_SLAB = getItemType("prismarine_slab");
    ItemType.Typed<ItemMeta> PRISMARINE_BRICK_SLAB = getItemType("prismarine_brick_slab");
    ItemType.Typed<ItemMeta> DARK_PRISMARINE_SLAB = getItemType("dark_prismarine_slab");
    ItemType.Typed<ItemMeta> SMOOTH_QUARTZ = getItemType("smooth_quartz");
    ItemType.Typed<ItemMeta> SMOOTH_RED_SANDSTONE = getItemType("smooth_red_sandstone");
    ItemType.Typed<ItemMeta> SMOOTH_SANDSTONE = getItemType("smooth_sandstone");
    ItemType.Typed<ItemMeta> SMOOTH_STONE = getItemType("smooth_stone");
    ItemType.Typed<ItemMeta> BRICKS = getItemType("bricks");
    ItemType.Typed<ItemMeta> BOOKSHELF = getItemType("bookshelf");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CHISELED_BOOKSHELF = getItemType("chiseled_bookshelf");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DECORATED_POT = getItemType("decorated_pot");
    ItemType.Typed<ItemMeta> MOSSY_COBBLESTONE = getItemType("mossy_cobblestone");
    ItemType.Typed<ItemMeta> OBSIDIAN = getItemType("obsidian");
    ItemType.Typed<ItemMeta> TORCH = getItemType("torch");
    ItemType.Typed<ItemMeta> END_ROD = getItemType("end_rod");
    ItemType.Typed<ItemMeta> CHORUS_PLANT = getItemType("chorus_plant");
    ItemType.Typed<ItemMeta> CHORUS_FLOWER = getItemType("chorus_flower");
    ItemType.Typed<ItemMeta> PURPUR_BLOCK = getItemType("purpur_block");
    ItemType.Typed<ItemMeta> PURPUR_PILLAR = getItemType("purpur_pillar");
    ItemType.Typed<ItemMeta> PURPUR_STAIRS = getItemType("purpur_stairs");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SPAWNER = getItemType("spawner");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CHEST = getItemType("chest");
    ItemType.Typed<ItemMeta> CRAFTING_TABLE = getItemType("crafting_table");
    ItemType.Typed<ItemMeta> FARMLAND = getItemType("farmland");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> FURNACE = getItemType("furnace");
    ItemType.Typed<ItemMeta> LADDER = getItemType("ladder");
    ItemType.Typed<ItemMeta> COBBLESTONE_STAIRS = getItemType("cobblestone_stairs");
    ItemType.Typed<ItemMeta> SNOW = getItemType("snow");
    ItemType.Typed<ItemMeta> ICE = getItemType("ice");
    ItemType.Typed<ItemMeta> SNOW_BLOCK = getItemType("snow_block");
    ItemType.Typed<ItemMeta> CACTUS = getItemType("cactus");
    ItemType.Typed<ItemMeta> CLAY = getItemType("clay");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> JUKEBOX = getItemType("jukebox");
    ItemType.Typed<ItemMeta> OAK_FENCE = getItemType("oak_fence");
    ItemType.Typed<ItemMeta> SPRUCE_FENCE = getItemType("spruce_fence");
    ItemType.Typed<ItemMeta> BIRCH_FENCE = getItemType("birch_fence");
    ItemType.Typed<ItemMeta> JUNGLE_FENCE = getItemType("jungle_fence");
    ItemType.Typed<ItemMeta> ACACIA_FENCE = getItemType("acacia_fence");
    ItemType.Typed<ItemMeta> CHERRY_FENCE = getItemType("cherry_fence");
    ItemType.Typed<ItemMeta> DARK_OAK_FENCE = getItemType("dark_oak_fence");
    ItemType.Typed<ItemMeta> MANGROVE_FENCE = getItemType("mangrove_fence");
    ItemType.Typed<ItemMeta> BAMBOO_FENCE = getItemType("bamboo_fence");
    ItemType.Typed<ItemMeta> CRIMSON_FENCE = getItemType("crimson_fence");
    ItemType.Typed<ItemMeta> WARPED_FENCE = getItemType("warped_fence");
    ItemType.Typed<ItemMeta> PUMPKIN = getItemType("pumpkin");
    ItemType.Typed<ItemMeta> CARVED_PUMPKIN = getItemType("carved_pumpkin");
    ItemType.Typed<ItemMeta> JACK_O_LANTERN = getItemType("jack_o_lantern");
    ItemType.Typed<ItemMeta> NETHERRACK = getItemType("netherrack");
    ItemType.Typed<ItemMeta> SOUL_SAND = getItemType("soul_sand");
    ItemType.Typed<ItemMeta> SOUL_SOIL = getItemType("soul_soil");
    ItemType.Typed<ItemMeta> BASALT = getItemType("basalt");
    ItemType.Typed<ItemMeta> POLISHED_BASALT = getItemType("polished_basalt");
    ItemType.Typed<ItemMeta> SMOOTH_BASALT = getItemType("smooth_basalt");
    ItemType.Typed<ItemMeta> SOUL_TORCH = getItemType("soul_torch");
    ItemType.Typed<ItemMeta> GLOWSTONE = getItemType("glowstone");
    ItemType.Typed<ItemMeta> INFESTED_STONE = getItemType("infested_stone");
    ItemType.Typed<ItemMeta> INFESTED_COBBLESTONE = getItemType("infested_cobblestone");
    ItemType.Typed<ItemMeta> INFESTED_STONE_BRICKS = getItemType("infested_stone_bricks");
    ItemType.Typed<ItemMeta> INFESTED_MOSSY_STONE_BRICKS = getItemType("infested_mossy_stone_bricks");
    ItemType.Typed<ItemMeta> INFESTED_CRACKED_STONE_BRICKS = getItemType("infested_cracked_stone_bricks");
    ItemType.Typed<ItemMeta> INFESTED_CHISELED_STONE_BRICKS = getItemType("infested_chiseled_stone_bricks");
    ItemType.Typed<ItemMeta> INFESTED_DEEPSLATE = getItemType("infested_deepslate");
    ItemType.Typed<ItemMeta> STONE_BRICKS = getItemType("stone_bricks");
    ItemType.Typed<ItemMeta> MOSSY_STONE_BRICKS = getItemType("mossy_stone_bricks");
    ItemType.Typed<ItemMeta> CRACKED_STONE_BRICKS = getItemType("cracked_stone_bricks");
    ItemType.Typed<ItemMeta> CHISELED_STONE_BRICKS = getItemType("chiseled_stone_bricks");
    ItemType.Typed<ItemMeta> PACKED_MUD = getItemType("packed_mud");
    ItemType.Typed<ItemMeta> MUD_BRICKS = getItemType("mud_bricks");
    ItemType.Typed<ItemMeta> DEEPSLATE_BRICKS = getItemType("deepslate_bricks");
    ItemType.Typed<ItemMeta> CRACKED_DEEPSLATE_BRICKS = getItemType("cracked_deepslate_bricks");
    ItemType.Typed<ItemMeta> DEEPSLATE_TILES = getItemType("deepslate_tiles");
    ItemType.Typed<ItemMeta> CRACKED_DEEPSLATE_TILES = getItemType("cracked_deepslate_tiles");
    ItemType.Typed<ItemMeta> CHISELED_DEEPSLATE = getItemType("chiseled_deepslate");
    ItemType.Typed<ItemMeta> REINFORCED_DEEPSLATE = getItemType("reinforced_deepslate");
    ItemType.Typed<ItemMeta> BROWN_MUSHROOM_BLOCK = getItemType("brown_mushroom_block");
    ItemType.Typed<ItemMeta> RED_MUSHROOM_BLOCK = getItemType("red_mushroom_block");
    ItemType.Typed<ItemMeta> MUSHROOM_STEM = getItemType("mushroom_stem");
    ItemType.Typed<ItemMeta> IRON_BARS = getItemType("iron_bars");
    ItemType.Typed<ItemMeta> CHAIN = getItemType("chain");
    ItemType.Typed<ItemMeta> GLASS_PANE = getItemType("glass_pane");
    ItemType.Typed<ItemMeta> MELON = getItemType("melon");
    ItemType.Typed<ItemMeta> VINE = getItemType("vine");
    ItemType.Typed<ItemMeta> GLOW_LICHEN = getItemType("glow_lichen");
    ItemType.Typed<ItemMeta> BRICK_STAIRS = getItemType("brick_stairs");
    ItemType.Typed<ItemMeta> STONE_BRICK_STAIRS = getItemType("stone_brick_stairs");
    ItemType.Typed<ItemMeta> MUD_BRICK_STAIRS = getItemType("mud_brick_stairs");
    ItemType.Typed<ItemMeta> MYCELIUM = getItemType("mycelium");
    ItemType.Typed<ItemMeta> LILY_PAD = getItemType("lily_pad");
    ItemType.Typed<ItemMeta> NETHER_BRICKS = getItemType("nether_bricks");
    ItemType.Typed<ItemMeta> CRACKED_NETHER_BRICKS = getItemType("cracked_nether_bricks");
    ItemType.Typed<ItemMeta> CHISELED_NETHER_BRICKS = getItemType("chiseled_nether_bricks");
    ItemType.Typed<ItemMeta> NETHER_BRICK_FENCE = getItemType("nether_brick_fence");
    ItemType.Typed<ItemMeta> NETHER_BRICK_STAIRS = getItemType("nether_brick_stairs");
    ItemType.Typed<ItemMeta> SCULK = getItemType("sculk");
    ItemType.Typed<ItemMeta> SCULK_VEIN = getItemType("sculk_vein");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SCULK_CATALYST = getItemType("sculk_catalyst");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SCULK_SHRIEKER = getItemType("sculk_shrieker");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> ENCHANTING_TABLE = getItemType("enchanting_table");
    ItemType.Typed<ItemMeta> END_PORTAL_FRAME = getItemType("end_portal_frame");
    ItemType.Typed<ItemMeta> END_STONE = getItemType("end_stone");
    ItemType.Typed<ItemMeta> END_STONE_BRICKS = getItemType("end_stone_bricks");
    ItemType.Typed<ItemMeta> DRAGON_EGG = getItemType("dragon_egg");
    ItemType.Typed<ItemMeta> SANDSTONE_STAIRS = getItemType("sandstone_stairs");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> ENDER_CHEST = getItemType("ender_chest");
    ItemType.Typed<ItemMeta> EMERALD_BLOCK = getItemType("emerald_block");
    ItemType.Typed<ItemMeta> OAK_STAIRS = getItemType("oak_stairs");
    ItemType.Typed<ItemMeta> SPRUCE_STAIRS = getItemType("spruce_stairs");
    ItemType.Typed<ItemMeta> BIRCH_STAIRS = getItemType("birch_stairs");
    ItemType.Typed<ItemMeta> JUNGLE_STAIRS = getItemType("jungle_stairs");
    ItemType.Typed<ItemMeta> ACACIA_STAIRS = getItemType("acacia_stairs");
    ItemType.Typed<ItemMeta> CHERRY_STAIRS = getItemType("cherry_stairs");
    ItemType.Typed<ItemMeta> DARK_OAK_STAIRS = getItemType("dark_oak_stairs");
    ItemType.Typed<ItemMeta> MANGROVE_STAIRS = getItemType("mangrove_stairs");
    ItemType.Typed<ItemMeta> BAMBOO_STAIRS = getItemType("bamboo_stairs");
    ItemType.Typed<ItemMeta> BAMBOO_MOSAIC_STAIRS = getItemType("bamboo_mosaic_stairs");
    ItemType.Typed<ItemMeta> CRIMSON_STAIRS = getItemType("crimson_stairs");
    ItemType.Typed<ItemMeta> WARPED_STAIRS = getItemType("warped_stairs");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> COMMAND_BLOCK = getItemType("command_block");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BEACON = getItemType("beacon");
    ItemType.Typed<ItemMeta> COBBLESTONE_WALL = getItemType("cobblestone_wall");
    ItemType.Typed<ItemMeta> MOSSY_COBBLESTONE_WALL = getItemType("mossy_cobblestone_wall");
    ItemType.Typed<ItemMeta> BRICK_WALL = getItemType("brick_wall");
    ItemType.Typed<ItemMeta> PRISMARINE_WALL = getItemType("prismarine_wall");
    ItemType.Typed<ItemMeta> RED_SANDSTONE_WALL = getItemType("red_sandstone_wall");
    ItemType.Typed<ItemMeta> MOSSY_STONE_BRICK_WALL = getItemType("mossy_stone_brick_wall");
    ItemType.Typed<ItemMeta> GRANITE_WALL = getItemType("granite_wall");
    ItemType.Typed<ItemMeta> STONE_BRICK_WALL = getItemType("stone_brick_wall");
    ItemType.Typed<ItemMeta> MUD_BRICK_WALL = getItemType("mud_brick_wall");
    ItemType.Typed<ItemMeta> NETHER_BRICK_WALL = getItemType("nether_brick_wall");
    ItemType.Typed<ItemMeta> ANDESITE_WALL = getItemType("andesite_wall");
    ItemType.Typed<ItemMeta> RED_NETHER_BRICK_WALL = getItemType("red_nether_brick_wall");
    ItemType.Typed<ItemMeta> SANDSTONE_WALL = getItemType("sandstone_wall");
    ItemType.Typed<ItemMeta> END_STONE_BRICK_WALL = getItemType("end_stone_brick_wall");
    ItemType.Typed<ItemMeta> DIORITE_WALL = getItemType("diorite_wall");
    ItemType.Typed<ItemMeta> BLACKSTONE_WALL = getItemType("blackstone_wall");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_WALL = getItemType("polished_blackstone_wall");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_BRICK_WALL = getItemType("polished_blackstone_brick_wall");
    ItemType.Typed<ItemMeta> COBBLED_DEEPSLATE_WALL = getItemType("cobbled_deepslate_wall");
    ItemType.Typed<ItemMeta> POLISHED_DEEPSLATE_WALL = getItemType("polished_deepslate_wall");
    ItemType.Typed<ItemMeta> DEEPSLATE_BRICK_WALL = getItemType("deepslate_brick_wall");
    ItemType.Typed<ItemMeta> DEEPSLATE_TILE_WALL = getItemType("deepslate_tile_wall");
    ItemType.Typed<ItemMeta> ANVIL = getItemType("anvil");
    ItemType.Typed<ItemMeta> CHIPPED_ANVIL = getItemType("chipped_anvil");
    ItemType.Typed<ItemMeta> DAMAGED_ANVIL = getItemType("damaged_anvil");
    ItemType.Typed<ItemMeta> CHISELED_QUARTZ_BLOCK = getItemType("chiseled_quartz_block");
    ItemType.Typed<ItemMeta> QUARTZ_BLOCK = getItemType("quartz_block");
    ItemType.Typed<ItemMeta> QUARTZ_BRICKS = getItemType("quartz_bricks");
    ItemType.Typed<ItemMeta> QUARTZ_PILLAR = getItemType("quartz_pillar");
    ItemType.Typed<ItemMeta> QUARTZ_STAIRS = getItemType("quartz_stairs");
    ItemType.Typed<ItemMeta> WHITE_TERRACOTTA = getItemType("white_terracotta");
    ItemType.Typed<ItemMeta> ORANGE_TERRACOTTA = getItemType("orange_terracotta");
    ItemType.Typed<ItemMeta> MAGENTA_TERRACOTTA = getItemType("magenta_terracotta");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_TERRACOTTA = getItemType("light_blue_terracotta");
    ItemType.Typed<ItemMeta> YELLOW_TERRACOTTA = getItemType("yellow_terracotta");
    ItemType.Typed<ItemMeta> LIME_TERRACOTTA = getItemType("lime_terracotta");
    ItemType.Typed<ItemMeta> PINK_TERRACOTTA = getItemType("pink_terracotta");
    ItemType.Typed<ItemMeta> GRAY_TERRACOTTA = getItemType("gray_terracotta");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_TERRACOTTA = getItemType("light_gray_terracotta");
    ItemType.Typed<ItemMeta> CYAN_TERRACOTTA = getItemType("cyan_terracotta");
    ItemType.Typed<ItemMeta> PURPLE_TERRACOTTA = getItemType("purple_terracotta");
    ItemType.Typed<ItemMeta> BLUE_TERRACOTTA = getItemType("blue_terracotta");
    ItemType.Typed<ItemMeta> BROWN_TERRACOTTA = getItemType("brown_terracotta");
    ItemType.Typed<ItemMeta> GREEN_TERRACOTTA = getItemType("green_terracotta");
    ItemType.Typed<ItemMeta> RED_TERRACOTTA = getItemType("red_terracotta");
    ItemType.Typed<ItemMeta> BLACK_TERRACOTTA = getItemType("black_terracotta");
    ItemType.Typed<ItemMeta> BARRIER = getItemType("barrier");
    ItemType.Typed<ItemMeta> LIGHT = getItemType("light");
    ItemType.Typed<ItemMeta> HAY_BLOCK = getItemType("hay_block");
    ItemType.Typed<ItemMeta> WHITE_CARPET = getItemType("white_carpet");
    ItemType.Typed<ItemMeta> ORANGE_CARPET = getItemType("orange_carpet");
    ItemType.Typed<ItemMeta> MAGENTA_CARPET = getItemType("magenta_carpet");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_CARPET = getItemType("light_blue_carpet");
    ItemType.Typed<ItemMeta> YELLOW_CARPET = getItemType("yellow_carpet");
    ItemType.Typed<ItemMeta> LIME_CARPET = getItemType("lime_carpet");
    ItemType.Typed<ItemMeta> PINK_CARPET = getItemType("pink_carpet");
    ItemType.Typed<ItemMeta> GRAY_CARPET = getItemType("gray_carpet");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_CARPET = getItemType("light_gray_carpet");
    ItemType.Typed<ItemMeta> CYAN_CARPET = getItemType("cyan_carpet");
    ItemType.Typed<ItemMeta> PURPLE_CARPET = getItemType("purple_carpet");
    ItemType.Typed<ItemMeta> BLUE_CARPET = getItemType("blue_carpet");
    ItemType.Typed<ItemMeta> BROWN_CARPET = getItemType("brown_carpet");
    ItemType.Typed<ItemMeta> GREEN_CARPET = getItemType("green_carpet");
    ItemType.Typed<ItemMeta> RED_CARPET = getItemType("red_carpet");
    ItemType.Typed<ItemMeta> BLACK_CARPET = getItemType("black_carpet");
    ItemType.Typed<ItemMeta> TERRACOTTA = getItemType("terracotta");
    ItemType.Typed<ItemMeta> PACKED_ICE = getItemType("packed_ice");
    ItemType.Typed<ItemMeta> DIRT_PATH = getItemType("dirt_path");
    ItemType.Typed<ItemMeta> SUNFLOWER = getItemType("sunflower");
    ItemType.Typed<ItemMeta> LILAC = getItemType("lilac");
    ItemType.Typed<ItemMeta> ROSE_BUSH = getItemType("rose_bush");
    ItemType.Typed<ItemMeta> PEONY = getItemType("peony");
    ItemType.Typed<ItemMeta> TALL_GRASS = getItemType("tall_grass");
    ItemType.Typed<ItemMeta> LARGE_FERN = getItemType("large_fern");
    ItemType.Typed<ItemMeta> WHITE_STAINED_GLASS = getItemType("white_stained_glass");
    ItemType.Typed<ItemMeta> ORANGE_STAINED_GLASS = getItemType("orange_stained_glass");
    ItemType.Typed<ItemMeta> MAGENTA_STAINED_GLASS = getItemType("magenta_stained_glass");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_STAINED_GLASS = getItemType("light_blue_stained_glass");
    ItemType.Typed<ItemMeta> YELLOW_STAINED_GLASS = getItemType("yellow_stained_glass");
    ItemType.Typed<ItemMeta> LIME_STAINED_GLASS = getItemType("lime_stained_glass");
    ItemType.Typed<ItemMeta> PINK_STAINED_GLASS = getItemType("pink_stained_glass");
    ItemType.Typed<ItemMeta> GRAY_STAINED_GLASS = getItemType("gray_stained_glass");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_STAINED_GLASS = getItemType("light_gray_stained_glass");
    ItemType.Typed<ItemMeta> CYAN_STAINED_GLASS = getItemType("cyan_stained_glass");
    ItemType.Typed<ItemMeta> PURPLE_STAINED_GLASS = getItemType("purple_stained_glass");
    ItemType.Typed<ItemMeta> BLUE_STAINED_GLASS = getItemType("blue_stained_glass");
    ItemType.Typed<ItemMeta> BROWN_STAINED_GLASS = getItemType("brown_stained_glass");
    ItemType.Typed<ItemMeta> GREEN_STAINED_GLASS = getItemType("green_stained_glass");
    ItemType.Typed<ItemMeta> RED_STAINED_GLASS = getItemType("red_stained_glass");
    ItemType.Typed<ItemMeta> BLACK_STAINED_GLASS = getItemType("black_stained_glass");
    ItemType.Typed<ItemMeta> WHITE_STAINED_GLASS_PANE = getItemType("white_stained_glass_pane");
    ItemType.Typed<ItemMeta> ORANGE_STAINED_GLASS_PANE = getItemType("orange_stained_glass_pane");
    ItemType.Typed<ItemMeta> MAGENTA_STAINED_GLASS_PANE = getItemType("magenta_stained_glass_pane");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_STAINED_GLASS_PANE = getItemType("light_blue_stained_glass_pane");
    ItemType.Typed<ItemMeta> YELLOW_STAINED_GLASS_PANE = getItemType("yellow_stained_glass_pane");
    ItemType.Typed<ItemMeta> LIME_STAINED_GLASS_PANE = getItemType("lime_stained_glass_pane");
    ItemType.Typed<ItemMeta> PINK_STAINED_GLASS_PANE = getItemType("pink_stained_glass_pane");
    ItemType.Typed<ItemMeta> GRAY_STAINED_GLASS_PANE = getItemType("gray_stained_glass_pane");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_STAINED_GLASS_PANE = getItemType("light_gray_stained_glass_pane");
    ItemType.Typed<ItemMeta> CYAN_STAINED_GLASS_PANE = getItemType("cyan_stained_glass_pane");
    ItemType.Typed<ItemMeta> PURPLE_STAINED_GLASS_PANE = getItemType("purple_stained_glass_pane");
    ItemType.Typed<ItemMeta> BLUE_STAINED_GLASS_PANE = getItemType("blue_stained_glass_pane");
    ItemType.Typed<ItemMeta> BROWN_STAINED_GLASS_PANE = getItemType("brown_stained_glass_pane");
    ItemType.Typed<ItemMeta> GREEN_STAINED_GLASS_PANE = getItemType("green_stained_glass_pane");
    ItemType.Typed<ItemMeta> RED_STAINED_GLASS_PANE = getItemType("red_stained_glass_pane");
    ItemType.Typed<ItemMeta> BLACK_STAINED_GLASS_PANE = getItemType("black_stained_glass_pane");
    ItemType.Typed<ItemMeta> PRISMARINE = getItemType("prismarine");
    ItemType.Typed<ItemMeta> PRISMARINE_BRICKS = getItemType("prismarine_bricks");
    ItemType.Typed<ItemMeta> DARK_PRISMARINE = getItemType("dark_prismarine");
    ItemType.Typed<ItemMeta> PRISMARINE_STAIRS = getItemType("prismarine_stairs");
    ItemType.Typed<ItemMeta> PRISMARINE_BRICK_STAIRS = getItemType("prismarine_brick_stairs");
    ItemType.Typed<ItemMeta> DARK_PRISMARINE_STAIRS = getItemType("dark_prismarine_stairs");
    ItemType.Typed<ItemMeta> SEA_LANTERN = getItemType("sea_lantern");
    ItemType.Typed<ItemMeta> RED_SANDSTONE = getItemType("red_sandstone");
    ItemType.Typed<ItemMeta> CHISELED_RED_SANDSTONE = getItemType("chiseled_red_sandstone");
    ItemType.Typed<ItemMeta> CUT_RED_SANDSTONE = getItemType("cut_red_sandstone");
    ItemType.Typed<ItemMeta> RED_SANDSTONE_STAIRS = getItemType("red_sandstone_stairs");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> REPEATING_COMMAND_BLOCK = getItemType("repeating_command_block");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CHAIN_COMMAND_BLOCK = getItemType("chain_command_block");
    ItemType.Typed<ItemMeta> MAGMA_BLOCK = getItemType("magma_block");
    ItemType.Typed<ItemMeta> NETHER_WART_BLOCK = getItemType("nether_wart_block");
    ItemType.Typed<ItemMeta> WARPED_WART_BLOCK = getItemType("warped_wart_block");
    ItemType.Typed<ItemMeta> RED_NETHER_BRICKS = getItemType("red_nether_bricks");
    ItemType.Typed<ItemMeta> BONE_BLOCK = getItemType("bone_block");
    ItemType.Typed<ItemMeta> STRUCTURE_VOID = getItemType("structure_void");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SHULKER_BOX = getItemType("shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> WHITE_SHULKER_BOX = getItemType("white_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> ORANGE_SHULKER_BOX = getItemType("orange_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> MAGENTA_SHULKER_BOX = getItemType("magenta_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> LIGHT_BLUE_SHULKER_BOX = getItemType("light_blue_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> YELLOW_SHULKER_BOX = getItemType("yellow_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> LIME_SHULKER_BOX = getItemType("lime_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> PINK_SHULKER_BOX = getItemType("pink_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> GRAY_SHULKER_BOX = getItemType("gray_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> LIGHT_GRAY_SHULKER_BOX = getItemType("light_gray_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CYAN_SHULKER_BOX = getItemType("cyan_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> PURPLE_SHULKER_BOX = getItemType("purple_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BLUE_SHULKER_BOX = getItemType("blue_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BROWN_SHULKER_BOX = getItemType("brown_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> GREEN_SHULKER_BOX = getItemType("green_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> RED_SHULKER_BOX = getItemType("red_shulker_box");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BLACK_SHULKER_BOX = getItemType("black_shulker_box");
    ItemType.Typed<ItemMeta> WHITE_GLAZED_TERRACOTTA = getItemType("white_glazed_terracotta");
    ItemType.Typed<ItemMeta> ORANGE_GLAZED_TERRACOTTA = getItemType("orange_glazed_terracotta");
    ItemType.Typed<ItemMeta> MAGENTA_GLAZED_TERRACOTTA = getItemType("magenta_glazed_terracotta");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_GLAZED_TERRACOTTA = getItemType("light_blue_glazed_terracotta");
    ItemType.Typed<ItemMeta> YELLOW_GLAZED_TERRACOTTA = getItemType("yellow_glazed_terracotta");
    ItemType.Typed<ItemMeta> LIME_GLAZED_TERRACOTTA = getItemType("lime_glazed_terracotta");
    ItemType.Typed<ItemMeta> PINK_GLAZED_TERRACOTTA = getItemType("pink_glazed_terracotta");
    ItemType.Typed<ItemMeta> GRAY_GLAZED_TERRACOTTA = getItemType("gray_glazed_terracotta");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_GLAZED_TERRACOTTA = getItemType("light_gray_glazed_terracotta");
    ItemType.Typed<ItemMeta> CYAN_GLAZED_TERRACOTTA = getItemType("cyan_glazed_terracotta");
    ItemType.Typed<ItemMeta> PURPLE_GLAZED_TERRACOTTA = getItemType("purple_glazed_terracotta");
    ItemType.Typed<ItemMeta> BLUE_GLAZED_TERRACOTTA = getItemType("blue_glazed_terracotta");
    ItemType.Typed<ItemMeta> BROWN_GLAZED_TERRACOTTA = getItemType("brown_glazed_terracotta");
    ItemType.Typed<ItemMeta> GREEN_GLAZED_TERRACOTTA = getItemType("green_glazed_terracotta");
    ItemType.Typed<ItemMeta> RED_GLAZED_TERRACOTTA = getItemType("red_glazed_terracotta");
    ItemType.Typed<ItemMeta> BLACK_GLAZED_TERRACOTTA = getItemType("black_glazed_terracotta");
    ItemType.Typed<ItemMeta> WHITE_CONCRETE = getItemType("white_concrete");
    ItemType.Typed<ItemMeta> ORANGE_CONCRETE = getItemType("orange_concrete");
    ItemType.Typed<ItemMeta> MAGENTA_CONCRETE = getItemType("magenta_concrete");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_CONCRETE = getItemType("light_blue_concrete");
    ItemType.Typed<ItemMeta> YELLOW_CONCRETE = getItemType("yellow_concrete");
    ItemType.Typed<ItemMeta> LIME_CONCRETE = getItemType("lime_concrete");
    ItemType.Typed<ItemMeta> PINK_CONCRETE = getItemType("pink_concrete");
    ItemType.Typed<ItemMeta> GRAY_CONCRETE = getItemType("gray_concrete");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_CONCRETE = getItemType("light_gray_concrete");
    ItemType.Typed<ItemMeta> CYAN_CONCRETE = getItemType("cyan_concrete");
    ItemType.Typed<ItemMeta> PURPLE_CONCRETE = getItemType("purple_concrete");
    ItemType.Typed<ItemMeta> BLUE_CONCRETE = getItemType("blue_concrete");
    ItemType.Typed<ItemMeta> BROWN_CONCRETE = getItemType("brown_concrete");
    ItemType.Typed<ItemMeta> GREEN_CONCRETE = getItemType("green_concrete");
    ItemType.Typed<ItemMeta> RED_CONCRETE = getItemType("red_concrete");
    ItemType.Typed<ItemMeta> BLACK_CONCRETE = getItemType("black_concrete");
    ItemType.Typed<ItemMeta> WHITE_CONCRETE_POWDER = getItemType("white_concrete_powder");
    ItemType.Typed<ItemMeta> ORANGE_CONCRETE_POWDER = getItemType("orange_concrete_powder");
    ItemType.Typed<ItemMeta> MAGENTA_CONCRETE_POWDER = getItemType("magenta_concrete_powder");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_CONCRETE_POWDER = getItemType("light_blue_concrete_powder");
    ItemType.Typed<ItemMeta> YELLOW_CONCRETE_POWDER = getItemType("yellow_concrete_powder");
    ItemType.Typed<ItemMeta> LIME_CONCRETE_POWDER = getItemType("lime_concrete_powder");
    ItemType.Typed<ItemMeta> PINK_CONCRETE_POWDER = getItemType("pink_concrete_powder");
    ItemType.Typed<ItemMeta> GRAY_CONCRETE_POWDER = getItemType("gray_concrete_powder");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_CONCRETE_POWDER = getItemType("light_gray_concrete_powder");
    ItemType.Typed<ItemMeta> CYAN_CONCRETE_POWDER = getItemType("cyan_concrete_powder");
    ItemType.Typed<ItemMeta> PURPLE_CONCRETE_POWDER = getItemType("purple_concrete_powder");
    ItemType.Typed<ItemMeta> BLUE_CONCRETE_POWDER = getItemType("blue_concrete_powder");
    ItemType.Typed<ItemMeta> BROWN_CONCRETE_POWDER = getItemType("brown_concrete_powder");
    ItemType.Typed<ItemMeta> GREEN_CONCRETE_POWDER = getItemType("green_concrete_powder");
    ItemType.Typed<ItemMeta> RED_CONCRETE_POWDER = getItemType("red_concrete_powder");
    ItemType.Typed<ItemMeta> BLACK_CONCRETE_POWDER = getItemType("black_concrete_powder");
    ItemType.Typed<ItemMeta> TURTLE_EGG = getItemType("turtle_egg");
    ItemType.Typed<ItemMeta> SNIFFER_EGG = getItemType("sniffer_egg");
    ItemType.Typed<ItemMeta> DEAD_TUBE_CORAL_BLOCK = getItemType("dead_tube_coral_block");
    ItemType.Typed<ItemMeta> DEAD_BRAIN_CORAL_BLOCK = getItemType("dead_brain_coral_block");
    ItemType.Typed<ItemMeta> DEAD_BUBBLE_CORAL_BLOCK = getItemType("dead_bubble_coral_block");
    ItemType.Typed<ItemMeta> DEAD_FIRE_CORAL_BLOCK = getItemType("dead_fire_coral_block");
    ItemType.Typed<ItemMeta> DEAD_HORN_CORAL_BLOCK = getItemType("dead_horn_coral_block");
    ItemType.Typed<ItemMeta> TUBE_CORAL_BLOCK = getItemType("tube_coral_block");
    ItemType.Typed<ItemMeta> BRAIN_CORAL_BLOCK = getItemType("brain_coral_block");
    ItemType.Typed<ItemMeta> BUBBLE_CORAL_BLOCK = getItemType("bubble_coral_block");
    ItemType.Typed<ItemMeta> FIRE_CORAL_BLOCK = getItemType("fire_coral_block");
    ItemType.Typed<ItemMeta> HORN_CORAL_BLOCK = getItemType("horn_coral_block");
    ItemType.Typed<ItemMeta> TUBE_CORAL = getItemType("tube_coral");
    ItemType.Typed<ItemMeta> BRAIN_CORAL = getItemType("brain_coral");
    ItemType.Typed<ItemMeta> BUBBLE_CORAL = getItemType("bubble_coral");
    ItemType.Typed<ItemMeta> FIRE_CORAL = getItemType("fire_coral");
    ItemType.Typed<ItemMeta> HORN_CORAL = getItemType("horn_coral");
    ItemType.Typed<ItemMeta> DEAD_BRAIN_CORAL = getItemType("dead_brain_coral");
    ItemType.Typed<ItemMeta> DEAD_BUBBLE_CORAL = getItemType("dead_bubble_coral");
    ItemType.Typed<ItemMeta> DEAD_FIRE_CORAL = getItemType("dead_fire_coral");
    ItemType.Typed<ItemMeta> DEAD_HORN_CORAL = getItemType("dead_horn_coral");
    ItemType.Typed<ItemMeta> DEAD_TUBE_CORAL = getItemType("dead_tube_coral");
    ItemType.Typed<ItemMeta> TUBE_CORAL_FAN = getItemType("tube_coral_fan");
    ItemType.Typed<ItemMeta> BRAIN_CORAL_FAN = getItemType("brain_coral_fan");
    ItemType.Typed<ItemMeta> BUBBLE_CORAL_FAN = getItemType("bubble_coral_fan");
    ItemType.Typed<ItemMeta> FIRE_CORAL_FAN = getItemType("fire_coral_fan");
    ItemType.Typed<ItemMeta> HORN_CORAL_FAN = getItemType("horn_coral_fan");
    ItemType.Typed<ItemMeta> DEAD_TUBE_CORAL_FAN = getItemType("dead_tube_coral_fan");
    ItemType.Typed<ItemMeta> DEAD_BRAIN_CORAL_FAN = getItemType("dead_brain_coral_fan");
    ItemType.Typed<ItemMeta> DEAD_BUBBLE_CORAL_FAN = getItemType("dead_bubble_coral_fan");
    ItemType.Typed<ItemMeta> DEAD_FIRE_CORAL_FAN = getItemType("dead_fire_coral_fan");
    ItemType.Typed<ItemMeta> DEAD_HORN_CORAL_FAN = getItemType("dead_horn_coral_fan");
    ItemType.Typed<ItemMeta> BLUE_ICE = getItemType("blue_ice");
    ItemType.Typed<ItemMeta> CONDUIT = getItemType("conduit");
    ItemType.Typed<ItemMeta> POLISHED_GRANITE_STAIRS = getItemType("polished_granite_stairs");
    ItemType.Typed<ItemMeta> SMOOTH_RED_SANDSTONE_STAIRS = getItemType("smooth_red_sandstone_stairs");
    ItemType.Typed<ItemMeta> MOSSY_STONE_BRICK_STAIRS = getItemType("mossy_stone_brick_stairs");
    ItemType.Typed<ItemMeta> POLISHED_DIORITE_STAIRS = getItemType("polished_diorite_stairs");
    ItemType.Typed<ItemMeta> MOSSY_COBBLESTONE_STAIRS = getItemType("mossy_cobblestone_stairs");
    ItemType.Typed<ItemMeta> END_STONE_BRICK_STAIRS = getItemType("end_stone_brick_stairs");
    ItemType.Typed<ItemMeta> STONE_STAIRS = getItemType("stone_stairs");
    ItemType.Typed<ItemMeta> SMOOTH_SANDSTONE_STAIRS = getItemType("smooth_sandstone_stairs");
    ItemType.Typed<ItemMeta> SMOOTH_QUARTZ_STAIRS = getItemType("smooth_quartz_stairs");
    ItemType.Typed<ItemMeta> GRANITE_STAIRS = getItemType("granite_stairs");
    ItemType.Typed<ItemMeta> ANDESITE_STAIRS = getItemType("andesite_stairs");
    ItemType.Typed<ItemMeta> RED_NETHER_BRICK_STAIRS = getItemType("red_nether_brick_stairs");
    ItemType.Typed<ItemMeta> POLISHED_ANDESITE_STAIRS = getItemType("polished_andesite_stairs");
    ItemType.Typed<ItemMeta> DIORITE_STAIRS = getItemType("diorite_stairs");
    ItemType.Typed<ItemMeta> COBBLED_DEEPSLATE_STAIRS = getItemType("cobbled_deepslate_stairs");
    ItemType.Typed<ItemMeta> POLISHED_DEEPSLATE_STAIRS = getItemType("polished_deepslate_stairs");
    ItemType.Typed<ItemMeta> DEEPSLATE_BRICK_STAIRS = getItemType("deepslate_brick_stairs");
    ItemType.Typed<ItemMeta> DEEPSLATE_TILE_STAIRS = getItemType("deepslate_tile_stairs");
    ItemType.Typed<ItemMeta> POLISHED_GRANITE_SLAB = getItemType("polished_granite_slab");
    ItemType.Typed<ItemMeta> SMOOTH_RED_SANDSTONE_SLAB = getItemType("smooth_red_sandstone_slab");
    ItemType.Typed<ItemMeta> MOSSY_STONE_BRICK_SLAB = getItemType("mossy_stone_brick_slab");
    ItemType.Typed<ItemMeta> POLISHED_DIORITE_SLAB = getItemType("polished_diorite_slab");
    ItemType.Typed<ItemMeta> MOSSY_COBBLESTONE_SLAB = getItemType("mossy_cobblestone_slab");
    ItemType.Typed<ItemMeta> END_STONE_BRICK_SLAB = getItemType("end_stone_brick_slab");
    ItemType.Typed<ItemMeta> SMOOTH_SANDSTONE_SLAB = getItemType("smooth_sandstone_slab");
    ItemType.Typed<ItemMeta> SMOOTH_QUARTZ_SLAB = getItemType("smooth_quartz_slab");
    ItemType.Typed<ItemMeta> GRANITE_SLAB = getItemType("granite_slab");
    ItemType.Typed<ItemMeta> ANDESITE_SLAB = getItemType("andesite_slab");
    ItemType.Typed<ItemMeta> RED_NETHER_BRICK_SLAB = getItemType("red_nether_brick_slab");
    ItemType.Typed<ItemMeta> POLISHED_ANDESITE_SLAB = getItemType("polished_andesite_slab");
    ItemType.Typed<ItemMeta> DIORITE_SLAB = getItemType("diorite_slab");
    ItemType.Typed<ItemMeta> COBBLED_DEEPSLATE_SLAB = getItemType("cobbled_deepslate_slab");
    ItemType.Typed<ItemMeta> POLISHED_DEEPSLATE_SLAB = getItemType("polished_deepslate_slab");
    ItemType.Typed<ItemMeta> DEEPSLATE_BRICK_SLAB = getItemType("deepslate_brick_slab");
    ItemType.Typed<ItemMeta> DEEPSLATE_TILE_SLAB = getItemType("deepslate_tile_slab");
    ItemType.Typed<ItemMeta> SCAFFOLDING = getItemType("scaffolding");
    ItemType.Typed<ItemMeta> REDSTONE = getItemType("redstone");
    ItemType.Typed<ItemMeta> REDSTONE_TORCH = getItemType("redstone_torch");
    ItemType.Typed<ItemMeta> REDSTONE_BLOCK = getItemType("redstone_block");
    ItemType.Typed<ItemMeta> REPEATER = getItemType("repeater");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> COMPARATOR = getItemType("comparator");
    ItemType.Typed<ItemMeta> PISTON = getItemType("piston");
    ItemType.Typed<ItemMeta> STICKY_PISTON = getItemType("sticky_piston");
    ItemType.Typed<ItemMeta> SLIME_BLOCK = getItemType("slime_block");
    ItemType.Typed<ItemMeta> HONEY_BLOCK = getItemType("honey_block");
    ItemType.Typed<ItemMeta> OBSERVER = getItemType("observer");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> HOPPER = getItemType("hopper");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DISPENSER = getItemType("dispenser");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DROPPER = getItemType("dropper");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> LECTERN = getItemType("lectern");
    ItemType.Typed<ItemMeta> TARGET = getItemType("target");
    ItemType.Typed<ItemMeta> LEVER = getItemType("lever");
    ItemType.Typed<ItemMeta> LIGHTNING_ROD = getItemType("lightning_rod");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DAYLIGHT_DETECTOR = getItemType("daylight_detector");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SCULK_SENSOR = getItemType("sculk_sensor");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CALIBRATED_SCULK_SENSOR = getItemType("calibrated_sculk_sensor");
    ItemType.Typed<ItemMeta> TRIPWIRE_HOOK = getItemType("tripwire_hook");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> TRAPPED_CHEST = getItemType("trapped_chest");
    ItemType.Typed<ItemMeta> TNT = getItemType("tnt");
    ItemType.Typed<ItemMeta> REDSTONE_LAMP = getItemType("redstone_lamp");
    ItemType.Typed<ItemMeta> NOTE_BLOCK = getItemType("note_block");
    ItemType.Typed<ItemMeta> STONE_BUTTON = getItemType("stone_button");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_BUTTON = getItemType("polished_blackstone_button");
    ItemType.Typed<ItemMeta> OAK_BUTTON = getItemType("oak_button");
    ItemType.Typed<ItemMeta> SPRUCE_BUTTON = getItemType("spruce_button");
    ItemType.Typed<ItemMeta> BIRCH_BUTTON = getItemType("birch_button");
    ItemType.Typed<ItemMeta> JUNGLE_BUTTON = getItemType("jungle_button");
    ItemType.Typed<ItemMeta> ACACIA_BUTTON = getItemType("acacia_button");
    ItemType.Typed<ItemMeta> CHERRY_BUTTON = getItemType("cherry_button");
    ItemType.Typed<ItemMeta> DARK_OAK_BUTTON = getItemType("dark_oak_button");
    ItemType.Typed<ItemMeta> MANGROVE_BUTTON = getItemType("mangrove_button");
    ItemType.Typed<ItemMeta> BAMBOO_BUTTON = getItemType("bamboo_button");
    ItemType.Typed<ItemMeta> CRIMSON_BUTTON = getItemType("crimson_button");
    ItemType.Typed<ItemMeta> WARPED_BUTTON = getItemType("warped_button");
    ItemType.Typed<ItemMeta> STONE_PRESSURE_PLATE = getItemType("stone_pressure_plate");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_PRESSURE_PLATE = getItemType("polished_blackstone_pressure_plate");
    ItemType.Typed<ItemMeta> LIGHT_WEIGHTED_PRESSURE_PLATE = getItemType("light_weighted_pressure_plate");
    ItemType.Typed<ItemMeta> HEAVY_WEIGHTED_PRESSURE_PLATE = getItemType("heavy_weighted_pressure_plate");
    ItemType.Typed<ItemMeta> OAK_PRESSURE_PLATE = getItemType("oak_pressure_plate");
    ItemType.Typed<ItemMeta> SPRUCE_PRESSURE_PLATE = getItemType("spruce_pressure_plate");
    ItemType.Typed<ItemMeta> BIRCH_PRESSURE_PLATE = getItemType("birch_pressure_plate");
    ItemType.Typed<ItemMeta> JUNGLE_PRESSURE_PLATE = getItemType("jungle_pressure_plate");
    ItemType.Typed<ItemMeta> ACACIA_PRESSURE_PLATE = getItemType("acacia_pressure_plate");
    ItemType.Typed<ItemMeta> CHERRY_PRESSURE_PLATE = getItemType("cherry_pressure_plate");
    ItemType.Typed<ItemMeta> DARK_OAK_PRESSURE_PLATE = getItemType("dark_oak_pressure_plate");
    ItemType.Typed<ItemMeta> MANGROVE_PRESSURE_PLATE = getItemType("mangrove_pressure_plate");
    ItemType.Typed<ItemMeta> BAMBOO_PRESSURE_PLATE = getItemType("bamboo_pressure_plate");
    ItemType.Typed<ItemMeta> CRIMSON_PRESSURE_PLATE = getItemType("crimson_pressure_plate");
    ItemType.Typed<ItemMeta> WARPED_PRESSURE_PLATE = getItemType("warped_pressure_plate");
    ItemType.Typed<ItemMeta> IRON_DOOR = getItemType("iron_door");
    ItemType.Typed<ItemMeta> OAK_DOOR = getItemType("oak_door");
    ItemType.Typed<ItemMeta> SPRUCE_DOOR = getItemType("spruce_door");
    ItemType.Typed<ItemMeta> BIRCH_DOOR = getItemType("birch_door");
    ItemType.Typed<ItemMeta> JUNGLE_DOOR = getItemType("jungle_door");
    ItemType.Typed<ItemMeta> ACACIA_DOOR = getItemType("acacia_door");
    ItemType.Typed<ItemMeta> CHERRY_DOOR = getItemType("cherry_door");
    ItemType.Typed<ItemMeta> DARK_OAK_DOOR = getItemType("dark_oak_door");
    ItemType.Typed<ItemMeta> MANGROVE_DOOR = getItemType("mangrove_door");
    ItemType.Typed<ItemMeta> BAMBOO_DOOR = getItemType("bamboo_door");
    ItemType.Typed<ItemMeta> CRIMSON_DOOR = getItemType("crimson_door");
    ItemType.Typed<ItemMeta> WARPED_DOOR = getItemType("warped_door");
    ItemType.Typed<ItemMeta> COPPER_DOOR = getItemType("copper_door");
    ItemType.Typed<ItemMeta> EXPOSED_COPPER_DOOR = getItemType("exposed_copper_door");
    ItemType.Typed<ItemMeta> WEATHERED_COPPER_DOOR = getItemType("weathered_copper_door");
    ItemType.Typed<ItemMeta> OXIDIZED_COPPER_DOOR = getItemType("oxidized_copper_door");
    ItemType.Typed<ItemMeta> WAXED_COPPER_DOOR = getItemType("waxed_copper_door");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_COPPER_DOOR = getItemType("waxed_exposed_copper_door");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_COPPER_DOOR = getItemType("waxed_weathered_copper_door");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_COPPER_DOOR = getItemType("waxed_oxidized_copper_door");
    ItemType.Typed<ItemMeta> IRON_TRAPDOOR = getItemType("iron_trapdoor");
    ItemType.Typed<ItemMeta> OAK_TRAPDOOR = getItemType("oak_trapdoor");
    ItemType.Typed<ItemMeta> SPRUCE_TRAPDOOR = getItemType("spruce_trapdoor");
    ItemType.Typed<ItemMeta> BIRCH_TRAPDOOR = getItemType("birch_trapdoor");
    ItemType.Typed<ItemMeta> JUNGLE_TRAPDOOR = getItemType("jungle_trapdoor");
    ItemType.Typed<ItemMeta> ACACIA_TRAPDOOR = getItemType("acacia_trapdoor");
    ItemType.Typed<ItemMeta> CHERRY_TRAPDOOR = getItemType("cherry_trapdoor");
    ItemType.Typed<ItemMeta> DARK_OAK_TRAPDOOR = getItemType("dark_oak_trapdoor");
    ItemType.Typed<ItemMeta> MANGROVE_TRAPDOOR = getItemType("mangrove_trapdoor");
    ItemType.Typed<ItemMeta> BAMBOO_TRAPDOOR = getItemType("bamboo_trapdoor");
    ItemType.Typed<ItemMeta> CRIMSON_TRAPDOOR = getItemType("crimson_trapdoor");
    ItemType.Typed<ItemMeta> WARPED_TRAPDOOR = getItemType("warped_trapdoor");
    ItemType.Typed<ItemMeta> COPPER_TRAPDOOR = getItemType("copper_trapdoor");
    ItemType.Typed<ItemMeta> EXPOSED_COPPER_TRAPDOOR = getItemType("exposed_copper_trapdoor");
    ItemType.Typed<ItemMeta> WEATHERED_COPPER_TRAPDOOR = getItemType("weathered_copper_trapdoor");
    ItemType.Typed<ItemMeta> OXIDIZED_COPPER_TRAPDOOR = getItemType("oxidized_copper_trapdoor");
    ItemType.Typed<ItemMeta> WAXED_COPPER_TRAPDOOR = getItemType("waxed_copper_trapdoor");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_COPPER_TRAPDOOR = getItemType("waxed_exposed_copper_trapdoor");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_COPPER_TRAPDOOR = getItemType("waxed_weathered_copper_trapdoor");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_COPPER_TRAPDOOR = getItemType("waxed_oxidized_copper_trapdoor");
    ItemType.Typed<ItemMeta> OAK_FENCE_GATE = getItemType("oak_fence_gate");
    ItemType.Typed<ItemMeta> SPRUCE_FENCE_GATE = getItemType("spruce_fence_gate");
    ItemType.Typed<ItemMeta> BIRCH_FENCE_GATE = getItemType("birch_fence_gate");
    ItemType.Typed<ItemMeta> JUNGLE_FENCE_GATE = getItemType("jungle_fence_gate");
    ItemType.Typed<ItemMeta> ACACIA_FENCE_GATE = getItemType("acacia_fence_gate");
    ItemType.Typed<ItemMeta> CHERRY_FENCE_GATE = getItemType("cherry_fence_gate");
    ItemType.Typed<ItemMeta> DARK_OAK_FENCE_GATE = getItemType("dark_oak_fence_gate");
    ItemType.Typed<ItemMeta> MANGROVE_FENCE_GATE = getItemType("mangrove_fence_gate");
    ItemType.Typed<ItemMeta> BAMBOO_FENCE_GATE = getItemType("bamboo_fence_gate");
    ItemType.Typed<ItemMeta> CRIMSON_FENCE_GATE = getItemType("crimson_fence_gate");
    ItemType.Typed<ItemMeta> WARPED_FENCE_GATE = getItemType("warped_fence_gate");
    ItemType.Typed<ItemMeta> POWERED_RAIL = getItemType("powered_rail");
    ItemType.Typed<ItemMeta> DETECTOR_RAIL = getItemType("detector_rail");
    ItemType.Typed<ItemMeta> RAIL = getItemType("rail");
    ItemType.Typed<ItemMeta> ACTIVATOR_RAIL = getItemType("activator_rail");
    ItemType.Typed<ItemMeta> SADDLE = getItemType("saddle");
    ItemType.Typed<ItemMeta> MINECART = getItemType("minecart");
    ItemType.Typed<ItemMeta> CHEST_MINECART = getItemType("chest_minecart");
    ItemType.Typed<ItemMeta> FURNACE_MINECART = getItemType("furnace_minecart");
    ItemType.Typed<ItemMeta> TNT_MINECART = getItemType("tnt_minecart");
    ItemType.Typed<ItemMeta> HOPPER_MINECART = getItemType("hopper_minecart");
    ItemType.Typed<ItemMeta> CARROT_ON_A_STICK = getItemType("carrot_on_a_stick");
    ItemType.Typed<ItemMeta> WARPED_FUNGUS_ON_A_STICK = getItemType("warped_fungus_on_a_stick");
    ItemType.Typed<ItemMeta> ELYTRA = getItemType("elytra");
    ItemType.Typed<ItemMeta> OAK_BOAT = getItemType("oak_boat");
    ItemType.Typed<ItemMeta> OAK_CHEST_BOAT = getItemType("oak_chest_boat");
    ItemType.Typed<ItemMeta> SPRUCE_BOAT = getItemType("spruce_boat");
    ItemType.Typed<ItemMeta> SPRUCE_CHEST_BOAT = getItemType("spruce_chest_boat");
    ItemType.Typed<ItemMeta> BIRCH_BOAT = getItemType("birch_boat");
    ItemType.Typed<ItemMeta> BIRCH_CHEST_BOAT = getItemType("birch_chest_boat");
    ItemType.Typed<ItemMeta> JUNGLE_BOAT = getItemType("jungle_boat");
    ItemType.Typed<ItemMeta> JUNGLE_CHEST_BOAT = getItemType("jungle_chest_boat");
    ItemType.Typed<ItemMeta> ACACIA_BOAT = getItemType("acacia_boat");
    ItemType.Typed<ItemMeta> ACACIA_CHEST_BOAT = getItemType("acacia_chest_boat");
    ItemType.Typed<ItemMeta> CHERRY_BOAT = getItemType("cherry_boat");
    ItemType.Typed<ItemMeta> CHERRY_CHEST_BOAT = getItemType("cherry_chest_boat");
    ItemType.Typed<ItemMeta> DARK_OAK_BOAT = getItemType("dark_oak_boat");
    ItemType.Typed<ItemMeta> DARK_OAK_CHEST_BOAT = getItemType("dark_oak_chest_boat");
    ItemType.Typed<ItemMeta> MANGROVE_BOAT = getItemType("mangrove_boat");
    ItemType.Typed<ItemMeta> MANGROVE_CHEST_BOAT = getItemType("mangrove_chest_boat");
    ItemType.Typed<ItemMeta> BAMBOO_RAFT = getItemType("bamboo_raft");
    ItemType.Typed<ItemMeta> BAMBOO_CHEST_RAFT = getItemType("bamboo_chest_raft");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> STRUCTURE_BLOCK = getItemType("structure_block");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> JIGSAW = getItemType("jigsaw");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> TURTLE_HELMET = getItemType("turtle_helmet");
    ItemType.Typed<ItemMeta> TURTLE_SCUTE = getItemType("turtle_scute");
    ItemType.Typed<ItemMeta> ARMADILLO_SCUTE = getItemType("armadillo_scute");
    /**
     * ItemMeta: {@link ColorableArmorMeta}
     */
    ItemType.Typed<ColorableArmorMeta> WOLF_ARMOR = getItemType("wolf_armor");
    ItemType.Typed<ItemMeta> BOWL = getItemType("bowl");
    ItemType.Typed<ItemMeta> FLINT_AND_STEEL = getItemType("flint_and_steel");
    ItemType.Typed<ItemMeta> APPLE = getItemType("apple");
    ItemType.Typed<ItemMeta> BOW = getItemType("bow");
    ItemType.Typed<ItemMeta> ARROW = getItemType("arrow");
    ItemType.Typed<ItemMeta> COAL = getItemType("coal");
    ItemType.Typed<ItemMeta> CHARCOAL = getItemType("charcoal");
    ItemType.Typed<ItemMeta> DIAMOND = getItemType("diamond");
    ItemType.Typed<ItemMeta> EMERALD = getItemType("emerald");
    ItemType.Typed<ItemMeta> LAPIS_LAZULI = getItemType("lapis_lazuli");
    ItemType.Typed<ItemMeta> QUARTZ = getItemType("quartz");
    ItemType.Typed<ItemMeta> AMETHYST_SHARD = getItemType("amethyst_shard");
    ItemType.Typed<ItemMeta> RAW_IRON = getItemType("raw_iron");
    ItemType.Typed<ItemMeta> IRON_INGOT = getItemType("iron_ingot");
    ItemType.Typed<ItemMeta> RAW_COPPER = getItemType("raw_copper");
    ItemType.Typed<ItemMeta> COPPER_INGOT = getItemType("copper_ingot");
    ItemType.Typed<ItemMeta> RAW_GOLD = getItemType("raw_gold");
    ItemType.Typed<ItemMeta> GOLD_INGOT = getItemType("gold_ingot");
    ItemType.Typed<ItemMeta> NETHERITE_INGOT = getItemType("netherite_ingot");
    ItemType.Typed<ItemMeta> NETHERITE_SCRAP = getItemType("netherite_scrap");
    ItemType.Typed<ItemMeta> WOODEN_SWORD = getItemType("wooden_sword");
    ItemType.Typed<ItemMeta> WOODEN_SHOVEL = getItemType("wooden_shovel");
    ItemType.Typed<ItemMeta> WOODEN_PICKAXE = getItemType("wooden_pickaxe");
    ItemType.Typed<ItemMeta> WOODEN_AXE = getItemType("wooden_axe");
    ItemType.Typed<ItemMeta> WOODEN_HOE = getItemType("wooden_hoe");
    ItemType.Typed<ItemMeta> STONE_SWORD = getItemType("stone_sword");
    ItemType.Typed<ItemMeta> STONE_SHOVEL = getItemType("stone_shovel");
    ItemType.Typed<ItemMeta> STONE_PICKAXE = getItemType("stone_pickaxe");
    ItemType.Typed<ItemMeta> STONE_AXE = getItemType("stone_axe");
    ItemType.Typed<ItemMeta> STONE_HOE = getItemType("stone_hoe");
    ItemType.Typed<ItemMeta> GOLDEN_SWORD = getItemType("golden_sword");
    ItemType.Typed<ItemMeta> GOLDEN_SHOVEL = getItemType("golden_shovel");
    ItemType.Typed<ItemMeta> GOLDEN_PICKAXE = getItemType("golden_pickaxe");
    ItemType.Typed<ItemMeta> GOLDEN_AXE = getItemType("golden_axe");
    ItemType.Typed<ItemMeta> GOLDEN_HOE = getItemType("golden_hoe");
    ItemType.Typed<ItemMeta> IRON_SWORD = getItemType("iron_sword");
    ItemType.Typed<ItemMeta> IRON_SHOVEL = getItemType("iron_shovel");
    ItemType.Typed<ItemMeta> IRON_PICKAXE = getItemType("iron_pickaxe");
    ItemType.Typed<ItemMeta> IRON_AXE = getItemType("iron_axe");
    ItemType.Typed<ItemMeta> IRON_HOE = getItemType("iron_hoe");
    ItemType.Typed<ItemMeta> DIAMOND_SWORD = getItemType("diamond_sword");
    ItemType.Typed<ItemMeta> DIAMOND_SHOVEL = getItemType("diamond_shovel");
    ItemType.Typed<ItemMeta> DIAMOND_PICKAXE = getItemType("diamond_pickaxe");
    ItemType.Typed<ItemMeta> DIAMOND_AXE = getItemType("diamond_axe");
    ItemType.Typed<ItemMeta> DIAMOND_HOE = getItemType("diamond_hoe");
    ItemType.Typed<ItemMeta> NETHERITE_SWORD = getItemType("netherite_sword");
    ItemType.Typed<ItemMeta> NETHERITE_SHOVEL = getItemType("netherite_shovel");
    ItemType.Typed<ItemMeta> NETHERITE_PICKAXE = getItemType("netherite_pickaxe");
    ItemType.Typed<ItemMeta> NETHERITE_AXE = getItemType("netherite_axe");
    ItemType.Typed<ItemMeta> NETHERITE_HOE = getItemType("netherite_hoe");
    ItemType.Typed<ItemMeta> STICK = getItemType("stick");
    ItemType.Typed<ItemMeta> MUSHROOM_STEW = getItemType("mushroom_stew");
    ItemType.Typed<ItemMeta> STRING = getItemType("string");
    ItemType.Typed<ItemMeta> FEATHER = getItemType("feather");
    ItemType.Typed<ItemMeta> GUNPOWDER = getItemType("gunpowder");
    ItemType.Typed<ItemMeta> WHEAT_SEEDS = getItemType("wheat_seeds");
    ItemType.Typed<ItemMeta> WHEAT = getItemType("wheat");
    ItemType.Typed<ItemMeta> BREAD = getItemType("bread");
    /**
     * ItemMeta: {@link ColorableArmorMeta}
     */
    ItemType.Typed<ColorableArmorMeta> LEATHER_HELMET = getItemType("leather_helmet");
    /**
     * ItemMeta: {@link ColorableArmorMeta}
     */
    ItemType.Typed<ColorableArmorMeta> LEATHER_CHESTPLATE = getItemType("leather_chestplate");
    /**
     * ItemMeta: {@link ColorableArmorMeta}
     */
    ItemType.Typed<ColorableArmorMeta> LEATHER_LEGGINGS = getItemType("leather_leggings");
    /**
     * ItemMeta: {@link ColorableArmorMeta}
     */
    ItemType.Typed<ColorableArmorMeta> LEATHER_BOOTS = getItemType("leather_boots");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> CHAINMAIL_HELMET = getItemType("chainmail_helmet");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> CHAINMAIL_CHESTPLATE = getItemType("chainmail_chestplate");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> CHAINMAIL_LEGGINGS = getItemType("chainmail_leggings");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> CHAINMAIL_BOOTS = getItemType("chainmail_boots");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> IRON_HELMET = getItemType("iron_helmet");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> IRON_CHESTPLATE = getItemType("iron_chestplate");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> IRON_LEGGINGS = getItemType("iron_leggings");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> IRON_BOOTS = getItemType("iron_boots");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> DIAMOND_HELMET = getItemType("diamond_helmet");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> DIAMOND_CHESTPLATE = getItemType("diamond_chestplate");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> DIAMOND_LEGGINGS = getItemType("diamond_leggings");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> DIAMOND_BOOTS = getItemType("diamond_boots");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> GOLDEN_HELMET = getItemType("golden_helmet");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> GOLDEN_CHESTPLATE = getItemType("golden_chestplate");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> GOLDEN_LEGGINGS = getItemType("golden_leggings");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> GOLDEN_BOOTS = getItemType("golden_boots");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> NETHERITE_HELMET = getItemType("netherite_helmet");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> NETHERITE_CHESTPLATE = getItemType("netherite_chestplate");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> NETHERITE_LEGGINGS = getItemType("netherite_leggings");
    /**
     * ItemMeta: {@link ArmorMeta}
     */
    ItemType.Typed<ArmorMeta> NETHERITE_BOOTS = getItemType("netherite_boots");
    ItemType.Typed<ItemMeta> FLINT = getItemType("flint");
    ItemType.Typed<ItemMeta> PORKCHOP = getItemType("porkchop");
    ItemType.Typed<ItemMeta> COOKED_PORKCHOP = getItemType("cooked_porkchop");
    ItemType.Typed<ItemMeta> PAINTING = getItemType("painting");
    ItemType.Typed<ItemMeta> GOLDEN_APPLE = getItemType("golden_apple");
    ItemType.Typed<ItemMeta> ENCHANTED_GOLDEN_APPLE = getItemType("enchanted_golden_apple");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> OAK_SIGN = getItemType("oak_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SPRUCE_SIGN = getItemType("spruce_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BIRCH_SIGN = getItemType("birch_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> JUNGLE_SIGN = getItemType("jungle_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> ACACIA_SIGN = getItemType("acacia_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CHERRY_SIGN = getItemType("cherry_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DARK_OAK_SIGN = getItemType("dark_oak_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> MANGROVE_SIGN = getItemType("mangrove_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BAMBOO_SIGN = getItemType("bamboo_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CRIMSON_SIGN = getItemType("crimson_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> WARPED_SIGN = getItemType("warped_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> OAK_HANGING_SIGN = getItemType("oak_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SPRUCE_HANGING_SIGN = getItemType("spruce_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BIRCH_HANGING_SIGN = getItemType("birch_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> JUNGLE_HANGING_SIGN = getItemType("jungle_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> ACACIA_HANGING_SIGN = getItemType("acacia_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CHERRY_HANGING_SIGN = getItemType("cherry_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> DARK_OAK_HANGING_SIGN = getItemType("dark_oak_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> MANGROVE_HANGING_SIGN = getItemType("mangrove_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BAMBOO_HANGING_SIGN = getItemType("bamboo_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CRIMSON_HANGING_SIGN = getItemType("crimson_hanging_sign");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> WARPED_HANGING_SIGN = getItemType("warped_hanging_sign");
    ItemType.Typed<ItemMeta> BUCKET = getItemType("bucket");
    ItemType.Typed<ItemMeta> WATER_BUCKET = getItemType("water_bucket");
    ItemType.Typed<ItemMeta> LAVA_BUCKET = getItemType("lava_bucket");
    ItemType.Typed<ItemMeta> POWDER_SNOW_BUCKET = getItemType("powder_snow_bucket");
    ItemType.Typed<ItemMeta> SNOWBALL = getItemType("snowball");
    ItemType.Typed<ItemMeta> LEATHER = getItemType("leather");
    ItemType.Typed<ItemMeta> MILK_BUCKET = getItemType("milk_bucket");
    ItemType.Typed<ItemMeta> PUFFERFISH_BUCKET = getItemType("pufferfish_bucket");
    ItemType.Typed<ItemMeta> SALMON_BUCKET = getItemType("salmon_bucket");
    ItemType.Typed<ItemMeta> COD_BUCKET = getItemType("cod_bucket");
    /**
     * ItemMeta: {@link TropicalFishBucketMeta}
     */
    ItemType.Typed<TropicalFishBucketMeta> TROPICAL_FISH_BUCKET = getItemType("tropical_fish_bucket");
    /**
     * ItemMeta: {@link AxolotlBucketMeta}
     */
    ItemType.Typed<AxolotlBucketMeta> AXOLOTL_BUCKET = getItemType("axolotl_bucket");
    ItemType.Typed<ItemMeta> TADPOLE_BUCKET = getItemType("tadpole_bucket");
    ItemType.Typed<ItemMeta> BRICK = getItemType("brick");
    ItemType.Typed<ItemMeta> CLAY_BALL = getItemType("clay_ball");
    ItemType.Typed<ItemMeta> DRIED_KELP_BLOCK = getItemType("dried_kelp_block");
    ItemType.Typed<ItemMeta> PAPER = getItemType("paper");
    ItemType.Typed<ItemMeta> BOOK = getItemType("book");
    ItemType.Typed<ItemMeta> SLIME_BALL = getItemType("slime_ball");
    ItemType.Typed<ItemMeta> EGG = getItemType("egg");
    /**
     * ItemMeta: {@link CompassMeta}
     */
    ItemType.Typed<CompassMeta> COMPASS = getItemType("compass");
    ItemType.Typed<ItemMeta> RECOVERY_COMPASS = getItemType("recovery_compass");
    /**
     * ItemMeta: {@link BundleMeta}
     */
    ItemType.Typed<BundleMeta> BUNDLE = getItemType("bundle");
    ItemType.Typed<ItemMeta> FISHING_ROD = getItemType("fishing_rod");
    ItemType.Typed<ItemMeta> CLOCK = getItemType("clock");
    ItemType.Typed<ItemMeta> SPYGLASS = getItemType("spyglass");
    ItemType.Typed<ItemMeta> GLOWSTONE_DUST = getItemType("glowstone_dust");
    ItemType.Typed<ItemMeta> COD = getItemType("cod");
    ItemType.Typed<ItemMeta> SALMON = getItemType("salmon");
    ItemType.Typed<ItemMeta> TROPICAL_FISH = getItemType("tropical_fish");
    ItemType.Typed<ItemMeta> PUFFERFISH = getItemType("pufferfish");
    ItemType.Typed<ItemMeta> COOKED_COD = getItemType("cooked_cod");
    ItemType.Typed<ItemMeta> COOKED_SALMON = getItemType("cooked_salmon");
    ItemType.Typed<ItemMeta> INK_SAC = getItemType("ink_sac");
    ItemType.Typed<ItemMeta> GLOW_INK_SAC = getItemType("glow_ink_sac");
    ItemType.Typed<ItemMeta> COCOA_BEANS = getItemType("cocoa_beans");
    ItemType.Typed<ItemMeta> WHITE_DYE = getItemType("white_dye");
    ItemType.Typed<ItemMeta> ORANGE_DYE = getItemType("orange_dye");
    ItemType.Typed<ItemMeta> MAGENTA_DYE = getItemType("magenta_dye");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_DYE = getItemType("light_blue_dye");
    ItemType.Typed<ItemMeta> YELLOW_DYE = getItemType("yellow_dye");
    ItemType.Typed<ItemMeta> LIME_DYE = getItemType("lime_dye");
    ItemType.Typed<ItemMeta> PINK_DYE = getItemType("pink_dye");
    ItemType.Typed<ItemMeta> GRAY_DYE = getItemType("gray_dye");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_DYE = getItemType("light_gray_dye");
    ItemType.Typed<ItemMeta> CYAN_DYE = getItemType("cyan_dye");
    ItemType.Typed<ItemMeta> PURPLE_DYE = getItemType("purple_dye");
    ItemType.Typed<ItemMeta> BLUE_DYE = getItemType("blue_dye");
    ItemType.Typed<ItemMeta> BROWN_DYE = getItemType("brown_dye");
    ItemType.Typed<ItemMeta> GREEN_DYE = getItemType("green_dye");
    ItemType.Typed<ItemMeta> RED_DYE = getItemType("red_dye");
    ItemType.Typed<ItemMeta> BLACK_DYE = getItemType("black_dye");
    ItemType.Typed<ItemMeta> BONE_MEAL = getItemType("bone_meal");
    ItemType.Typed<ItemMeta> BONE = getItemType("bone");
    ItemType.Typed<ItemMeta> SUGAR = getItemType("sugar");
    ItemType.Typed<ItemMeta> CAKE = getItemType("cake");
    ItemType.Typed<ItemMeta> WHITE_BED = getItemType("white_bed");
    ItemType.Typed<ItemMeta> ORANGE_BED = getItemType("orange_bed");
    ItemType.Typed<ItemMeta> MAGENTA_BED = getItemType("magenta_bed");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_BED = getItemType("light_blue_bed");
    ItemType.Typed<ItemMeta> YELLOW_BED = getItemType("yellow_bed");
    ItemType.Typed<ItemMeta> LIME_BED = getItemType("lime_bed");
    ItemType.Typed<ItemMeta> PINK_BED = getItemType("pink_bed");
    ItemType.Typed<ItemMeta> GRAY_BED = getItemType("gray_bed");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_BED = getItemType("light_gray_bed");
    ItemType.Typed<ItemMeta> CYAN_BED = getItemType("cyan_bed");
    ItemType.Typed<ItemMeta> PURPLE_BED = getItemType("purple_bed");
    ItemType.Typed<ItemMeta> BLUE_BED = getItemType("blue_bed");
    ItemType.Typed<ItemMeta> BROWN_BED = getItemType("brown_bed");
    ItemType.Typed<ItemMeta> GREEN_BED = getItemType("green_bed");
    ItemType.Typed<ItemMeta> RED_BED = getItemType("red_bed");
    ItemType.Typed<ItemMeta> BLACK_BED = getItemType("black_bed");
    ItemType.Typed<ItemMeta> COOKIE = getItemType("cookie");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CRAFTER = getItemType("crafter");
    /**
     * ItemMeta: {@link MapMeta}
     */
    ItemType.Typed<MapMeta> FILLED_MAP = getItemType("filled_map");
    ItemType.Typed<ItemMeta> SHEARS = getItemType("shears");
    ItemType.Typed<ItemMeta> MELON_SLICE = getItemType("melon_slice");
    ItemType.Typed<ItemMeta> DRIED_KELP = getItemType("dried_kelp");
    ItemType.Typed<ItemMeta> PUMPKIN_SEEDS = getItemType("pumpkin_seeds");
    ItemType.Typed<ItemMeta> MELON_SEEDS = getItemType("melon_seeds");
    ItemType.Typed<ItemMeta> BEEF = getItemType("beef");
    ItemType.Typed<ItemMeta> COOKED_BEEF = getItemType("cooked_beef");
    ItemType.Typed<ItemMeta> CHICKEN = getItemType("chicken");
    ItemType.Typed<ItemMeta> COOKED_CHICKEN = getItemType("cooked_chicken");
    ItemType.Typed<ItemMeta> ROTTEN_FLESH = getItemType("rotten_flesh");
    ItemType.Typed<ItemMeta> ENDER_PEARL = getItemType("ender_pearl");
    ItemType.Typed<ItemMeta> BLAZE_ROD = getItemType("blaze_rod");
    ItemType.Typed<ItemMeta> GHAST_TEAR = getItemType("ghast_tear");
    ItemType.Typed<ItemMeta> GOLD_NUGGET = getItemType("gold_nugget");
    ItemType.Typed<ItemMeta> NETHER_WART = getItemType("nether_wart");
    /**
     * ItemMeta: {@link PotionMeta}
     */
    ItemType.Typed<PotionMeta> POTION = getItemType("potion");
    ItemType.Typed<ItemMeta> GLASS_BOTTLE = getItemType("glass_bottle");
    ItemType.Typed<ItemMeta> SPIDER_EYE = getItemType("spider_eye");
    ItemType.Typed<ItemMeta> FERMENTED_SPIDER_EYE = getItemType("fermented_spider_eye");
    ItemType.Typed<ItemMeta> BLAZE_POWDER = getItemType("blaze_powder");
    ItemType.Typed<ItemMeta> MAGMA_CREAM = getItemType("magma_cream");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BREWING_STAND = getItemType("brewing_stand");
    ItemType.Typed<ItemMeta> CAULDRON = getItemType("cauldron");
    ItemType.Typed<ItemMeta> ENDER_EYE = getItemType("ender_eye");
    ItemType.Typed<ItemMeta> GLISTERING_MELON_SLICE = getItemType("glistering_melon_slice");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ARMADILLO_SPAWN_EGG = getItemType("armadillo_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ALLAY_SPAWN_EGG = getItemType("allay_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> AXOLOTL_SPAWN_EGG = getItemType("axolotl_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> BAT_SPAWN_EGG = getItemType("bat_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> BEE_SPAWN_EGG = getItemType("bee_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> BLAZE_SPAWN_EGG = getItemType("blaze_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> BOGGED_SPAWN_EGG = getItemType("bogged_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> BREEZE_SPAWN_EGG = getItemType("breeze_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> CAT_SPAWN_EGG = getItemType("cat_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> CAMEL_SPAWN_EGG = getItemType("camel_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> CAVE_SPIDER_SPAWN_EGG = getItemType("cave_spider_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> CHICKEN_SPAWN_EGG = getItemType("chicken_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> COD_SPAWN_EGG = getItemType("cod_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> COW_SPAWN_EGG = getItemType("cow_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> CREEPER_SPAWN_EGG = getItemType("creeper_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> DOLPHIN_SPAWN_EGG = getItemType("dolphin_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> DONKEY_SPAWN_EGG = getItemType("donkey_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> DROWNED_SPAWN_EGG = getItemType("drowned_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ELDER_GUARDIAN_SPAWN_EGG = getItemType("elder_guardian_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ENDER_DRAGON_SPAWN_EGG = getItemType("ender_dragon_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ENDERMAN_SPAWN_EGG = getItemType("enderman_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ENDERMITE_SPAWN_EGG = getItemType("endermite_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> EVOKER_SPAWN_EGG = getItemType("evoker_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> FOX_SPAWN_EGG = getItemType("fox_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> FROG_SPAWN_EGG = getItemType("frog_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> GHAST_SPAWN_EGG = getItemType("ghast_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> GLOW_SQUID_SPAWN_EGG = getItemType("glow_squid_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> GOAT_SPAWN_EGG = getItemType("goat_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> GUARDIAN_SPAWN_EGG = getItemType("guardian_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> HOGLIN_SPAWN_EGG = getItemType("hoglin_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> HORSE_SPAWN_EGG = getItemType("horse_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> HUSK_SPAWN_EGG = getItemType("husk_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> IRON_GOLEM_SPAWN_EGG = getItemType("iron_golem_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> LLAMA_SPAWN_EGG = getItemType("llama_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> MAGMA_CUBE_SPAWN_EGG = getItemType("magma_cube_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> MOOSHROOM_SPAWN_EGG = getItemType("mooshroom_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> MULE_SPAWN_EGG = getItemType("mule_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> OCELOT_SPAWN_EGG = getItemType("ocelot_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PANDA_SPAWN_EGG = getItemType("panda_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PARROT_SPAWN_EGG = getItemType("parrot_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PHANTOM_SPAWN_EGG = getItemType("phantom_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PIG_SPAWN_EGG = getItemType("pig_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PIGLIN_SPAWN_EGG = getItemType("piglin_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PIGLIN_BRUTE_SPAWN_EGG = getItemType("piglin_brute_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PILLAGER_SPAWN_EGG = getItemType("pillager_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> POLAR_BEAR_SPAWN_EGG = getItemType("polar_bear_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> PUFFERFISH_SPAWN_EGG = getItemType("pufferfish_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> RABBIT_SPAWN_EGG = getItemType("rabbit_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> RAVAGER_SPAWN_EGG = getItemType("ravager_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SALMON_SPAWN_EGG = getItemType("salmon_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SHEEP_SPAWN_EGG = getItemType("sheep_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SHULKER_SPAWN_EGG = getItemType("shulker_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SILVERFISH_SPAWN_EGG = getItemType("silverfish_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SKELETON_SPAWN_EGG = getItemType("skeleton_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SKELETON_HORSE_SPAWN_EGG = getItemType("skeleton_horse_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SLIME_SPAWN_EGG = getItemType("slime_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SNIFFER_SPAWN_EGG = getItemType("sniffer_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SNOW_GOLEM_SPAWN_EGG = getItemType("snow_golem_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SPIDER_SPAWN_EGG = getItemType("spider_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> SQUID_SPAWN_EGG = getItemType("squid_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> STRAY_SPAWN_EGG = getItemType("stray_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> STRIDER_SPAWN_EGG = getItemType("strider_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> TADPOLE_SPAWN_EGG = getItemType("tadpole_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> TRADER_LLAMA_SPAWN_EGG = getItemType("trader_llama_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> TROPICAL_FISH_SPAWN_EGG = getItemType("tropical_fish_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> TURTLE_SPAWN_EGG = getItemType("turtle_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> VEX_SPAWN_EGG = getItemType("vex_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> VILLAGER_SPAWN_EGG = getItemType("villager_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> VINDICATOR_SPAWN_EGG = getItemType("vindicator_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WANDERING_TRADER_SPAWN_EGG = getItemType("wandering_trader_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WARDEN_SPAWN_EGG = getItemType("warden_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WITCH_SPAWN_EGG = getItemType("witch_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WITHER_SPAWN_EGG = getItemType("wither_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WITHER_SKELETON_SPAWN_EGG = getItemType("wither_skeleton_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> WOLF_SPAWN_EGG = getItemType("wolf_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ZOGLIN_SPAWN_EGG = getItemType("zoglin_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ZOMBIE_SPAWN_EGG = getItemType("zombie_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ZOMBIE_HORSE_SPAWN_EGG = getItemType("zombie_horse_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ZOMBIE_VILLAGER_SPAWN_EGG = getItemType("zombie_villager_spawn_egg");
    /**
     * ItemMeta: {@link SpawnEggMeta}
     */
    ItemType.Typed<SpawnEggMeta> ZOMBIFIED_PIGLIN_SPAWN_EGG = getItemType("zombified_piglin_spawn_egg");
    ItemType.Typed<ItemMeta> EXPERIENCE_BOTTLE = getItemType("experience_bottle");
    ItemType.Typed<ItemMeta> FIRE_CHARGE = getItemType("fire_charge");
    ItemType.Typed<ItemMeta> WIND_CHARGE = getItemType("wind_charge");
    /**
     * ItemMeta: {@link BookMeta}
     */
    ItemType.Typed<BookMeta> WRITABLE_BOOK = getItemType("writable_book");
    /**
     * ItemMeta: {@link BookMeta}
     */
    ItemType.Typed<BookMeta> WRITTEN_BOOK = getItemType("written_book");
    ItemType.Typed<ItemMeta> MACE = getItemType("mace");
    ItemType.Typed<ItemMeta> ITEM_FRAME = getItemType("item_frame");
    ItemType.Typed<ItemMeta> GLOW_ITEM_FRAME = getItemType("glow_item_frame");
    ItemType.Typed<ItemMeta> FLOWER_POT = getItemType("flower_pot");
    ItemType.Typed<ItemMeta> CARROT = getItemType("carrot");
    ItemType.Typed<ItemMeta> POTATO = getItemType("potato");
    ItemType.Typed<ItemMeta> BAKED_POTATO = getItemType("baked_potato");
    ItemType.Typed<ItemMeta> POISONOUS_POTATO = getItemType("poisonous_potato");
    ItemType.Typed<ItemMeta> MAP = getItemType("map");
    ItemType.Typed<ItemMeta> GOLDEN_CARROT = getItemType("golden_carrot");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> SKELETON_SKULL = getItemType("skeleton_skull");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> WITHER_SKELETON_SKULL = getItemType("wither_skeleton_skull");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> PLAYER_HEAD = getItemType("player_head");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> ZOMBIE_HEAD = getItemType("zombie_head");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> CREEPER_HEAD = getItemType("creeper_head");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> DRAGON_HEAD = getItemType("dragon_head");
    /**
     * ItemMeta: {@link SkullMeta}
     */
    ItemType.Typed<SkullMeta> PIGLIN_HEAD = getItemType("piglin_head");
    ItemType.Typed<ItemMeta> NETHER_STAR = getItemType("nether_star");
    ItemType.Typed<ItemMeta> PUMPKIN_PIE = getItemType("pumpkin_pie");
    /**
     * ItemMeta: {@link FireworkMeta}
     */
    ItemType.Typed<FireworkMeta> FIREWORK_ROCKET = getItemType("firework_rocket");
    /**
     * ItemMeta: {@link FireworkEffectMeta}
     */
    ItemType.Typed<FireworkEffectMeta> FIREWORK_STAR = getItemType("firework_star");
    /**
     * ItemMeta: {@link EnchantmentStorageMeta}
     */
    ItemType.Typed<EnchantmentStorageMeta> ENCHANTED_BOOK = getItemType("enchanted_book");
    ItemType.Typed<ItemMeta> NETHER_BRICK = getItemType("nether_brick");
    ItemType.Typed<ItemMeta> PRISMARINE_SHARD = getItemType("prismarine_shard");
    ItemType.Typed<ItemMeta> PRISMARINE_CRYSTALS = getItemType("prismarine_crystals");
    ItemType.Typed<ItemMeta> RABBIT = getItemType("rabbit");
    ItemType.Typed<ItemMeta> COOKED_RABBIT = getItemType("cooked_rabbit");
    ItemType.Typed<ItemMeta> RABBIT_STEW = getItemType("rabbit_stew");
    ItemType.Typed<ItemMeta> RABBIT_FOOT = getItemType("rabbit_foot");
    ItemType.Typed<ItemMeta> RABBIT_HIDE = getItemType("rabbit_hide");
    ItemType.Typed<ItemMeta> ARMOR_STAND = getItemType("armor_stand");
    ItemType.Typed<ItemMeta> IRON_HORSE_ARMOR = getItemType("iron_horse_armor");
    ItemType.Typed<ItemMeta> GOLDEN_HORSE_ARMOR = getItemType("golden_horse_armor");
    ItemType.Typed<ItemMeta> DIAMOND_HORSE_ARMOR = getItemType("diamond_horse_armor");
    /**
     * ItemMeta: {@link LeatherArmorMeta}
     */
    ItemType.Typed<LeatherArmorMeta> LEATHER_HORSE_ARMOR = getItemType("leather_horse_armor");
    ItemType.Typed<ItemMeta> LEAD = getItemType("lead");
    ItemType.Typed<ItemMeta> NAME_TAG = getItemType("name_tag");
    ItemType.Typed<ItemMeta> COMMAND_BLOCK_MINECART = getItemType("command_block_minecart");
    ItemType.Typed<ItemMeta> MUTTON = getItemType("mutton");
    ItemType.Typed<ItemMeta> COOKED_MUTTON = getItemType("cooked_mutton");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> WHITE_BANNER = getItemType("white_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> ORANGE_BANNER = getItemType("orange_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> MAGENTA_BANNER = getItemType("magenta_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> LIGHT_BLUE_BANNER = getItemType("light_blue_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> YELLOW_BANNER = getItemType("yellow_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> LIME_BANNER = getItemType("lime_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> PINK_BANNER = getItemType("pink_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> GRAY_BANNER = getItemType("gray_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> LIGHT_GRAY_BANNER = getItemType("light_gray_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> CYAN_BANNER = getItemType("cyan_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> PURPLE_BANNER = getItemType("purple_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> BLUE_BANNER = getItemType("blue_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> BROWN_BANNER = getItemType("brown_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> GREEN_BANNER = getItemType("green_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> RED_BANNER = getItemType("red_banner");
    /**
     * ItemMeta: {@link BannerMeta}
     */
    ItemType.Typed<BannerMeta> BLACK_BANNER = getItemType("black_banner");
    ItemType.Typed<ItemMeta> END_CRYSTAL = getItemType("end_crystal");
    ItemType.Typed<ItemMeta> CHORUS_FRUIT = getItemType("chorus_fruit");
    ItemType.Typed<ItemMeta> POPPED_CHORUS_FRUIT = getItemType("popped_chorus_fruit");
    ItemType.Typed<ItemMeta> TORCHFLOWER_SEEDS = getItemType("torchflower_seeds");
    ItemType.Typed<ItemMeta> PITCHER_POD = getItemType("pitcher_pod");
    ItemType.Typed<ItemMeta> BEETROOT = getItemType("beetroot");
    ItemType.Typed<ItemMeta> BEETROOT_SEEDS = getItemType("beetroot_seeds");
    ItemType.Typed<ItemMeta> BEETROOT_SOUP = getItemType("beetroot_soup");
    ItemType.Typed<ItemMeta> DRAGON_BREATH = getItemType("dragon_breath");
    /**
     * ItemMeta: {@link PotionMeta}
     */
    ItemType.Typed<PotionMeta> SPLASH_POTION = getItemType("splash_potion");
    ItemType.Typed<ItemMeta> SPECTRAL_ARROW = getItemType("spectral_arrow");
    /**
     * ItemMeta: {@link PotionMeta}
     */
    ItemType.Typed<PotionMeta> TIPPED_ARROW = getItemType("tipped_arrow");
    /**
     * ItemMeta: {@link PotionMeta}
     */
    ItemType.Typed<PotionMeta> LINGERING_POTION = getItemType("lingering_potion");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SHIELD = getItemType("shield");
    ItemType.Typed<ItemMeta> TOTEM_OF_UNDYING = getItemType("totem_of_undying");
    ItemType.Typed<ItemMeta> SHULKER_SHELL = getItemType("shulker_shell");
    ItemType.Typed<ItemMeta> IRON_NUGGET = getItemType("iron_nugget");
    /**
     * ItemMeta: {@link KnowledgeBookMeta}
     */
    ItemType.Typed<KnowledgeBookMeta> KNOWLEDGE_BOOK = getItemType("knowledge_book");
    ItemType.Typed<ItemMeta> DEBUG_STICK = getItemType("debug_stick");
    ItemType.Typed<ItemMeta> MUSIC_DISC_13 = getItemType("music_disc_13");
    ItemType.Typed<ItemMeta> MUSIC_DISC_CAT = getItemType("music_disc_cat");
    ItemType.Typed<ItemMeta> MUSIC_DISC_BLOCKS = getItemType("music_disc_blocks");
    ItemType.Typed<ItemMeta> MUSIC_DISC_CHIRP = getItemType("music_disc_chirp");
    ItemType.Typed<ItemMeta> MUSIC_DISC_CREATOR = getItemType("music_disc_creator");
    ItemType.Typed<ItemMeta> MUSIC_DISC_CREATOR_MUSIC_BOX = getItemType("music_disc_creator_music_box");
    ItemType.Typed<ItemMeta> MUSIC_DISC_FAR = getItemType("music_disc_far");
    ItemType.Typed<ItemMeta> MUSIC_DISC_MALL = getItemType("music_disc_mall");
    ItemType.Typed<ItemMeta> MUSIC_DISC_MELLOHI = getItemType("music_disc_mellohi");
    ItemType.Typed<ItemMeta> MUSIC_DISC_STAL = getItemType("music_disc_stal");
    ItemType.Typed<ItemMeta> MUSIC_DISC_STRAD = getItemType("music_disc_strad");
    ItemType.Typed<ItemMeta> MUSIC_DISC_WARD = getItemType("music_disc_ward");
    ItemType.Typed<ItemMeta> MUSIC_DISC_11 = getItemType("music_disc_11");
    ItemType.Typed<ItemMeta> MUSIC_DISC_WAIT = getItemType("music_disc_wait");
    ItemType.Typed<ItemMeta> MUSIC_DISC_OTHERSIDE = getItemType("music_disc_otherside");
    ItemType.Typed<ItemMeta> MUSIC_DISC_RELIC = getItemType("music_disc_relic");
    ItemType.Typed<ItemMeta> MUSIC_DISC_5 = getItemType("music_disc_5");
    ItemType.Typed<ItemMeta> MUSIC_DISC_PIGSTEP = getItemType("music_disc_pigstep");
    ItemType.Typed<ItemMeta> MUSIC_DISC_PRECIPICE = getItemType("music_disc_precipice");
    ItemType.Typed<ItemMeta> DISC_FRAGMENT_5 = getItemType("disc_fragment_5");
    ItemType.Typed<ItemMeta> TRIDENT = getItemType("trident");
    ItemType.Typed<ItemMeta> PHANTOM_MEMBRANE = getItemType("phantom_membrane");
    ItemType.Typed<ItemMeta> NAUTILUS_SHELL = getItemType("nautilus_shell");
    ItemType.Typed<ItemMeta> HEART_OF_THE_SEA = getItemType("heart_of_the_sea");
    /**
     * ItemMeta: {@link CrossbowMeta}
     */
    ItemType.Typed<CrossbowMeta> CROSSBOW = getItemType("crossbow");
    /**
     * ItemMeta: {@link SuspiciousStewMeta}
     */
    ItemType.Typed<SuspiciousStewMeta> SUSPICIOUS_STEW = getItemType("suspicious_stew");
    ItemType.Typed<ItemMeta> LOOM = getItemType("loom");
    ItemType.Typed<ItemMeta> FLOWER_BANNER_PATTERN = getItemType("flower_banner_pattern");
    ItemType.Typed<ItemMeta> CREEPER_BANNER_PATTERN = getItemType("creeper_banner_pattern");
    ItemType.Typed<ItemMeta> SKULL_BANNER_PATTERN = getItemType("skull_banner_pattern");
    ItemType.Typed<ItemMeta> MOJANG_BANNER_PATTERN = getItemType("mojang_banner_pattern");
    ItemType.Typed<ItemMeta> GLOBE_BANNER_PATTERN = getItemType("globe_banner_pattern");
    ItemType.Typed<ItemMeta> PIGLIN_BANNER_PATTERN = getItemType("piglin_banner_pattern");
    ItemType.Typed<ItemMeta> FLOW_BANNER_PATTERN = getItemType("flow_banner_pattern");
    ItemType.Typed<ItemMeta> GUSTER_BANNER_PATTERN = getItemType("guster_banner_pattern");
    /**
     * ItemMeta: {@link MusicInstrumentMeta}
     */
    ItemType.Typed<MusicInstrumentMeta> GOAT_HORN = getItemType("goat_horn");
    ItemType.Typed<ItemMeta> COMPOSTER = getItemType("composter");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BARREL = getItemType("barrel");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SMOKER = getItemType("smoker");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BLAST_FURNACE = getItemType("blast_furnace");
    ItemType.Typed<ItemMeta> CARTOGRAPHY_TABLE = getItemType("cartography_table");
    ItemType.Typed<ItemMeta> FLETCHING_TABLE = getItemType("fletching_table");
    ItemType.Typed<ItemMeta> GRINDSTONE = getItemType("grindstone");
    ItemType.Typed<ItemMeta> SMITHING_TABLE = getItemType("smithing_table");
    ItemType.Typed<ItemMeta> STONECUTTER = getItemType("stonecutter");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BELL = getItemType("bell");
    ItemType.Typed<ItemMeta> LANTERN = getItemType("lantern");
    ItemType.Typed<ItemMeta> SOUL_LANTERN = getItemType("soul_lantern");
    ItemType.Typed<ItemMeta> SWEET_BERRIES = getItemType("sweet_berries");
    ItemType.Typed<ItemMeta> GLOW_BERRIES = getItemType("glow_berries");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> CAMPFIRE = getItemType("campfire");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> SOUL_CAMPFIRE = getItemType("soul_campfire");
    ItemType.Typed<ItemMeta> SHROOMLIGHT = getItemType("shroomlight");
    ItemType.Typed<ItemMeta> HONEYCOMB = getItemType("honeycomb");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BEE_NEST = getItemType("bee_nest");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> BEEHIVE = getItemType("beehive");
    ItemType.Typed<ItemMeta> HONEY_BOTTLE = getItemType("honey_bottle");
    ItemType.Typed<ItemMeta> HONEYCOMB_BLOCK = getItemType("honeycomb_block");
    ItemType.Typed<ItemMeta> LODESTONE = getItemType("lodestone");
    ItemType.Typed<ItemMeta> CRYING_OBSIDIAN = getItemType("crying_obsidian");
    ItemType.Typed<ItemMeta> BLACKSTONE = getItemType("blackstone");
    ItemType.Typed<ItemMeta> BLACKSTONE_SLAB = getItemType("blackstone_slab");
    ItemType.Typed<ItemMeta> BLACKSTONE_STAIRS = getItemType("blackstone_stairs");
    ItemType.Typed<ItemMeta> GILDED_BLACKSTONE = getItemType("gilded_blackstone");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE = getItemType("polished_blackstone");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_SLAB = getItemType("polished_blackstone_slab");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_STAIRS = getItemType("polished_blackstone_stairs");
    ItemType.Typed<ItemMeta> CHISELED_POLISHED_BLACKSTONE = getItemType("chiseled_polished_blackstone");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_BRICKS = getItemType("polished_blackstone_bricks");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_BRICK_SLAB = getItemType("polished_blackstone_brick_slab");
    ItemType.Typed<ItemMeta> POLISHED_BLACKSTONE_BRICK_STAIRS = getItemType("polished_blackstone_brick_stairs");
    ItemType.Typed<ItemMeta> CRACKED_POLISHED_BLACKSTONE_BRICKS = getItemType("cracked_polished_blackstone_bricks");
    ItemType.Typed<ItemMeta> RESPAWN_ANCHOR = getItemType("respawn_anchor");
    ItemType.Typed<ItemMeta> CANDLE = getItemType("candle");
    ItemType.Typed<ItemMeta> WHITE_CANDLE = getItemType("white_candle");
    ItemType.Typed<ItemMeta> ORANGE_CANDLE = getItemType("orange_candle");
    ItemType.Typed<ItemMeta> MAGENTA_CANDLE = getItemType("magenta_candle");
    ItemType.Typed<ItemMeta> LIGHT_BLUE_CANDLE = getItemType("light_blue_candle");
    ItemType.Typed<ItemMeta> YELLOW_CANDLE = getItemType("yellow_candle");
    ItemType.Typed<ItemMeta> LIME_CANDLE = getItemType("lime_candle");
    ItemType.Typed<ItemMeta> PINK_CANDLE = getItemType("pink_candle");
    ItemType.Typed<ItemMeta> GRAY_CANDLE = getItemType("gray_candle");
    ItemType.Typed<ItemMeta> LIGHT_GRAY_CANDLE = getItemType("light_gray_candle");
    ItemType.Typed<ItemMeta> CYAN_CANDLE = getItemType("cyan_candle");
    ItemType.Typed<ItemMeta> PURPLE_CANDLE = getItemType("purple_candle");
    ItemType.Typed<ItemMeta> BLUE_CANDLE = getItemType("blue_candle");
    ItemType.Typed<ItemMeta> BROWN_CANDLE = getItemType("brown_candle");
    ItemType.Typed<ItemMeta> GREEN_CANDLE = getItemType("green_candle");
    ItemType.Typed<ItemMeta> RED_CANDLE = getItemType("red_candle");
    ItemType.Typed<ItemMeta> BLACK_CANDLE = getItemType("black_candle");
    ItemType.Typed<ItemMeta> SMALL_AMETHYST_BUD = getItemType("small_amethyst_bud");
    ItemType.Typed<ItemMeta> MEDIUM_AMETHYST_BUD = getItemType("medium_amethyst_bud");
    ItemType.Typed<ItemMeta> LARGE_AMETHYST_BUD = getItemType("large_amethyst_bud");
    ItemType.Typed<ItemMeta> AMETHYST_CLUSTER = getItemType("amethyst_cluster");
    ItemType.Typed<ItemMeta> POINTED_DRIPSTONE = getItemType("pointed_dripstone");
    ItemType.Typed<ItemMeta> OCHRE_FROGLIGHT = getItemType("ochre_froglight");
    ItemType.Typed<ItemMeta> VERDANT_FROGLIGHT = getItemType("verdant_froglight");
    ItemType.Typed<ItemMeta> PEARLESCENT_FROGLIGHT = getItemType("pearlescent_froglight");
    ItemType.Typed<ItemMeta> FROGSPAWN = getItemType("frogspawn");
    ItemType.Typed<ItemMeta> ECHO_SHARD = getItemType("echo_shard");
    ItemType.Typed<ItemMeta> BRUSH = getItemType("brush");
    ItemType.Typed<ItemMeta> NETHERITE_UPGRADE_SMITHING_TEMPLATE = getItemType("netherite_upgrade_smithing_template");
    ItemType.Typed<ItemMeta> SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("sentry_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> DUNE_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("dune_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> COAST_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("coast_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> WILD_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("wild_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> WARD_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("ward_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> EYE_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("eye_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> VEX_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("vex_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> TIDE_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("tide_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("snout_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> RIB_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("rib_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("spire_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("wayfinder_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("shaper_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("silence_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> RAISER_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("raiser_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> HOST_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("host_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> FLOW_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("flow_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> BOLT_ARMOR_TRIM_SMITHING_TEMPLATE = getItemType("bolt_armor_trim_smithing_template");
    ItemType.Typed<ItemMeta> ANGLER_POTTERY_SHERD = getItemType("angler_pottery_sherd");
    ItemType.Typed<ItemMeta> ARCHER_POTTERY_SHERD = getItemType("archer_pottery_sherd");
    ItemType.Typed<ItemMeta> ARMS_UP_POTTERY_SHERD = getItemType("arms_up_pottery_sherd");
    ItemType.Typed<ItemMeta> BLADE_POTTERY_SHERD = getItemType("blade_pottery_sherd");
    ItemType.Typed<ItemMeta> BREWER_POTTERY_SHERD = getItemType("brewer_pottery_sherd");
    ItemType.Typed<ItemMeta> BURN_POTTERY_SHERD = getItemType("burn_pottery_sherd");
    ItemType.Typed<ItemMeta> DANGER_POTTERY_SHERD = getItemType("danger_pottery_sherd");
    ItemType.Typed<ItemMeta> EXPLORER_POTTERY_SHERD = getItemType("explorer_pottery_sherd");
    ItemType.Typed<ItemMeta> FLOW_POTTERY_SHERD = getItemType("flow_pottery_sherd");
    ItemType.Typed<ItemMeta> FRIEND_POTTERY_SHERD = getItemType("friend_pottery_sherd");
    ItemType.Typed<ItemMeta> GUSTER_POTTERY_SHERD = getItemType("guster_pottery_sherd");
    ItemType.Typed<ItemMeta> HEART_POTTERY_SHERD = getItemType("heart_pottery_sherd");
    ItemType.Typed<ItemMeta> HEARTBREAK_POTTERY_SHERD = getItemType("heartbreak_pottery_sherd");
    ItemType.Typed<ItemMeta> HOWL_POTTERY_SHERD = getItemType("howl_pottery_sherd");
    ItemType.Typed<ItemMeta> MINER_POTTERY_SHERD = getItemType("miner_pottery_sherd");
    ItemType.Typed<ItemMeta> MOURNER_POTTERY_SHERD = getItemType("mourner_pottery_sherd");
    ItemType.Typed<ItemMeta> PLENTY_POTTERY_SHERD = getItemType("plenty_pottery_sherd");
    ItemType.Typed<ItemMeta> PRIZE_POTTERY_SHERD = getItemType("prize_pottery_sherd");
    ItemType.Typed<ItemMeta> SCRAPE_POTTERY_SHERD = getItemType("scrape_pottery_sherd");
    ItemType.Typed<ItemMeta> SHEAF_POTTERY_SHERD = getItemType("sheaf_pottery_sherd");
    ItemType.Typed<ItemMeta> SHELTER_POTTERY_SHERD = getItemType("shelter_pottery_sherd");
    ItemType.Typed<ItemMeta> SKULL_POTTERY_SHERD = getItemType("skull_pottery_sherd");
    ItemType.Typed<ItemMeta> SNORT_POTTERY_SHERD = getItemType("snort_pottery_sherd");
    ItemType.Typed<ItemMeta> COPPER_GRATE = getItemType("copper_grate");
    ItemType.Typed<ItemMeta> EXPOSED_COPPER_GRATE = getItemType("exposed_copper_grate");
    ItemType.Typed<ItemMeta> WEATHERED_COPPER_GRATE = getItemType("weathered_copper_grate");
    ItemType.Typed<ItemMeta> OXIDIZED_COPPER_GRATE = getItemType("oxidized_copper_grate");
    ItemType.Typed<ItemMeta> WAXED_COPPER_GRATE = getItemType("waxed_copper_grate");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_COPPER_GRATE = getItemType("waxed_exposed_copper_grate");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_COPPER_GRATE = getItemType("waxed_weathered_copper_grate");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_COPPER_GRATE = getItemType("waxed_oxidized_copper_grate");
    ItemType.Typed<ItemMeta> COPPER_BULB = getItemType("copper_bulb");
    ItemType.Typed<ItemMeta> EXPOSED_COPPER_BULB = getItemType("exposed_copper_bulb");
    ItemType.Typed<ItemMeta> WEATHERED_COPPER_BULB = getItemType("weathered_copper_bulb");
    ItemType.Typed<ItemMeta> OXIDIZED_COPPER_BULB = getItemType("oxidized_copper_bulb");
    ItemType.Typed<ItemMeta> WAXED_COPPER_BULB = getItemType("waxed_copper_bulb");
    ItemType.Typed<ItemMeta> WAXED_EXPOSED_COPPER_BULB = getItemType("waxed_exposed_copper_bulb");
    ItemType.Typed<ItemMeta> WAXED_WEATHERED_COPPER_BULB = getItemType("waxed_weathered_copper_bulb");
    ItemType.Typed<ItemMeta> WAXED_OXIDIZED_COPPER_BULB = getItemType("waxed_oxidized_copper_bulb");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> TRIAL_SPAWNER = getItemType("trial_spawner");
    ItemType.Typed<ItemMeta> TRIAL_KEY = getItemType("trial_key");
    ItemType.Typed<ItemMeta> OMINOUS_TRIAL_KEY = getItemType("ominous_trial_key");
    /**
     * ItemMeta: {@link BlockStateMeta}
     */
    ItemType.Typed<BlockStateMeta> VAULT = getItemType("vault");
    /**
     * ItemMeta: {@link OminousBottleMeta}
     */
    ItemType.Typed<OminousBottleMeta> OMINOUS_BOTTLE = getItemType("ominous_bottle");
    ItemType.Typed<ItemMeta> BREEZE_ROD = getItemType("breeze_rod");


    //</editor-fold>

    @NotNull
    private static <M extends ItemType> M getItemType(@NotNull String key) {
        NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
        ItemType itemType = Registry.ITEM.get(namespacedKey);
        Preconditions.checkNotNull(itemType, "No ItemType found for %s. This is a bug.", namespacedKey);
        // Cast instead of using ItemType#typed, since item type can be a mock during testing and would return null
        return (M) itemType;
    }

    /**
     * Yields this item type as a typed version of itself with a plain {@link ItemMeta} representing it.
     *
     * @return the typed item type.
     */
    @NotNull
    Typed<ItemMeta> typed();

    /**
     * Yields this item type as a typed version of itself with a plain {@link ItemMeta} representing it.
     *
     * @param itemMetaType the class type of the {@link ItemMeta} to type this {@link ItemType} with.
     * @param <M> the generic type of the item meta to type this item type with.
     * @return the typed item type.
     */
    @NotNull
    <M extends ItemMeta> Typed<M> typed(@NotNull Class<M> itemMetaType);

    /**
     * Constructs a new itemstack with this item type that has the amount 1.
     *
     * @return the constructed item stack.
     */
    @NotNull
    ItemStack createItemStack();

    /**
     * Constructs a new itemstack with this item type.
     *
     * @param amount the amount of the item stack.
     * @return the constructed item stack.
     */
    @NotNull
    ItemStack createItemStack(int amount);

    /**
     * Returns true if this ItemType has a corresponding {@link BlockType}.
     *
     * @return true if there is a corresponding BlockType, otherwise false
     * @see #getBlockType()
     */
    boolean hasBlockType();

    /**
     * Returns the corresponding {@link BlockType} for the given ItemType.
     * <p>
     * If there is no corresponding {@link BlockType} an error will be thrown.
     *
     * @return the corresponding BlockType
     * @see #hasBlockType()
     */
    @NotNull
    BlockType getBlockType();

    /**
     * Gets the ItemMeta class of this ItemType
     *
     * @return the ItemMeta class of this ItemType
     */
    @NotNull
    Class<? extends ItemMeta> getItemMetaClass();

    /**
     * Gets the maximum amount of this item type that can be held in a stack
     *
     * @return Maximum stack size for this item type
     */
    int getMaxStackSize();

    /**
     * Gets the maximum durability of this item type
     *
     * @return Maximum durability for this item type
     */
    short getMaxDurability();

    /**
     * Checks if this item type is edible.
     *
     * @return true if this item type is edible.
     */
    boolean isEdible();

    /**
     * @return True if this item type represents a playable music disk.
     */
    boolean isRecord();

    /**
     * Checks if this item type can be used as fuel in a Furnace
     *
     * @return true if this item type can be used as fuel.
     */
    boolean isFuel();

    /**
     * Checks whether this item type is compostable (can be inserted into a
     * composter).
     *
     * @return true if this item type is compostable
     * @see #getCompostChance()
     */
    boolean isCompostable();

    /**
     * Get the chance that this item type will successfully compost. The
     * returned value is between 0 and 1 (inclusive).
     *
     * Items with a compost chance of 1 will always raise the composter's level,
     * while items with a compost chance of 0 will never raise it.
     *
     * Plugins should check that {@link #isCompostable} returns true before
     * calling this method.
     *
     * @return the chance that this item type will successfully compost
     * @throws IllegalArgumentException if this item type is not compostable
     * @see #isCompostable()
     */
    float getCompostChance();

    /**
     * Determines the remaining item in a crafting grid after crafting with this
     * ingredient.
     *
     * @return the item left behind when crafting, or null if nothing is.
     */
    @Nullable
    ItemType getCraftingRemainingItem();

//    /**
//     * Get the best suitable slot for this item type.
//     *
//     * For most items this will be {@link EquipmentSlot#HAND}.
//     *
//     * @return the best EquipmentSlot for this item type
//     */
//    @NotNull
//    EquipmentSlot getEquipmentSlot();

    /**
     * Return an immutable copy of all default {@link Attribute}s and their
     * {@link AttributeModifier}s for a given {@link EquipmentSlot}.
     *
     * Default attributes are those that are always preset on some items, such
     * as the attack damage on weapons or the armor value on armor.
     *
     * @param slot the {@link EquipmentSlot} to check
     * @return the immutable {@link Multimap} with the respective default
     * Attributes and modifiers, or an empty map if no attributes are set.
     */
    @NotNull
    Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot);

    /**
     * Get the {@link CreativeCategory} to which this item type belongs.
     *
     * @return the creative category. null if does not belong to a category
     * @deprecated creative categories no longer exist on the server
     */
    @Nullable
    @Deprecated
    CreativeCategory getCreativeCategory();

    /**
     * Gets if the ItemType is enabled by the features in a world.
     *
     * @param world the world to check
     * @return true if this ItemType can be used in this World.
     */
    boolean isEnabledByFeature(@NotNull World world);

    /**
     * Tries to convert this ItemType into a Material
     *
     * @return the converted Material or null
     * @deprecated only for internal use
     */
    @Nullable
    @Deprecated
    Material asMaterial();
}
