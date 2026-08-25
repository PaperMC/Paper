package io.papermc.paper.event.connection;

import io.papermc.paper.connection.PlayerConnection;
import org.bukkit.event.Event;

/**
 * Represents a connection related event
 */
public interface ConnectionEvent extends Event {

    /**
     * {@return the player connection involved in this event}
     */
    PlayerConnection getConnection();
}
