package org.bukkit.event.server;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventTmp;

/**
 * Miscellaneous server events
 */
public abstract class ServerEvent extends EventTmp {

    public ServerEvent() {
        super(!Bukkit.isPrimaryThread()); // Paper
    }

    public ServerEvent(boolean isAsync) {
        super(isAsync);
    }
}
