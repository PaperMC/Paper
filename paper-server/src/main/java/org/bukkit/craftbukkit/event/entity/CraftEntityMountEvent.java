package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityMountEvent;

public class CraftEntityMountEvent extends CraftEntityEvent implements EntityMountEvent {

    private final Entity mount;
    private boolean cancelled;

    public CraftEntityMountEvent(final Entity entity, final Entity mount) {
        super(entity);
        this.mount = mount;
    }

    @Override
    public Entity getMount() {
        return this.mount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityMountEvent.getHandlerList();
    }
}
