package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class CraftEntityPickupItemEvent extends CraftEntityEvent implements EntityPickupItemEvent {

    private final Item item;
    private final int remaining;

    private boolean cancelled;

    public CraftEntityPickupItemEvent(final LivingEntity entity, final Item item, final int remaining) {
        super(entity);
        this.item = item;
        this.remaining = remaining;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public int getRemaining() {
        return this.remaining;
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
        return EntityPickupItemEvent.getHandlerList();
    }
}
