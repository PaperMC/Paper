package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDropItemEvent;

public class CraftEntityDropItemEvent extends CraftEntityEvent implements EntityDropItemEvent {

    private final Item drop;
    private boolean cancelled;

    public CraftEntityDropItemEvent(final Entity entity, final Item drop) {
        super(entity);
        this.drop = drop;
    }

    @Override
    public Item getItemDrop() {
        return this.drop;
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
        return EntityDropItemEvent.getHandlerList();
    }
}
