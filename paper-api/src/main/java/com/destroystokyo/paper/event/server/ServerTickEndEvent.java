package com.destroystokyo.paper.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the server has finished ticking the main loop
 */
@NullMarked
public class ServerTickEndEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int tickNumber;
    private final double tickDuration;
    private final long timeEnd;

    @ApiStatus.Internal
    public ServerTickEndEvent(final int tickNumber, final double tickDuration, final long timeRemaining) {
        this.tickNumber = tickNumber;
        this.tickDuration = tickDuration;
        this.timeEnd = System.nanoTime() + timeRemaining;
    }

    /**
     * @return What tick this was since start (first tick = 1)
     */
    public int getTickNumber() {
        return this.tickNumber;
    }

    /**
     * @return Time in milliseconds of how long this tick took
     */
    public double getTickDuration() {
        return this.tickDuration;
    }

    /**
     * Amount of nanoseconds remaining before the next tick should start.
     * <p>
     * If this value is negative, then that means the server has exceeded the tick time limit and TPS has been lost.
     * <p>
     * Method will continuously return the updated time remaining value. (return value is not static)
     *
     * @return Amount of nanoseconds remaining before the next tick should start
     */
    public long getTimeRemaining() {
        return this.timeEnd - System.nanoTime();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
