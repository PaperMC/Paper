package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity attempts to ride another entity.
 */
public interface EntityMountEvent extends EntityEvent, Cancellable {

    /**
     * Gets the entity which will be ridden.
     *
     * @return mounted entity
     */
    Entity getMount();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
