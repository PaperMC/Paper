package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.HorseJumpEvent;

public class CraftHorseJumpEvent extends CraftEntityEvent implements HorseJumpEvent {

    private float power;
    private boolean cancelled;

    public CraftHorseJumpEvent(final AbstractHorse horse, final float power) {
        super(horse);
        this.power = power;
    }

    @Override
    public AbstractHorse getEntity() {
        return (AbstractHorse) this.entity;
    }

    @Override
    public float getPower() {
        return this.power;
    }

    @Override
    @Deprecated(since = "1.9")
    public void setPower(final float power) {
        this.power = power;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    @Deprecated(since = "1.9")
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HorseJumpEvent.getHandlerList();
    }
}
