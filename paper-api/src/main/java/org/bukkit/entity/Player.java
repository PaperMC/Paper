
package org.bukkit.entity;

import java.net.InetSocketAddress;
import org.bukkit.Location;

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
     * Checks if this player is currently op
     *
     * @return true if they are online
     */
    public boolean isOp();

    /**
     * Sends this player a message, which will be displayed in their chat
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message);

    /**
     * Gets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @return String containing a color formatted name to display for this player
     */
    public String getDisplayName();

    /**
     * Sets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @return String containing a color formatted name to display for this player
     */
    public void setDisplayName(String name);
    
    /**
     * Set the target of the player's compass.
     * 
     * @param loc
     */
    public void setCompassTarget(Location loc);
    
    /**
     * Gets the socket address of this player
     * @return the player's address
     */
    public InetSocketAddress getAddress();

    /**
     * Kicks player with custom kick message.
     *
     * @return
     */
    public void kickPlayer(String message);

    /**
     * Makes the player perform the given command
     *
     * @param command Command to perform
     * @return true if the command was successful, otherwise false
     */
    public boolean performCommand(String command);
}
