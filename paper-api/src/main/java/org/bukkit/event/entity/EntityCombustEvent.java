package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * The event when a skeleton or zombie catch on fire due to the sun.
 * If the event is cancelled, the fire is stopped.
 */
public class EntityCombustEvent extends EntityEvent implements Cancellable {
    private boolean cancel;

    public EntityCombustEvent(Entity what) {
        super(Type.ENTITY_COMBUST, what);
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
