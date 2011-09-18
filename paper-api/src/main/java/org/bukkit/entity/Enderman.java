package org.bukkit.entity;

import org.bukkit.material.MaterialData;

/**
 * Represents an Enderman.
 */
public interface Enderman extends Monster {

    /**
     * Get the id and data of the block that the Enderman is carrying.
     *
     * @return MaterialData containing the id and data of the block
     */
    public MaterialData getCarriedMaterial();

    /**
     * Set the id and data of the block that the Enderman is carring.
     *
     * @param material data to set the carried block to
     */
    public void setCarriedMaterial(MaterialData material);
}
