package org.bukkit.inventory;

import org.bukkit.block.Furnace;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Furnace.
 */
public interface FurnaceInventory extends Inventory {

    /**
     * Get the current item in the result slot.
     *
     * @return The item
     */
    @Nullable
    ItemStack getResult();

    /**
     * Get the current fuel.
     *
     * @return The item
     */
    @Nullable
    ItemStack getFuel();

    /**
     * Get the item currently smelting.
     *
     * @return The item
     */
    @Nullable
    ItemStack getSmelting();

    /**
     * Set the current fuel.
     *
     * @param stack The item
     */
    void setFuel(@Nullable ItemStack stack);

    /**
     * Set the current item in the result slot.
     *
     * @param stack The item
     */
    void setResult(@Nullable ItemStack stack);

    /**
     * Set the item currently smelting.
     *
     * @param stack The item
     */
    void setSmelting(@Nullable ItemStack stack);

    @Override
    @Nullable
    Furnace getHolder();
}
