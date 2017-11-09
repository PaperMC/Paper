package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

/**
 * A score entry for an {@link #getEntry() entry} on an {@link
 * #getObjective() objective}. Changing this will not affect any other
 * objective or scoreboard.
 */
public interface Score {

    /**
     * Gets the OfflinePlayer being tracked by this Score
     *
     * @return this Score's tracked player
     * @deprecated Scoreboards can contain entries that aren't players
     * @see #getEntry()
     */
    @Deprecated
    OfflinePlayer getPlayer();

    /**
     * Gets the entry being tracked by this Score
     *
     * @return this Score's tracked entry
     */
    String getEntry();

    /**
     * Gets the Objective being tracked by this Score
     *
     * @return this Score's tracked objective
     */
    Objective getObjective();

    /**
     * Gets the current score
     *
     * @return the current score
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    int getScore() throws IllegalStateException;

    /**
     * Sets the current score.
     *
     * @param score New score
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    void setScore(int score) throws IllegalStateException;

    /**
     * Shows if this score has been set at any point in time.
     *
     * @return if this score has been set before
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    boolean isScoreSet() throws IllegalStateException;

    /**
     * Gets the scoreboard for the associated objective.
     *
     * @return the owning objective's scoreboard, or null if it has been
     *     {@link Objective#unregister() unregistered}
     */
    Scoreboard getScoreboard();
}
