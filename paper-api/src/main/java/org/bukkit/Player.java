
package org.bukkit;

/**
 * Represents a player, connected or not
 * 
 */
public interface Player extends HumanEntity {
    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();

    /**
     * Sends this player a message, which will be displayed in their chat
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message);
}
