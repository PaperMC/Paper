
package org.bukkit.event;

import org.bukkit.Server;

/**
 * Represents an event
 */
public abstract class Event {
    private final Server server;

    protected Event(final Server instance) {
        server = instance;
    }

    /**
     * Gets the Server instance that triggered this event
     * @return Server which this event was triggered on
     */
    public final Server getServer() {
        return server;
    }

    /**
     * Represents an events priority
     */
    public enum Priority {
        /**
         * Event is critical and must be called near-first
         */
        Highest,

        /**
         * Event is of high importance
         */
        High,

        /**
         * Event is neither important or unimportant, and may be ran normally
         */
        Normal,

        /**
         * Event is of low importance
         */
        Low,

        /**
         * Event is of extremely low importance, most likely just to monitor events, and must be run near-last
         */
        Lowest
    }
}
