
package org.bukkit.entity;

import java.net.InetSocketAddress;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 * Represents a player, connected or not
 *
 */
public interface Player extends HumanEntity, CommandSender {
    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();

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
}
