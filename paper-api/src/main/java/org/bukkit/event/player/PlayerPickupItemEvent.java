package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * Thrown when a player picks an item up from the ground
 * @deprecated {@link EntityPickupItemEvent}
 */
@Warning
@Deprecated(since = "1.12")
public interface PlayerPickupItemEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the Item picked up by the player.
     *
     * @return Item
     */
    Item getItem();

    /**
     * Gets the amount remaining on the ground, if any
     *
     * @return amount remaining on the ground
     */
    int getRemaining();

    /**
     * Set if the item will fly at the player
     * <p>
     * Cancelling the event will set this value to {@code false}.
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
