
package org.bukkit.event.player;

/**
 * Handles all events thrown in relation to a Player
 */
public abstract class PlayerListener {
    private PlayerListener() {
    }

    public abstract void onPlayerJoin(PlayerJoinEvent event);
    public abstract void onPlayerQuit(PlayerQuitEvent event);
}
