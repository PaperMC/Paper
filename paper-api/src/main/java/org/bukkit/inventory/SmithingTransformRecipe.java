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
     * @param template The template item ({@link RecipeChoice#empty()} can be used)
     * @param base The base ingredient ({@link RecipeChoice#empty()} can be used)
     * @param addition The addition ingredient ({@link RecipeChoice#empty()} can be used)
     */
    public SmithingTransformRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice template, @NotNull RecipeChoice base, @NotNull RecipeChoice addition) { // Paper - fix issues with recipe api - prevent null choices
        super(key, result, base, addition);
        this.template = template == null ? RecipeChoice.empty() : template.validate(true).clone(); // Paper - fix issues with recipe api - prevent null choices
    }
    // Paper start
    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param template The template item ({@link RecipeChoice#empty()} can be used)
     * @param base The base ingredient ({@link RecipeChoice#empty()} can be used)
     * @param addition The addition ingredient ({@link RecipeChoice#empty()} can be used)
     * @param copyDataComponents whether to copy the data components from the input base item to the output
     */
    public SmithingTransformRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice template, @NotNull RecipeChoice base, @NotNull RecipeChoice addition, boolean copyDataComponents) {
        super(key, result, base, addition, copyDataComponents);
        this.template = template == null ? RecipeChoice.empty() : template.validate(true).clone(); // Paper - fix issues with recipe api - prevent null choices
    }
    // Paper end

    /**
     * Get the template recipe item.
     *
     * @return template choice
     */
    @NotNull // Paper - fix issues with recipe api - prevent null choices
    public RecipeChoice getTemplate() {
        return (template != null) ? template.clone() : null;
    }
}
