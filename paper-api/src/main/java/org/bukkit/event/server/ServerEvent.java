package org.bukkit.event.server;

import org.bukkit.event.Event;

/**
 * Miscellaneous server events
 */
@SuppressWarnings("serial")
public class ServerEvent extends Event {
    public ServerEvent(final Type type) {
        super(type);
    }
}
