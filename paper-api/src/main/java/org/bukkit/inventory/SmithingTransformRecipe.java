package org.bukkit.inventory;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public SmithingTransformRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @Nullable RecipeChoice template, @Nullable RecipeChoice base, @Nullable RecipeChoice addition) {
        super(key, result, base, addition);
        this.template = template;
    }
    // Paper start
    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param template The template item.
     * @param base The base ingredient
     * @param addition The addition ingredient
     * @param copyDataComponents whether to copy the data components from the input base item to the output
     */
    public SmithingTransformRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @Nullable RecipeChoice template, @Nullable RecipeChoice base, @Nullable RecipeChoice addition, boolean copyDataComponents) {
        super(key, result, base, addition, copyDataComponents);
        this.template = template;
    }
    // Paper end

    /**
     * Get the template recipe item.
     *
     * @return template choice
     */
    @Nullable
    public RecipeChoice getTemplate() {
        return (template != null) ? template.clone() : null;
    }
}
