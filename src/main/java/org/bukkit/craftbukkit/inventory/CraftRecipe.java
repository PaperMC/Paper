package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.Recipe;

public interface CraftRecipe extends Recipe {
    void addToCraftingManager();
}
