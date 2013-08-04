package org.bukkit.inventory;

public interface BeaconInventory extends Inventory {
    /**
     * Set the item powering the beacon.
     *
     * @param item The new item
     */
    void setItem(ItemStack item);
    /**
     * Get the item powering the beacon.
     *
     * @return The current item.
     */
    ItemStack getItem();
}
