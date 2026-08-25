package org.bukkit.event.inventory;

import org.bukkit.inventory.SmithingInventory;

/**
 * Called when the recipe of an Item is completed inside a smithing table.
 */
public interface SmithItemEvent extends InventoryClickEvent {

    @Override
    SmithingInventory getInventory();
}
