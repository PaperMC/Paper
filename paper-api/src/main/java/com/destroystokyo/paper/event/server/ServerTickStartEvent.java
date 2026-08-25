package com.destroystokyo.paper.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when the server has started ticking the main loop
 */
public interface ServerTickStartEvent extends Event {

    /**
     * @return What tick this is going be since start (first tick = 1)
     */
    int getTickNumber();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
