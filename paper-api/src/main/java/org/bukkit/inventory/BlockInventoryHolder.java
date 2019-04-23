package org.bukkit.inventory;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a block inventory holder - either a BlockState, or a regular
 * Block.
 */
public interface BlockInventoryHolder extends InventoryHolder {

    /**
     * Gets the block associated with this holder.
     *
     * @return the block associated with this holder
     * @throws IllegalStateException if the holder is a block state and is not
     * placed
     */
    @NotNull
    Block getBlock();
}
