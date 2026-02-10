package org.bukkit.event.block;

import org.bukkit.inventory.CampfireRecipe;
import org.jetbrains.annotations.ApiStatus;

/**
 * Called when a Campfire starts to cook.
 */
@ApiStatus.Experimental
public interface CampfireStartEvent extends InventoryBlockStartEvent {

    /**
     * Gets the CampfireRecipe associated with this event.
     *
     * @return the CampfireRecipe being cooked
     */
    CampfireRecipe getRecipe();

    /**
     * Gets the total cook time associated with this event.
     *
     * @return the total cook time
     */
    int getTotalCookTime();

    /**
     * Sets the total cook time for this event.
     *
     * @param cookTime the new total cook time
     */
    void setTotalCookTime(int cookTime);
}
