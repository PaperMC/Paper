package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.Recipes;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<Recipes<?>, RecipeHolder<?>>> recipes;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().byType.entries().iterator();
    }

    @Override
    public boolean hasNext() {
        return recipes.hasNext();
    }

    @Override
    public Recipe next() {
        return recipes.next().getValue().toBukkitRecipe();
    }

    @Override
    public void remove() {
        recipes.remove();
    }
}
