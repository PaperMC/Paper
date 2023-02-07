package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Campfire starts to cook.
 */
@org.jetbrains.annotations.ApiStatus.Experimental // Paper
public class CampfireStartEvent extends InventoryBlockStartEvent {

    // Paper - remove HandlerList
    private int cookingTime;
    private CampfireRecipe campfireRecipe;

    public CampfireStartEvent(@NotNull final Block furnace, @NotNull ItemStack source, @NotNull CampfireRecipe recipe) {
        super(furnace, source);
        this.cookingTime = recipe.getCookingTime();
        this.campfireRecipe = recipe;
    }

    /**
     * Gets the CampfireRecipe associated with this event.
     *
     * @return the CampfireRecipe being cooked
     */
    @NotNull
    public CampfireRecipe getRecipe() {
        return campfireRecipe;
    }

    /**
     * Gets the total cook time associated with this event.
     *
     * @return the total cook time
     */
    public int getTotalCookTime() {
        return cookingTime;
    }

    /**
     * Sets the total cook time for this event.
     *
     * @param cookTime the new total cook time
     */
    public void setTotalCookTime(int cookTime) {
        this.cookingTime = cookTime;
    }

    // Paper - remove HandlerList
}
