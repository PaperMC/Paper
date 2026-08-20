package org.bukkit.event.block;

import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the moisture level of a soil block changes.
 */
public interface MoistureChangeEvent extends BlockEvent, Cancellable {

    /**
     * Gets the new state of the affected block.
     *
     * @return new block state
     */
    BlockState getNewState();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
