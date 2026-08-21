package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity stops riding another entity.
 */
public interface EntityDismountEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the entity which will no longer be ridden.
     *
     * @return dismounted entity
     */
    Entity getDismounted();

    boolean isCancellable();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
