package org.bukkit.inventory;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a smithing transform recipe.
 */
public class SmithingTransformRecipe extends SmithingRecipe {

    private final RecipeChoice template;

    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param template The template item.
     * @param base The base ingredient
     * @param addition The addition ingredient
     */
    public SmithingTransformRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice template, @NotNull RecipeChoice base, @NotNull RecipeChoice addition) {
        super(key, result, base, addition);
        this.template = template;
    }

    /**
     * Get the template recipe item.
     *
     * @return template choice
     */
    @NotNull
    public RecipeChoice getTemplate() {
        return template.clone();
    }
}
