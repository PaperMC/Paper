package org.bukkit.inventory;

/**
 * Represents a slot in an inventory
 */
public interface Slot {
    /**
     * Gets the inventory this slot belongs to
     *
     * @return The inventory
     */
    public Inventory getInventory();

    /**
     * Get the index this slot belongs to
     *
     * @return Index of the slot
     */
    public int getIndex();

    /**
     * Get the item from the slot.
     *
     * @return ItemStack in the slot.
     */
    public ItemStack getItem();
}
