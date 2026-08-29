package org.bukkit.inventory;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

/**
 * Represents a recipe that is applicable to brewing stands.
 */
public class BrewingRecipe implements Recipe, Keyed {

    private final NamespacedKey key;
    private final ItemStack result;
    private final RecipeChoice input;
    private final RecipeChoice ingredient;

    /**
     * Creates a new brewing recipe.
     *
     * @param key        a unique key for the brewing recipe
     * @param result     the resulting itemstack that will appear in the 3 bottom slots
     * @param input      the input placed into the bottom 3 slots
     * @param ingredient the ingredient placed into the top slot
     */
    public BrewingRecipe(final NamespacedKey key, final ItemStack result, final RecipeChoice input, final RecipeChoice ingredient) {
        this.key = key;
        this.result = result.clone();
        this.input = input.clone();
        this.ingredient = ingredient.clone();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Gets the resulting itemstack after the brew has finished.
     *
     * @return the result itemstack
     */
    public ItemStack getResult() {
        return this.result.clone();
    }

    /**
     * Gets the input for the bottom 3 slots in the brewing stand.
     *
     * @return the bottom 3 slot ingredients
     */
    public RecipeChoice getInput() {
        return this.input.clone();
    }

    /**
     * Gets the ingredient in the top slot of the brewing stand.
     *
     * @return the top slot input
     */
    public RecipeChoice getIngredient() {
        return this.ingredient.clone();
    }

}
