package org.bukkit.scoreboard;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A team on a scoreboard that has a common display theme and other
 * properties. This team is only relevant to the display of the associated
 * {@link #getScoreboard() scoreboard}.
 */
public interface Team extends net.kyori.adventure.audience.ForwardingAudience { // Paper - Make Team extend ForwardingAudience

    /**
     * Gets the name of this Team
     *
     * @return Objective name
     * @throws IllegalStateException if this team has been unregistered
     */
    @NotNull
    String getName();

    // Paper start - Adventure
    /**
     * Gets the display name for this team
     *
     * @return Team display name
     * @throws IllegalStateException if this team has been unregistered
     */
    net.kyori.adventure.text.@NotNull Component displayName();

    /**
     * Sets the name displayed to entries for this team
     *
     * @param displayName New display name
     * @throws IllegalStateException if this team has been unregistered
     */
    void displayName(net.kyori.adventure.text.@Nullable Component displayName);

    /**
     * Gets the prefix prepended to the display of entries on this team.
     *
     * @return Team prefix
     * @throws IllegalStateException if this team has been unregistered
     */
    net.kyori.adventure.text.@NotNull Component prefix();

    /**
     * Sets the prefix prepended to the display of entries on this team.
     *
     * @param prefix New prefix
     * @throws IllegalStateException if this team has been unregistered
     */
    void prefix(net.kyori.adventure.text.@Nullable Component prefix);

    /**
     * Gets the suffix appended to the display of entries on this team.
     *
     * @return the team's current suffix
     * @throws IllegalStateException if this team has been unregistered
     */
    net.kyori.adventure.text.@NotNull Component suffix();

    /**
     * Sets the suffix appended to the display of entries on this team.
     *
     * @param suffix the new suffix for this team.
     * @throws IllegalStateException if this team has been unregistered
     */
    void suffix(net.kyori.adventure.text.@Nullable Component suffix);

    /**
     * Checks if the team has a color specified
     *
     * @return true if it has a <b>color</b>
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean hasColor();

    /**
     * Gets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @return team color
     * @throws IllegalStateException if this team has been unregistered
     * @throws IllegalStateException if the team doesn't have a color
     * @see #hasColor()
     */
    net.kyori.adventure.text.format.@NotNull TextColor color();

    /**
     * Sets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @param color new color, null for no color
     */
    void color(net.kyori.adventure.text.format.@Nullable NamedTextColor color);
    // Paper end - Adventure

    /**
     * Gets the name displayed to entries for this team
     *
     * @return Team display name
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #displayName()}
     */
    @NotNull
    @Deprecated // Paper
    String getDisplayName();

    /**
     * Sets the name displayed to entries for this team
     *
     * @param displayName New display name
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #displayName(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    void setDisplayName(@NotNull String displayName);

    /**
     * Gets the prefix prepended to the display of entries on this team.
     *
     * @return Team prefix
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #prefix()}
     */
    @NotNull
    @Deprecated // Paper
    String getPrefix();

    /**
     * Sets the prefix prepended to the display of entries on this team.
     *
     * @param prefix New prefix
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #prefix(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    void setPrefix(@NotNull String prefix);

    /**
     * Gets the suffix appended to the display of entries on this team.
     *
     * @return the team's current suffix
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #suffix()}
     */
    @NotNull
    @Deprecated // Paper
    String getSuffix();

    /**
     * Sets the suffix appended to the display of entries on this team.
     *
     * @param suffix the new suffix for this team.
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #suffix(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    void setSuffix(@NotNull String suffix);

    /**
     * Gets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @return team color, defaults to {@link ChatColor#RESET}
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated in favour of {@link #color()}
     */
    @NotNull
    @Deprecated // Paper
    ChatColor getColor();

    /**
     * Sets the color of the team.
     * <br>
     * This only sets the team outline, other occurrences of colors such as in
     * names are handled by prefixes / suffixes.
     *
     * @param color new color, must be non-null. Use {@link ChatColor#RESET} for
     * no color
     * @deprecated in favour of {@link #color(net.kyori.adventure.text.format.NamedTextColor)}
     */
    @Deprecated // Paper
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
    @Deprecated(since = "1.9")
    @NotNull
    NameTagVisibility getNameTagVisibility();

    /**
     * Set's the team's ability to see name tags
     *
     * @param visibility The nameTagVisibility to set
     * @throws IllegalArgumentException if this team has been unregistered
     * @deprecated see {@link #setOption(Team.Option, Team.OptionStatus)}
     */
    @Deprecated(since = "1.9")
    void setNameTagVisibility(@NotNull NameTagVisibility visibility);

    /**
     * Gets the Set of players on the team
     *
     * @return players on the team
     * @throws IllegalStateException if this team has been unregistered
     * @see #getEntries()
     * @deprecated Teams can contain entries that aren't players
     */
    @Deprecated(since = "1.8.6")
    @NotNull
    Set<OfflinePlayer> getPlayers();

    /**
     * Gets the Set of entries on the team
     *
     * @return entries on the team
     * @throws IllegalStateException if this team has been unregistered
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
     * @deprecated use {@link #addEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default void addPlayer(@NotNull OfflinePlayer player) {
        this.addEntry(player);
    }

    /**
     * This puts the specified entry onto this team for the scoreboard.
     * <p>
     * This will remove the entry from any other team on the scoreboard.
     *
     * @param entry the entry to add
     * @throws IllegalStateException if this team has been unregistered
     */
    default void addEntry(@NotNull String entry) {
        this.addEntry(ScoreHolder.scoreHolder(entry));
    }

    /**
     * This puts the specified score holder onto this team for the scoreboard.
     * <p>
     * This will remove the entry from any other team on the scoreboard.
     *
     * @param holder the score holder to add
     */
    void addEntry(@NotNull ScoreHolder holder);

    /**
     * This puts a collection of score holders onto this team for the scoreboard which results in one
     * packet for the updates rather than a packet-per-holder.
     * <p>
     * Score holders on other teams will be removed from their respective teams.
     *
     * @param holders the score holders to add
     * @throws IllegalArgumentException if entities are null
     * @throws IllegalStateException if this team has been unregistered
     */
    void addScoreHolders(@NotNull Collection<? extends ScoreHolder> holders);

    boolean removeScoreHolders(@NotNull Collection<? extends ScoreHolder> holders);

    /**
     * This puts a collection of entities onto this team for the scoreboard which results in one
     * packet for the updates rather than a packet-per-entity.
     * <p>
     * Entities on other teams will be removed from their respective teams.
     *
     * @param entities the entities to add
     * @throws IllegalArgumentException if entities are null
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated use {@link #addScoreHolders(Collection)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default void addEntities(@NotNull Entity @NotNull ...entities) {
        this.addScoreHolders(List.of(entities));
    }

    /**
     * This puts a collection of entities onto this team for the scoreboard which results in one
     * packet for the updates rather than a packet-per-entity.
     * <p>
     * Entities on other teams will be removed from their respective teams.
     *
     * @param entities the entities to add
     * @throws IllegalArgumentException if entities are null
     * @throws IllegalStateException if this team has been unregistered
     * @deprecated use {@link #addScoreHolders(Collection)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default void addEntities(@NotNull Collection<Entity> entities) {
        this.addScoreHolders(entities);
    }

    /**
     * This puts a collection of entries onto this team for the scoreboard which results in one
     * packet for the updates rather than a packet-per-entry.
     * <p>
     * Entries on other teams will be removed from their respective teams.
     *
     * @param entries the entries to add
     * @throws IllegalArgumentException if entries are null
     * @throws IllegalStateException if this team has been unregistered
     */
    default void addEntries(@NotNull String... entries) {
        this.addEntries(List.of(entries));
    }

    /**
     * This puts a collection of entries onto this team for the scoreboard which results in one
     * packet for the updates rather than a packet-per-entry.
     * <p>
     * Entries on other teams will be removed from their respective teams.
     *
     * @param entries the entries to add
     * @throws IllegalArgumentException if entries are null
     * @throws IllegalStateException if this team has been unregistered
     */
    void addEntries(@NotNull java.util.Collection<String> entries);

    /**
     * Removes the player from this team.
     *
     * @param player the player to remove
     * @return if the player was on this team
     * @throws IllegalStateException if this team has been unregistered
     * @see #removeEntry(String)
     * @deprecated use {@link #removeEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default boolean removePlayer(@NotNull OfflinePlayer player) {
        return this.removeEntry(player);
    }

    /**
     * Removes the entry from this team.
     *
     * @param entry the entry to remove
     * @return if the entry was a part of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    default boolean removeEntry(@NotNull String entry) {
        return this.removeEntry(ScoreHolder.scoreHolder(entry));
    }

    /**
     * Removes the score holder from this team.
     *
     * @param holder the holder to remove
     * @return if the entry was a part of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean removeEntry(@NotNull ScoreHolder holder);

    /**
     * Removes a collection of entities from this team which results in one
     * packet for the updates rather than a packet-per-entity.
     *
     * @param entities the entries to remove
     * @return if any of the entities were a part of this team
     * @throws IllegalArgumentException if entities is null
     * @throws IllegalStateException if this team has been unregistered
     */
    default boolean removeEntities(@NotNull Entity @NotNull ... entities) {
        return this.removeEntities(List.of(entities));
    }

    /**
     * Removes a collection of entities from this team which results in one
     * packet for the updates rather than a packet-per-entity.
     *
     * @param entities the entries to remove
     * @return if any of the entities were a part of this team
     * @throws IllegalArgumentException if entities is null
     * @throws IllegalStateException if this team has been unregistered
     */
    default boolean removeEntities(@NotNull Collection<Entity> entities) {
        return this.removeScoreHolders(entities);
    }

    /**
     * Removes a collection of entries from this team which results in one
     * packet for the updates rather than a packet-per-entry.
     *
     * @param entries the entries to remove
     * @return if any of the entries were a part of this team
     * @throws IllegalArgumentException if entries is null
     * @throws IllegalStateException if this team has been unregistered
     */
    default boolean removeEntries(@NotNull String... entries) throws IllegalStateException, IllegalArgumentException {
        return this.removeEntries(java.util.List.of(entries));
    }

    /**
     * Removes a collection of entries from this team which results in one
     * packet for the updates rather than a packet-per-entry.
     *
     * @param entries the entries to remove
     * @return if any of the entries were a part of this team
     * @throws IllegalArgumentException if entries is null
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean removeEntries(@NotNull java.util.Collection<String> entries) throws IllegalStateException, IllegalArgumentException;

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
     * @deprecated use {@link #hasEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default boolean hasPlayer(@NotNull OfflinePlayer player) {
        return this.hasEntry(player);
    }

    /**
     * Checks to see if the specified entry is a member of this team.
     *
     * @param entry the entry to search for
     * @return true if the entry is a member of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    default boolean hasEntry(@NotNull String entry) {
        return this.hasEntry(ScoreHolder.scoreHolder(entry));
    }

    /**
     * Checks to see if the specified score holder is a member of this team.
     *
     * @param holder the score holder to search for
     * @return true if the score holder is a member of this team
     * @throws IllegalStateException if this team has been unregistered
     */
    boolean hasEntry(@NotNull ScoreHolder holder);

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
     * This puts the specified entity onto this team for the scoreboard.
     * <p>
     * This will remove the entity from any other team on the scoreboard.
     *
     * @param entity the entity to add
     * @throws IllegalArgumentException if entity is null
     * @throws IllegalStateException if this team has been unregistered
     * @see #addEntry(String)
     * @deprecated use {@link #addEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default void addEntity(@NotNull org.bukkit.entity.Entity entity) {
        this.addEntry(entity);
    }

    /**
     * Removes the entity from this team.
     *
     * @param entity the entity to remove
     * @return if the entity was on this team
     * @throws IllegalArgumentException if entity is null
     * @throws IllegalStateException if this team has been unregistered
     * @see #removeEntry(String)
     * @deprecated use {@link #removeEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default boolean removeEntity(@NotNull org.bukkit.entity.Entity entity) {
        return this.removeEntry(entity);
    }

    /**
     * Checks to see if the specified entity is a member of this team.
     *
     * @param entity the entity to search for
     * @return true if the entity is a member of this team
     * @throws IllegalArgumentException if entity is null
     * @throws IllegalStateException if this team has been unregistered
     * @see #hasEntry(String)
     * @deprecated use {@link #hasEntry(ScoreHolder)}
     */
    @Deprecated(since = "1.21.8", forRemoval = true)
    default boolean hasEntity(@NotNull org.bukkit.entity.Entity entity) {
        return this.hasEntry(entity);
    }

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
