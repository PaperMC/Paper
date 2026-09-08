package com.destroystokyo.paper.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.checkerframework.checker.index.qual.Positive;

/**
 * Called when the server has started ticking the main loop
 */
public interface ServerTickStartEvent extends ServerEvent {

    /**
     * @return What tick this is going be since start (first tick = 1)
     */
    @Positive int getTickNumber();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
