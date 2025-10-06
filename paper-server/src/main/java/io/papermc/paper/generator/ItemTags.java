package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public interface ItemTags {

    TagKey<Item> BUCKET = create("bucket");
    TagKey<Item> BEEHIVES = create("beehives");
    TagKey<Item> FISH_BUCKET = create("fish_bucket");
    TagKey<Item> COBBLESTONE_WALL = create("cobblestone_wall");
    TagKey<Item> COBBLESTONE = create("cobblestone");
    TagKey<Item> CONCRETE = create("concrete");
    TagKey<Item> CONCRETE_POWDER = create("concrete_powder");
    TagKey<Item> DYE = create("dye");
    TagKey<Item> ICE = create("ice");
    TagKey<Item> SNOW = create("snow");
    TagKey<Item> GLASS = create("glass");
    TagKey<Item> GLASS_PANE = create("glass_pane");
    TagKey<Item> GLAZED_TERRACOTTA = create("glazed_terracotta");
    TagKey<Item> STAINED_TERRACOTTA = create("stained_terracotta");
    TagKey<Item> TERRACOTTA = create("terracotta");
    TagKey<Item> GOLDEN_APPLE = create("golden_apple");
    TagKey<Item> HORSE_ARMORS = create("horse_armors");
    TagKey<Item> INFESTED_BLOCKS = create("infested_blocks");
    TagKey<Item> MUSHROOM_BLOCKS = create("mushroom_blocks");
    TagKey<Item> MUSHROOMS = create("mushrooms");
    TagKey<Item> MUSIC_DISCS = create("music_discs");
    TagKey<Item> PISTONS = create("pistons");
    TagKey<Item> POTATO = create("potato");
    TagKey<Item> PRISMARINE = create("prismarine");
    TagKey<Item> PRISMARINE_SLABS = create("prismarine_slabs");
    TagKey<Item> PRISMARINE_STAIRS = create("prismarine_stairs");
    TagKey<Item> PUMPKIN = create("pumpkin");
    TagKey<Item> QUARTZ_BLOCKS = create("quartz_blocks");
    TagKey<Item> COOKED_FISH = create("cooked_fish");
    TagKey<Item> RAW_FISH = create("raw_fish");
    TagKey<Item> RED_SANDSTONES = create("red_sandstones");
    TagKey<Item> SANDSTONES = create("sandstones");
    TagKey<Item> SPONGE = create("sponge");
    TagKey<Item> SPAWN_EGGS = create("spawn_eggs");
    TagKey<Item> STAINED_GLASS = create("stained_glass");
    TagKey<Item> STAINED_GLASS_PANE = create("stained_glass_pane");
    TagKey<Item> PURPUR = create("purpur");
    TagKey<Item> TORCHES = create("torches");
    TagKey<Item> CAMPFIRES = create("campfires");
    TagKey<Item> ARMOR = create("armor");
    TagKey<Item> BOWS = create("bows");
    TagKey<Item> THROWABLE_PROJECTILES = create("throwable_projectiles");
    TagKey<Item> COLORABLE = create("colorable");
    TagKey<Item> CORAL = create("coral");
    TagKey<Item> CORAL_FAN = create("coral_fan");
    TagKey<Item> CORAL_BLOCKS = create("coral_blocks");
    TagKey<Item> NYLIUM = create("nylium");
    TagKey<Item> RAW_ORES = create("raw_ores");
    TagKey<Item> COMMAND_BLOCKS = create("command_blocks");
    TagKey<Item> FURNACES = create("furnaces");
    TagKey<Item> PRESSURE_PLATES = create("pressure_plates");
    TagKey<Item> STONE_PRESSURE_PLATES = create("stone_pressure_plates");
    TagKey<Item> ORES = create("ores");
    TagKey<Item> DEEPSLATE_ORES = create("deepslate_ores");
    TagKey<Item> RAW_ORE_BLOCKS = create("raw_ore_blocks");
    TagKey<Item> OXIDIZED_COPPER_BLOCKS = create("oxidized_copper_blocks");
    TagKey<Item> WEATHERED_COPPER_BLOCKS = create("weathered_copper_blocks");
    TagKey<Item> EXPOSED_COPPER_BLOCKS = create("exposed_copper_blocks");
    TagKey<Item> UNAFFECTED_COPPER_BLOCKS = create("unaffected_copper_blocks");
    TagKey<Item> WAXED_COPPER_BLOCKS = create("waxed_copper_blocks");
    TagKey<Item> UNWAXED_COPPER_BLOCKS = create("unwaxed_copper_blocks");
    TagKey<Item> COPPER_BLOCKS = create("copper_blocks");
    TagKey<Item> FULL_COPPER_BLOCKS = create("full_copper_blocks");
    TagKey<Item> CUT_COPPER_BLOCKS = create("cut_copper_blocks");
    TagKey<Item> CUT_COPPER_STAIRS = create("cut_copper_stairs");
    TagKey<Item> CUT_COPPER_SLABS = create("cut_copper_slabs");
    TagKey<Item> WOODEN_TOOLS = create("wooden_tools");
    TagKey<Item> STONE_TOOLS = create("stone_tools");
    TagKey<Item> COPPER_TOOLS = create("copper_tools");
    TagKey<Item> IRON_TOOLS = create("iron_tools");
    TagKey<Item> GOLDEN_TOOLS = create("golden_tools");
    TagKey<Item> DIAMOND_TOOLS = create("diamond_tools");
    TagKey<Item> NETHERITE_TOOLS = create("netherite_tools");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name));
    }
}
