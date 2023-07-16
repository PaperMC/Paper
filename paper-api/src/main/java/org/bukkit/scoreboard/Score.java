package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @see #getEntry()
     * @deprecated Scoreboards can contain entries that aren't players
     */
    @Deprecated
    @NotNull
    OfflinePlayer getPlayer();

    /**
     * Gets the entry being tracked by this Score
     *
     * @return this Score's tracked entry
     */
    @NotNull
    String getEntry();

    /**
     * Gets the Objective being tracked by this Score
     *
     * @return this Score's tracked objective
     */
    @NotNull
    Objective getObjective();

    /**
     * Gets the current score
     *
     * @return the current score
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    int getScore();

    /**
     * Sets the current score.
     *
     * @param score New score
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    void setScore(int score);

    /**
     * Shows if this score has been set at any point in time.
     *
     * @return if this score has been set before
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    boolean isScoreSet();

    /**
     * Gets the scoreboard for the associated objective.
     *
     * @return the owning objective's scoreboard, or null if it has been
     *     {@link Objective#unregister() unregistered}
     */
    @Nullable
    Scoreboard getScoreboard();
}
