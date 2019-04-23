package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a ShulkerBox.
 */
public interface ShulkerBox extends Container, Lootable {

    /**
     * Get the {@link DyeColor} corresponding to this ShulkerBox
     *
     * @return the {@link DyeColor} of this ShulkerBox
     */
    @NotNull
    public DyeColor getColor();
}
