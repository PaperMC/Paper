package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;

public class CraftBrewingStartEvent extends CraftInventoryBlockStartEvent implements BrewingStartEvent {

    private int brewingTime;
    private int recipeBrewTime = 400;

    public CraftBrewingStartEvent(final Block brewingStand, final ItemStack source, final int brewingTime) {
        super(brewingStand, source);
        this.brewingTime = brewingTime;
    }

    @Override
    public @NonNegative int getBrewingTime() {
        return this.brewingTime;
    }

    @Override
    public void setBrewingTime(final @NonNegative int brewTime) {
        Preconditions.checkArgument(brewTime >= 0, "brewTime must be non-negative");
        this.brewingTime = brewTime;
    }

    @Override
    public @Positive int getRecipeBrewTime() {
        return this.recipeBrewTime;
    }

    @Override
    public void setRecipeBrewTime(final @Positive int recipeBrewTime) {
        Preconditions.checkArgument(recipeBrewTime > 0, "recipeBrewTime must be positive");
        this.recipeBrewTime = recipeBrewTime;
    }
}
