package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import org.bukkit.inventory.Recipe;

import net.minecraft.server.IRecipe;
import net.minecraft.server.MinecraftServer;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<IRecipe> recipes;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getCraftingManager().recipes.values().iterator();
    }

    public boolean hasNext() {
        return recipes.hasNext();
    }

    public Recipe next() {
        return recipes.next().toBukkitRecipe();
    }

    public void remove() {
        recipes.remove();
    }
}
