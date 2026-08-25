package org.bukkit.event.world;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a World is unloaded
 */
public interface WorldUnloadEvent extends WorldEventNew, Cancellable {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
