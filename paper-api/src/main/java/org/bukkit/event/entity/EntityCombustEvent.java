package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when an entity combusts due to the sun.
 *<p />
 * If an Entity Combust event is cancelled, the entity will not combust.
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
