package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.block.CampfireStartEvent;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftCampfireStartEvent extends CraftInventoryBlockStartEvent implements CampfireStartEvent {

    private final CampfireRecipe campfireRecipe;
    private int cookingTime;

    public CraftCampfireStartEvent(final Block campfire, final ItemStack source, final CampfireRecipe recipe) {
        super(campfire, source);
        this.cookingTime = recipe.getCookingTime();
        this.campfireRecipe = recipe;
    }

    @Override
    public CampfireRecipe getRecipe() {
        return this.campfireRecipe;
    }

    @Override
    public int getTotalCookTime() {
        return this.cookingTime;
    }

    @Override
    public void setTotalCookTime(final int cookTime) {
        this.cookingTime = cookTime;
    }
}
