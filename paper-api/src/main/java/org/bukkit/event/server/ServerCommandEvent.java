
package org.bukkit.event.server;

import org.bukkit.event.Event;

/**
 * Server Command events
 */
public class ServerCommandEvent extends Event {
    public ServerCommandEvent() {
        super(Type.SERVER_COMMAND);
    }
}