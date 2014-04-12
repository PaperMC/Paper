package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

/**
 * An objective on a scoreboard that can show scores specific to entries. This
 * objective is only relevant to the display of the associated {@link
 * #getScoreboard() scoreboard}.
 */
public interface Objective {

    /**
     * Gets the name of this Objective
     *
     * @return this objective'ss name
     * @throws IllegalStateException if this objective has been unregistered
     */
    String getName() throws IllegalStateException;

    /**
     * Gets the name displayed to players for this objective
     *
     * @return this objective's display name
     * @throws IllegalStateException if this objective has been unregistered
     */
    String getDisplayName() throws IllegalStateException;

    /**
     * Sets the name displayed to players for this objective.
     *
     * @param displayName Display name to set
     * @throws IllegalStateException if this objective has been unregistered
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 32
     *     characters.
     */
    void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException;

    /**
     * Gets the criteria this objective tracks.
     *
     * @return this objective's criteria
     * @throws IllegalStateException if this objective has been unregistered
     */
    String getCriteria() throws IllegalStateException;

    /**
     * Gets if the objective's scores can be modified directly by a plugin.
     *
     * @return true if scores are modifiable
     * @throws IllegalStateException if this objective has been unregistered
     * @see Criterias#HEALTH
     */
    boolean isModifiable() throws IllegalStateException;

    /**
     * Gets the scoreboard to which this objective is attached.
     *
     * @return Owning scoreboard, or null if it has been {@link #unregister()
     *     unregistered}
     */
    Scoreboard getScoreboard();

    /**
     * Unregisters this objective from the {@link Scoreboard scoreboard.}
     *
     * @throws IllegalStateException if this objective has been unregistered
     */
    void unregister() throws IllegalStateException;

    /**
     * Sets this objective to display on the specified slot for the
     * scoreboard, removing it from any other display slot.
     *
     * @param slot display slot to change, or null to not display
     * @throws IllegalStateException if this objective has been unregistered
     */
    void setDisplaySlot(DisplaySlot slot) throws IllegalStateException;

    /**
     * Gets the display slot this objective is displayed at.
     *
     * @return the display slot for this objective, or null if not displayed
     * @throws IllegalStateException if this objective has been unregistered
     */
    DisplaySlot getDisplaySlot() throws IllegalStateException;

    /**
     * Gets a player's Score for an Objective on this Scoreboard
     *
     * @param player Player for the Score
     * @return Score tracking the Objective and player specified
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException if this objective has been unregistered
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #getScore(String)
     */
    @Deprecated
    Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException;

    /**
     * Gets an entry's Score for an Objective on this Scoreboard.
     *
     * @param entry Entry for the Score
     * @return Score tracking the Objective and entry specified
     * @throws IllegalArgumentException if entry is null
     * @throws IllegalStateException if this objective has been unregistered
     */
    Score getScore(String entry) throws IllegalArgumentException, IllegalStateException;
}
