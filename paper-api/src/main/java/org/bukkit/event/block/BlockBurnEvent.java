package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * Called when a block is destroyed because of being burnt by fire
 * @author tkelly
 */
public class BlockBurnEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;

    public BlockBurnEvent(Block block) {
        super(Type.BLOCK_BURN, block);
        this.cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Allow for the block to be stopped from being destroyed
     * @param cancel
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
