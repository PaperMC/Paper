package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import org.bukkit.inventory.Recipe;

import net.minecraft.server.CraftingManager;
import net.minecraft.server.IRecipe;
import net.minecraft.server.RecipesFurnace;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<IRecipe> recipes;
    private final Iterator<net.minecraft.server.ItemStack> smelting;
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
            net.minecraft.server.ItemStack item = smelting.next();
            CraftItemStack stack = CraftItemStack.asCraftMirror(RecipesFurnace.getInstance().getResult(item));

            return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
        }
    }

    public void remove() {
        if (removeFrom == null) {
            throw new IllegalStateException();
        }
        removeFrom.remove();
    }
}
