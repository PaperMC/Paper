package org.bukkit.block.data;

/**
 * 'waterlogged' denotes whether this block has fluid in it.
 *
 * @since 1.13
 */
public interface Waterlogged extends BlockData {

    /**
     * Gets the value of the 'waterlogged' property.
     *
     * @return the 'waterlogged' value
     */
    boolean isWaterlogged();

    /**
     * Sets the value of the 'waterlogged' property.
     *
     * @param waterlogged the new 'waterlogged' value
     */
    void setWaterlogged(boolean waterlogged);
}
