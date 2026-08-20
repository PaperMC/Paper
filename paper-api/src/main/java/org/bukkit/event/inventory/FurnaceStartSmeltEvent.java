package org.bukkit.event.inventory;

import org.bukkit.event.block.InventoryBlockStartEvent;
import org.bukkit.inventory.CookingRecipe;

/**
 * Called when any of the furnace-like blocks start smelting.
 * <p>
 * Furnace-like blocks are {@link org.bukkit.block.Furnace},
 * {@link org.bukkit.block.Smoker}, and {@link org.bukkit.block.BlastFurnace}.
 */
public interface FurnaceStartSmeltEvent extends InventoryBlockStartEvent {

    /**
     * Gets the FurnaceRecipe associated with this event
     *
     * @return the FurnaceRecipe being cooked
     */
    CookingRecipe<?> getRecipe();

    /**
     * Gets the total cook time associated with this event
     *
     * @return the total cook time
     */
    int getTotalCookTime();

    /**
     * Sets the total cook time for this event
     *
     * @param cookTime the new total cook time
     */
    void setTotalCookTime(int cookTime);
}
