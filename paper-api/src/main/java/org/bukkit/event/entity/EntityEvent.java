package org.bukkit.event.entity;

import org.bukkit.LivingEntity;
import org.bukkit.event.Event;

/**
 * Represents an LivingEntity-related event
 */
public class EntityEvent extends Event {
    protected LivingEntity entity;

    public EntityEvent(final Event.Type type, final LivingEntity what)
    {
        super(type);
        entity = what;
    }

    /**
     * Returns the LivingEntity involved in this event
     * @return LivingEntity who is involved in this event
     */
    public final LivingEntity getEntity()
    {
        return entity;
    }
}
