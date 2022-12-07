package org.bukkit.block;

import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a chiseled bookshelf.
 */
public interface ChiseledBookshelf extends TileState, BlockInventoryHolder {

    /**
     * Gets the last interacted inventory slot.
     *
     * @return the last interacted slot
     */
    int getLastInteractedSlot();

    /**
     * Sets the last interacted inventory slot.
     *
     * @param lastInteractedSlot the new last interacted slot
     */
    void setLastInteractedSlot(int lastInteractedSlot);

    /**
     * @return inventory
     * @see Container#getInventory()
     */
    @NotNull
    @Override
    ChiseledBookshelfInventory getInventory();

    /**
     * @return snapshot inventory
     * @see Container#getSnapshotInventory()
     */
    @NotNull
    ChiseledBookshelfInventory getSnapshotInventory();
}
