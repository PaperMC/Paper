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
