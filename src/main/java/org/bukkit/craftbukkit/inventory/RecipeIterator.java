package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import net.minecraft.server.IRecipe;
import net.minecraft.server.MinecraftServer;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<IRecipe<?>> recipes;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getCraftingManager().b().iterator();
    }

    @Override
    public boolean hasNext() {
        return recipes.hasNext();
    }

    @Override
    public Recipe next() {
        return recipes.next().toBukkitRecipe();
    }

    @Override
    public void remove() {
        recipes.remove();
    }
}
