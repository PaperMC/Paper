package io.papermc.paper.registry.keys.tags;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockType;

import static net.kyori.adventure.key.Key.key;

/**
 * Tag keys for {@link RegistryKey#BLOCK}
 * provided by the Paper data pack.
 */
public class PaperBlockTypeTagKeys {

    /**
     * Covers both cobblestone wall variants.
     */
    public static final TagKey<BlockType> COBBLESTONE_WALL = create(key("paper:cobblestone_wall"));

    /**
     * Covers both cobblestone and mossy cobblestone.
     */
    public static final TagKey<BlockType> COBBLESTONE = create(key("paper:cobblestone"));

    /**
     * Covers all colors of concrete.
     */
    public static final TagKey<BlockType> CONCRETE = create(key("paper:concrete"));

    /**
     * Covers the non-colored glass and stained-glasses (not panes).
     */
    public static final TagKey<BlockType> GLASS = create(key("paper:glass"));

    /**
     * Covers the non-colored glass panes and stained-glass panes (panes only).
     */
    public static final TagKey<BlockType> GLASS_PANE = create(key("paper:glass_pane"));

    /**
     * Covers all glazed terracotta blocks.
     */
    public static final TagKey<BlockType> GLAZED_TERRACOTTA = create(key("paper:glazed_terracotta"));

    /**
     * Covers the stained terracotta.
     */
    public static final TagKey<BlockType> STAINED_TERRACOTTA = create(key("paper:stained_terracotta"));

    /**
     * Covers terracotta along with the stained variants.
     */
    public static final TagKey<BlockType> TERRACOTTA = create(key("paper:terracotta"));

    /**
     * Covers the variants of infested blocks.
     */
    public static final TagKey<BlockType> INFESTED_BLOCKS = create(key("paper:infested_blocks"));

    /**
     * Covers the variants of mushroom blocks.
     */
    public static final TagKey<BlockType> MUSHROOM_BLOCKS = create(key("paper:mushroom_blocks"));

    /**
     * Covers all mushrooms.
     */
    public static final TagKey<BlockType> MUSHROOMS = create(key("paper:mushrooms"));

    /**
     * Covers all piston typed blocks including the piston head and moving piston.
     */
    public static final TagKey<BlockType> PISTONS = create(key("paper:pistons"));

    /**
     * Covers the variants of prismarine blocks.
     */
    public static final TagKey<BlockType> PRISMARINE = create(key("paper:prismarine"));

    /**
     * Covers the variants of prismarine slabs.
     */
    public static final TagKey<BlockType> PRISMARINE_SLABS = create(key("paper:prismarine_slabs"));

    /**
     * Covers the variants of prismarine stairs.
     */
    public static final TagKey<BlockType> PRISMARINE_STAIRS = create(key("paper:prismarine_stairs"));

    /**
     * Covers the variants of pumpkins.
     */
    public static final TagKey<BlockType> PUMPKIN = create(key("paper:pumpkin"));

    /**
     * Covers the variants of quartz blocks.
     */
    public static final TagKey<BlockType> QUARTZ_BLOCKS = create(key("paper:quartz_blocks"));

    /**
     * Covers the variants of red sandstone blocks.
     */
    public static final TagKey<BlockType> RED_SANDSTONES = create(key("paper:red_sandstones"));

    /**
     * Covers the variants of sandstone blocks.
     */
    public static final TagKey<BlockType> SANDSTONES = create(key("paper:sandstones"));

    /**
     * Covers sponge and wet sponge.
     */
    public static final TagKey<BlockType> SPONGE = create(key("paper:sponge"));

    /**
     * Covers skull and heads.
     */
    public static final TagKey<BlockType> SKULLS = create(key("paper:skulls"));

    /**
     * Covers all colors of stained-glass.
     */
    public static final TagKey<BlockType> STAINED_GLASS = create(key("paper:stained_glass"));

    /**
     * Covers all colors of stained-glass panes.
     */
    public static final TagKey<BlockType> STAINED_GLASS_PANE = create(key("paper:stained_glass_pane"));

    /**
     * Covers the variants of purpur.
     */
    public static final TagKey<BlockType> PURPUR = create(key("paper:purpur"));

    /**
     * Covers the variants of a regular torch.
     */
    public static final TagKey<BlockType> TORCH = create(key("paper:torch"));

