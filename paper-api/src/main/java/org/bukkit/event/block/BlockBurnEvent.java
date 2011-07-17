package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * Called when a block is destroyed as a result of being burnt by fire.
 *<p />
 * If a Block Burn event is cancelled, the block will not be destroyed as a result of being burnt by fire.
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

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
