package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<RecipeType<?>, RecipeHolder<?>>> recipes;
    private RecipeHolder<?> currentRecipe; // Paper - fix removing recipes from RecipeIterator

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.byType.entries().iterator();
    }

    @Override
    public boolean hasNext() {
        return this.recipes.hasNext();
    }

    @Override
    public Recipe next() {
        // Paper start - fix removing recipes from RecipeIterator
        this.currentRecipe = this.recipes.next().getValue();
        return this.currentRecipe.toBukkitRecipe();
        // Paper end - fix removing recipes from RecipeIterator
    }

    @Override
    public void remove() {
        MinecraftServer.getServer().getRecipeManager().recipes.byKey.remove(this.currentRecipe.id()); // Paper - fix removing recipes from RecipeIterator
        this.recipes.remove();
    }
}
