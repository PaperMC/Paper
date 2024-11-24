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
        this.key = key;
        this.result = result;
        this.base = base;
        this.addition = addition;
    }

    /**
     * Get the base recipe item.
     *
     * @return base choice
     */
    @Nullable
    public RecipeChoice getBase() {
        return (base != null) ? base.clone() : null;
    }

    /**
     * Get the addition recipe item.
     *
     * @return addition choice
     */
    @Nullable
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
}
