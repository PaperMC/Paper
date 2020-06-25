package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'charges' represents the amount of times the anchor may still be used.
 */
public interface RespawnAnchor extends BlockData {

    /**
     * Gets the value of the 'charges' property.
     *
     * @return the 'charges' value
     */
    int getCharges();

    /**
     * Sets the value of the 'charges' property.
     *
     * @param charges the new 'charges' value
     */
    void setCharges(int charges);

    /**
     * Gets the maximum allowed value of the 'charges' property.
     *
     * @return the maximum 'charges' value
     */
    int getMaximumCharges();
}
