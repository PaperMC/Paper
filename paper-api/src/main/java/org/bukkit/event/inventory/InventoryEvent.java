package org.bukkit.event.inventory;

import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * Represents a player related inventory event
 */
public interface InventoryEvent extends Event {

    /**
     * Gets the primary Inventory involved in this transaction
     *
     * @return The upper inventory.
     */
    Inventory getInventory();

    /**
     * Gets the list of players viewing the primary (upper) inventory involved
     * in this event
     *
     * @return A list of people viewing.
     */
    List<HumanEntity> getViewers();

    /**
     * Gets the view object itself
     *
     * @return InventoryView
     */
    InventoryView getView();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
