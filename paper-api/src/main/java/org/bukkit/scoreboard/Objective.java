package org.bukkit.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An objective on a scoreboard that can show scores specific to entries. This
 * objective is only relevant to the display of the associated {@link
 * #getScoreboard() scoreboard}.
 */
public interface Objective {

    /**
     * Gets the name of this Objective
     *
     * @return this objective's name
     * @throws IllegalStateException if this objective has been unregistered
     */
    @NotNull
    String getName();

    // Paper start - Adventure
    /**
     * Gets the display name for this objective
     *
     * @return this objective's display name
     * @throws IllegalStateException if this objective has been unregistered
     */
    net.kyori.adventure.text.@NotNull Component displayName();
    /**
     * Sets the name displayed to players for this objective.
     *
     * @param displayName Display name to set
     * @throws IllegalStateException if this objective has been unregistered
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 128
     *     characters.
     */
    void displayName(net.kyori.adventure.text.@Nullable Component displayName);
    // Paper end - Adventure

    /**
     * Gets the name displayed to players for this objective
     *
     * @return this objective's display name
     * @throws IllegalStateException if this objective has been unregistered
     * @deprecated in favour of {@link #displayName()}
     */
    @NotNull
    @Deprecated // Paper
    String getDisplayName();

    /**
     * Sets the name displayed to players for this objective.
     *
     * @param displayName Display name to set
     * @throws IllegalStateException if this objective has been unregistered
     * @deprecated in favour of {@link #displayName(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    void setDisplayName(@NotNull String displayName);

    /**
     * Gets the criteria this objective tracks.
     *
     * @return this objective's criteria
     * @throws IllegalStateException if this objective has been unregistered
     * @deprecated use {@link #getTrackedCriteria()}
     */
    @Deprecated(since = "1.19.2")
    @NotNull
    String getCriteria();

    /**
     * Gets the criteria this objective tracks.
     *
     * @return this objective's criteria
     * @throws IllegalStateException if this objective has been unregistered
     */
    @NotNull
    Criteria getTrackedCriteria();

    /**
     * Gets if the objective's scores can be modified directly by a plugin.
     *
     * @return true if scores are modifiable
     * @throws IllegalStateException if this objective has been unregistered
     * @see Criteria#HEALTH
     */
    boolean isModifiable();

    /**
     * Gets the scoreboard to which this objective is attached.
     *
     * @return Owning scoreboard, or null if it has been {@link #unregister()
     *     unregistered}
     */
    @Nullable
    Scoreboard getScoreboard();

    /**
     * Unregisters this objective from the {@link Scoreboard scoreboard.}
     *
     * @throws IllegalStateException if this objective has been unregistered
     */
    void unregister();

    /**
     * Sets this objective to display on the specified slot for the
     * scoreboard, removing it from any other display slot.
     *
     * @param slot display slot to change, or null to not display
     * @throws IllegalStateException if this objective has been unregistered
     */
    void setDisplaySlot(@Nullable DisplaySlot slot);

    /**
     * Gets the display slot this objective is displayed at.
     *
     * @return the display slot for this objective, or null if not displayed
     * @throws IllegalStateException if this objective has been unregistered
     */
    @Nullable
    DisplaySlot getDisplaySlot();

    /**
     * Sets manner in which this objective will be rendered.
     *
     * @param renderType new render type
     * @throws IllegalStateException if this objective has been unregistered
     */
    void setRenderType(@NotNull RenderType renderType);

    /**
     * Sets manner in which this objective will be rendered.
     *
     * @return the render type
     * @throws IllegalStateException if this objective has been unregistered
     */
    @NotNull
    RenderType getRenderType();

    /**
     * Gets a player's Score for an Objective on this Scoreboard
     *
     * @param player Player for the Score
     * @return Score tracking the Objective and player specified
     * @throws IllegalStateException if this objective has been unregistered
     * @see #getScore(String)
     * @apiNote use {@link #getScore(ScoreHolder)} instead
     */
    @NotNull
    @ApiStatus.Obsolete(since = "1.21.11")
    default Score getScore(@NotNull OfflinePlayer player) {
        return this.getScore((ScoreHolder) player);
    }

    /**
     * Gets an entry's Score for an Objective on this Scoreboard.
     *
     * @param entry Entry for the Score
     * @return Score tracking the Objective and entry specified
     * @throws IllegalStateException if this objective has been unregistered
     * @throws IllegalArgumentException if entry is longer than 32767 characters.
     */
    @NotNull
    default Score getScore(@NotNull String entry) {
        return this.getScore(ScoreHolder.scoreHolder(entry));
    }

    /**
     * Gets a score holder's Score for an Objective on this Scoreboard.
     *
     * @param holder score holder for the Score
     * @return Score tracking the Objective and score holder specified
     * @throws IllegalStateException if this objective has been unregistered
     * @throws IllegalArgumentException if the holder's name is longer than 32767 characters.
     */
    @NotNull
    Score getScore(@NotNull ScoreHolder holder);

    /**
     * Gets an entity's Score for an Objective on this Scoreboard.
     *
     * @param entity Entity for the Score
     * @return Score tracking the Objective and entity specified
     * @throws IllegalArgumentException if entity is null
     * @throws IllegalStateException if this objective has been unregistered
     * @apiNote use {@link #getScore(ScoreHolder)} instead
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    default @NotNull Score getScoreFor(@NotNull org.bukkit.entity.Entity entity) {
        return this.getScore(entity);
    }

    /**
     * Gets if this objective will auto update score
     * displays on changes.
     *
     * @return true if auto updating
     * @throws IllegalStateException if this objective has been unregistered
     */
    boolean willAutoUpdateDisplay();

    /**
     * Sets if this objective will auto update
     * score displays on changes.
     *
     * @param autoUpdateDisplay true to auto update
     * @throws IllegalStateException if this objective has been unregistered
     */
    void setAutoUpdateDisplay(boolean autoUpdateDisplay);

    /**
     * Gets the number format for this objective's scores or null if the client default is used.
     *
     * @return this objective's number format, or null if the client default is used
     * @throws IllegalStateException if this objective has been unregistered
     */
    @Nullable NumberFormat numberFormat();

    /**
     * Sets the number format for this objective's scores.
     *
     * @param format the number format to set, pass null to reset format to default
     * @throws IllegalStateException if this objective has been unregistered
     */
    void numberFormat(@Nullable NumberFormat format);
}
