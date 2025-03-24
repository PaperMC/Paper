package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'tip' indicates whether this block is a tip.
 */
public interface HangingMoss extends BlockData {

    /**
     * Gets the value of the 'tip' property.
     *
     * @return the 'tip' value
     */
    boolean isTip();

    /**
     * Sets the value of the 'tip' property.
     *
     * @param tip the new 'tip' value
     */
    void setTip(boolean tip);
}
