package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Beacon.
 *
 * @since 1.4.5 R1.0
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
