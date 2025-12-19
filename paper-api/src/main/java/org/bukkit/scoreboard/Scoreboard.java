package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A scoreboard
 */
public interface Scoreboard {

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated a displayName should be explicitly specified
     */
    @Deprecated(since = "1.13")
    @NotNull
    default Objective registerNewObjective(@NotNull String name, @NotNull String criteria) {
        return this.registerNewObjective(name, criteria, name);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName display name for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated use {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component)}
     */
    @NotNull
    @Deprecated
    default Objective registerNewObjective(@NotNull String name, @NotNull String criteria, net.kyori.adventure.text.@Nullable Component displayName) {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @param renderType Manner of rendering the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated use {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component, RenderType)}
     */
    @NotNull
    @Deprecated
    Objective registerNewObjective(@NotNull String name, @NotNull String criteria, net.kyori.adventure.text.@Nullable Component displayName, @NotNull RenderType renderType) throws IllegalArgumentException;

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     */
    @NotNull
    default Objective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, net.kyori.adventure.text.@Nullable Component displayName) throws IllegalArgumentException {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @param renderType Manner of rendering the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     */
    @NotNull
    Objective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, net.kyori.adventure.text.@Nullable Component displayName, @NotNull RenderType renderType) throws IllegalArgumentException;

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated use {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component)}
     */
    @Deprecated(since = "1.20.5")
    @NotNull
    default Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName) {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @param renderType Manner of rendering the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated use {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component, RenderType)}
     */
    @Deprecated(since = "1.20.5")
    @NotNull
    Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName, @NotNull RenderType renderType);

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated in favour of {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component)}
     */
    @NotNull
    @Deprecated // Paper
    default Objective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, @NotNull String displayName) {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @param renderType Manner of rendering the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is longer than 32767
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated in favour of {@link #registerNewObjective(String, Criteria, net.kyori.adventure.text.Component, RenderType)}
     */
    @NotNull
    @Deprecated // Paper
    default Objective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, @NotNull String displayName, @NotNull RenderType renderType) {
        return this.registerNewObjective(name, criteria, net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(displayName), renderType); // Paper - Adventure
    }

    /**
     * Gets an Objective on this Scoreboard by name
     *
     * @param name Name of the Objective
     * @return the Objective or null if it does not exist
     */
    @Nullable
    Objective getObjective(@NotNull String name);

    /**
     * Gets all Objectives of a Criteria on the Scoreboard
     *
     * @param criteria Criteria to search by
     * @return an immutable set of Objectives using the specified Criteria
     * @deprecated use {@link #getObjectivesByCriteria(Criteria)}
     */
    @Deprecated(since = "1.19.2")
    @NotNull
    Set<Objective> getObjectivesByCriteria(@NotNull String criteria);

    /**
     * Gets all Objectives of a Criteria on the Scoreboard
     *
     * @param criteria Criteria to search by
     * @return an immutable set of Objectives using the specified Criteria
     */
    @NotNull
    Set<Objective> getObjectivesByCriteria(@NotNull Criteria criteria);

    /**
     * Gets all Objectives on this Scoreboard
     *
     * @return An immutable set of all Objectives on this Scoreboard
     */
    @NotNull
    Set<Objective> getObjectives();

    /**
     * Gets the Objective currently displayed in a DisplaySlot on this
     * Scoreboard
     *
     * @param slot The DisplaySlot
     * @return the Objective currently displayed or null if nothing is
     *     displayed in that DisplaySlot
     */
    @Nullable
    Objective getObjective(@NotNull DisplaySlot slot);

    /**
     * Gets all scores for a player on this Scoreboard
     *
     * @param player the player whose scores are being retrieved
     * @return immutable set of all scores tracked for the player
     * @see #getScores(String)
     */
    // @Deprecated(since = "1.7.8") // Paper
    @NotNull
    Set<Score> getScores(@NotNull OfflinePlayer player);

    /**
     * Gets all scores for an entry on this Scoreboard
     *
     * @param entry the entry whose scores are being retrieved
     * @return immutable set of all scores tracked for the entry
     */
    @NotNull
    Set<Score> getScores(@NotNull String entry);

    /**
     * Removes all scores for a player on this Scoreboard
     *
     * @param player the player to drop all current scores for
     * @see #resetScores(String)
     */
    // @Deprecated(since = "1.7.8") // Paper
    void resetScores(@NotNull OfflinePlayer player);

    /**
     * Removes all scores for an entry on this Scoreboard
     *
     * @param entry the entry to drop all current scores for
     */
    void resetScores(@NotNull String entry);

    /**
     * Gets a player's Team on this Scoreboard
     *
     * @param player the player to search for
     * @return the player's Team or null if the player is not on a team
     * @see #getEntryTeam(String)
     */
    // @Deprecated(since = "1.8.6") // Paper
    @Nullable
    Team getPlayerTeam(@NotNull OfflinePlayer player);

    /**
     * Gets an entry's Team on this Scoreboard
     *
     * @param entry the entry to search for
     * @return the entry's Team or null if the entry is not on a team
     */
    @Nullable
    Team getEntryTeam(@NotNull String entry);

    /**
     * Gets a Team by name on this Scoreboard
     *
     * @param teamName Team name
     * @return the matching Team or null if no matches
     */
    @Nullable
    Team getTeam(@NotNull String teamName);

    /**
     * Gets all teams on this Scoreboard
     *
     * @return an immutable set of Teams
     */
    @NotNull
    Set<Team> getTeams();

    /**
     * Registers a Team on this Scoreboard
     *
     * @param name Team name
     * @return registered Team
     * @throws IllegalArgumentException if team by that name already exists
     */
    @NotNull
    Team registerNewTeam(@NotNull String name);

    /**
     * Gets all players tracked by this Scoreboard
     *
     * @return immutable set of all tracked players
     * @see #getEntries()
     * @deprecated Scoreboards can contain entries that aren't players
     */
    @Deprecated(since = "1.7.8")
    @NotNull
    Set<OfflinePlayer> getPlayers();

    /**
     * Gets all entries tracked by this Scoreboard
     *
     * @return immutable set of all tracked entries
     */
    @NotNull
    Set<String> getEntries();

    /**
     * Clears any objective in the specified slot.
     *
     * @param slot the slot to remove objectives
     */
    void clearSlot(@NotNull DisplaySlot slot);

    // Paper start - improve scoreboard entries
    /**
     * Gets all scores for an entity on this Scoreboard
     *
     * @param entity the entity whose scores are being retrieved
     * @return immutable set of all scores tracked for the entity
     * @throws IllegalArgumentException if entity is null
     * @see #getScores(String)
     */
    @NotNull Set<Score> getScoresFor(@NotNull org.bukkit.entity.Entity entity) throws IllegalArgumentException;

    /**
     * Removes all scores for an entity on this Scoreboard
     *
     * @param entity the entity to drop all current scores for
     * @throws IllegalArgumentException if entity is null
     * @see #resetScores(String)
     */
    void resetScoresFor(@NotNull org.bukkit.entity.Entity entity) throws IllegalArgumentException;

    /**
     * Gets an entity's Team on this Scoreboard
     *
     * @param entity the entity to search for
     * @return the entity's Team or null if the entity is not on a team
     * @throws IllegalArgumentException if entity is null
     * @see #getEntryTeam(String)
     */
    @Nullable Team getEntityTeam(@NotNull org.bukkit.entity.Entity entity) throws IllegalArgumentException;
    // Paper end - improve scoreboard entries
}
