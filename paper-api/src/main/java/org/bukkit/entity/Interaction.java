package org.bukkit.entity;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an entity designed to only record interactions.
 */
public interface Interaction extends Entity {

    /**
     * Gets the width of this interaction entity.
     *
     * @return width
     */
    public float getInteractionWidth();

    /**
     * Sets the width of this interaction entity.
     *
     * @param width new width
     */
    public void setInteractionWidth(float width);

    /**
     * Gets the height of this interaction entity.
     *
     * @return height
     */
    public float getInteractionHeight();

    /**
     * Sets the height of this interaction entity.
     *
     * @param height new height
     */
    public void setInteractionHeight(float height);

    /**
     * Gets if this interaction entity should trigger a response when interacted
     * with.
     *
     * @return response setting
     */
    public boolean isResponsive();

    /**
     * Sets if this interaction entity should trigger a response when interacted
     * with.
     *
     * @param response new setting
     */
    public void setResponsive(boolean response);

    /**
     * Gets the last attack on this interaction entity.
     *
     * @return last attack data, if present
     */
    @Nullable
    public PreviousInteraction getLastAttack();

    /**
     * Gets the last interaction on this entity.
     *
     * @return last interaction data, if present
     */
    @Nullable
    public PreviousInteraction getLastInteraction();

    /**
     * Represents a previous interaction with this entity.
     */
    public interface PreviousInteraction {

        /**
         * Get the previous interacting player.
         *
         * @return interacting player
         */
        @NotNull
        public OfflinePlayer getPlayer();

        /**
         * Gets the Unix timestamp at when this interaction occurred.
         *
         * @return interaction timestamp
         */
        public long getTimestamp();
    }
}
