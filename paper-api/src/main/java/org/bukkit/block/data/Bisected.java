package org.bukkit.block.data;

import org.jetbrains.annotations.NotNull;

/**
 * 'half' denotes which half of a two block tall material this block is.
 * <br>
 * In game it may be referred to as either (top, bottom) or (upper, lower).
 */
public interface Bisected extends BlockData {

    /**
     * Gets the value of the 'half' property.
     *
     * @return the 'half' value
     */
    @NotNull
    Half getHalf();

    /**
     * Sets the value of the 'half' property.
     *
     * @param half the new 'half' value
     */
    void setHalf(@NotNull Half half);

    /**
     * Determines whether this bisected block fits within a single block space.
     * <p>
     * Returns {@code true} for blocks like {@link org.bukkit.Tag#TRAPDOORS trapdoors} and {@link org.bukkit.Tag#STAIRS stairs},
     * which are bisected but occupy only one block in height. Blocks that span two blocks vertically, such as doors or tall plants,
     * return {@code false}.
     * <p>
     * The result influences in-game terminology: single-block bisected types use {@code top}/{@code bottom},
     * while two-block structures use {@code upper}/{@code lower}.
     *
     * @return true if the block fits within a single block space; false otherwise
     */
    default boolean isSingleBlock() {
        return false;
    }

    /**
     * The half of a vertically bisected block.
     */
    public enum Half {
        /**
         * The top half of the block, normally with the higher y coordinate.
         */
        TOP,
        /**
         * The bottom half of the block, normally with the lower y coordinate.
         */
        BOTTOM;
    }
}
