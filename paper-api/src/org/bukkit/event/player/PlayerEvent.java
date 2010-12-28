
package org.bukkit.event.player;

import org.bukkit.Player;
import org.bukkit.Server;
import org.bukkit.event.Event;

/**
 * Represents a player related event
 */
public class PlayerEvent extends Event {
    private final Player player;

    public PlayerEvent(final Server server, Event.Type type, final Player who) {
        super(server, type);
        player = who;
    }

    /**
     * Returns the player involved in this event
     * @return Player who is involved in this event
     */
    public final Player getPlayer() {
        return player;
    }
}
