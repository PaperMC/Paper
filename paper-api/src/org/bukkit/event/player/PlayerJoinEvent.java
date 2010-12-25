
package org.bukkit.event.player;

import org.bukkit.Player;
import org.bukkit.Server;

/**
 * Handles all event arguments in relation to a player joining a server
 */
public class PlayerJoinEvent extends PlayerEvent {
    public PlayerJoinEvent(final Server server, final Player player) {
        super(server, player);
    }
}
