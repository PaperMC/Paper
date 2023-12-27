package org.bukkit.inventory;

import org.bukkit.block.DecoratedPot;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a DecoratedPot.
 */
public interface DecoratedPotInventory extends Inventory {

    /**
     * Set the item stack in the decorated pot.
     *
     * @param item the new item stack
     */
    public void setItem(@Nullable ItemStack item);

    /**
     * Get the item stack in the decorated pot.
     *
     * @return the current item stack
     */
    @Nullable
    public ItemStack getItem();

    @Nullable
    @Override
    public DecoratedPot getHolder();
}
