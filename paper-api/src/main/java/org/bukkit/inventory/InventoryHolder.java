package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;

/**
 * @since 1.1.0 R5
 */
public interface InventoryHolder {

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    public Inventory getInventory();
}
