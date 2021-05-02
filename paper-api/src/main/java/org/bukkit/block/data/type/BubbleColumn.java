package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'drag' indicates whether a force will be applied on entities moving through
 * this block.
 */
public interface BubbleColumn extends BlockData {

    /**
     * Gets the value of the 'drag' property.
     *
     * @return the 'drag' value
     */
    boolean isDrag();

    /**
     * Sets the value of the 'drag' property.
     *
     * @param drag the new 'drag' value
     */
    void setDrag(boolean drag);

}
