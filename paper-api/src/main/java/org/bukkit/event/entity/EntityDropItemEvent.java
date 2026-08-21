package org.bukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when an entity creates an item drop.
 */
public interface EntityDropItemEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the Item created by the entity
     *
     * @return Item created by the entity
     */
    Item getItemDrop();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
