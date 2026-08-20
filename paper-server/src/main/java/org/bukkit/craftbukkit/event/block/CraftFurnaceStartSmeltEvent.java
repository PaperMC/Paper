package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceStartSmeltEvent extends CraftInventoryBlockStartEvent implements FurnaceStartSmeltEvent {

    private final CookingRecipe<?> recipe;
    private int totalCookTime;

    public CraftFurnaceStartSmeltEvent(final Block furnace, final ItemStack source, final CookingRecipe<?> recipe, final int cookingTime) {
        super(furnace, source);
        this.recipe = recipe;
        this.totalCookTime = cookingTime;
    }

    @Override
    public CookingRecipe<?> getRecipe() {
        return this.recipe;
    }

    @Override
    public int getTotalCookTime() {
        return this.totalCookTime;
    }

    @Override
    public void setTotalCookTime(final int cookTime) {
        this.totalCookTime = cookTime;
    }
}
