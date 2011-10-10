package org.bukkit.entity;

import org.bukkit.inventory.Inventory;

/**
 * Represents a storage minecart.
 */
public interface StorageMinecart extends Minecart {

    /**
     * Return the inventory object for this StorageMinecart.
     *
     * @return The inventory for this Minecart
     */
    public Inventory getInventory();
}
