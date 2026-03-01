package io.papermc.paper.registry.keys.tags;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemType;

import static net.kyori.adventure.key.Key.key;

/**
 * Tag keys for {@link RegistryKey#ITEM}
 * provided by the Paper data pack.
 */
public class PaperItemTypeTagKeys {

    /**
     * Covers all bucket items.
     */
    public static final TagKey<ItemType> BUCKET = create(key("paper:bucket"));

    /**
     * Covers all beehive items.
     */
    public static final TagKey<ItemType> BEEHIVES = create(key("paper:beehives"));

    /**
     * Covers all variants of fish buckets.
     */
    public static final TagKey<ItemType> FISH_BUCKET = create(key("paper:fish_bucket"));

    /**
     * Covers both cobblestone wall variants.
     */
    public static final TagKey<ItemType> COBBLESTONE_WALL = create(key("paper:cobblestone_wall"));

    /**
     * Covers both cobblestone and mossy cobblestone.
     */
    public static final TagKey<ItemType> COBBLESTONE = create(key("paper:cobblestone"));

    /**
     * Covers all colors of concrete.
     */
    public static final TagKey<ItemType> CONCRETE = create(key("paper:concrete"));

    /**
     * Covers all colors of concrete powder.
     */
    public static final TagKey<ItemType> CONCRETE_POWDER = create(key("paper:concrete_powder"));

    /**
     * Covers all dyes.
     */
    public static final TagKey<ItemType> DYE = create(key("paper:dye"));

    /**
     * Covers all ice variants.
     */
    public static final TagKey<ItemType> ICE = create(key("paper:ice"));

    /**
     * Covers snow layer and snow block.
     */
    public static final TagKey<ItemType> SNOW = create(key("paper:snow"));

    /**
     * Covers the non-colored glass and stained-glasses (not panes).
     */
    public static final TagKey<ItemType> GLASS = create(key("paper:glass"));

    /**
     * Covers the non-colored glass panes and stained-glass panes (panes only).
     */
    public static final TagKey<ItemType> GLASS_PANE = create(key("paper:glass_pane"));

    /**
     * Covers all glazed terracotta blocks.
     */
    public static final TagKey<ItemType> GLAZED_TERRACOTTA = create(key("paper:glazed_terracotta"));

    /**
     * Covers the stained terracotta.
     */
    public static final TagKey<ItemType> STAINED_TERRACOTTA = create(key("paper:stained_terracotta"));

    /**
     * Covers terracotta along with the stained variants.
     */
    public static final TagKey<ItemType> TERRACOTTA = create(key("paper:terracotta"));

    /**
     * Covers both golden apples.
     */
    public static final TagKey<ItemType> GOLDEN_APPLE = create(key("paper:golden_apple"));

    /**
     * Covers the variants of horse armor.
     */
    public static final TagKey<ItemType> HORSE_ARMORS = create(key("paper:horse_armors"));

    /**
     * Covers the variants of infested blocks.
     */
    public static final TagKey<ItemType> INFESTED_BLOCKS = create(key("paper:infested_blocks"));

    /**
     * Covers the variants of mushroom blocks.
     */
    public static final TagKey<ItemType> MUSHROOM_BLOCKS = create(key("paper:mushroom_blocks"));

    /**
     * Covers all mushrooms.
     */
    public static final TagKey<ItemType> MUSHROOMS = create(key("paper:mushrooms"));

    /**
     * Covers all music disc items.
     */
    public static final TagKey<ItemType> MUSIC_DISCS = create(key("paper:music_discs"));

    /**
     * Covers all piston typed items including the piston head and moving piston.
     */
    public static final TagKey<ItemType> PISTONS = create(key("paper:pistons"));

    /**
     * Covers all potato items.
     */
    public static final TagKey<ItemType> POTATO = create(key("paper:potato"));

    /**
     * Covers the variants of prismarine blocks.
     */
    public static final TagKey<ItemType> PRISMARINE = create(key("paper:prismarine"));

    /**
     * Covers the variants of prismarine slabs.
     */
    public static final TagKey<ItemType> PRISMARINE_SLABS = create(key("paper:prismarine_slabs"));

