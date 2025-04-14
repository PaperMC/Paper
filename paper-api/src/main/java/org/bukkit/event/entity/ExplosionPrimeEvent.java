package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity has made a decision to explode.
 */
public class ExplosionPrimeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private float radius;
    private boolean fire;

    private boolean cancelled;

    @ApiStatus.Internal
    public ExplosionPrimeEvent(@NotNull final Entity entity, final float radius, final boolean fire) {
        super(entity);
        this.radius = radius;
        this.fire = fire;
    }

    @ApiStatus.Internal
    public ExplosionPrimeEvent(@NotNull final Explosive explosive) {
        this(explosive, explosive.getYield(), explosive.isIncendiary());
    }

    /**
     * Gets the radius of the explosion
     *
     * @return returns the radius of the explosion
     */
    public float getRadius() {
        return this.radius;
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
     * @return {@code true} if this explosion will create fire
     */
    public boolean getFire() {
        return this.fire;
    }

    /**
     * Sets whether this explosion will create fire or not
     *
     * @param fire {@code true} if you want this explosion to create fire
     */
    public void setFire(boolean fire) {
        this.fire = fire;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
