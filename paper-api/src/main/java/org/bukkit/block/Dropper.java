package org.bukkit.block;

import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a dropper.
 */
public interface Dropper extends BlockState, InventoryHolder {
    /**
     * Tries to drop a randomly selected item from the Dropper's inventory,
     * following the normal behavior of a Dropper.
     * <p>
     * Normal behavior of a Dropper is as follows:
     * <p>
     * If the block that the Dropper is facing is an InventoryHolder or
     * ContainerBlock the randomly selected ItemStack is placed within that 
     * Inventory in the first slot that's available, starting with 0 and
     * counting up.  If the inventory is full, nothing happens.
     * <p>
     * If the block that the Dropper is facing is not an InventoryHolder or
     * ContainerBlock, the randomly selected ItemStack is dropped on
     * the ground in the form of an {@link org.bukkit.entity.Item Item}.
     */
     public void drop();
}
