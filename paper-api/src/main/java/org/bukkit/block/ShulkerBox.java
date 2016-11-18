package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.Nameable;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a ShulkerBox.
 */
public interface ShulkerBox extends BlockState, InventoryHolder, Lockable, Nameable {

    /**
     * Get the {@link DyeColor} corresponding to this ShulkerBox
     *
     * @return the {@link DyeColor} of this ShulkerBox
     */
    public DyeColor getColor();
}
