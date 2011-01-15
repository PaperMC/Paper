package org.bukkit.entity;

import org.bukkit.Inventory;

/**
 * Represents a storage minecart.
 * 
 * @author sk89q
 */
public interface StorageMinecart extends Minecart {
    /**
     * Return the inventory object for this StorageMinecart.
     * 
     * @return The inventory for this Minecart
     */
    public Inventory getInventory();
}
