package org.bukkit.craftbukkit;

import java.util.Map;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.bukkit.TreeType;
import org.jspecify.annotations.Nullable;

public final class CraftTreeType {

    private static final Map<ResourceKey<Feature>, TreeType> BY_FEATURE = Map.ofEntries(
        Map.entry(TreeFeatures.OAK, TreeType.TREE),
        Map.entry(TreeFeatures.OAK_BEES_005, TreeType.TREE),
        Map.entry(TreeFeatures.FANCY_OAK, TreeType.BIG_TREE),
        Map.entry(TreeFeatures.FANCY_OAK_BEES_005, TreeType.BIG_TREE),
        Map.entry(TreeFeatures.SPRUCE, TreeType.REDWOOD),
        Map.entry(TreeFeatures.PINE, TreeType.TALL_REDWOOD),
        Map.entry(TreeFeatures.MEGA_SPRUCE, TreeType.MEGA_REDWOOD),
        Map.entry(TreeFeatures.MEGA_PINE, TreeType.MEGA_PINE),
        Map.entry(TreeFeatures.BIRCH, TreeType.BIRCH),
        Map.entry(TreeFeatures.BIRCH_BEES_005, TreeType.BIRCH),
        Map.entry(TreeFeatures.SUPER_BIRCH_BEES_0002, TreeType.TALL_BIRCH),
        Map.entry(TreeFeatures.MEGA_JUNGLE_TREE, TreeType.JUNGLE),
        Map.entry(TreeFeatures.JUNGLE_TREE_NO_VINE, TreeType.SMALL_JUNGLE),
        Map.entry(TreeFeatures.JUNGLE_TREE, TreeType.COCOA_TREE),
        Map.entry(TreeFeatures.JUNGLE_BUSH, TreeType.JUNGLE_BUSH),
        Map.entry(TreeFeatures.HUGE_RED_MUSHROOM, TreeType.RED_MUSHROOM),
        Map.entry(TreeFeatures.HUGE_BROWN_MUSHROOM, TreeType.BROWN_MUSHROOM),
        Map.entry(TreeFeatures.SWAMP_OAK, TreeType.SWAMP),
        Map.entry(TreeFeatures.ACACIA, TreeType.ACACIA),
        Map.entry(TreeFeatures.DARK_OAK, TreeType.DARK_OAK),
        Map.entry(TreeFeatures.CRIMSON_FUNGUS, TreeType.CRIMSON_FUNGUS),
        Map.entry(TreeFeatures.CRIMSON_FUNGUS_PLANTED, TreeType.CRIMSON_FUNGUS),
        Map.entry(TreeFeatures.WARPED_FUNGUS, TreeType.WARPED_FUNGUS),
        Map.entry(TreeFeatures.WARPED_FUNGUS_PLANTED, TreeType.WARPED_FUNGUS),
        Map.entry(TreeFeatures.AZALEA_TREE, TreeType.AZALEA),
        Map.entry(TreeFeatures.MANGROVE, TreeType.MANGROVE),
        Map.entry(TreeFeatures.TALL_MANGROVE, TreeType.TALL_MANGROVE),
        Map.entry(TreeFeatures.CHERRY, TreeType.CHERRY),
        Map.entry(TreeFeatures.CHERRY_BEES_005, TreeType.CHERRY),
        Map.entry(TreeFeatures.PALE_OAK, TreeType.PALE_OAK),
        Map.entry(TreeFeatures.PALE_OAK_BONEMEAL, TreeType.PALE_OAK),
        Map.entry(TreeFeatures.PALE_OAK_CREAKING, TreeType.PALE_OAK_CREAKING),
        Map.entry(TreeFeatures.RED_POPLAR, TreeType.RED_POPLAR),
        Map.entry(TreeFeatures.ORANGE_POPLAR, TreeType.ORANGE_POPLAR),
        Map.entry(TreeFeatures.YELLOW_POPLAR, TreeType.YELLOW_POPLAR)
    );

    private CraftTreeType() {
    }

    public static TreeType fromFeature(ResourceKey<Feature> feature) {
        TreeType treeType = BY_FEATURE.get(feature);
        if (treeType == null) {
            throw new IllegalArgumentException("Unknown tree generator " + feature);
        }
        return treeType;
    }

    /**
     * @return the feature for the tree type, or {@code null} for {@link TreeType#CHORUS_PLANT}
     * which isn't placed through a feature
     */
    public static @Nullable ResourceKey<Feature> toFeature(TreeType treeType, boolean normalWorld) {
        return switch (treeType) {
            case TREE -> TreeFeatures.OAK;
            case BIG_TREE -> TreeFeatures.FANCY_OAK;
            case REDWOOD -> TreeFeatures.SPRUCE;
            case TALL_REDWOOD -> TreeFeatures.PINE;
            case JUNGLE -> TreeFeatures.MEGA_JUNGLE_TREE;
            case SMALL_JUNGLE -> TreeFeatures.JUNGLE_TREE_NO_VINE;
            case COCOA_TREE -> TreeFeatures.JUNGLE_TREE;
            case JUNGLE_BUSH -> TreeFeatures.JUNGLE_BUSH;
            case RED_MUSHROOM -> TreeFeatures.HUGE_RED_MUSHROOM;
            case BROWN_MUSHROOM -> TreeFeatures.HUGE_BROWN_MUSHROOM;
            case SWAMP -> TreeFeatures.SWAMP_OAK;
            case ACACIA -> TreeFeatures.ACACIA;
            case DARK_OAK -> TreeFeatures.DARK_OAK;
            case MEGA_REDWOOD -> TreeFeatures.MEGA_SPRUCE;
            case MEGA_PINE -> TreeFeatures.MEGA_PINE;
            case BIRCH -> TreeFeatures.BIRCH;
            case TALL_BIRCH -> TreeFeatures.SUPER_BIRCH_BEES_0002;
            case CHORUS_PLANT -> null;
            case CRIMSON_FUNGUS -> normalWorld ? TreeFeatures.CRIMSON_FUNGUS_PLANTED : TreeFeatures.CRIMSON_FUNGUS; // fix async entity add due to fungus trees; if world gen, don't use planted version
            case WARPED_FUNGUS -> normalWorld ? TreeFeatures.WARPED_FUNGUS_PLANTED : TreeFeatures.WARPED_FUNGUS; // fix async entity add due to fungus trees; if world gen, don't use planted version
            case AZALEA -> TreeFeatures.AZALEA_TREE;
            case MANGROVE -> TreeFeatures.MANGROVE;
            case TALL_MANGROVE -> TreeFeatures.TALL_MANGROVE;
            case CHERRY -> TreeFeatures.CHERRY;
            case PALE_OAK -> TreeFeatures.PALE_OAK;
            case PALE_OAK_CREAKING -> TreeFeatures.PALE_OAK_CREAKING;
            case RED_POPLAR -> TreeFeatures.RED_POPLAR;
            case ORANGE_POPLAR -> TreeFeatures.ORANGE_POPLAR;
            case YELLOW_POPLAR -> TreeFeatures.YELLOW_POPLAR;
        };
    }
}
