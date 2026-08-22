package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityRemoveEvent;

public class CraftEntityRemoveEvent extends CraftEntityEvent implements EntityRemoveEvent {

    private final Cause cause;

    public CraftEntityRemoveEvent(final Entity entity, final Cause cause) {
        super(entity);
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityRemoveEvent.getHandlerList();
    }
}
