
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

    public final Server getServer() {
        return server;
    }
}
