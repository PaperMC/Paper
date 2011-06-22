package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * Called when a block is destroyed because of being burnt by fire
 */
public class BlockBurnEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;

    public BlockBurnEvent(Block block) {
        super(Type.BLOCK_BURN, block);
        this.cancelled = false;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block burn event is cancelled, the block will not be destroyed from being burnt by fire
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block burn event is cancelled, the block will not be destroyed from being burnt by fire
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
