package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity has made a decision to explode.
 */
public class ExplosionPrimeEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private float radius;
    private boolean fire;

    public ExplosionPrimeEvent(@NotNull final Entity what, final float radius, final boolean fire) {
        super(what);
        this.cancel = false;
        this.radius = radius;
        this.fire = fire;
    }

    public ExplosionPrimeEvent(@NotNull final Explosive explosive) {
        this(explosive, explosive.getYield(), explosive.isIncendiary());
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the radius of the explosion
     *
     * @return returns the radius of the explosion
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the explosion
     *
     * @param radius the radius of the explosion
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Gets whether this explosion will create fire or not
     *
     * @return true if this explosion will create fire
     */
    public boolean getFire() {
        return fire;
    }

    /**
     * Sets whether this explosion will create fire or not
     *
     * @param fire true if you want this explosion to create fire
     */
    public void setFire(boolean fire) {
        this.fire = fire;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
