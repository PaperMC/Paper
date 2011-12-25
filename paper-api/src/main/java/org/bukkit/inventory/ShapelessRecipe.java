package org.bukkit.inventory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * Represents a shapeless recipe, where the arrangement of the ingredients on the crafting grid
 * does not matter.
 */
public class ShapelessRecipe implements Recipe {
    private ItemStack output;
    private ArrayList<MaterialData> ingredients = new ArrayList<MaterialData>();

    /**
     * Create a shapeless recipe to craft the specified ItemStack. The constructor merely determines the
     * result and type; to set the actual recipe, you'll need to call the appropriate methods.
     *
     * @param result The item you want the recipe to create.
     * @see ShapelessRecipe#addIngredient(Material)
     * @see ShapelessRecipe#addIngredient(MaterialData)
     */
    public ShapelessRecipe(ItemStack result) {
        this.output = result;
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(MaterialData ingredient) {
        return addIngredient(1, ingredient);
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(Material ingredient) {
        return addIngredient(1, ingredient, 0);
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @param rawdata The data value.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(Material ingredient, int rawdata) {
        return addIngredient(1, ingredient, rawdata);
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(int count, MaterialData ingredient) {
        if (ingredients.size() + count > 9) {
            throw new IllegalArgumentException("Shapeless recipes cannot have more than 9 ingredients");
        }
        while (count-- > 0) {
            ingredients.add(ingredient);
        }
        return this;
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(int count, Material ingredient) {
        return addIngredient(count, ingredient, 0);
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @param rawdata The data value.
     * @return The changed recipe, so you can chain calls.
     */
    public ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata) {
        MaterialData data = ingredient.getNewData((byte) rawdata);

        if (data == null) {
            data = new MaterialData(ingredient, (byte) rawdata);
        }
        return addIngredient(count, data);
    }

    /**
     * Removes an ingredient from the list. If the ingredient occurs multiple times,
     * only one instance of it is removed.
     *
     * @param ingredient The ingredient to remove
     * @return The changed recipe.
     */
    public ShapelessRecipe removeIngredient(MaterialData ingredient) {
        this.ingredients.remove(ingredient);
        return this;
    }

    /**
     * Get the result of this recipe.
     *
     * @return The result stack.
     */
    public ItemStack getResult() {
        return output;
    }

    /**
     * Get the list of ingredients used for this recipe.
     *
     * @return The input list
     */
    public ArrayList<MaterialData> getIngredientList() {
        return ingredients;
    }
}
