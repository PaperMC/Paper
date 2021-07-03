package org.bukkit.block;

import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a lectern.
 */
public interface Lectern extends TileState, BlockInventoryHolder {

    /**
     * Get the current lectern page.
     *
     * @return current page
     */
    int getPage();

    /**
     * Set the current lectern page.
     *
     * If the page is greater than the number of pages of the book currently in
     * the inventory, then behavior is undefined.
     *
     * @param page new page
     */
    void setPage(int page);

    /**
     * @return inventory
     * @see Container#getInventory()
     */
    @NotNull
    @Override
    Inventory getInventory();

    /**
     * @return snapshot inventory
     * @see Container#getSnapshotInventory()
     */
    @NotNull
    Inventory getSnapshotInventory();
}
