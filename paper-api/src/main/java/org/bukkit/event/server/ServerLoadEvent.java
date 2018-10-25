package org.bukkit.event.server;

import org.bukkit.event.HandlerList;

/**
 * This event is called when either the server startup or reload has completed.
 */
public class ServerLoadEvent extends ServerEvent {

    /**
     * Represents the context in which the enclosing event has been completed.
     */
    public enum LoadType {
        STARTUP, RELOAD;
    }

    private static final HandlerList handlers = new HandlerList();
    private final LoadType type;

    /**
     * Creates a {@code ServerLoadEvent} with a given loading type.
     *
     * @param type the context in which the server was loaded
     */
    public ServerLoadEvent(LoadType type) {
        this.type = type;
    }

    /**
     * Gets the context in which the server was loaded.
     *
     * @return the context in which the server was loaded
     */
    public LoadType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
