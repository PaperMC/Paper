package io.papermc.paper.event.player;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CartographyInventory;

/**
 * Called when the recipe of an Item is completed inside a cartography table.
 */
public interface CartographyItemEvent extends InventoryClickEvent {

    @Override
    CartographyInventory getInventory();
}
