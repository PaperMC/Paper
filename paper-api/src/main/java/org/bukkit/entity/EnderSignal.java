package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an EnderSignal, which is created upon throwing an ender eye.
 *
 * @since 1.0.0
 */
public interface EnderSignal extends Entity {

    /**
     * Get the location this EnderSignal is moving towards.
     *
     * @return the {@link Location} this EnderSignal is moving towards.
     * @since 1.12.2
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
     * @since 1.12.2
     */
    public void setTargetLocation(@NotNull Location location);

    // Paper start
    /**
     * Set the {@link Location} this EnderSignal is moving towards.
     *
     * @param location the new target location
     * @param update true to reset the {@link #getDropItem()}
     *               to a random value and {@link #getDespawnTimer()} to 0
     * @since 1.17.1
     */
    public void setTargetLocation(@NotNull Location location, boolean update);
    // Paper end

    /**
     * Gets if the EnderSignal should drop an item on death.<br>
     * If {@code true}, it will drop an item. If {@code false}, it will shatter.
     *
     * @return true if the EnderSignal will drop an item on death, or false if
     * it will shatter
     * @since 1.12.2
     */
    public boolean getDropItem();

    /**
     * Sets if the EnderSignal should drop an item on death; or if it should
     * shatter.
     *
     * @param drop true if the EnderSignal should drop an item on death, or
     * false if it should shatter.
     * @since 1.12.2
     */
    public void setDropItem(boolean drop);

    /**
     * Get the {@link ItemStack} to be displayed while in the air and to be
     * dropped on death.
     *
     * @return the item stack
     * @since 1.16.2
     */
    @NotNull
    public ItemStack getItem();

    /**
     * Set the {@link ItemStack} to be displayed while in the air and to be
     * dropped on death.
     *
     * @param item the item to set. If null, resets to the default eye of ender
     * @since 1.16.2
     */
    public void setItem(@Nullable ItemStack item);

    /**
     * Gets the amount of time this entity has been alive (in ticks).
     * <br>
     * When this number is greater than 80, it will despawn on the next tick.
     *
     * @return the number of ticks this EnderSignal has been alive.
     * @since 1.12.2
     */
    public int getDespawnTimer();

    /**
     * Set how long this entity has been alive (in ticks).
     * <br>
     * When this number is greater than 80, it will despawn on the next tick.
     *
     * @param timer how long (in ticks) this EnderSignal has been alive.
     * @since 1.12.2
     */
    public void setDespawnTimer(int timer);
}
