package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a player drops an item from their inventory
 */
public interface PlayerDropItemEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the item dropped by the player.
     *
     * @return the item dropped by the player
     */
    Item getItemDrop();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
