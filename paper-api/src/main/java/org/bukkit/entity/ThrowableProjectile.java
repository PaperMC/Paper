package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ThrowableProjectile extends Projectile {

    /**
     * Gets the ItemStack the thrown projectile will display.
     *
     * @return The thrown item display ItemStack
     */
    ItemStack getItem();

    /**
     * Sets the display ItemStack for the thrown projectile.
     *
     * @param item ItemStack set to be displayed
     */
    void setItem(ItemStack item);
    /**
     * Enables opt-in legacy flight order, gravity, owner grace and entity intersections.
     * Native impact callbacks and protection events remain active.
     *
     * @param legacyFlight whether the profile is enabled (disabled by default)
     */
    void setLegacyFlight(boolean legacyFlight);

    /** @return whether opt-in legacy flight is enabled */
    boolean hasLegacyFlight();

}
