package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CraftBlockDispenseEvent extends CraftBlockEvent implements BlockDispenseEvent {

    private ItemStack item;
    private Vector velocity;

    private boolean cancelled;

    public CraftBlockDispenseEvent(final Block block, final ItemStack item, final Vector velocity) {
        super(block);
        this.item = item;
        this.velocity = velocity;
    }

    @Override
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    public void setItem(final ItemStack item) {
        this.item = item;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity.clone();
    }

    @Override
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity.clone();
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
        return BlockDispenseEvent.getHandlerList();
    }
}
