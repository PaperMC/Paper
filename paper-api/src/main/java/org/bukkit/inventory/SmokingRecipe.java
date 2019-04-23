package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a campfire recipe.
 */
public class SmokingRecipe extends CookingRecipe<SmokingRecipe> {

    public SmokingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        super(key, result, source, experience, cookingTime);
    }

    public SmokingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice input, float experience, int cookingTime) {
        super(key, result, input, experience, cookingTime);
    }
}
