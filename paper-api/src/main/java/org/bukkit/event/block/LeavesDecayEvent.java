package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * Called when leaves are decaying naturally.
 *<p />
 * If a Leaves Decay event is cancelled, the leaves will not decay.
 */
public class LeavesDecayEvent extends BlockEvent implements Cancellable {
    private boolean cancel = false;

    public LeavesDecayEvent(final Block block) {
        super(Type.LEAVES_DECAY, block);
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
