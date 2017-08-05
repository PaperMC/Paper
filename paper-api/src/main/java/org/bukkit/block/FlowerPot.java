package org.bukkit.block;

import org.bukkit.material.MaterialData;

/**
 * Represents a captured state of a flower pot.
 */
public interface FlowerPot extends BlockState {

    /**
     * Gets the item present in this flower pot.
     *
     * @return item present, or null for empty.
     */
    MaterialData getContents();

    /**
     * Sets the item present in this flower pot.
     *
     * NOTE: The Vanilla Minecraft client will currently not refresh this until
     * a block update is triggered.
     *
     * @param item new item, or null for empty.
     */
    void setContents(MaterialData item);
}
