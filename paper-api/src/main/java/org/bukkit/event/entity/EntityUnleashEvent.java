package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called immediately prior to an entity being unleashed.
 * <p>
 * Cancelling this event when either:
 * <ul>
 *     <li>the leashed entity dies,</li>
 *     <li>the entity changes dimension, or</li>
 *     <li>the client has disconnected the leash</li>
 * </ul>
 * will have no effect.
 */
public interface EntityUnleashEvent extends EntityEvent, Cancellable {

    /**
     * Returns the reason for the unleashing.
     *
     * @return The reason
     */
    UnleashReason getReason();

    /**
     * Returns whether a leash item will be dropped.
     *
     * @return Whether the leash item will be dropped
     */
    boolean isDropLeash();

    /**
     * Sets whether a leash item should be dropped.
     *
     * @param dropLeash Whether the leash item should be dropped
     */
    void setDropLeash(boolean dropLeash);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum UnleashReason {
        /**
         * When the entity's leashholder has died or logged out, and so is
         * unleashed
         */
        HOLDER_GONE,
        /**
         * When the entity's leashholder attempts to unleash it
         */
        PLAYER_UNLEASH,
        /**
         * When the entity's leashholder is more than 10 blocks away
         */
        DISTANCE,
        /**
         * When the leashed entity is removed from the game
         */
        LEASHED_GONE,
        UNKNOWN
    }
}
