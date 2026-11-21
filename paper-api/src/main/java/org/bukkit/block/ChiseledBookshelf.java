package org.bukkit.block;

import io.papermc.paper.block.TileStateInventoryHolder;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a captured state of a chiseled bookshelf.
 */
@NullMarked
public interface ChiseledBookshelf extends TileStateInventoryHolder {

    @Override
    ChiseledBookshelfInventory getInventory();

    @Override
    ChiseledBookshelfInventory getSnapshotInventory();

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
     * Gets the appropriate slot based on a vector relative to this block.
     * <br>
     * The supplied vector should only contain components with values between 0.0
     * and 1.0, inclusive.
     *
     * @param position a vector relative to this block
     * @return the slot under the given vector
     * @throws IllegalArgumentException if the vector is not finite
     * @throws IllegalStateException if this block state is not valid
     */
    int getSlot(Vector position);
}
