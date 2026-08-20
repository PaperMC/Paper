package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperBlockPreDispenseEvent extends CraftBlockEvent implements BlockPreDispenseEvent {

    private final ItemStack itemStack;
    private final int slot;

    private boolean cancelled;

    public PaperBlockPreDispenseEvent(final Block block, final ItemStack itemStack, final int slot) {
        super(block);
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public int getSlot() {
        return this.slot;
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
        return BlockPreDispenseEvent.getHandlerList();
    }
}
