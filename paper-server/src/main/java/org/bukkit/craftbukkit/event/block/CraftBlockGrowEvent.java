package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockGrowEvent;

public class CraftBlockGrowEvent extends CraftBlockEvent implements BlockGrowEvent {

    private final BlockState newState;
    private boolean cancelled;

    public CraftBlockGrowEvent(final Block block, final BlockState newState) {
        super(block);
        this.newState = newState; // todo lazy
    }

    @Override
    public BlockState getNewState() {
        return this.newState;
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
        return BlockGrowEvent.getHandlerList();
    }
}
