package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default net.minecraft.server.RecipeItemStack toNMS(RecipeChoice bukkit) {
        if (bukkit instanceof RecipeChoice.MaterialChoice) {
            return new net.minecraft.server.RecipeItemStack(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.server.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }
    }
}
