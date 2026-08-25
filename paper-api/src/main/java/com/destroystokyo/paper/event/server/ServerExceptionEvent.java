package com.destroystokyo.paper.event.server;

import com.destroystokyo.paper.exception.ServerException;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called whenever an exception is thrown in a recoverable section of the server.
 */
public interface ServerExceptionEvent extends Event {

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
