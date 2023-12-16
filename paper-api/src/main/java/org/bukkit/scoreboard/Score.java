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
    @Deprecated(since = "1.7.8")
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

    // Paper start
    /**
     * Resets this score, if a value has been set.
     *
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    void resetScore() throws IllegalStateException;
    // Paper end

    // Paper start - add more score API
    /**
     * Gets if this score is triggerable and cannot
     * be used by the {@code /trigger} command executed
     * by the owner of this score.
     *
     * @return true if triggerable, false if not triggerable, score isn't set, or the objective isn't {@link Criteria#TRIGGER}
     * @throws IllegalStateException if the associated objective has been unregistered
     */
    boolean isTriggerable();

    /**
     * Sets if this score is triggerable and can
     * be used by the {@code /trigger} command
     * executed by the owner of this score. Can
     * only be set on {@link Criteria#TRIGGER} objectives.
     * <p>
     * If the score doesn't exist (aka {@link #isScoreSet()} returns false),
     * this will create the score with a 0 value.
     *
     * @param triggerable true to enable trigger, false to disable
     * @throws IllegalArgumentException if this objective isn't {@link Criteria#TRIGGER}
     * @throws IllegalStateException if the associated objective has been unregistered
     */
    void setTriggerable(boolean triggerable);

    /**
     * Get the custom name for this entry.
     *
     * @return the custom name or null if not set (or score isn't set)
     * @throws IllegalStateException if the associated objective has been unregistered
     */
    @Nullable net.kyori.adventure.text.Component customName();

    /**
     * Sets the custom name for this entry.
     * <p>
     * If the score doesn't exist (aka {@link #isScoreSet()} returns false),
     * this will create the score with a 0 value.
     *
     * @param customName the custom name or null to reset
     * @throws IllegalStateException if the associated objective has been unregistered
     */
    void customName(net.kyori.adventure.text.@Nullable Component customName);
    // Paper end - add more score API

    // Paper start - number format api
    /**
     * Gets the number format for this score or null if the score has not been set yet
     * or the objective's default is being used.
     *
     * @return this score's number format, or null if the objective's default is used or the score doesn't exist
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    @Nullable io.papermc.paper.scoreboard.numbers.NumberFormat numberFormat();

    /**
     * Sets the number format for this score. If this score has not been set yet {@link #isScoreSet()}, it will be created
     *
     * @param format the number format to set, pass null to reset format to default
     * @throws IllegalStateException if the associated objective has been
     *     unregistered
     */
    void numberFormat(@Nullable io.papermc.paper.scoreboard.numbers.NumberFormat format);
    // Paper end - number format api

}
