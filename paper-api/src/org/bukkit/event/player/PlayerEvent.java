
package org.bukkit.event.player;

import org.bukkit.Player;
import org.bukkit.Server;
import org.bukkit.event.Event;

/**
 * Represents a player related event
 */
public abstract class PlayerEvent extends Event {
    private final Player player;

    protected PlayerEvent(final Server server, final Player who) {
        super(server);
        player = who;
    }

    /**
     * Returns the player involved in this event
     * @return Player who is involved in this event
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Represents the different types of events
     */
    public enum EventType {
        /**
         * A player joins a server
         */
        Join,
        /**
         * A player leaves a server
         */
        Quit
    }
}
