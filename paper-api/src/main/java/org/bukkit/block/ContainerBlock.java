package org.bukkit.block;

import org.bukkit.inventory.Inventory;

/**
 * Indicates a block type that has inventory.
 */
public interface ContainerBlock {

    /**
     * Get the block's inventory.
     *
     * @return The inventory.
     */
    public Inventory getInventory();
}
