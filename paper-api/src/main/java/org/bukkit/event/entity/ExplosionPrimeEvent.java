package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity has made a decision to explode.
 */
public interface ExplosionPrimeEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the radius of the explosion
     *
     * @return returns the radius of the explosion
     */
    float getRadius();

    /**
     * Sets the radius of the explosion
     *
     * @param radius the radius of the explosion
     */
    void setRadius(float radius);

    /**
     * Gets whether this explosion will create fire or not
     *
     * @return {@code true} if this explosion will create fire
     */
    boolean getFire();

    /**
     * Sets whether this explosion will create fire or not
     *
     * @param fire {@code true} if you want this explosion to create fire
     */
    void setFire(boolean fire);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
