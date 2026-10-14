package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<RecipeType<?>, RecipeHolder<?>>> recipes;
    private RecipeHolder<?> currentRecipe;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.byType.entries().iterator();
    }

    @Override
    public boolean hasNext() {
        return this.recipes.hasNext();
    }

    @Override
    public Recipe next() {
        this.currentRecipe = this.recipes.next().getValue();
        return this.currentRecipe.toBukkitRecipe();
    }

    @Override
    public void remove() {
        MinecraftServer.getServer().getRecipeManager().recipes.byKey.remove(this.currentRecipe.id());
        this.recipes.remove();
        MinecraftServer.getServer().getRecipeManager().finalizeRecipeLoading();
        MinecraftServer.getServer().getPlayerList().reloadRecipes();
    }
}
