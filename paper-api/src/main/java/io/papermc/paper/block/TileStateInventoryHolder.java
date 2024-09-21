package io.papermc.paper.block;

import org.bukkit.block.TileState;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a {@link TileState} block that has an inventory.
 */
@NullMarked
public interface TileStateInventoryHolder extends TileState, BlockInventoryHolder {

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
