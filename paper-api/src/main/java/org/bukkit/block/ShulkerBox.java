package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.Nameable;

/**
 * Represents a captured state of a ShulkerBox.
 */
public interface ShulkerBox extends Container, Nameable {

    /**
     * Get the {@link DyeColor} corresponding to this ShulkerBox
     *
     * @return the {@link DyeColor} of this ShulkerBox
     */
    public DyeColor getColor();
}
