package org.bukkit.inventory.meta;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public interface BlockDataMeta extends ItemMeta {

    /**
     * Returns whether the item has block data currently attached to it.
     *
     * @return whether block data is already attached
     */
    boolean hasBlockData();

    /**
     * Returns the currently attached block data for this item or creates a new
     * one if one doesn't exist.
     *
     * The state is a copy, it must be set back (or to another item) with
     * {@link #setBlockData(org.bukkit.block.data.BlockData)}
     *
     * @param material the material we wish to get this data in the context of
     * @return the attached data or new data
     */
    @NotNull
    BlockData getBlockData(@NotNull Material material);

    /**
     * Attaches a copy of the passed block data to the item.
     *
     * @param blockData the block data to attach to the block.
     * @throws IllegalArgumentException if the blockData is null or invalid for
     * this item.
     */
    void setBlockData(@NotNull BlockData blockData);
}
