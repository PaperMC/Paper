package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimePathfindEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.event.HandlerList;

public abstract class PaperSlimePathfindEvent extends CraftEntityEvent implements SlimePathfindEvent {

    private boolean cancelled;

    protected PaperSlimePathfindEvent(final AbstractCubeMob cubeMob) {
        super(cubeMob);
    }

    @Override
    public AbstractCubeMob getEntity() {
        return (AbstractCubeMob) this.entity;
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
        return SlimePathfindEvent.getHandlerList();
    }
}
