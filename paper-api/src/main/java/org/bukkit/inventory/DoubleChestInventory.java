package org.bukkit.inventory;

import org.bukkit.block.DoubleChest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Double Chest.
 *
 * @since 1.1.0
 */
public interface DoubleChestInventory extends Inventory {

    /**
     * Get the left half of this double chest.
     *
     * @return The left side inventory
     */
    @NotNull
    Inventory getLeftSide();

    /**
     * Get the right side of this double chest.
     *
     * @return The right side inventory
     */
    @NotNull
    Inventory getRightSide();

    /**
     * @since 1.3.1
     */
    @Override
    @Nullable
    DoubleChest getHolder();
}
