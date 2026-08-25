
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;

/**
 * This event is called when a player closes an inventory.
 * <p>
 * Because InventoryCloseEvent occurs within a modification of the Inventory,
 * not all Inventory related methods are safe to use.
 * <p>
 * Methods that change the view a player is looking at should never be invoked
 * by an EventHandler for InventoryCloseEvent using the HumanEntity or
 * InventoryView associated with this event.
 * Examples of these include:
 * <ul>
 * <li>{@link HumanEntity#closeInventory()}
 * <li>{@link HumanEntity#openInventory(org.bukkit.inventory.Inventory)}
 * <li>{@link InventoryView#close()}
 * </ul>
 * To invoke one of these methods, schedule a task using
 * {@link org.bukkit.scheduler.BukkitScheduler#runTask(org.bukkit.plugin.Plugin, Runnable)}, which will run the task
 * on the next tick. Also be aware that this is not an exhaustive list, and
 * other methods could potentially create issues as well.
 */
public interface InventoryCloseEvent extends InventoryEvent {

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    HumanEntity getPlayer(); // todo PlayerEvent?

    Reason getReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Reason {
        /**
         * Unknown reason
         */
        UNKNOWN,
        /**
         * Player is teleporting
         *
         * @deprecated As of 1.21.10, this is not called anymore as inventories are not closed on teleportation.
         */
        @Deprecated(since = "1.21.10")
        TELEPORT,
        /**
         * Player is no longer permitted to use this inventory
         */
        CANT_USE,
        /**
         * The chunk the inventory was in was unloaded
         */
        UNLOADED,
        /**
         * Opening new inventory instead
         */
        OPEN_NEW,
        /**
         * Closed
         */
        PLAYER,
        /**
         * Closed due to disconnect
         */
        DISCONNECT,
        /**
         * The player died
         */
        DEATH,
        /**
         * Closed by Bukkit API
         */
        PLUGIN,
    }
}
