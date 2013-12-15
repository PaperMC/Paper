package org.bukkit.event.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Called when some entity or block (e.g. hopper) tries to move items directly
 * from one inventory to another.
 * <p>
 * When this event is called, the initiator may already have removed the item
 * from the source inventory and is ready to move it into the destination
 * inventory.
 * <p>
 * If this event is cancelled, the items will be returned to the source
 * inventory, if needed.
 * <p>
 * If this event is not cancelled, the initiator will try to put the ItemStack
 * into the destination inventory. If this is not possible and the ItemStack
 * has not been modified, the source inventory slot will be restored to its
 * former state. Otherwise any additional items will be discarded.
 */
public class InventoryMoveItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Inventory sourceInventory;
    private final Inventory destinationInventory;
    private ItemStack itemStack;
    private final boolean didSourceInitiate;

    public InventoryMoveItemEvent(final Inventory sourceInventory, final ItemStack itemStack, final Inventory destinationInventory, final boolean didSourceInitiate) {
        Validate.notNull(itemStack, "ItemStack cannot be null");
        this.sourceInventory = sourceInventory;
        this.itemStack = itemStack;
        this.destinationInventory = destinationInventory;
        this.didSourceInitiate = didSourceInitiate;
    }

    /**
     * Gets the Inventory that the ItemStack is being taken from
     *
     * @return Inventory that the ItemStack is being taken from
     */
    public Inventory getSource() {
        return sourceInventory;
    }

    /**
     * Gets the ItemStack being moved; if modified, the original item will not
     * be removed from the source inventory.
     *
     * @return ItemStack
     */
    public ItemStack getItem() {
        return itemStack.clone();
    }

    /**
     * Sets the ItemStack being moved; if this is different from the original
     * ItemStack, the original item will not be removed from the source
     * inventory.
     *
     * @param itemStack The ItemStack
     */
    public void setItem(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack cannot be null.  Cancel the event if you want nothing to be transferred.");
        this.itemStack = itemStack.clone();
    }

    /**
     * Gets the Inventory that the ItemStack is being put into
     *
     * @return Inventory that the ItemStack is being put into
     */
    public Inventory getDestination() {
        return destinationInventory;
    }

    /**
     * Gets the Inventory that initiated the transfer. This will always be
     * either the destination or source Inventory.
     *
     * @return Inventory that initiated the transfer
     */
    public Inventory getInitiator() {
        return didSourceInitiate ? sourceInventory : destinationInventory;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
