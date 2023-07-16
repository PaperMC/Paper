package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;
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

    /**
     * Gets the name displayed to players for this objective
     *
     * @return this objective's display name
     * @throws IllegalStateException if this objective has been unregistered
     */
    @NotNull
    String getDisplayName();

    /**
     * Sets the name displayed to players for this objective.
     *
     * @param displayName Display name to set
     * @throws IllegalStateException if this objective has been unregistered
     */
    void setDisplayName(@NotNull String displayName);

    /**
     * Gets the criteria this objective tracks.
     *
     * @return this objective's criteria
     * @throws IllegalStateException if this objective has been unregistered
     * @deprecated use {@link #getTrackedCriteria()}
     */
    @Deprecated
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
     * @see Criterias#HEALTH
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
     * @deprecated Scoreboards can contain entries that aren't players
     */
    @Deprecated
    @NotNull
    Score getScore(@NotNull OfflinePlayer player);

    /**
     * Gets an entry's Score for an Objective on this Scoreboard.
     *
     * @param entry Entry for the Score
     * @return Score tracking the Objective and entry specified
     * @throws IllegalStateException if this objective has been unregistered
     * @throws IllegalArgumentException if entry is longer than 32767 characters.
     */
    @NotNull
    Score getScore(@NotNull String entry);
}
