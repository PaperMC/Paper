package org.bukkit.inventory;

import org.bukkit.block.DoubleChest;

/**
 * Interface to the inventory of a Double Chest.
 */
public interface DoubleChestInventory extends Inventory {

    /**
     * Get the left half of this double chest.
     *
     * @return The left side inventory
     */
    Inventory getLeftSide();

    /**
     * Get the right side of this double chest.
     *
     * @return The right side inventory
     */
    Inventory getRightSide();

    DoubleChest getHolder();
}
