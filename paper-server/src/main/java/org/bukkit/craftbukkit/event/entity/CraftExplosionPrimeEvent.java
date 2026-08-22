package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class CraftExplosionPrimeEvent extends CraftEntityEvent implements ExplosionPrimeEvent {

    private float radius;
    private boolean fire;

    private boolean cancelled;

    public CraftExplosionPrimeEvent(final Entity entity, final float radius, final boolean fire) {
        super(entity);
        this.radius = radius;
        this.fire = fire;
    }

    public CraftExplosionPrimeEvent(final Explosive explosive) {
        this(explosive, explosive.getYield(), explosive.isIncendiary());
    }

    @Override
    public float getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(final float radius) {
        this.radius = radius;
    }

    @Override
    public boolean getFire() {
        return this.fire;
    }

    @Override
    public void setFire(final boolean fire) {
        this.fire = fire;
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
        return ExplosionPrimeEvent.getHandlerList();
    }
}
