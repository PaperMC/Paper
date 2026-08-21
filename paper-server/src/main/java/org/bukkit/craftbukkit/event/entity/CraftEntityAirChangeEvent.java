package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityAirChangeEvent;

public class CraftEntityAirChangeEvent extends CraftEntityEvent implements EntityAirChangeEvent {

    private int amount;
    private boolean cancelled;

    public CraftEntityAirChangeEvent(final Entity entity, final int amount) {
        super(entity);
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
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
        return EntityAirChangeEvent.getHandlerList();
    }
}
