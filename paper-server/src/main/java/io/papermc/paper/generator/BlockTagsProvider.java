package io.papermc.paper.generator;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public BlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> parentProvider) {
        super(output, Registries.BLOCK, lookupProvider, parentProvider, block -> block.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        (new BlockItemTagsProvider() {
            @Override
            protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
                return BlockTagsProvider.this.tag(blockTag);
            }
        }).run();
        this.tag(BlockTags.PISTONS)
            .add(
                Blocks.MOVING_PISTON,
                Blocks.PISTON_HEAD
            );
        this.tag(BlockTags.SKULLS)
            .add(
                Blocks.SKELETON_SKULL,
                Blocks.SKELETON_WALL_SKULL,
                Blocks.CREEPER_WALL_HEAD,
                Blocks.WITHER_SKELETON_SKULL,
                Blocks.WITHER_SKELETON_WALL_SKULL,
                Blocks.PIGLIN_WALL_HEAD,
                Blocks.DRAGON_WALL_HEAD,
                Blocks.ZOMBIE_HEAD,
                Blocks.ZOMBIE_WALL_HEAD,
                Blocks.PLAYER_WALL_HEAD,
                Blocks.CREEPER_HEAD,
                Blocks.PLAYER_HEAD,
                Blocks.PIGLIN_HEAD,
                Blocks.DRAGON_HEAD
            );
        this.tag(BlockTags.TORCH)
            .add(
                Blocks.WALL_TORCH,
                Blocks.TORCH
            );
        this.tag(BlockTags.REDSTONE_TORCH)
            .add(
                Blocks.REDSTONE_TORCH,
                Blocks.REDSTONE_WALL_TORCH
            );
        this.tag(BlockTags.SOUL_TORCH)
            .add(
                Blocks.SOUL_TORCH,
                Blocks.SOUL_WALL_TORCH
            );
        this.tag(BlockTags.COPPER_TORCH)
            .add(
                Blocks.COPPER_WALL_TORCH,
                Blocks.COPPER_TORCH
            );
        this.tag(BlockTags.TORCHES)
            .addTag(BlockTags.TORCH)
            .addTag(BlockTags.REDSTONE_TORCH)
            .addTag(BlockTags.SOUL_TORCH)
            .addTag(BlockTags.COPPER_TORCH);
        this.tag(BlockTags.COLORABLE)
            .addTag(net.minecraft.tags.BlockTags.CONCRETE_POWDER)
            .addTag(net.minecraft.tags.BlockTags.CANDLE_CAKES);
        this.tag(BlockTags.CORAL_FAN)
            .add(
                Blocks.DEAD_FIRE_CORAL_WALL_FAN,
                Blocks.HORN_CORAL_WALL_FAN,
                Blocks.DEAD_HORN_CORAL_WALL_FAN,
                Blocks.BRAIN_CORAL_WALL_FAN,
                Blocks.TUBE_CORAL_WALL_FAN,
                Blocks.BUBBLE_CORAL_WALL_FAN,
                Blocks.DEAD_BRAIN_CORAL_WALL_FAN,
                Blocks.FIRE_CORAL_WALL_FAN,
                Blocks.DEAD_BUBBLE_CORAL_WALL_FAN,
                Blocks.DEAD_TUBE_CORAL_WALL_FAN
            );
    }
}
