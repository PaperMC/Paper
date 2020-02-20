package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a sized fireball.
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
