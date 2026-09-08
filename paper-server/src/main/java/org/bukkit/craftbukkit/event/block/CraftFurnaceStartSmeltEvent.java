package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

    public CraftFurnaceStartSmeltEvent(
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack source,
        final RecipeHolder<? extends AbstractCookingRecipe> recipe,
        final int cookingTime
    ) {
        this(CraftBlock.at(level, pos), CraftItemStack.asCraftMirror(source), (CookingRecipe<?>) recipe.toBukkitRecipe(), cookingTime);
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
