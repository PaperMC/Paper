
package org.bukkit.event.player;

import org.bukkit.event.Listener;

/**
 * Handles all events thrown in relation to a Player
 */
public abstract class PlayerListener implements Listener {
    private PlayerListener() {
    }

    /**
     * Called when a player joins a server
     * @param event Relevant event details
     */
    public abstract void onPlayerJoin(PlayerJoinEvent event);

    /**
     * Called when a player leaves a server
     * @param event Relevant event details
     */
    public abstract void onPlayerQuit(PlayerQuitEvent event);
}
