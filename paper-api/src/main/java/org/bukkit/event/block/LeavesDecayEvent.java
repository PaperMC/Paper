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

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *<p />
     * If a Leaves Decay event is cancelled, the leaves will not decay.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *<p />
     * If a Leaves Decay event is cancelled, the leaves will not decay.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
