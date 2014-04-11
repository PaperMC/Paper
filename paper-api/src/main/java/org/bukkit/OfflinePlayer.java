package org.bukkit;

import java.util.Date;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

public interface OfflinePlayer extends ServerOperator, AnimalTamer, ConfigurationSerializable {

    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline();

    /**
     * Returns the name of this player
     * <p>
     * Names are no longer unique past a single game session. For persistent storage
     * it is recommended that you use {@link #getUniqueId()} instead.
     *
     * @return Player name or null if we have not seen a name for this player yet
     */
    public String getName();

    /**
     * Returns the UUID of this player
     *
     * @return Player UUID
     */
    public UUID getUniqueId();

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
     * @deprecated Use {@link org.bukkit.BanList#addBan(String, String, Date,
     *     String)} or {@link org.bukkit.BanList#pardon(String)} to enhance
     *     functionality
     */
    @Deprecated
    public void setBanned(boolean banned);

    /**
     * Checks if this player is whitelisted or not
     *
     * @return true if whitelisted
     */
    public boolean isWhitelisted();

    /**
     * Sets if this player is whitelisted or not
     *
     * @param value true if whitelisted
     */
    public void setWhitelisted(boolean value);

    /**
     * Gets a {@link Player} object that this represents, if there is one
     * <p>
     * If the player is online, this will return that player. Otherwise,
     * it will return null.
     *
     * @return Online player
     */
    public Player getPlayer();

    /**
     * Gets the first date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of first log-in for this player, or 0
     */
    public long getFirstPlayed();

    /**
     * Gets the last date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of last log-in for this player, or 0
     */
    public long getLastPlayed();

    /**
     * Checks if this player has played on this server before.
     *
     * @return True if the player has played before, otherwise false
     */
    public boolean hasPlayedBefore();

    /**
     * Gets the Location where the player will spawn at their bed, null if
     * they have not slept in one or their current bed spawn is invalid.
     *
     * @return Bed Spawn Location if bed exists, otherwise null.
     */
    public Location getBedSpawnLocation();

}