    /**
     * Covers the variants of prismarine stairs.
     */
    public static final TagKey<ItemType> PRISMARINE_STAIRS = create(key("paper:prismarine_stairs"));

    /**
     * Covers the variants of pumpkins.
     */
    public static final TagKey<ItemType> PUMPKIN = create(key("paper:pumpkin"));

    /**
     * Covers the variants of quartz blocks.
     */
    public static final TagKey<ItemType> QUARTZ_BLOCKS = create(key("paper:quartz_blocks"));

    /**
     * Covers the two types of cooked fish.
     */
    public static final TagKey<ItemType> COOKED_FISH = create(key("paper:cooked_fish"));

    /**
     * Covers all uncooked fish items.
     */
    public static final TagKey<ItemType> RAW_FISH = create(key("paper:raw_fish"));

    /**
     * Covers the variants of red sandstone blocks.
     */
    public static final TagKey<ItemType> RED_SANDSTONES = create(key("paper:red_sandstones"));

    /**
     * Covers the variants of sandstone blocks.
     */
    public static final TagKey<ItemType> SANDSTONES = create(key("paper:sandstones"));

    /**
     * Covers sponge and wet sponge.
     */
    public static final TagKey<ItemType> SPONGE = create(key("paper:sponge"));

    /**
     * Covers all spawn egg items.
     */
    public static final TagKey<ItemType> SPAWN_EGGS = create(key("paper:spawn_eggs"));

    /**
     * Covers all colors of stained-glass.
     */
    public static final TagKey<ItemType> STAINED_GLASS = create(key("paper:stained_glass"));

    /**
     * Covers all colors of stained-glass panes.
     */
    public static final TagKey<ItemType> STAINED_GLASS_PANE = create(key("paper:stained_glass_pane"));

    /**
     * Covers the variants of purpur.
     */
    public static final TagKey<ItemType> PURPUR = create(key("paper:purpur"));

    /**
     * Covers the variants of torches.
     */
    public static final TagKey<ItemType> TORCHES = create(key("paper:torches"));

    /**
     * Covers the variants of campfires.
     */
    public static final TagKey<ItemType> CAMPFIRES = create(key("paper:campfires"));

    /**
     * Covers all variants of armor.
     */
    public static final TagKey<ItemType> ARMOR = create(key("paper:armor"));

    /**
     * Covers the variants of bows.
     */
    public static final TagKey<ItemType> BOWS = create(key("paper:bows"));

    /**
     * Covers the variants of player-throwable projectiles (not requiring a bow or any other "assistance").
     */
    public static final TagKey<ItemType> THROWABLE_PROJECTILES = create(key("paper:throwable_projectiles"));

    /**
     * Covers blocks that can be colored, such as wool, shulker boxes, stained glasses etc.
     */
    public static final TagKey<ItemType> COLORABLE = create(key("paper:colorable"));

    /**
     * Covers the variants of coral.
     */
    public static final TagKey<ItemType> CORAL = create(key("paper:coral"));

    /**
     * Covers the variants of coral fans.
     */
    public static final TagKey<ItemType> CORAL_FAN = create(key("paper:coral_fan"));

    /**
     * Covers the variants of coral blocks.
     */
    public static final TagKey<ItemType> CORAL_BLOCKS = create(key("paper:coral_blocks"));

    /**
     * Covers the variants of nylium.
     */
    public static final TagKey<ItemType> NYLIUM = create(key("paper:nylium"));

    /**
     * Covers the variants of raw ores.
     */
    public static final TagKey<ItemType> RAW_ORES = create(key("paper:raw_ores"));

    /**
     * Covers all command block types.
     */
    public static final TagKey<ItemType> COMMAND_BLOCKS = create(key("paper:command_blocks"));

    /**
     * Covers all furnace types.
     */
    public static final TagKey<ItemType> FURNACES = create(key("paper:furnaces"));

    /**
     * Covers all pressure plates including weighted, wooden and stone variants.
     */
    public static final TagKey<ItemType> PRESSURE_PLATES = create(key("paper:pressure_plates"));

