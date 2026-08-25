package io.papermc.paper.event.server;

import org.bukkit.craftbukkit.event.server.CraftServerEvent;
import org.bukkit.event.HandlerList;

public class PaperServerResourcesReloadedEvent extends CraftServerEvent implements ServerResourcesReloadedEvent {

    private final Cause cause;

    public PaperServerResourcesReloadedEvent(final Cause cause) {
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public HandlerList getHandlers() {
        return ServerResourcesReloadedEvent.getHandlerList();
    }
}
