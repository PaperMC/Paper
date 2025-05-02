package org.bukkit.event.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Inventory sourceInventory;
    private final Inventory destinationInventory;
    private ItemStack itemStack;
    private final boolean didSourceInitiate;

    private boolean cancelled;

    @ApiStatus.Internal
    public InventoryMoveItemEvent(@NotNull final Inventory sourceInventory, @NotNull final ItemStack itemStack, @NotNull final Inventory destinationInventory, final boolean didSourceInitiate) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null");
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
    @NotNull
    public Inventory getSource() {
        return this.sourceInventory;
    }

    /**
     * Gets the ItemStack being moved; if modified, the original item will not
     * be removed from the source inventory.
     *
     * @return ItemStack
     */
    @NotNull
    public ItemStack getItem() {
        return this.itemStack;
    }

    /**
     * Sets the ItemStack being moved; if this is different from the original
     * ItemStack, the original item will not be removed from the source
     * inventory.
     *
     * @param itemStack The ItemStack
     */
    public void setItem(@NotNull ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null. Cancel the event if you want nothing to be transferred.");
        this.itemStack = itemStack.clone();
    }

    /**
     * Gets the Inventory that the ItemStack is being put into
     *
     * @return Inventory that the ItemStack is being put into
     */
    @NotNull
    public Inventory getDestination() {
        return this.destinationInventory;
    }

    /**
     * Gets the Inventory that initiated the transfer. This will always be
     * either the destination or source Inventory.
     *
     * @return Inventory that initiated the transfer
     */
    @NotNull
    public Inventory getInitiator() {
        return this.didSourceInitiate ? this.sourceInventory : this.destinationInventory;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
