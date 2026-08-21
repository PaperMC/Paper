package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityEnterLoveModeEvent extends CraftEntityEvent implements EntityEnterLoveModeEvent {

    private final HumanEntity humanEntity;
    private int ticksInLove;

    private boolean cancelled;

    public CraftEntityEnterLoveModeEvent(final Animals animalInLove, final @Nullable HumanEntity humanEntity, final int ticksInLove) {
        super(animalInLove);
        this.humanEntity = humanEntity;
        this.ticksInLove = ticksInLove;
    }

    @Override
    public Animals getEntity() {
        return (Animals) this.entity;
    }

    @Override
    public @Nullable HumanEntity getHumanEntity() {
        return this.humanEntity;
    }

    @Override
    public int getTicksInLove() {
        return this.ticksInLove;
    }

    @Override
    public void setTicksInLove(final int ticksInLove) {
        this.ticksInLove = ticksInLove;
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
        return EntityEnterLoveModeEvent.getHandlerList();
    }
}
