package org.bukkit.entity;

import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;

/**
 * Represents an Enderman.
 */
public interface Enderman extends Monster {

    /**
     * Gets the id and data of the block that the Enderman is carrying.
     *
     * @return MaterialData containing the id and data of the block
     */
    public MaterialData getCarriedMaterial();

    /**
     * Sets the id and data of the block that the Enderman is carrying.
     *
     * @param material data to set the carried block to
     */
    public void setCarriedMaterial(MaterialData material);

    /**
     * Gets the data of the block that the Enderman is carrying.
     *
     * @return BlockData containing the carried block
     */
    public BlockData getCarriedBlock();

    /**
     * Sets the data of the block that the Enderman is carrying.
     *
     * @param blockData data to set the carried block to
     */
    public void setCarriedBlock(BlockData blockData);
}
