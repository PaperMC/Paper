package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.Collections;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cooking recipe.
 * @param <T> type of recipe
 */
public abstract class CookingRecipe<T extends CookingRecipe> implements Recipe, Keyed {
    private final NamespacedKey key;
    private ItemStack output;
    private RecipeChoice ingredient;
    private float experience;
    private int cookingTime;
    private String group = "";

    /**
     * Create a cooking recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param source The input material.
     * @param experience The experience given by this recipe
     * @param cookingTime The cooking time (in ticks)
     */
    public CookingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        this(key, result, new RecipeChoice.MaterialChoice(Collections.singletonList(source)), experience, cookingTime);
    }

    /**
     * Create a cooking recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param input The input choices.
     * @param experience The experience given by this recipe
     * @param cookingTime The cooking time (in ticks)
     */
    public CookingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice input, float experience, int cookingTime) {
        this.key = key;
        this.output = new ItemStack(result);
        this.ingredient = input;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    /**
     * Sets the input of this cooking recipe.
     *
     * @param input The input material.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public CookingRecipe setInput(@NotNull Material input) {
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
    public T setInputChoice(@NotNull RecipeChoice input) {
        this.ingredient = input;
        return (T) this;
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

    /**
     * Sets the experience given by this recipe.
     *
     * @param experience the experience level
     */
    public void setExperience(float experience) {
        this.experience = experience;
    }

    /**
     * Get the experience given by this recipe.
     *
     * @return experience level
     */
    public float getExperience() {
        return experience;
    }

    /**
     * Set the cooking time for this recipe in ticks.
     *
     * @param cookingTime new cooking time
     */
    public void setCookingTime(int cookingTime) {
        Preconditions.checkArgument(cookingTime >= 0, "cookingTime must be >= 0");
        this.cookingTime = cookingTime;
    }

    /**
     * Get the cooking time for this recipe in ticks.
     *
     * @return cooking time
     */
    public int getCookingTime() {
        return cookingTime;
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
        Preconditions.checkArgument(group != null, "group");
        this.group = group;
    }
}
