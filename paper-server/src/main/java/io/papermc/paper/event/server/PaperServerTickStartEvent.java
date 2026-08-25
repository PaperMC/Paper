package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperServerTickStartEvent extends CraftEvent implements ServerTickStartEvent {

    private final int tickNumber;

    public PaperServerTickStartEvent(final int tickNumber) {
        this.tickNumber = tickNumber;
    }

    @Override
    public int getTickNumber() {
        return this.tickNumber;
    }

    @Override
    public HandlerList getHandlers() {
        return ServerTickStartEvent.getHandlerList();
    }
}
