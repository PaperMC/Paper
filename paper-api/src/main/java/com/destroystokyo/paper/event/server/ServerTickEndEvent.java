package com.destroystokyo.paper.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;

/**
 * Called when the server has finished ticking the main loop
 */
public interface ServerTickEndEvent extends ServerEvent {

    /**
     * @return What tick this was since start (first tick = 1)
     */
    int getTickNumber();

    /**
     * @return Time in milliseconds of how long this tick took
     */
    double getTickDuration();

    /**
     * Amount of nanoseconds remaining before the next tick should start.
     * <p>
     * If this value is negative, then that means the server has exceeded the tick time limit and TPS has been lost.
     * <p>
     * Method will continuously return the updated time remaining value. (return value is not static)
     *
     * @return Amount of nanoseconds remaining before the next tick should start
     */
    long getTimeRemaining();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
