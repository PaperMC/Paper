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

    /**
     * Checks if this player is banned or not
     *
     * @return true if banned, otherwise false
     */
    public boolean isBanned();

    /**
     * Bans or unbans this player
     *
     * @param banned true if banned
     */
    public void setBanned(boolean banned);
}
