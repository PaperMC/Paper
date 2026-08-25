package org.bukkit.craftbukkit.event.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryMoveItemEvent extends CraftEvent implements InventoryMoveItemEvent {

    private final Inventory sourceInventory;
    private final Inventory destinationInventory;
    private ItemStack itemStack;
    private final boolean didSourceInitiate;

    private boolean cancelled;

    public boolean calledSetItem;
    public boolean calledGetItem;

    public CraftInventoryMoveItemEvent(final Inventory sourceInventory, final ItemStack itemStack, final Inventory destinationInventory, final boolean didSourceInitiate) {
        this.sourceInventory = sourceInventory;
        this.itemStack = itemStack;
        this.destinationInventory = destinationInventory;
        this.didSourceInitiate = didSourceInitiate;
    }

    @Override
    public Inventory getSource() {
        return this.sourceInventory;
    }

    @Override
    public ItemStack getItem() {
        this.calledGetItem = true;
        return this.itemStack;
    }

    @Override
    public void setItem(final ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null. Cancel the event if you want nothing to be transferred.");
        this.itemStack = itemStack.clone();
        this.calledSetItem = true;
    }

    @Override
    public Inventory getDestination() {
        return this.destinationInventory;
    }

    @Override
    public Inventory getInitiator() {
        return this.didSourceInitiate ? this.sourceInventory : this.destinationInventory;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryMoveItemEvent.getHandlerList();
    }
}
