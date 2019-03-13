package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;

public interface InventoryHolder {

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    public Inventory getInventory();
}
