
package org.bukkit;

/**
 * Represents a player, connected or not
 * 
 */
public interface Player {
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

    /**
     * Gets the players current position in the world
     *
     * @return Location of this player
     */
    public Location getLocation();

    /**
     * Gets the current world this player is on
     *
     * @return World
     */
    public World getWorld();
}
