package org.bukkit.craftbukkit.event.entity;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityEventNew;

public abstract class CraftEntityEvent extends CraftEvent implements EntityEventNew {

    protected Entity entity;

    protected CraftEntityEvent(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public EntityType getEntityType() {
        return this.entity.getType();
    }
}
