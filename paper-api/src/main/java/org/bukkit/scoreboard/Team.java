package org.bukkit.scoreboard;

import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.potion.PotionEffectType;

/**
 * A team on a scoreboard that has a common display theme and other
 * properties. This team is only relevant to the display of the associated
 * {@link #getScoreboard() scoreboard}.
 */
public interface Team {

    /**
     * Gets the name of this Team
     *
     * @return Objective name
     * @throws IllegalStateException if this team has been unregistered
     */
    String getName() throws IllegalStateException;

    /**
     * Gets the name displayed to entries for this team
     *
     * @return Team display name
     * @throws IllegalStateException if this team has been unregistered
     */
    String getDisplayName() throws IllegalStateException;

    /**
     * Sets the name displayed to entries for this team
     *
     * @param displayName New display name
     * @throws IllegalArgumentException if displayName is longer than 32
     *     characters.
     * @throws IllegalStateException if this team has been unregistered
     */
    void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException;

    /**
     * Gets the prefix prepended to the display of entries on this team.
     *
     * @return Team prefix
     * @throws IllegalStateException if this team has been unregistered
     */
    String getPrefix() throws IllegalStateException;

    /**
     * Sets the prefix prepended to the display of entries on this team.
     *
     * @param prefix New prefix
     * @throws IllegalArgumentException if prefix is null
     * @throws IllegalArgumentException if prefix is longer than 16
     *     characters
     * @throws IllegalStateException if this team has been unregistered
     */
    void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException;

    /**
     * Gets the suffix appended to the display of entries on this team.
     *
     * @return the team's current suffix
     * @throws IllegalStateException if this team has been unregistered
     */
    String getSuffix() throws IllegalStateException;

    /**
     * Sets the suffix appended to the display of entries on this team.
     *
     * @param suffix the new suffix for this team.
     * @throws IllegalArgumentException if suffix is null
     * @throws IllegalArgumentException if suffix is longer than 16
     *     characters
     * @throws IllegalStateException if this team has been unregistered
     */
    void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException;

    /**
     * Gets the team friendly fire state
     *
     * @return true if friendly fire is enabled
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean allowFriendlyFire() throws IllegalStateException;

    /**
     * Sets the team friendly fire state
     *
     * @param enabled true if friendly fire is to be allowed
     * @throws IllegalStateException if this team has been unregistered
     */
    void setAllowFriendlyFire(boolean enabled) throws IllegalStateException;

    /**
     * Gets the team's ability to see {@link PotionEffectType#INVISIBILITY
     * invisible} teammates.
     *
     * @return true if team members can see invisible members
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean canSeeFriendlyInvisibles() throws IllegalStateException;

    /**
     * Sets the team's ability to see {@link PotionEffectType#INVISIBILITY
     * invisible} teammates.
     *
     * @param enabled true if invisible teammates are to be visible
     * @throws IllegalStateException if this team has been unregistered
     */
    void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException;

    /**
     * Gets the team's ability to see name tags
     *
     * @return the current name tag visibilty for the team
     * @throws IllegalArgumentException if this team has been unregistered
     */
    NameTagVisibility getNameTagVisibility() throws IllegalArgumentException;

    /**
     * Set's the team's ability to see name tags
     *
     * @param visibility The nameTagVisibilty to set
     * @throws IllegalArgumentException if this team has been unregistered
     */
    void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException;

    /**
     * Gets the Set of players on the team
     *
     * @return players on the team
     * @throws IllegalStateException if this team has been unregistered\
     * @deprecated Teams can contain entries that aren't players
     * @see #getEntries()
     */
    @Deprecated
    Set<OfflinePlayer> getPlayers() throws IllegalStateException;

    /**
     * Gets the Set of entries on the team
     *
     * @return entries on the team
     * @throws IllegalStateException if this entries has been unregistered\
     */
    Set<String> getEntries() throws IllegalStateException;

    /**
     * Gets the size of the team
     *
     * @return number of entries on the team
     * @throws IllegalStateException if this team has been unregistered
     */
    int getSize() throws IllegalStateException;

    /**
     * Gets the Scoreboard to which this team is attached
     *
     * @return Owning scoreboard, or null if this team has been {@link
     *     #unregister() unregistered}
     */
    Scoreboard getScoreboard();

    /**
     * This puts the specified player onto this team for the scoreboard.
     * <p>
     * This will remove the player from any other team on the scoreboard.
     *
     * @param player the player to add
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated Teams can contain entries that aren't players
     * @see #addEntry(String)
     */
    @Deprecated
    void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException;

    /**
     * This puts the specified entry onto this team for the scoreboard.
     * <p>
     * This will remove the entry from any other team on the scoreboard.
     *
     * @param entry the entry to add
     * @throws IllegalArgumentException if entry is null
     * @throws IllegalStateException if this team has been unregistered
     */
    void addEntry(String entry) throws IllegalStateException, IllegalArgumentException;

    /**
     * Removes the player from this team.
     *
     * @param player the player to remove
     * @return if the player was on this team
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated Teams can contain entries that aren't players
     * @see #removeEntry(String)
     */
    @Deprecated
    boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException;

    /**
     * Removes the entry from this team.
     *
     * @param entry the entry to remove
     * @throws IllegalArgumentException if entry is null
     * @throws IllegalStateException if this team has been unregistered
     * @return if the entry was a part of this team
     */
    boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException;

    /**
     * Unregisters this team from the Scoreboard
     *
     * @throws IllegalStateException if this team has been unregistered
     */
    void unregister() throws IllegalStateException;

    /**
     * Checks to see if the specified player is a member of this team.
     *
     * @param player the player to search for
     * @return true if the player is a member of this team
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated Teams can contain entries that aren't players
     * @see #hasEntry(String)
     */
    @Deprecated
    boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException;
    /**
     * Checks to see if the specified entry is a member of this team.
     *
     * @param entry the entry to search for
     * @return true if the entry is a member of this team
     * @throws IllegalArgumentException if entry is null
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean hasEntry(String entry) throws IllegalArgumentException,IllegalStateException;
}
