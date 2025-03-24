package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.InventoryBlockStartEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when any of the furnace-like blocks start smelting.
 * <p>
 * Furnace-like blocks are {@link org.bukkit.block.Furnace},
 * {@link org.bukkit.block.Smoker}, and {@link org.bukkit.block.BlastFurnace}.
 */
public class FurnaceStartSmeltEvent extends InventoryBlockStartEvent {
    // Paper - remove HandlerList
    private final CookingRecipe<?> recipe;
    private int totalCookTime;

    @Deprecated(forRemoval = true)
    public FurnaceStartSmeltEvent(@NotNull final Block furnace, @NotNull ItemStack source, @NotNull final CookingRecipe<?> recipe) {
        // Paper start
        this(furnace, source, recipe, recipe.getCookingTime());
    }

    @ApiStatus.Internal
    public FurnaceStartSmeltEvent(final @NotNull Block furnace, final @NotNull ItemStack source, final @NotNull CookingRecipe<?> recipe, final int cookingTime) {
        // Paper end
        super(furnace, source);
        this.recipe = recipe;
        this.totalCookTime = cookingTime; // Paper - furnace cook speed multiplier
    }

    /**
     * Gets the FurnaceRecipe associated with this event
     *
     * @return the FurnaceRecipe being cooked
     */
    @NotNull
    public CookingRecipe<?> getRecipe() {
        return recipe;
    }

    /**
     * Gets the total cook time associated with this event
     *
     * @return the total cook time
     */
    public int getTotalCookTime() {
        return totalCookTime;
    }

    /**
     * Sets the total cook time for this event
     *
     * @param cookTime the new total cook time
     */
    public void setTotalCookTime(int cookTime) {
        this.totalCookTime = cookTime;
    }

    // Paper - remove HandlerList
}
