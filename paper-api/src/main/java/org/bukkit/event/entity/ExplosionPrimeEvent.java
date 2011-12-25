package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Cancellable;

/**
 * Called when an entity has made a decision to explode.
 */
@SuppressWarnings("serial")
public class ExplosionPrimeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private float radius;
    private boolean fire;

    public ExplosionPrimeEvent(Entity what, float radius, boolean fire) {
        super(Type.EXPLOSION_PRIME, what);
        this.cancel = false;
        this.radius = radius;
        this.fire = fire;
    }

    public ExplosionPrimeEvent(Explosive explosive) {
        this(explosive, explosive.getYield(), explosive.isIncendiary());
    }

    public boolean isCancelled() {
        return cancel;
    }

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
}
