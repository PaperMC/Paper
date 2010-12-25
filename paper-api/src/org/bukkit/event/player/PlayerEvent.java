
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

    public final Player getPlayer() {
        return player;
    }
}
