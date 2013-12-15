package org.bukkit.inventory;

/**
 * Interface to the inventory of a Beacon.
 */
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
