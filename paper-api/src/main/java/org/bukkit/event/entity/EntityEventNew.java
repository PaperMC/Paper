package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

/**
 * Represents an Entity-related event
 */
public interface EntityEventNew extends Event {

    /**
     * Returns the Entity involved in this event
     *
     * @return Entity who is involved in this event
     */
    Entity getEntity();

    /**
     * Gets the type of the Entity involved in this event.
     *
     * @return type of the Entity involved in this event
     */
    EntityType getEntityType();
}
