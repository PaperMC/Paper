package org.bukkit.entity.minecart;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Minecart;
import org.jspecify.annotations.Nullable;

/**
 * Represents a Minecart with TNT inside it that can explode when triggered.
 */
public interface ExplosiveMinecart extends Minecart, Explosive {

    /**
     * Set the fuse ticks of this minecart.
     * <p>
     * If the fuse ticks are set to a non-zero value, this will ignite the
     * explosive.
     *
     * @param ticks the ticks
     */
    public void setFuseTicks(int ticks);

    /**
     * Get the fuse ticks of this minecart.
     * <p>
     * If the fuse ticks reach 0, the minecart will explode.
     *
     * @return the fuse ticks, or -1 if this minecart's fuse has not yet been
     * ignited
     */
    public int getFuseTicks();

    /**
     * Gets the factor by which explosion yield increases based on Minecart
     * speed.
     *
     * @return increase factor
     */
    public float getExplosionSpeedFactor();

    /**
     * Sets the factor by which explosion yield increases based on Minecart
     * speed.
     *
     * @param factor new factor
     */
    public void setExplosionSpeedFactor(float factor);

    /**
     * Ignite this minecart's fuse naturally.
     * <p>
     * Calling ignite on an already-ignited minecart resets the fuse time, but
     * does not reset the igniter if it previously existed.
     */
    public void ignite();

    /**
     * Ignite this minecart's fuse with a specified fuse time.
     * <p>
     * Calling ignite on an already-ignited minecart resets the fuse time, but
     * does not reset the igniter if it previously existed.
     *
     * @param fuseTime the amount of ticks the fuse lasts before exploding
     */
    public void ignite(int fuseTime);

    /**
     * Ignite this minecart's fuse with a specified igniter.
     * <p>
     * Calling ignite on an already-ignited minecart resets the fuse time, but
     * does not set the igniter if it previously existed.
     *
     * @param igniter the entity which ignited the minecart
     */
    public void ignite(@Nullable Entity igniter);

    /**
     * Ignite this minecart's fuse with a specified igniter and fuse time.
     * <p>
     * Calling ignite on an already-ignited minecart resets the fuse time, but
     * does not set the igniter if it previously existed.
     *
     * @param igniter the entity which ignited the minecart
     * @param fuseTime the amount of ticks the fuse lasts before exploding
     */
    public void ignite(@Nullable Entity igniter, int fuseTime);

    /**
     * Check whether or not this minecart's fuse has been ignited.
     *
     * @return true if ignited, false otherwise
     */
    public boolean isIgnited();

    /**
     * Gets the entity which ignited the minecart, if available.
     *
     * @return the entity which ignited the minecart (if available) or null
     */
    @Nullable
    public Entity getIgniter();

    /**
     * Immediately explode this minecart with the power assumed by its current
     * movement.
     */
    public void explode();

    /**
     * Immediately explode this minecart with the given power.
     *
     * @param power the power to use. Must be positive and cannot exceed 25.0
     */
    public void explode(double power);

    /**
     * Immediately explode this minecart with the power assumed by its current
     * movement, with the explosion caused by the given entity.
     *
     * @param entity the entity which caused the minecart's explosion
     */
    public void explode(@Nullable Entity entity);

    /**
     * Immediately explode this minecart with the given power, with the explosion
     * caused by the given entity.
     *
     * @param entity the entity which caused the minecart's explosion
     * @param power the power to use. Must be positive and cannot exceed 25.0
     */
    public void explode(@Nullable Entity entity, double power);
}
