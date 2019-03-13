package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Beacon.
 */
public interface BeaconInventory extends Inventory {

    /**
     * Set the item powering the beacon.
     *
     * @param item The new item
     */
    void setItem(@Nullable ItemStack item);

    /**
     * Get the item powering the beacon.
     *
     * @return The current item.
     */
    @Nullable
    ItemStack getItem();
}
