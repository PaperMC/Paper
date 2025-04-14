package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when either the server startup or reload has completed.
 */
public class ServerLoadEvent extends ServerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LoadType type;

    @ApiStatus.Internal
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
        return this.type;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents the context in which the enclosing event has been completed.
     */
    public enum LoadType {
        STARTUP,
        RELOAD
    }
}
