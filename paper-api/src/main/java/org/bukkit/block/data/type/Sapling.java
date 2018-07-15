package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'stage' represents the growth stage of a sapling.
 * <br>
 * When the sapling reaches {@link #getMaximumStage()} it will attempt to grow
 * into a tree as the next stage.
 */
public interface Sapling extends BlockData {

    /**
     * Gets the value of the 'stage' property.
     *
     * @return the 'stage' value
     */
    int getStage();

    /**
     * Sets the value of the 'stage' property.
     *
     * @param stage the new 'stage' value
     */
    void setStage(int stage);

    /**
     * Gets the maximum allowed value of the 'stage' property.
     *
     * @return the maximum 'stage' value
     */
    int getMaximumStage();
}
