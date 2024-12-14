package org.bukkit.entity.minecart;

import org.bukkit.entity.Explosive;
import org.bukkit.entity.Minecart;

/**
 * Represents a Minecart with TNT inside it that can explode when triggered.
 *
 * @since 1.5.1 R0.2
 */
public interface ExplosiveMinecart extends Minecart, Explosive {

    /**
     * Set the fuse ticks of this minecart.
     *
     * If the fuse ticks are set to a non-zero value, this will ignite the
     * explosive.
     *
     * @param ticks the ticks
     * @since 1.19.2
     */
    public void setFuseTicks(int ticks);

    /**
     * Get the fuse ticks of this minecart.
     *
     * If the fuse ticks reach 0, the minecart will explode.
     *
     * @return the fuse ticks, or -1 if this minecart's fuse has not yet been
     * ignited
     * @since 1.19.2
     */
    public int getFuseTicks();

    /**
     * Gets the factor by which explosion yield increases based on Minecart
     * speed.
     *
     * @return increase factor
     * @since 1.21.4
     */
    public float getExplosionSpeedFactor();

    /**
     * Sets the factor by which explosion yield increases based on Minecart
     * speed.
     *
     * @param factor new factor
     * @since 1.21.4
     */
    public void setExplosionSpeedFactor(float factor);

    /**
     * Ignite this minecart's fuse naturally.
     *
     * @since 1.19.3
     */
    public void ignite();

    /**
     * Check whether or not this minecart's fuse has been ignited.
     *
     * @return true if ignited, false otherwise
     * @since 1.19.3
     */
    public boolean isIgnited();

    /**
     * Immediately explode this minecart with the power assumed by its current
     * movement.
     *
     * @since 1.19.3
     */
    public void explode();

    /**
     * Immediately explode this minecart with the given power.
     *
     * @param power the power to use. Must be positive and cannot exceed 5.0
     * @since 1.19.3
     */
    public void explode(double power);
}