    /**
     * Covers all stone pressure plates.
     */
    public static final TagKey<ItemType> STONE_PRESSURE_PLATES = create(key("paper:stone_pressure_plates"));

    /**
     * Covers all ores.
     */
    public static final TagKey<ItemType> ORES = create(key("paper:ores"));

    /**
     * Covers the variants of deepslate ores.
     */
    public static final TagKey<ItemType> DEEPSLATE_ORES = create(key("paper:deepslate_ores"));

    /**
     * Covers the variants of raw ore blocks.
     */
    public static final TagKey<ItemType> RAW_ORE_BLOCKS = create(key("paper:raw_ore_blocks"));

    /**
     * Covers all oxidized copper blocks.
     */
    public static final TagKey<ItemType> OXIDIZED_COPPER_BLOCKS = create(key("paper:oxidized_copper_blocks"));

    /**
     * Covers all weathered copper blocks.
     */
    public static final TagKey<ItemType> WEATHERED_COPPER_BLOCKS = create(key("paper:weathered_copper_blocks"));

    /**
     * Covers all exposed copper blocks.
     */
    public static final TagKey<ItemType> EXPOSED_COPPER_BLOCKS = create(key("paper:exposed_copper_blocks"));

    /**
     * Covers all un-weathered copper blocks.
     */
    public static final TagKey<ItemType> UNAFFECTED_COPPER_BLOCKS = create(key("paper:unaffected_copper_blocks"));

    /**
     * Covers all waxed copper blocks.
     * <p>
     * Combine with other copper-related tags to filter is-waxed or not.
     */
    public static final TagKey<ItemType> WAXED_COPPER_BLOCKS = create(key("paper:waxed_copper_blocks"));

    /**
     * Covers all un-waxed copper blocks.
     * <p>
     * Combine with other copper-related tags to filter is-un-waxed or not.
     */
    public static final TagKey<ItemType> UNWAXED_COPPER_BLOCKS = create(key("paper:unwaxed_copper_blocks"));

    /**
     * Covers all copper block variants.
     */
    public static final TagKey<ItemType> COPPER_BLOCKS = create(key("paper:copper_blocks"));

    /**
     * Covers all weathering/waxed states of the plain copper block.
     */
    public static final TagKey<ItemType> FULL_COPPER_BLOCKS = create(key("paper:full_copper_blocks"));

    /**
     * Covers all weathering/waxed states of the cut copper block.
     */
    public static final TagKey<ItemType> CUT_COPPER_BLOCKS = create(key("paper:cut_copper_blocks"));

    /**
     * Covers all weathering/waxed states of the cut copper stairs.
     */
    public static final TagKey<ItemType> CUT_COPPER_STAIRS = create(key("paper:cut_copper_stairs"));

    /**
     * Covers all weathering/waxed states of the cut copper slab.
     */
    public static final TagKey<ItemType> CUT_COPPER_SLABS = create(key("paper:cut_copper_slabs"));

    /**
     * Covers all wooden tools.
     */
    public static final TagKey<ItemType> WOODEN_TOOLS = create(key("paper:wooden_tools"));

    /**
     * Covers all stone tools.
     */
    public static final TagKey<ItemType> STONE_TOOLS = create(key("paper:stone_tools"));

    /**
     * Covers all copper tools.
     */
    public static final TagKey<ItemType> COPPER_TOOLS = create(key("paper:copper_tools"));

    /**
     * Covers all iron tools.
     */
    public static final TagKey<ItemType> IRON_TOOLS = create(key("paper:iron_tools"));

    /**
     * Covers all golden tools.
     */
    public static final TagKey<ItemType> GOLDEN_TOOLS = create(key("paper:golden_tools"));

    /**
     * Covers all diamond tools.
     */
    public static final TagKey<ItemType> DIAMOND_TOOLS = create(key("paper:diamond_tools"));

    /**
     * Covers all netherite tools.
     */
    public static final TagKey<ItemType> NETHERITE_TOOLS = create(key("paper:netherite_tools"));

    private PaperItemTypeTagKeys() {
    }

    private static TagKey<ItemType> create(final Key key) {
        return ItemTypeTagKeys.create(key);
    }
}
