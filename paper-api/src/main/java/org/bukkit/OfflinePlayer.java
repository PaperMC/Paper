package org.bukkit;

import org.bukkit.permissions.ServerOperator;

public interface OfflinePlayer extends ServerOperator {
    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    public String getName();
}
