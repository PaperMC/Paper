package org.bukkit.inventory;

import org.bukkit.block.Furnace;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Furnace.
 *
 * @since 1.1.0
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

    // Paper start
    /**
     * Check if an item can be used as a fuel source in this furnace container
     *
     * @param item Item to check
     * @return True if a valid fuel source
     * @since 1.18.1
     */
    public boolean isFuel(@Nullable ItemStack item);

    /**
     * Check if an item can be smelted in this furnace container
     *
     * @param item Item to check
     * @return True if can be smelt
     * @since 1.18.1
     */
    public boolean canSmelt(@Nullable ItemStack item);
    // Paper end

    @Override
    @Nullable
    Furnace getHolder();
}
