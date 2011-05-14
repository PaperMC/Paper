package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

/**
 * Represents an Entity-related event
 */
public class EntityEvent extends Event {
    protected Entity entity;

    public EntityEvent(final Event.Type type, final Entity what) {
        super(type);
        entity = what;
    }

    /**
     * Returns the Entity involved in this event
     * @return Entity who is involved in this event
     */
    public final Entity getEntity() {
        return entity;
    }
}
