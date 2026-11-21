package org.bukkit.entity;

import net.kyori.adventure.util.TriState;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents an entity designed to only record interactions.
 */
@NullMarked
public interface Interaction extends Entity {

    /**
     * Gets the width of this interaction entity.
     *
     * @return width
     */
    float getInteractionWidth();

    /**
     * Sets the width of this interaction entity.
     *
     * @param width new width
     */
    void setInteractionWidth(float width);

    /**
     * Gets the height of this interaction entity.
     *
     * @return height
     */
    float getInteractionHeight();

    /**
     * Sets the height of this interaction entity.
     *
     * @param height new height
     */
    void setInteractionHeight(float height);

    /**
     * Gets if this interaction entity should trigger a response when interacted
     * with.
     *
     * @return response setting
     */
    boolean isResponsive();

    /**
     * Sets if this interaction entity should trigger a response when interacted
     * with.
     *
     * @param response new setting
     */
    void setResponsive(boolean response);

    /**
     * Gets if this interaction entity can be hit by projectiles.
     *
     * @return A TriState indicating the current state if this entity can be hit by projectiles
     */
    TriState canBeHitByProjectile();

    /**
     * Sets if this interaction entity can be hit by projectiles.
     * <ul>
     *     <li>{@link TriState#NOT_SET} – will revert to default</li>
     *     <li>{@link TriState#TRUE} – will make the entity can be hit by projectiles</li>
     *     <li>{@link TriState#FALSE} – will make the entity cannot be hit by projectiles</li>
     * </ul>
     *
     * @param canBeHitByProjectile a TriState value representing the state of an interaction can be hit by projectiles
     */
    void setCanBeHitByProjectile(final TriState canBeHitByProjectile);

    /**
     * Gets the last attack on this interaction entity.
     *
     * @return last attack data, if present
     */
    @Nullable PreviousInteraction getLastAttack();

    /**
     * Gets the last interaction on this entity.
     *
     * @return last interaction data, if present
     */
    @Nullable PreviousInteraction getLastInteraction();

    /**
     * Represents a previous interaction with this entity.
     */
    interface PreviousInteraction {

        /**
         * Get the previous interacting player.
         *
         * @return interacting player
         */
        OfflinePlayer getPlayer();

        /**
         * Gets the Unix timestamp at when this interaction occurred.
         *
         * @return interaction timestamp
         */
        long getTimestamp();
    }
}
