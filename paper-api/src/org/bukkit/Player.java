
package org.bukkit;

/**
 * Represents a player, connected or not
 * 
 */
public interface Player extends EntityHuman {
    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    public String getName();

    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();
}
