package org.bukkit.inventory;

import java.util.Collections;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a furnace recipe.
 */
public class FurnaceRecipe extends CookingRecipe<FurnaceRecipe> {

    @Deprecated
    public FurnaceRecipe(@NotNull ItemStack result, @NotNull Material source) {
        this(NamespacedKey.randomKey(), result, source, 0, 0, 200);
    }

    @Deprecated
    public FurnaceRecipe(@NotNull ItemStack result, @NotNull MaterialData source) {
        this(NamespacedKey.randomKey(), result, source.getItemType(), source.getData(), 0, 200);
    }

    @Deprecated
    public FurnaceRecipe(@NotNull ItemStack result, @NotNull MaterialData source, float experience) {
        this(NamespacedKey.randomKey(), result, source.getItemType(), source.getData(), experience, 200);
    }

    @Deprecated
    public FurnaceRecipe(@NotNull ItemStack result, @NotNull Material source, int data) {
        this(NamespacedKey.randomKey(), result, source, data, 0, 200);
    }

    /**
     * Create a furnace recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param source The input material.
     * @param experience The experience given by this recipe
     * @param cookingTime The cooking time (in ticks)
     */
    public FurnaceRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        this(key, result, source, 0, experience, cookingTime);
    }

    @Deprecated
    public FurnaceRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, int data, float experience, int cookingTime) {
        this(key, result, new RecipeChoice.MaterialChoice(Collections.singletonList(source)), experience, cookingTime);
    }

    /**
     * Create a furnace recipe to craft the specified ItemStack.
     *
     * @param key The unique recipe key
     * @param result The item you want the recipe to create.
     * @param input The input choices.
     * @param experience The experience given by this recipe
     * @param cookingTime The cooking time (in ticks)
     */
    public FurnaceRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice input, float experience, int cookingTime) {
        super(key, result, input, experience, cookingTime);
    }

    /**
     * Sets the input of this furnace recipe.
     *
     * @param input The input material.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public FurnaceRecipe setInput(@NotNull MaterialData input) {
        return setInput(input.getItemType(), input.getData());
    }

    @NotNull
    @Override
    public FurnaceRecipe setInput(@NotNull Material input) {
        return (FurnaceRecipe) super.setInput(input);
    }

    /**
     * Sets the input of this furnace recipe.
     *
     * @param input The input material.
     * @param data The data value. (Note: This is currently ignored by the
     *     CraftBukkit server.)
     * @return The changed recipe, so you can chain calls.
     * @deprecated Magic value
     */
    @Deprecated
    public FurnaceRecipe setInput(@NotNull Material input, int data) {
        return setInputChoice(new RecipeChoice.MaterialChoice(Collections.singletonList(input)));
    }

    @NotNull
    @Override
    public FurnaceRecipe setInputChoice(@NotNull RecipeChoice input) {
        return (FurnaceRecipe) super.setInputChoice(input);
    }
}
