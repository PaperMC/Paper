package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a player attempts to pick an item up from the ground
 */
public interface PlayerAttemptPickupItemEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the Item attempted by the player.
     *
     * @return Item
     */
    Item getItem();

    /**
     * Gets the amount that will remain on the ground, if any
     *
     * @return amount that will remain on the ground
     */
    int getRemaining();

    /**
     * Set if the item will fly at the player
     * <p>Cancelling the event will set this value to {@code false}.</p>
     *
     * @param flyAtPlayer {@code true} for item to fly at player
     */
    void setFlyAtPlayer(boolean flyAtPlayer);

    /**
     * Gets if the item will fly at the player
     *
     * @return {@code true} if the item will fly at the player
     */
    boolean getFlyAtPlayer();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
