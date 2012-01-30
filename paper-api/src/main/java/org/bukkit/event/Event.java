package org.bukkit.event;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * Represents an event
 */
@SuppressWarnings("serial")
public abstract class Event implements Serializable {
    private final String name;

    protected Event() {
        this.name = getClass().getName();
    }

    protected Event(final String name) {
        Validate.notNull(name, "name is cannot be null");
        this.name = name;
    }

    /**
     * Gets the event's name. Should only be used if getType() == Type.CUSTOM
     *
     * @return Name of this event
     */
    public final String getEventName() {
        return name;
    }

    public HandlerList getHandlers() {
        throw new IllegalStateException("Event must implement getHandlers()");
    }

    public enum Result {

        /**
         * Deny the event.
         * Depending on the event, the action indicated by the event will either not take place or will be reverted.
         * Some actions may not be denied.
         */
        DENY,
        /**
         * Neither deny nor allow the event.
         * The server will proceed with its normal handling.
         */
        DEFAULT,
        /**
         * Allow / Force the event.
         * The action indicated by the event will take place if possible, even if the server would not normally allow the action.
         * Some actions may not be allowed.
         */
        ALLOW;
    }
}