    /**
     * Covers the variants of a redstone torch.
     */
    public static final TagKey<BlockType> REDSTONE_TORCH = create(key("paper:redstone_torch"));

    /**
     * Covers the variants of a soul torch.
     */
    public static final TagKey<BlockType> SOUL_TORCH = create(key("paper:soul_torch"));

    /**
     * Covers the variants of a copper torch.
     */
    public static final TagKey<BlockType> COPPER_TORCH = create(key("paper:copper_torch"));

    /**
     * Covers the variants of torches.
     */
    public static final TagKey<BlockType> TORCHES = create(key("paper:torches"));

    /**
     * Covers blocks that can be colored, such as wool, shulker boxes, stained glasses etc.
     */
    public static final TagKey<BlockType> COLORABLE = create(key("paper:colorable"));

    /**
     * Covers the variants of coral.
     */
    public static final TagKey<BlockType> CORAL = create(key("paper:coral"));

    /**
     * Covers the variants of coral fans.
     */
    public static final TagKey<BlockType> CORAL_FAN = create(key("paper:coral_fan"));

    /**
     * Covers the variants of coral blocks.
     */
    public static final TagKey<BlockType> CORAL_BLOCKS = create(key("paper:coral_blocks"));

    /**
     * Covers all command block types.
     */
    public static final TagKey<BlockType> COMMAND_BLOCKS = create(key("paper:command_blocks"));

    /**
     * Covers all furnace types.
     */
    public static final TagKey<BlockType> FURNACES = create(key("paper:furnaces"));

    /**
     * Covers all ores.
     */
    public static final TagKey<BlockType> ORES = create(key("paper:ores"));

    /**
     * Covers the variants of deepslate ores.
     */
    public static final TagKey<BlockType> DEEPSLATE_ORES = create(key("paper:deepslate_ores"));

    /**
     * Covers the variants of raw ore blocks.
     */
    public static final TagKey<BlockType> RAW_ORE_BLOCKS = create(key("paper:raw_ore_blocks"));

    /**
     * Covers all oxidized copper blocks.
     */
    public static final TagKey<BlockType> OXIDIZED_COPPER_BLOCKS = create(key("paper:oxidized_copper_blocks"));

    /**
     * Covers all weathered copper blocks.
     */
    public static final TagKey<BlockType> WEATHERED_COPPER_BLOCKS = create(key("paper:weathered_copper_blocks"));

    /**
     * Covers all exposed copper blocks.
     */
    public static final TagKey<BlockType> EXPOSED_COPPER_BLOCKS = create(key("paper:exposed_copper_blocks"));

    /**
     * Covers all un-weathered copper blocks.
     */
    public static final TagKey<BlockType> UNAFFECTED_COPPER_BLOCKS = create(key("paper:unaffected_copper_blocks"));

    /**
     * Covers all waxed copper blocks.
     * <p>
     * Combine with other copper-related tags to filter is-waxed or not.
     */
    public static final TagKey<BlockType> WAXED_COPPER_BLOCKS = create(key("paper:waxed_copper_blocks"));

    /**
     * Covers all un-waxed copper blocks.
     * <p>
     * Combine with other copper-related tags to filter is-un-waxed or not.
     */
    public static final TagKey<BlockType> UNWAXED_COPPER_BLOCKS = create(key("paper:unwaxed_copper_blocks"));

    /**
     * Covers all copper block variants.
     */
    public static final TagKey<BlockType> COPPER_BLOCKS = create(key("paper:copper_blocks"));

    /**
     * Covers all weathering/waxed states of the plain copper block.
     */
    public static final TagKey<BlockType> FULL_COPPER_BLOCKS = create(key("paper:full_copper_blocks"));

    /**
     * Covers all weathering/waxed states of the cut copper block.
     */
    public static final TagKey<BlockType> CUT_COPPER_BLOCKS = create(key("paper:cut_copper_blocks"));

    /**
     * Covers all weathering/waxed states of the cut copper stairs.
     */
    public static final TagKey<BlockType> CUT_COPPER_STAIRS = create(key("paper:cut_copper_stairs"));

    /**
     * Covers all weathering/waxed states of the cut copper slab.
     */
    public static final TagKey<BlockType> CUT_COPPER_SLABS = create(key("paper:cut_copper_slabs"));

    private PaperBlockTypeTagKeys() {
    }

    private static TagKey<BlockType> create(final Key key) {
        return BlockTypeTagKeys.create(key);
    }
}
