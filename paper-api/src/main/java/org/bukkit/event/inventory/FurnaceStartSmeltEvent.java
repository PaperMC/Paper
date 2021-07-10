package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FurnaceStartSmeltEvent extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack source;
    private final FurnaceRecipe recipe;
    private int totalCookTime;

    public FurnaceStartSmeltEvent(@NotNull final Block furnace, @NotNull ItemStack source, @NotNull final FurnaceRecipe recipe) {
        super(furnace);
        this.source = source;
        this.recipe = recipe;
        this.totalCookTime = recipe.getCookingTime();
    }

    /**
     * Gets the source ItemStack for this event
     *
     * @return the source ItemStack
     */
    @NotNull
    public ItemStack getSource() {
        return source;
    }

    /**
     * Gets the FurnaceRecipe associated with this event
     *
     * @return the FurnaceRecipe being cooked
     */
    @NotNull
    public FurnaceRecipe getRecipe() {
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
