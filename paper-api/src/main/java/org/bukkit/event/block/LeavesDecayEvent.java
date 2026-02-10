package org.bukkit.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when leaves are decaying naturally.
 * <p>
 * If this event is cancelled, the leaves will not decay.
 */
public interface LeavesDecayEvent extends BlockEventNew, Cancellable {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
