package io.papermc.paper.generator;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public class BlockItemTags {

    public static final BlockItemTagId COBBLESTONE_WALL = create("cobblestone_wall");
    public static final BlockItemTagId COBBLESTONE = create("cobblestone");
    public static final BlockItemTagId GLASS = create("glass");
    public static final BlockItemTagId GLASS_PANE = create("glass_pane");
    public static final BlockItemTagId STAINED_TERRACOTTA = create("stained_terracotta");
    public static final BlockItemTagId TERRACOTTA = create("terracotta");
    public static final BlockItemTagId INFESTED_BLOCKS = create("infested_blocks");
    public static final BlockItemTagId MUSHROOM_BLOCKS = create("mushroom_blocks");
    public static final BlockItemTagId MUSHROOMS = create("mushrooms");
    public static final BlockItemTagId PISTONS = create("pistons");
    public static final BlockItemTagId PRISMARINE = create("prismarine");
    public static final BlockItemTagId PRISMARINE_SLABS = create("prismarine_slabs");
    public static final BlockItemTagId PRISMARINE_STAIRS = create("prismarine_stairs");
    public static final BlockItemTagId PUMPKIN = create("pumpkin");
    public static final BlockItemTagId QUARTZ_BLOCKS = create("quartz_blocks");
    public static final BlockItemTagId RED_SANDSTONES = create("red_sandstones");
    public static final BlockItemTagId SANDSTONES = create("sandstones");
    public static final BlockItemTagId SPONGE = create("sponge");
    public static final BlockItemTagId STAINED_GLASS = create("stained_glass");
    public static final BlockItemTagId STAINED_GLASS_PANE = create("stained_glass_pane");
    public static final BlockItemTagId PURPUR = create("purpur");
    public static final BlockItemTagId COLORABLE = create("colorable");
    public static final BlockItemTagId CORAL = create("coral");
    public static final BlockItemTagId CORAL_FAN = create("coral_fan");
    public static final BlockItemTagId CORAL_BLOCKS = create("coral_blocks");
    public static final BlockItemTagId COMMAND_BLOCKS = create("command_blocks");
    public static final BlockItemTagId FURNACES = create("furnaces");
    public static final BlockItemTagId ORES = create("ores");
    public static final BlockItemTagId DEEPSLATE_ORES = create("deepslate_ores");
    public static final BlockItemTagId RAW_ORE_BLOCKS = create("raw_ore_blocks");
    public static final BlockItemTagId OXIDIZED_COPPER_BLOCKS = create("oxidized_copper_blocks");
    public static final BlockItemTagId WEATHERED_COPPER_BLOCKS = create("weathered_copper_blocks");
    public static final BlockItemTagId EXPOSED_COPPER_BLOCKS = create("exposed_copper_blocks");
    public static final BlockItemTagId UNAFFECTED_COPPER_BLOCKS = create("unaffected_copper_blocks");
    public static final BlockItemTagId WAXED_COPPER_BLOCKS = create("waxed_copper_blocks");
    public static final BlockItemTagId UNWAXED_COPPER_BLOCKS = create("unwaxed_copper_blocks");
    public static final BlockItemTagId COPPER_BLOCKS = create("copper_blocks");
    public static final BlockItemTagId FULL_COPPER_BLOCKS = create("full_copper_blocks");
    public static final BlockItemTagId CUT_COPPER_BLOCKS = create("cut_copper_blocks");
    public static final BlockItemTagId CUT_COPPER_STAIRS = create("cut_copper_stairs");
    public static final BlockItemTagId CUT_COPPER_SLABS = create("cut_copper_slabs");

    private static BlockItemTagId create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name);
        return BlockItemTagId.create(id, id);
    }
}
