package com.destroystokyo.paper.exception;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static com.google.common.base.Preconditions.*;

/**
 * Exception thrown when a server event listener throws an exception
 */
public class ServerEventException extends ServerPluginException {

    private final Listener listener;
    private final Event event;

    public ServerEventException(String message, Throwable cause, Plugin responsiblePlugin, Listener listener, Event event) {
        super(message, cause, responsiblePlugin);
        this.listener = checkNotNull(listener, "listener");
        this.event = checkNotNull(event, "event");
    }

    public ServerEventException(Throwable cause, Plugin responsiblePlugin, Listener listener, Event event) {
        super(cause, responsiblePlugin);
        this.listener = checkNotNull(listener, "listener");
        this.event = checkNotNull(event, "event");
    }

    protected ServerEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Plugin responsiblePlugin, Listener listener, Event event) {
        super(message, cause, enableSuppression, writableStackTrace, responsiblePlugin);
        this.listener = checkNotNull(listener, "listener");
        this.event = checkNotNull(event, "event");
    }

    /**
     * Gets the listener which threw the exception
     *
     * @return event listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Gets the event which caused the exception
     *
     * @return event
     */
    public Event getEvent() {
        return event;
    }
}
