package org.bukkit.craftbukkit.event.entity;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityEvent;

public abstract class CraftEntityEvent extends CraftEvent implements EntityEvent {

    protected final Entity entity;

    protected CraftEntityEvent(final Entity entity) {
        this.entity = entity;
    }

    protected CraftEntityEvent(final net.minecraft.world.entity.Entity entity) {
        this(entity.getBukkitEntity());
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }
}
