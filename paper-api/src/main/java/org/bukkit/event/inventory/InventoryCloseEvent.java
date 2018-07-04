
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

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
 * <li>{@link HumanEntity#openWorkbench(org.bukkit.Location, boolean)}
 * <li>{@link HumanEntity#openEnchanting(org.bukkit.Location, boolean)}
 * <li>{@link InventoryView#close()}
 * </ul>
 * To invoke one of these methods, schedule a task using
 * {@link org.bukkit.scheduler.BukkitScheduler#runTask(org.bukkit.plugin.Plugin, Runnable)}, which will run the task
 * on the next tick. Also be aware that this is not an exhaustive list, and
 * other methods could potentially create issues as well.
 */
public class InventoryCloseEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    // Paper start
    private final Reason reason;
    @NotNull
    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        /**
         * Unknown reason
         */
        UNKNOWN,
        /**
         * Player is teleporting
         */
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

    public InventoryCloseEvent(@NotNull InventoryView transaction) {
        this(transaction, Reason.UNKNOWN);
    }

    public InventoryCloseEvent(@NotNull InventoryView transaction, @NotNull Reason reason) {
        super(transaction);
        this.reason = reason;
        // Paper end
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final HumanEntity getPlayer() {
        return transaction.getPlayer();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
