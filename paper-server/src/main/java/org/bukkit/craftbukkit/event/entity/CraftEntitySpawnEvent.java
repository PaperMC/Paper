package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntitySpawnEvent;

public class CraftEntitySpawnEvent extends CraftEntityEvent implements EntitySpawnEvent {

    private boolean cancelled;

    public CraftEntitySpawnEvent(final Entity spawnee) {
        super(spawnee);
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
        return EntitySpawnEvent.getHandlerList();
    }
}
