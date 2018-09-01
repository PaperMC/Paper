package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.RecipeItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default net.minecraft.server.RecipeItemStack toNMS(RecipeChoice bukkit) {
        if (bukkit == null) {
            return RecipeItemStack.a;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            return new RecipeItemStack(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.server.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }
    }
}
