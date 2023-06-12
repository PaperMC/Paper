package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.Recipes;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<Recipes<?>, Object2ObjectLinkedOpenHashMap<MinecraftKey, IRecipe<?>>>> recipes;
    private Iterator<IRecipe<?>> current;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        if (current != null && current.hasNext()) {
            return true;
        }

        if (recipes.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return hasNext();
        }

        return false;
    }

    @Override
    public Recipe next() {
        if (current == null || !current.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return next();
        }

        return current.next().toBukkitRecipe();
    }

    @Override
    public void remove() {
        Preconditions.checkState(current != null, "next() not yet called");
        current.remove();
    }
}
