package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a smithing trim recipe.
 */
public class SmithingTrimRecipe extends SmithingRecipe implements ComplexRecipe {

    private final RecipeChoice template;

    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param template The template item ({@link RecipeChoice#empty()} can be used)
     * @param base The base ingredient ({@link RecipeChoice#empty()} can be used)
     * @param addition The addition ingredient ({@link RecipeChoice#empty()} can be used)
     */
    public SmithingTrimRecipe(@NotNull NamespacedKey key, @NotNull RecipeChoice template, @NotNull RecipeChoice base, @NotNull RecipeChoice addition) { // Paper - fix issues with recipe api - prevent null choices
        super(key, new ItemStack(Material.AIR), base, addition);
        this.template = template == null ? RecipeChoice.empty() : template.validate(true).clone(); // Paper
    }
    // Paper start
    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param template The template item. ({@link RecipeChoice#empty()} can be used)
     * @param base The base ingredient ({@link RecipeChoice#empty()} can be used)
     * @param addition The addition ingredient ({@link RecipeChoice#empty()} can be used)
     * @param copyDataComponents whether to copy the data components from the input base item to the output
     */
    public SmithingTrimRecipe(@NotNull NamespacedKey key, @NotNull RecipeChoice template, @NotNull RecipeChoice base, @NotNull RecipeChoice addition, boolean copyDataComponents) { // Paper - fix issues with recipe api - prevent null choices
        super(key, new ItemStack(Material.AIR), base, addition, copyDataComponents);
        this.template = template == null ? RecipeChoice.empty() : template.validate(true).clone(); // Paper
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
