package org.bukkit.event.inventory;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Called when a hopper or hopper minecart picks up a dropped item.
 */
public interface InventoryPickupItemEvent extends Event, Cancellable {

    /**
     * Gets the Inventory that picked up the item
     */
    Inventory getInventory();

    /**
     * Gets the Item entity that was picked up
     */
    Item getItem();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
