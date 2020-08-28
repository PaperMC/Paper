package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an EnderSignal, which is created upon throwing an ender eye.
 */
public interface EnderSignal extends Entity {

    /**
     * Get the location this EnderSignal is moving towards.
     *
     * @return the {@link Location} this EnderSignal is moving towards.
     */
    @NotNull
    public Location getTargetLocation();

    /**
     * Set the {@link Location} this EnderSignal is moving towards.
     * <br>
     * When setting a new target location, the {@link #getDropItem()} resets to
     * a random value and the despawn timer gets set back to 0.
     *
     * @param location the new target location
     */
    public void setTargetLocation(@NotNull Location location);

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
     * Get the {@link ItemStack} to be displayed while in the air and to be
     * dropped on death.
     *
     * @return the item stack
     */
    @NotNull
    public ItemStack getItem();

    /**
     * Set the {@link ItemStack} to be displayed while in the air and to be
     * dropped on death.
     *
     * @param item the item to set. If null, resets to the default eye of ender
     */
    public void setItem(@Nullable ItemStack item);

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
