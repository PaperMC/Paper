package org.bukkit.block;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a captured state of a container block.
 */
public interface Container extends BlockState, InventoryHolder, Lockable {

    /**
     * Gets the inventory of the block represented by this block state.
     * <p>
     * If the block was changed to a different type in the meantime, the
     * returned inventory might no longer be valid.
     * <p>
     * If this block state is not placed this will return the captured inventory
     * snapshot instead.
     *
     * @return the inventory
     */
    @Override
    Inventory getInventory();

    /**
     * Gets the captured inventory snapshot of this container.
     * <p>
     * The returned inventory is not linked to any block. Any modifications to
     * the returned inventory will not be applied to the block represented by
     * this block state up until {@link #update(boolean, boolean)} has been
     * called.
     *
     * @return the captured inventory snapshot
     */
    Inventory getSnapshotInventory();
}
