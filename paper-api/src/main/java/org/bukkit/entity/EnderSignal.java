package org.bukkit.entity;

import org.bukkit.Location;

/**
 * Represents an EnderSignal, which is created upon throwing an ender eye.
 */
public interface EnderSignal extends Entity {

    /**
     * Get the location this EnderSignal is moving towards.
     *
     * @return the {@link Location} this EnderSignal is moving towards.
     */
    public Location getTargetLocation();

    /**
     * Set the {@link Location} this EnderSignal is moving towards.
     * <br>
     * When setting a new target location, the {@link #getDropItem()} resets to
     * a random value and the despawn timer gets set back to 0.
     *
     * @param location the new target location
     */
    public void setTargetLocation(Location location);

    /**
     * Gets if the EnderSignal should drop an item on death.<br>
     * If {@code true}, it will drop an item. If {@code false}, it will shatter.
     *
     * @return true if the EnderSignal will drop an item on death, or false if
     * it will shatter
     */
    public boolean getDropItem();

    /**
     * Sets if the EnderSignal should drop an item on death; or if it should
     * shatter.
     *
     * @param drop true if the EnderSignal should drop an item on death, or
     * false if it should shatter.
     */
    public void setDropItem(boolean drop);

    /**
     * Gets the amount of time this entity has been alive (in ticks).
     * <br>
     * When this number is greater than 80, it will despawn on the next tick.
     *
     * @return the number of ticks this EnderSignal has been alive.
     */
    public int getDespawnTimer();

    /**
     * Set how long this entity has been alive (in ticks).
     * <br>
     * When this number is greater than 80, it will despawn on the next tick.
     *
     * @param timer how long (in ticks) this EnderSignal has been alive.
     */
    public void setDespawnTimer(int timer);
}
