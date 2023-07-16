package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    String getName();

    /**
     * Gets the name displayed to entries for this team
     *
     * @return Team display name
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    String getDisplayName();

    /**
     * Sets the name displayed to entries for this team
     *
     * @param displayName New display name
     * @throws IllegalStateException if this team has been unregistered
     */
    void setDisplayName(@NotNull String displayName);

    /**
     * Gets the prefix prepended to the display of entries on this team.
     *
     * @return Team prefix
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    String getPrefix();

    /**
     * Sets the prefix prepended to the display of entries on this team.
     *
     * @param prefix New prefix
     * @throws IllegalStateException if this team has been unregistered
     */
    void setPrefix(@NotNull String prefix);

    /**
     * Gets the suffix appended to the display of entries on this team.
     *
     * @return the team's current suffix
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    String getSuffix();

    /**
     * Sets the suffix appended to the display of entries on this team.
     *
     * @param suffix the new suffix for this team.
     * @throws IllegalStateException if this team has been unregistered
     */
    void setSuffix(@NotNull String suffix);

    /**
     * Gets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @return team color, defaults to {@link ChatColor#RESET}
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    ChatColor getColor();

    /**
     * Sets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @param color new color, must be non-null. Use {@link ChatColor#RESET} for
     * no color
     */
    void setColor(@NotNull ChatColor color);

    /**
     * Gets the team friendly fire state
     *
     * @return true if friendly fire is enabled
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean allowFriendlyFire();

    /**
     * Sets the team friendly fire state
     *
     * @param enabled true if friendly fire is to be allowed
     * @throws IllegalStateException if this team has been unregistered
     */
    void setAllowFriendlyFire(boolean enabled);

    /**
     * Gets the team's ability to see {@link PotionEffectType#INVISIBILITY
     * invisible} teammates.
     *
     * @return true if team members can see invisible members
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean canSeeFriendlyInvisibles();

    /**
     * Sets the team's ability to see {@link PotionEffectType#INVISIBILITY
     * invisible} teammates.
     *
     * @param enabled true if invisible teammates are to be visible
     * @throws IllegalStateException if this team has been unregistered
     */
    void setCanSeeFriendlyInvisibles(boolean enabled);

    /**
     * Gets the team's ability to see name tags
     *
     * @return the current name tag visibility for the team
     * @throws IllegalArgumentException if this team has been unregistered
     * @deprecated see {@link #getOption(Team.Option)}
     */
    @Deprecated
    @NotNull
    NameTagVisibility getNameTagVisibility();

    /**
     * Set's the team's ability to see name tags
     *
     * @param visibility The nameTagVisibility to set
     * @throws IllegalArgumentException if this team has been unregistered
     * @deprecated see {@link #setOption(Team.Option, Team.OptionStatus)}
     */
    @Deprecated
    void setNameTagVisibility(@NotNull NameTagVisibility visibility);

    /**
     * Gets the Set of players on the team
     *
     * @return players on the team
     * @throws IllegalStateException if this team has been unregistered
     * @see #getEntries()
     * @deprecated Teams can contain entries that aren't players
     */
    @Deprecated
    @NotNull
    Set<OfflinePlayer> getPlayers();

    /**
     * Gets the Set of entries on the team
     *
     * @return entries on the team
     * @throws IllegalStateException if this entries has been unregistered
     */
    @NotNull
    Set<String> getEntries();

    /**
     * Gets the size of the team
     *
     * @return number of entries on the team
     * @throws IllegalStateException if this team has been unregistered
     */
    int getSize();

    /**
     * Gets the Scoreboard to which this team is attached
     *
     * @return Owning scoreboard, or null if this team has been {@link
     *     #unregister() unregistered}
     */
    @Nullable
    Scoreboard getScoreboard();

    /**
     * This puts the specified player onto this team for the scoreboard.
     * <p>
     * This will remove the player from any other team on the scoreboard.
     *
     * @param player the player to add
     * @throws IllegalStateException if this team has been unregistered
     * @see #addEntry(String)
     * @deprecated Teams can contain entries that aren't players
     */
    @Deprecated
    void addPlayer(@NotNull OfflinePlayer player);

    /**
     * This puts the specified entry onto this team for the scoreboard.
     * <p>
     * This will remove the entry from any other team on the scoreboard.
     *
     * @param entry the entry to add
     * @throws IllegalStateException if this team has been unregistered
     */
    void addEntry(@NotNull String entry);

    /**
     * Removes the player from this team.
     *
     * @param player the player to remove
     * @return if the player was on this team
     * @throws IllegalStateException if this team has been unregistered
     * @see #removeEntry(String)
     * @deprecated Teams can contain entries that aren't players
     */
    @Deprecated
    boolean removePlayer(@NotNull OfflinePlayer player);

    /**
     * Removes the entry from this team.
     *
     * @param entry the entry to remove
     * @return if the entry was a part of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean removeEntry(@NotNull String entry);

    /**
     * Unregisters this team from the Scoreboard
     *
     * @throws IllegalStateException if this team has been unregistered
     */
    void unregister();

    /**
     * Checks to see if the specified player is a member of this team.
     *
     * @param player the player to search for
     * @return true if the player is a member of this team
     * @throws IllegalStateException if this team has been unregistered
     * @see #hasEntry(String)
     * @deprecated Teams can contain entries that aren't players
     */
    @Deprecated
    boolean hasPlayer(@NotNull OfflinePlayer player);
    /**
     * Checks to see if the specified entry is a member of this team.
     *
     * @param entry the entry to search for
     * @return true if the entry is a member of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean hasEntry(@NotNull String entry);

    /**
     * Get an option for this team
     *
     * @param option the option to get
     * @return the option status
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    OptionStatus getOption(@NotNull Option option);

    /**
     * Set an option for this team
     *
     * @param option the option to set
     * @param status the new option status
     * @throws IllegalStateException if this team has been unregistered
     */
    void setOption(@NotNull Option option, @NotNull OptionStatus status);

    /**
     * Represents an option which may be applied to this team.
     */
    public enum Option {

        /**
         * How to display the name tags of players on this team.
         */
        NAME_TAG_VISIBILITY,
        /**
         * How to display the death messages for players on this team.
         */
        DEATH_MESSAGE_VISIBILITY,
        /**
         * How players of this team collide with others.
         */
        COLLISION_RULE;
    }

    /**
     * How an option may be applied to members of this team.
     */
    public enum OptionStatus {

        /**
         * Apply this option to everyone.
         */
        ALWAYS,
        /**
         * Never apply this option.
         */
        NEVER,
        /**
         * Apply this option only for opposing teams.
         */
        FOR_OTHER_TEAMS,
        /**
         * Apply this option for only team members.
         */
        FOR_OWN_TEAM;
    }
}
