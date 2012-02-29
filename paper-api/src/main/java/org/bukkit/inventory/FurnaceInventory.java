package org.bukkit.inventory;

import org.bukkit.block.Furnace;

public interface FurnaceInventory extends Inventory {
    /**
     * Get the current item in the result slot.
     * @return The item
     */
    ItemStack getResult();

    /**
     * Get the current fuel.
     * @return The item
     */
    ItemStack getFuel();

    /**
     * Get the item currently smelting.
     * @return The item
     */
    ItemStack getSmelting();

    /**
     * Set the current fuel.
     * @param stack The item
     */
    void setFuel(ItemStack stack);

    /**
     * Set the current item in the result slot.
     * @param stack The item
     */
    void setResult(ItemStack stack);

    /**
     * Set the item currently smelting.
     * @param stack The item
     */
    void setSmelting(ItemStack stack);

    Furnace getHolder();
}
