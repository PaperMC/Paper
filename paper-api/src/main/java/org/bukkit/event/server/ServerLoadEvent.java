package org.bukkit.event.server;

import org.bukkit.event.HandlerList;

/**
 * This event is called when either the server startup or reload has completed.
 */
public interface ServerLoadEvent extends ServerEventNew {

    /**
     * Gets the context in which the server was loaded.
     *
     * @return the context in which the server was loaded
     */
    LoadType getType();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Represents the context in which the enclosing event has been completed.
     */
    enum LoadType {
        STARTUP,
        RELOAD
    }
}
