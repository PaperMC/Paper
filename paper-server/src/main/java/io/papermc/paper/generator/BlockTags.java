package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public interface BlockTags {

    TagKey<Block> COBBLESTONE_WALL = create("cobblestone_wall");
    TagKey<Block> COBBLESTONE = create("cobblestone");
    TagKey<Block> CONCRETE = create("concrete");
    TagKey<Block> GLASS = create("glass");
    TagKey<Block> GLASS_PANE = create("glass_pane");
    TagKey<Block> GLAZED_TERRACOTTA = create("glazed_terracotta");
    TagKey<Block> STAINED_TERRACOTTA = create("stained_terracotta");
    TagKey<Block> TERRACOTTA = create("terracotta");
    TagKey<Block> INFESTED_BLOCKS = create("infested_blocks");
    TagKey<Block> MUSHROOM_BLOCKS = create("mushroom_blocks");
    TagKey<Block> MUSHROOMS = create("mushrooms");
    TagKey<Block> PISTONS = create("pistons");
    TagKey<Block> PRISMARINE = create("prismarine");
    TagKey<Block> PRISMARINE_SLABS = create("prismarine_slabs");
    TagKey<Block> PRISMARINE_STAIRS = create("prismarine_stairs");
    TagKey<Block> PUMPKIN = create("pumpkin");
    TagKey<Block> QUARTZ_BLOCKS = create("quartz_blocks");
    TagKey<Block> RED_SANDSTONES = create("red_sandstones");
    TagKey<Block> SANDSTONES = create("sandstones");
    TagKey<Block> SPONGE = create("sponge");
    TagKey<Block> SKULLS = create("skulls");
    TagKey<Block> STAINED_GLASS = create("stained_glass");
    TagKey<Block> STAINED_GLASS_PANE = create("stained_glass_pane");
    TagKey<Block> PURPUR = create("purpur");
    TagKey<Block> TORCH = create("torch");
    TagKey<Block> REDSTONE_TORCH = create("redstone_torch");
    TagKey<Block> SOUL_TORCH = create("soul_torch");
    TagKey<Block> COPPER_TORCH = create("copper_torch");
    TagKey<Block> TORCHES = create("torches");
    TagKey<Block> COLORABLE = create("colorable");
    TagKey<Block> CORAL = create("coral");
    TagKey<Block> CORAL_FAN = create("coral_fan");
    TagKey<Block> CORAL_BLOCKS = create("coral_blocks");
    TagKey<Block> COMMAND_BLOCKS = create("command_blocks");
    TagKey<Block> FURNACES = create("furnaces");
    TagKey<Block> ORES = create("ores");
    TagKey<Block> DEEPSLATE_ORES = create("deepslate_ores");
    TagKey<Block> RAW_ORE_BLOCKS = create("raw_ore_blocks");
    TagKey<Block> OXIDIZED_COPPER_BLOCKS = create("oxidized_copper_blocks");
    TagKey<Block> WEATHERED_COPPER_BLOCKS = create("weathered_copper_blocks");
    TagKey<Block> EXPOSED_COPPER_BLOCKS = create("exposed_copper_blocks");
    TagKey<Block> UNAFFECTED_COPPER_BLOCKS = create("unaffected_copper_blocks");
    TagKey<Block> WAXED_COPPER_BLOCKS = create("waxed_copper_blocks");
    TagKey<Block> UNWAXED_COPPER_BLOCKS = create("unwaxed_copper_blocks");
    TagKey<Block> COPPER_BLOCKS = create("copper_blocks");
    TagKey<Block> FULL_COPPER_BLOCKS = create("full_copper_blocks");
    TagKey<Block> CUT_COPPER_BLOCKS = create("cut_copper_blocks");
    TagKey<Block> CUT_COPPER_STAIRS = create("cut_copper_stairs");
    TagKey<Block> CUT_COPPER_SLABS = create("cut_copper_slabs");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name));
    }
}
