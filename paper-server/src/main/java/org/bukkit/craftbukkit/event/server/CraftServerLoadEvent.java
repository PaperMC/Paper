package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerLoadEvent;

public class CraftServerLoadEvent extends CraftServerEvent implements ServerLoadEvent {

    private final LoadType type;

    public CraftServerLoadEvent(final LoadType type) {
        this.type = type;
    }

    @Override
    public LoadType getType() {
        return this.type;
    }

    @Override
    public HandlerList getHandlers() {
        return ServerLoadEvent.getHandlerList();
    }
}
