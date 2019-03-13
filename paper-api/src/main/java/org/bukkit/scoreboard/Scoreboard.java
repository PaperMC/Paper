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
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if name is longer than 16
     *     characters.
     * @throws IllegalArgumentException if criteria is null
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     * @deprecated a displayName should be explicitly specified
     */
    @Deprecated
    @NotNull
    Objective registerNewObjective(@NotNull String name, @NotNull String criteria) throws IllegalArgumentException;

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if name is longer than 16
     *     characters.
     * @throws IllegalArgumentException if criteria is null
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 128
     *     characters.
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     */
    @NotNull
    Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName) throws IllegalArgumentException;

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name Name of the Objective
     * @param criteria Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @param renderType Manner of rendering the Objective
     * @return The registered Objective
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if name is longer than 16
     *     characters.
     * @throws IllegalArgumentException if criteria is null
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 128
     *     characters.
     * @throws IllegalArgumentException if renderType is null
     * @throws IllegalArgumentException if an objective by that name already
     *     exists
     */
    @NotNull
    Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName, @NotNull RenderType renderType) throws IllegalArgumentException;

    /**
     * Gets an Objective on this Scoreboard by name
     *
     * @param name Name of the Objective
     * @return the Objective or null if it does not exist
     * @throws IllegalArgumentException if name is null
     */
    @Nullable
    Objective getObjective(@NotNull String name) throws IllegalArgumentException;

    /**
     * Gets all Objectives of a Criteria on the Scoreboard
     *
     * @param criteria Criteria to search by
     * @return an immutable set of Objectives using the specified Criteria
     */
    @NotNull
    Set<Objective> getObjectivesByCriteria(@NotNull String criteria) throws IllegalArgumentException;

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
     * @throws IllegalArgumentException if slot is null
     */
    @Nullable
    Objective getObjective(@NotNull DisplaySlot slot) throws IllegalArgumentException;

    /**
     * Gets all scores for a player on this Scoreboard
     *
     * @param player the player whose scores are being retrieved
     * @return immutable set of all scores tracked for the player
     * @throws IllegalArgumentException if player is null
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #getScores(String)
     */
    @Deprecated
    @NotNull
    Set<Score> getScores(@NotNull OfflinePlayer player) throws IllegalArgumentException;

    /**
     * Gets all scores for an entry on this Scoreboard
     *
     * @param entry the entry whose scores are being retrieved
     * @return immutable set of all scores tracked for the entry
     * @throws IllegalArgumentException if entry is null
     */
    @NotNull
    Set<Score> getScores(@NotNull String entry) throws IllegalArgumentException;

    /**
     * Removes all scores for a player on this Scoreboard
     *
     * @param player the player to drop all current scores for
     * @throws IllegalArgumentException if player is null
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #resetScores(String)
     */
    @Deprecated
    void resetScores(@NotNull OfflinePlayer player) throws IllegalArgumentException;

    /**
     * Removes all scores for an entry on this Scoreboard
     *
     * @param entry the entry to drop all current scores for
     * @throws IllegalArgumentException if entry is null
     */
    void resetScores(@NotNull String entry) throws IllegalArgumentException;

    /**
     * Gets a player's Team on this Scoreboard
     *
     * @param player the player to search for
     * @return the player's Team or null if the player is not on a team
     * @throws IllegalArgumentException if player is null
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #getEntryTeam(String)
     */
    @Deprecated
    @Nullable
    Team getPlayerTeam(@NotNull OfflinePlayer player) throws IllegalArgumentException;

    /**
     * Gets a entries Team on this Scoreboard
     *
     * @param entry the entry to search for
     * @return the entries Team or null if the entry is not on a team
     * @throws IllegalArgumentException if entry is null
     */
    @Nullable
    Team getEntryTeam(@NotNull String entry) throws IllegalArgumentException;

    /**
     * Gets a Team by name on this Scoreboard
     *
     * @param teamName Team name
     * @return the matching Team or null if no matches
     * @throws IllegalArgumentException if teamName is null
     */
    @Nullable
    Team getTeam(@NotNull String teamName) throws IllegalArgumentException;

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
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if team by that name already exists
     */
    @NotNull
    Team registerNewTeam(@NotNull String name) throws IllegalArgumentException;

    /**
     * Gets all players tracked by this Scoreboard
     *
     * @return immutable set of all tracked players
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #getEntries()
     */
    @Deprecated
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
     * @throws IllegalArgumentException if slot is null
     */
    void clearSlot(@NotNull DisplaySlot slot) throws IllegalArgumentException;
}
