package org.bukkit.block;

import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.util.Vector;
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

    /**
     * Gets the appropriate slot based on a vector relative to this block.<br>
     * Will return -1 if the given vector is not within the bounds of any slot.
     * <p>
     * The supplied vector should only contain components with values between 0.0
     * and 1.0, inclusive.
     *
     * @param position a vector relative to this block
     * @return the slot under the given vector or -1
     */
    int getSlot(@NotNull Vector position);
}
