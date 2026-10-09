package org.bukkit.block;

import io.papermc.paper.block.TileStateInventoryHolder;
import org.bukkit.inventory.ShelfInventory;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Shelf extends TileStateInventoryHolder {

    @Override
    ShelfInventory getInventory();

    @Override
    ShelfInventory getSnapshotInventory();

    /**
     * Gets the appropriate slot based on a vector relative to this block.
     * <p>
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
