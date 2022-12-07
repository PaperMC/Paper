package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.Collections;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Stonecutting recipe.
 */
public class StonecuttingRecipe implements Recipe, Keyed {
    private final NamespacedKey key;
    private ItemStack output;
    private RecipeChoice ingredient;
    private String group = "";

    /**
     * Create a Stonecutting recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param source The input material.
     */
    public StonecuttingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source) {
        this(key, result, new RecipeChoice.MaterialChoice(Collections.singletonList(source)));
    }

    /**
     * Create a cooking recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param input The input choices.
     */
    public StonecuttingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice input) {
        Preconditions.checkArgument(result.getType() != Material.AIR, "Recipe must have non-AIR result.");
        this.key = key;
        this.output = new ItemStack(result);
        this.ingredient = input;
    }

    /**
     * Sets the input of this cooking recipe.
     *
     * @param input The input material.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public StonecuttingRecipe setInput(@NotNull Material input) {
        this.ingredient = new RecipeChoice.MaterialChoice(Collections.singletonList(input));
        return this;
    }

    /**
     * Get the input material.
     *
     * @return The input material.
     */
    @NotNull
    public ItemStack getInput() {
        return this.ingredient.getItemStack();
    }

    /**
     * Sets the input of this cooking recipe.
     *
     * @param input The input choice.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public StonecuttingRecipe setInputChoice(@NotNull RecipeChoice input) {
        this.ingredient = input;
        return (StonecuttingRecipe) this;
    }

    /**
     * Get the input choice.
     *
     * @return The input choice.
     */
    @NotNull
    public RecipeChoice getInputChoice() {
        return this.ingredient.clone();
    }

    /**
     * Get the result of this recipe.
     *
     * @return The resulting stack.
     */
    @NotNull
    @Override
    public ItemStack getResult() {
        return output.clone();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get the group of this recipe. Recipes with the same group may be grouped
     * together when displayed in the client.
     *
     * @return recipe group. An empty string denotes no group. May not be null.
     */
    @NotNull
    public String getGroup() {
        return group;
    }

    /**
     * Set the group of this recipe. Recipes with the same group may be grouped
     * together when displayed in the client.
     *
     * @param group recipe group. An empty string denotes no group. May not be
     * null.
     */
    public void setGroup(@NotNull String group) {
        Preconditions.checkArgument(group != null, "group cannot be null");
        this.group = group;
    }
}
