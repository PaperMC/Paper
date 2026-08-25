package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.craftbukkit.event.server.CraftServerEvent;
import org.bukkit.event.HandlerList;

public class PaperServerTickEndEvent extends CraftServerEvent implements ServerTickEndEvent {

    private final int tickNumber;
    private final double tickDuration;
    private final long timeEnd;

    public PaperServerTickEndEvent(final int tickNumber, final double tickDuration, final long timeRemaining) {
        this.tickNumber = tickNumber;
        this.tickDuration = tickDuration;
        this.timeEnd = System.nanoTime() + timeRemaining;
    }

    @Override
    public int getTickNumber() {
        return this.tickNumber;
    }

    @Override
    public double getTickDuration() {
        return this.tickDuration;
    }

    @Override
    public long getTimeRemaining() {
        return this.timeEnd - System.nanoTime();
    }

    @Override
    public HandlerList getHandlers() {
        return ServerTickEndEvent.getHandlerList();
    }
}
