package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'mode' is the reaction of the block to a redstone pulse or its supply.
 */
public interface TestBlock extends BlockData {

    /**
     * Gets the value of the 'mode' property.
     *
     * @return the 'mode' value
     */
    Mode getMode();

    /**
     * Sets the value of the 'mode' property.
     *
     * @param mode the new 'mode' value
     */
    void setMode(Mode mode);

    enum Mode {
        START,
        LOG,
        FAIL,
        ACCEPT
    }
}
