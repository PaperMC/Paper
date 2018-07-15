package org.bukkit.block.data;

/**
 * 'waterlogged' denotes whether this block has fluid in it.
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
