
package org.bukkit.event.player;

import org.bukkit.Player;
import org.bukkit.Server;

/**
 * Handles all event arguments in relation to a player joining a server
 */
public class PlayerQuitEvent extends PlayerEvent {
    private final PlayerQuitReason reason;

    public PlayerQuitEvent(final Server server, final Player player, final PlayerQuitReason because) {
        super(server, player);
        reason = because;
    }

    /**
     * Return the reason for a player quitting the game
     * @return PlayerQuitReason with the relevant reason
     */
    public PlayerQuitReason getReason() {
        return reason;
    }

    /**
     * The reason a player quit the game
     */
    public enum PlayerQuitReason {
        /**
         * A player willingly quit the game
         */
        Quit,

        /**
         * A player disconnected due to a technical fault
         */
        Disconnected,
        
        /**
         * A player was kicked by another player
         */
        Kicked
    }
}
