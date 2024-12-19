package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a sized fireball.
 *
 * @since 1.15.2
 */
public interface SizedFireball extends Fireball {

    /**
     * Gets the display {@link ItemStack}.
     *
     * @return display ItemStack
     */
    @NotNull
    ItemStack getDisplayItem();

    /**
     * Sets the display {@link ItemStack} for the fireball.
     *
     * @param item the ItemStack to display
     */
    void setDisplayItem(@NotNull ItemStack item);
}
