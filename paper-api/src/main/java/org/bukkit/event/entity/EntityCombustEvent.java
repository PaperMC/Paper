package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Cancellable;

/**
 * The event when a skeleton or zombie catch on fire due to the sun.
 * If the event is cancelled, the fire is stopped.
 */
public class EntityCombustEvent extends EntityEvent implements Cancellable {
    private boolean cancel;

    public EntityCombustEvent(Type type, Entity what) {
        super(type, what);
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
