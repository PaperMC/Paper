package org.bukkit.block;

import org.bukkit.inventory.Inventory;

/**
 * Represents a chest.
 */
public interface Chest extends BlockState, ContainerBlock {
    /**
     * Returns the chest's inventory. If this is a double chest, it returns just
     * the portion of the inventory linked to this half of the chest.
     *
     * @return The inventory.
     */
    Inventory getBlockInventory();
}
