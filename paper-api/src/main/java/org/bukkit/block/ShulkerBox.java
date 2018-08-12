package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.Nameable;
import org.bukkit.loot.Lootable;

/**
 * Represents a captured state of a ShulkerBox.
 */
public interface ShulkerBox extends Container, Nameable, Lootable {

    /**
     * Get the {@link DyeColor} corresponding to this ShulkerBox
     *
     * @return the {@link DyeColor} of this ShulkerBox
     */
    public DyeColor getColor();
}
