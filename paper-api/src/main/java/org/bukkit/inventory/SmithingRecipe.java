package org.bukkit.inventory;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a smithing recipe.
 */
public class SmithingRecipe implements Recipe, Keyed {

    private final NamespacedKey key;
    private final ItemStack result;
    private final RecipeChoice base;
    private final RecipeChoice addition;
    private final boolean copyDataComponents; // Paper

    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param base The base ingredient
     * @param addition The addition ingredient
     * @deprecated as of Minecraft 1.20, smithing recipes are now separated into two
     * distinct recipe types, {@link SmithingTransformRecipe} and {@link SmithingTrimRecipe}.
     * This class now acts as a base class to these two classes and will do nothing when
     * added to the server.
     */
    @Deprecated(since = "1.20.1")
    public SmithingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @Nullable RecipeChoice base, @Nullable RecipeChoice addition) {
        // Paper start
        this(key, result, base, addition, true);
    }
    /**
     * Create a smithing recipe to produce the specified result ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param base The base ingredient
     * @param addition The addition ingredient
     * @param copyDataComponents whether to copy the data components from the input base item to the output
     * @deprecated use {@link SmithingTrimRecipe} or {@link SmithingTransformRecipe}
     */
    @Deprecated
    public SmithingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @Nullable RecipeChoice base, @Nullable RecipeChoice addition, boolean copyDataComponents) {
        com.google.common.base.Preconditions.checkArgument(!result.isEmpty() || this instanceof ComplexRecipe, "Recipe cannot have an empty result."); // Paper
        this.copyDataComponents = copyDataComponents;
        // Paper end
        this.key = key;
        this.result = result;
        this.base = base == null ? RecipeChoice.empty() : base.validate(true).clone(); // Paper
        this.addition = addition == null ? RecipeChoice.empty() : addition.validate(true).clone(); // Paper
    }

    /**
     * Get the base recipe item.
     *
     * @return base choice
     */
    @NotNull // Paper - fix issues with recipe api
    public RecipeChoice getBase() {
        return (base != null) ? base.clone() : null;
    }

    /**
     * Get the addition recipe item.
     *
     * @return addition choice
     */
    @NotNull // Paper - fix issues with recipe api
    public RecipeChoice getAddition() {
        return (addition != null) ? addition.clone() : null;
    }

    @NotNull
    @Override
    public ItemStack getResult() {
        return result.clone();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    // Paper start
    /**
     * Whether to copy the NBT of the input base item to the output.
     *
     * @return true to copy the NBT (default for vanilla smithing recipes)
     * @apiNote use {@link #willCopyDataComponents()}
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.5")
    public boolean willCopyNbt() {
        return this.willCopyDataComponents();
    }

    /**
     * Whether to copy the data components of the input base item to the output.
     *
     * @return true to copy the data components (default for vanilla smithing recipes)
     */
    public boolean willCopyDataComponents() {
        return this.copyDataComponents;
    }
    // Paper end
}
