package io.papermc.paper.generator;

import io.papermc.paper.generator.references.BlockTypeIds;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTagsProvider extends TagsProvider<Block> {

    public BlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> parentProvider) {
        super(output, Registries.BLOCK, lookupProvider, parentProvider);
    }

    @Override
    protected BlockItemTagAppender<Block> tag(final TagKey<Block> tag) {
        return new BlockItemTagAppender<>(super.tag(tag)) {
            @Override
            protected ResourceKey<Block> convertElement(final BlockItemId element) {
                return element.block();
            }
        };
    }

    @Override
    protected void addTags(final HolderLookup.Provider registries) {
        new BlockItemTagsProvider(tagId -> net.minecraft.data.tags.BlockItemTagsProvider.wrapForBlocks(this.tag(tagId.block()))).run();
        this.tag(BlockItemTags.PISTONS.block())
            .add(
                BlockTypeIds.MOVING_PISTON,
                BlockTypeIds.PISTON_HEAD
            );
        this.tag(BlockTags.SKULLS)
            .add(
                BlockTypeIds.SKELETON_SKULL,
                BlockTypeIds.SKELETON_WALL_SKULL,
                BlockTypeIds.CREEPER_WALL_HEAD,
                BlockTypeIds.WITHER_SKELETON_SKULL,
                BlockTypeIds.WITHER_SKELETON_WALL_SKULL,
                BlockTypeIds.PIGLIN_WALL_HEAD,
                BlockTypeIds.DRAGON_WALL_HEAD,
                BlockTypeIds.ZOMBIE_HEAD,
                BlockTypeIds.ZOMBIE_WALL_HEAD,
                BlockTypeIds.PLAYER_WALL_HEAD,
                BlockTypeIds.CREEPER_HEAD,
                BlockTypeIds.PLAYER_HEAD,
                BlockTypeIds.PIGLIN_HEAD,
                BlockTypeIds.DRAGON_HEAD
            );
        this.tag(BlockTags.TORCH)
            .add(
                BlockTypeIds.WALL_TORCH,
                BlockTypeIds.TORCH
            );
        this.tag(BlockTags.REDSTONE_TORCH)
            .add(
                BlockTypeIds.REDSTONE_TORCH,
                BlockTypeIds.REDSTONE_WALL_TORCH
            );
        this.tag(BlockTags.SOUL_TORCH)
            .add(
                BlockTypeIds.SOUL_TORCH,
                BlockTypeIds.SOUL_WALL_TORCH
            );
        this.tag(BlockTags.COPPER_TORCH)
            .add(
                BlockTypeIds.COPPER_WALL_TORCH,
                BlockTypeIds.COPPER_TORCH
            );
        this.tag(BlockTags.TORCHES)
            .addTag(BlockTags.TORCH)
            .addTag(BlockTags.REDSTONE_TORCH)
            .addTag(BlockTags.SOUL_TORCH)
            .addTag(BlockTags.COPPER_TORCH);
        this.tag(BlockTags.COLORABLE)
            .addTag(net.minecraft.tags.BlockTags.CONCRETE_POWDERS)
            .addTag(net.minecraft.tags.BlockTags.CANDLE_CAKES);
        this.tag(BlockItemTags.CORAL_FAN.block())
            .add(
                BlockTypeIds.DEAD_FIRE_CORAL_WALL_FAN,
                BlockTypeIds.HORN_CORAL_WALL_FAN,
                BlockTypeIds.DEAD_HORN_CORAL_WALL_FAN,
                BlockTypeIds.BRAIN_CORAL_WALL_FAN,
                BlockTypeIds.TUBE_CORAL_WALL_FAN,
                BlockTypeIds.BUBBLE_CORAL_WALL_FAN,
                BlockTypeIds.DEAD_BRAIN_CORAL_WALL_FAN,
                BlockTypeIds.FIRE_CORAL_WALL_FAN,
                BlockTypeIds.DEAD_BUBBLE_CORAL_WALL_FAN,
                BlockTypeIds.DEAD_TUBE_CORAL_WALL_FAN
            );
        this.tag(BlockItemTags.UNWAXED_COPPER_BLOCKS.block())
            .add(BlockTypeIds.COPPER_WALL_TORCH);
        this.tag(BlockItemTags.UNAFFECTED_COPPER_BLOCKS.block())
            .add(BlockTypeIds.COPPER_WALL_TORCH);
    }
}
