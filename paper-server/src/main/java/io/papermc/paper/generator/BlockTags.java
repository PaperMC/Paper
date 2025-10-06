package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class BlockTags extends BlockItemTags {

    public static final TagKey<Block> SKULLS = create("skulls");
    public static final TagKey<Block> TORCH = create("torch");
    public static final TagKey<Block> REDSTONE_TORCH = create("redstone_torch");
    public static final TagKey<Block> SOUL_TORCH = create("soul_torch");
    public static final TagKey<Block> COPPER_TORCH = create("copper_torch");
    public static final TagKey<Block> TORCHES = create("torches");
    public static final TagKey<Block> COLORABLE = create("colorable");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name));
    }
}
