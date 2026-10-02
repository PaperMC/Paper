package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/// Represents a sized fireball.
public interface SizedFireball extends Fireball {

    /// Gets the display [ItemStack].
    ///
    /// @return display ItemStack
    @NotNull
    ItemStack getDisplayItem();

    /// Sets the display [ItemStack] for the fireball.
    ///
    /// @param item the ItemStack to display
    void setDisplayItem(@NotNull ItemStack item);
}
