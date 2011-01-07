package org.bukkit;

/**
 * Represents a slot in an inventory
 */
public class Slot {
    private Inventory inventory;
    private int index;

    public Slot(Inventory inventory, int index) {
        this.inventory = inventory;
        this.index = index;
    }

    /**
     * Gets the inventory this slot belongs to
     * 
     * @return The inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the index this slot belongs to
     * 
     * @return Index of the slot
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the item from the slot.
     * 
     * @return ItemStack in the slot.
     */
    public ItemStack getItem() {
        return inventory.getItem(index);
    }
}
