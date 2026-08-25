package com.destroystokyo.paper.event.server;

import com.destroystokyo.paper.exception.ServerException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;

/**
 * Called whenever an exception is thrown in a recoverable section of the server.
 */
public interface ServerExceptionEvent extends ServerEvent {

    /**
     * Gets the wrapped exception that was thrown.
     *
     * @return Exception thrown
     */
    ServerException getException();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
