package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import net.minecraft.server.CraftingManager;
import net.minecraft.server.IRecipe;
import net.minecraft.server.RecipesFurnace;

public class RecipeIterator implements Iterator<Recipe> {
    private Iterator<IRecipe> recipes;
    private Iterator<Integer> smelting;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.recipes = CraftingManager.getInstance().getRecipes().iterator();
        this.smelting = RecipesFurnace.getInstance().getRecipes().keySet().iterator();
    }

    public boolean hasNext() {
        if (recipes.hasNext()) {
            return true;
        } else {
            return smelting.hasNext();
        }
    }

    public Recipe next() {
        if (recipes.hasNext()) {
            removeFrom = recipes;
            return recipes.next().toBukkitRecipe();
        } else {
            removeFrom = smelting;
            int id = smelting.next();
            CraftItemStack stack = new CraftItemStack(RecipesFurnace.getInstance().getResult(id));
            CraftFurnaceRecipe recipe = new CraftFurnaceRecipe(stack, new ItemStack(id, 1, (short) -1));
            return recipe;
        }
    }

    public void remove() {
        if (removeFrom == null) {
            throw new IllegalStateException();
        }
        removeFrom.remove();
    }
}
