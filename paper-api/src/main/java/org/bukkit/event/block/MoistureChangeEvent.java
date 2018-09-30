package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the moisture level of a soil block changes.
 */
public class MoistureChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final BlockState newState;

    public MoistureChangeEvent(final Block block, final BlockState newState) {
        super(block);
        this.newState = newState;
        this.cancelled = false;
    }

    /**
     * Gets the new state of the affected block.
     *
     * @return new block state
     */
    public BlockState getNewState() {
        return newState;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
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
