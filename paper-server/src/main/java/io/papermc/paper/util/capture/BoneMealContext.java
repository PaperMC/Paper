package io.papermc.paper.util.capture;

import net.minecraft.Optionull;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.bukkit.TreeType;
import org.jspecify.annotations.Nullable;

public class BoneMealContext {

    public final ServerLevel originalLevel;

    public @Nullable Player player;
    public boolean precancelStructureEvent = false;
    public TreeType treeHook; // just make this a field
    public boolean usedBoneMeal; // names are kinda messed

    public BoneMealContext(ServerLevel originalLevel) {
        this.originalLevel = originalLevel;
    }

    public org.bukkit.entity.@Nullable Player getBukkitPlayer() {
        return (org.bukkit.entity.Player) Optionull.map(this.player, Entity::getBukkitEntity);
    }

    public static TreeType getTreeType(Holder<ConfiguredFeature<?, ?>> feature) {
        if (feature.is(TreeFeatures.HUGE_RED_MUSHROOM)) {
            return TreeType.RED_MUSHROOM;
        } else if (feature.is(TreeFeatures.HUGE_BROWN_MUSHROOM)) {
            return TreeType.BROWN_MUSHROOM;
        } else if (feature.is(TreeFeatures.WARPED_FUNGUS_PLANTED)) {
            return TreeType.WARPED_FUNGUS;
        } else if (feature.is(TreeFeatures.CRIMSON_FUNGUS_PLANTED)) {
            return TreeType.CRIMSON_FUNGUS;
        } else if (feature.is(TreeFeatures.OAK) || feature.is(TreeFeatures.OAK_BEES_005)) {
            return TreeType.TREE;
        } else if (feature.is(TreeFeatures.JUNGLE_TREE)) {
            return TreeType.COCOA_TREE;
        } else if (feature.is(TreeFeatures.JUNGLE_TREE_NO_VINE)) {
            return TreeType.SMALL_JUNGLE;
        } else if (feature.is(TreeFeatures.PINE)) {
            return TreeType.TALL_REDWOOD;
        } else if (feature.is(TreeFeatures.SPRUCE)) {
            return TreeType.REDWOOD;
        } else if (feature.is(TreeFeatures.ACACIA)) {
            return TreeType.ACACIA;
        } else if (feature.is(TreeFeatures.BIRCH) || feature.is(TreeFeatures.BIRCH_BEES_005)) {
            return TreeType.BIRCH;
        } else if (feature.is(TreeFeatures.SUPER_BIRCH_BEES_0002)) {
            return TreeType.TALL_BIRCH;
        } else if (feature.is(TreeFeatures.SWAMP_OAK)) {
            return TreeType.SWAMP;
        } else if (feature.is(TreeFeatures.FANCY_OAK) || feature.is(TreeFeatures.FANCY_OAK_BEES_005)) {
            return TreeType.BIG_TREE;
        } else if (feature.is(TreeFeatures.JUNGLE_BUSH)) {
            return TreeType.JUNGLE_BUSH;
        } else if (feature.is(TreeFeatures.DARK_OAK)) {
            return TreeType.DARK_OAK;
        } else if (feature.is(TreeFeatures.MEGA_SPRUCE)) {
            return TreeType.MEGA_REDWOOD;
        } else if (feature.is(TreeFeatures.MEGA_PINE)) {
            return TreeType.MEGA_PINE;
        } else if (feature.is(TreeFeatures.MEGA_JUNGLE_TREE)) {
            return TreeType.JUNGLE;
        } else if (feature.is(TreeFeatures.AZALEA_TREE)) {
            return TreeType.AZALEA;
        } else if (feature.is(TreeFeatures.MANGROVE)) {
            return TreeType.MANGROVE;
        } else if (feature.is(TreeFeatures.TALL_MANGROVE)) {
            return TreeType.TALL_MANGROVE;
        } else if (feature.is(TreeFeatures.CHERRY) || feature.is(TreeFeatures.CHERRY_BEES_005)) {
            return TreeType.CHERRY;
        } else if (feature.is(TreeFeatures.PALE_OAK) || feature.is(TreeFeatures.PALE_OAK_BONEMEAL)) {
            return TreeType.PALE_OAK;
        } else if (feature.is(TreeFeatures.PALE_OAK_CREAKING)) {
            return TreeType.PALE_OAK_CREAKING;
        } else {
            throw new IllegalArgumentException("Unknown tree feature: " + feature);
        }
    }
}
