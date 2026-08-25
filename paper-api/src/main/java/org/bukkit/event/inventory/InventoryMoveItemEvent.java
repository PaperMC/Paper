package org.bukkit.event.inventory;

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
public interface InventoryMoveItemEvent extends Event, Cancellable {

    /**
     * Gets the Inventory that the ItemStack is being taken from
     *
     * @return Inventory that the ItemStack is being taken from
     */
    Inventory getSource();

    /**
     * Gets the ItemStack being moved; if modified, the original item will not
     * be removed from the source inventory.
     *
     * @return ItemStack
     */
    ItemStack getItem();

    /**
     * Sets the ItemStack being moved; if this is different from the original
     * ItemStack, the original item will not be removed from the source
     * inventory.
     *
     * @param itemStack The ItemStack
     */
    void setItem(ItemStack itemStack);

    /**
     * Gets the Inventory that the ItemStack is being put into
     *
     * @return Inventory that the ItemStack is being put into
     */
    Inventory getDestination();

    /**
     * Gets the Inventory that initiated the transfer. This will always be
     * either the destination or source Inventory.
     *
     * @return Inventory that initiated the transfer
     */
    Inventory getInitiator();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
