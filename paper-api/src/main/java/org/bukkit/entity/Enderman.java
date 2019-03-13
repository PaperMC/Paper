package org.bukkit.entity;

import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Enderman.
 */
public interface Enderman extends Monster {

    /**
     * Gets the id and data of the block that the Enderman is carrying.
     *
     * @return MaterialData containing the id and data of the block
     */
    @NotNull
    public MaterialData getCarriedMaterial();

    /**
     * Sets the id and data of the block that the Enderman is carrying.
     *
     * @param material data to set the carried block to
     */
    public void setCarriedMaterial(@NotNull MaterialData material);

    /**
     * Gets the data of the block that the Enderman is carrying.
     *
     * @return BlockData containing the carried block, or null if none
     */
    @Nullable
    public BlockData getCarriedBlock();

    /**
     * Sets the data of the block that the Enderman is carrying.
     *
     * @param blockData data to set the carried block to, or null to remove
     */
    public void setCarriedBlock(@Nullable BlockData blockData);
}
