package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// Represents an EnderSignal, which is created upon throwing an ender eye.
public interface EnderSignal extends Entity {

    /// Get the location this EnderSignal is moving towards.
    ///
    /// @return the [Location] this EnderSignal is moving towards.
    @Nullable
    public Location getTargetLocation();

    /// Set the [Location] this EnderSignal is moving towards.
    ///
    /// When setting a new target location, the [#getDropItem()] resets to
    /// a random value and the despawn timer gets set back to 0.
    ///
    /// @param location the new target location
    public void setTargetLocation(@NotNull Location location);

    // Paper start
    /// Set the [Location] this EnderSignal is moving towards.
    ///
    /// @param location the new target location
    /// @param update true to reset the [#getDropItem()]
    ///               to a random value and [#getDespawnTimer()] to 0
    public void setTargetLocation(@NotNull Location location, boolean update);
    // Paper end

    /// Gets if the EnderSignal should drop an item on death.
    ///
    /// If `true`, it will drop an item. If `false`, it will shatter.
    ///
    /// @return true if the EnderSignal will drop an item on death, or false if
    /// it will shatter
    public boolean getDropItem();

    /// Sets if the EnderSignal should drop an item on death; or if it should
    /// shatter.
    ///
    /// @param drop true if the EnderSignal should drop an item on death, or
    /// false if it should shatter.
    public void setDropItem(boolean drop);

    /// Get the [ItemStack] to be displayed while in the air and to be
    /// dropped on death.
    ///
    /// @return the item stack
    @NotNull
    public ItemStack getItem();

    /// Set the [ItemStack] to be displayed while in the air and to be
    /// dropped on death.
    ///
    /// @param item the item to set. If null, resets to the default eye of ender
    public void setItem(@Nullable ItemStack item);

    /// Gets the amount of time this entity has been alive (in ticks).
    ///
    /// When this number is greater than 80, it will despawn on the next tick.
    ///
    /// @return the number of ticks this EnderSignal has been alive.
    public int getDespawnTimer();

    /// Set how long this entity has been alive (in ticks).
    ///
    /// When this number is greater than 80, it will despawn on the next tick.
    ///
    /// @param timer how long (in ticks) this EnderSignal has been alive.
    public void setDespawnTimer(int timer);
}
