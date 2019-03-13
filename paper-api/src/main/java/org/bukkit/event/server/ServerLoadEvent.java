package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

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
    public ServerLoadEvent(@NotNull LoadType type) {
        this.type = type;
    }

    /**
     * Gets the context in which the server was loaded.
     *
     * @return the context in which the server was loaded
     */
    @NotNull
    public LoadType getType() {
        return type;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
