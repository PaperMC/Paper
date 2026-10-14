package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an Entity-related event
 */
public abstract class EntityEvent extends Event {

    protected Entity entity;

    protected EntityEvent(@NotNull final Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the Entity involved in this event
     *
     * @return Entity who is involved in this event
     */
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Gets the EntityType of the Entity involved in this event.
     *
     * @return EntityType of the Entity involved in this event
     */
    @NotNull
    public EntityType getEntityType() {
        return this.entity.getType();
    }
}
