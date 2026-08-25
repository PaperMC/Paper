package org.bukkit.event.inventory;

import org.bukkit.inventory.ItemStack;

/**
 * This event is called when a player in creative mode puts down or picks up
 * an item in their inventory / hotbar and when they drop items from their
 * Inventory while in creative mode.
 */
public interface InventoryCreativeEvent extends InventoryClickEvent {

    @Override
    void setCursor(ItemStack item);
}
