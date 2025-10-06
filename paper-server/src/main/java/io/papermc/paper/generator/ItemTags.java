package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ItemTags extends BlockItemTags {

    public static final TagKey<Item> BUCKET = create("bucket");
    public static final TagKey<Item> BEEHIVES = create("beehives");
    public static final TagKey<Item> FISH_BUCKET = create("fish_bucket");
    public static final TagKey<Item> ICE = create("ice");
    public static final TagKey<Item> SNOW = create("snow");
    public static final TagKey<Item> GOLDEN_APPLE = create("golden_apple");
    public static final TagKey<Item> HORSE_ARMORS = create("horse_armors");
    public static final TagKey<Item> MUSIC_DISCS = create("music_discs");
    public static final TagKey<Item> POTATO = create("potato");
    public static final TagKey<Item> COOKED_FISH = create("cooked_fish");
    public static final TagKey<Item> RAW_FISH = create("raw_fish");
    public static final TagKey<Item> SPAWN_EGGS = create("spawn_eggs");
    public static final TagKey<Item> TORCHES = create("torches");
    public static final TagKey<Item> CAMPFIRES = create("campfires");
    public static final TagKey<Item> ARMOR = create("armor");
    public static final TagKey<Item> BOWS = create("bows");
    public static final TagKey<Item> THROWABLE_PROJECTILES = create("throwable_projectiles");
    public static final TagKey<Item> COLORABLE = create("colorable");
    public static final TagKey<Item> NYLIUM = create("nylium");
    public static final TagKey<Item> RAW_ORES = create("raw_ores");
    public static final TagKey<Item> PRESSURE_PLATES = create("pressure_plates");
    public static final TagKey<Item> STONE_PRESSURE_PLATES = create("stone_pressure_plates");
    public static final TagKey<Item> WOODEN_TOOLS = create("wooden_tools");
    public static final TagKey<Item> STONE_TOOLS = create("stone_tools");
    public static final TagKey<Item> COPPER_TOOLS = create("copper_tools");
    public static final TagKey<Item> IRON_TOOLS = create("iron_tools");
    public static final TagKey<Item> GOLDEN_TOOLS = create("golden_tools");
    public static final TagKey<Item> DIAMOND_TOOLS = create("diamond_tools");
    public static final TagKey<Item> NETHERITE_TOOLS = create("netherite_tools");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, name));
    }
}
