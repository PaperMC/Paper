package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a shapeless recipe, where the arrangement of the ingredients on
 * the crafting grid does not matter.
 */
public class ShapelessRecipe extends CraftingRecipe {
    private final List<RecipeChoice> ingredients = new ArrayList<>();

    @Deprecated
    public ShapelessRecipe(@NotNull ItemStack result) {
        super(NamespacedKey.randomKey(), result);
    }

    /**
     * Create a shapeless recipe to craft the specified ItemStack. The
     * constructor merely determines the result and type; to set the actual
     * recipe, you'll need to call the appropriate methods.
     *
     * @param key the unique recipe key
     * @param result The item you want the recipe to create.
     * @see ShapelessRecipe#addIngredient(Material)
     * @see ShapelessRecipe#addIngredient(MaterialData)
     * @see ShapelessRecipe#addIngredient(Material,int)
     * @see ShapelessRecipe#addIngredient(int,Material)
     * @see ShapelessRecipe#addIngredient(int,MaterialData)
     * @see ShapelessRecipe#addIngredient(int,Material,int)
     */
    public ShapelessRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
        super(key, result);
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public ShapelessRecipe addIngredient(@NotNull MaterialData ingredient) {
        return addIngredient(1, ingredient);
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public ShapelessRecipe addIngredient(@NotNull Material ingredient) {
        return addIngredient(1, ingredient, 0);
    }

    /**
     * Adds the specified ingredient.
     *
     * @param ingredient The ingredient to add.
     * @param rawdata The data value, or -1 to allow any data value.
     * @return The changed recipe, so you can chain calls.
     * @deprecated Magic value
     */
    @Deprecated
    @NotNull
    public ShapelessRecipe addIngredient(@NotNull Material ingredient, int rawdata) {
        return addIngredient(1, ingredient, rawdata);
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public ShapelessRecipe addIngredient(int count, @NotNull MaterialData ingredient) {
        return addIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @return The changed recipe, so you can chain calls.
     */
    @NotNull
    public ShapelessRecipe addIngredient(int count, @NotNull Material ingredient) {
        return addIngredient(count, ingredient, 0);
    }

    /**
     * Adds multiples of the specified ingredient.
     *
     * @param count How many to add (can't be more than 9!)
     * @param ingredient The ingredient to add.
     * @param rawdata The data value, or -1 to allow any data value.
     * @return The changed recipe, so you can chain calls.
     * @deprecated Magic value
     */
    @Deprecated
    @NotNull
    public ShapelessRecipe addIngredient(int count, @NotNull Material ingredient, int rawdata) {
        Preconditions.checkArgument(ingredients.size() + count <= 9, "Shapeless recipes cannot have more than 9 ingredients");

        // -1 is the old wildcard, map to Short.MAX_VALUE as the new one
        if (rawdata == -1) {
            rawdata = Short.MAX_VALUE;
        }

        while (count-- > 0) {
            ingredients.add(new RecipeChoice.MaterialChoice(Collections.singletonList(ingredient)));
        }
        return this;
    }

    @NotNull
    public ShapelessRecipe addIngredient(@NotNull RecipeChoice ingredient) {
        Preconditions.checkArgument(ingredients.size() + 1 <= 9, "Shapeless recipes cannot have more than 9 ingredients");

        ingredients.add(ingredient);
        return this;
    }

    /**
     * Removes an ingredient from the list.
     *
     * @param ingredient The ingredient to remove
     * @return The changed recipe.
     */
    @NotNull
    public ShapelessRecipe removeIngredient(@NotNull RecipeChoice ingredient) {
        ingredients.remove(ingredient);

        return this;
    }

    /**
     * Removes an ingredient from the list. If the ingredient occurs multiple
     * times, only one instance of it is removed. Only removes exact matches,
     * with a data value of 0.
     *
     * @param ingredient The ingredient to remove
     * @return The changed recipe.
     */
    @NotNull
    public ShapelessRecipe removeIngredient(@NotNull Material ingredient) {
        return removeIngredient(ingredient, 0);
    }

    /**
     * Removes an ingredient from the list. If the ingredient occurs multiple
     * times, only one instance of it is removed. If the data value is -1,
     * only ingredients with a -1 data value will be removed.
     *
     * @param ingredient The ingredient to remove
     * @return The changed recipe.
     */
    @NotNull
    public ShapelessRecipe removeIngredient(@NotNull MaterialData ingredient) {
        return removeIngredient(ingredient.getItemType(), ingredient.getData());
    }

    /**
     * Removes multiple instances of an ingredient from the list. If there are
     * less instances then specified, all will be removed. Only removes exact
     * matches, with a data value of 0.
     *
     * @param count The number of copies to remove.
     * @param ingredient The ingredient to remove
     * @return The changed recipe.
     */
    @NotNull
    public ShapelessRecipe removeIngredient(int count, @NotNull Material ingredient) {
        return removeIngredient(count, ingredient, 0);
    }

    /**
     * Removes multiple instances of an ingredient from the list. If there are
     * less instances then specified, all will be removed. If the data value
     * is -1, only ingredients with a -1 data value will be removed.
     *
     * @param count The number of copies to remove.
     * @param ingredient The ingredient to remove.
     * @return The changed recipe.
     */
    @NotNull
    public ShapelessRecipe removeIngredient(int count, @NotNull MaterialData ingredient) {
        return removeIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    /**
     * Removes an ingredient from the list. If the ingredient occurs multiple
     * times, only one instance of it is removed. If the data value is -1,
     * only ingredients with a -1 data value will be removed.
     *
     * @param ingredient The ingredient to remove
     * @param rawdata The data value;
     * @return The changed recipe.
     * @deprecated Magic value
     */
    @Deprecated
    @NotNull
    public ShapelessRecipe removeIngredient(@NotNull Material ingredient, int rawdata) {
        return removeIngredient(1, ingredient, rawdata);
    }

    /**
     * Removes multiple instances of an ingredient from the list. If there are
     * less instances then specified, all will be removed. If the data value
     * is -1, only ingredients with a -1 data value will be removed.
     *
     * @param count The number of copies to remove.
     * @param ingredient The ingredient to remove.
     * @param rawdata The data value.
     * @return The changed recipe.
     * @deprecated Magic value
     */
    @Deprecated
    @NotNull
    public ShapelessRecipe removeIngredient(int count, @NotNull Material ingredient, int rawdata) {
        Iterator<RecipeChoice> iterator = ingredients.iterator();
        while (count > 0 && iterator.hasNext()) {
            ItemStack stack = iterator.next().getItemStack();
            if (stack.getType() == ingredient && stack.getDurability() == rawdata) {
                iterator.remove();
                count--;
            }
        }
        return this;
    }

    /**
     * Get the list of ingredients used for this recipe.
     *
     * @return The input list
     */
    @NotNull
    public List<ItemStack> getIngredientList() {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>(ingredients.size());
        for (RecipeChoice ingredient : ingredients) {
            result.add(ingredient.getItemStack().clone());
        }
        return result;
    }

    @NotNull
    public List<RecipeChoice> getChoiceList() {
        List<RecipeChoice> result = new ArrayList<>(ingredients.size());
        for (RecipeChoice ingredient : ingredients) {
            result.add(ingredient.clone());
        }
        return result;
    }
}
