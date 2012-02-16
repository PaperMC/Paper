package org.bukkit.event;

import java.io.Serializable;

/**
 * Represents an event
 */
@SuppressWarnings("serial")
public abstract class Event implements Serializable {
    private final String name;

    protected Event() {
        this(null);
    }

    protected Event(final String name) {
        if (name == null) {
            this.name = getClass().getName();
        } else {
            this.name = name;
        }
    }

    /**
     * @return Name of this event
     */
    public final String getEventName() {
        return name;
    }

    public HandlerList getHandlers() {
        throw new IllegalStateException(getEventName() + " must implement getHandlers()");
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
