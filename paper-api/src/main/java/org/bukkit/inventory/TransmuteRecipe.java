package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a recipe which will change the type of the input material when
 * combined with an additional material, but preserve all custom data. Only the
 * item type of the result stack will be used.
 * <br>
 * Used for dyeing shulker boxes in Vanilla.
 */
public class TransmuteRecipe extends CraftingRecipe implements ComplexRecipe {

    private final RecipeChoice input;
    private final RecipeChoice material;

    /**
     * Create a transmute recipe to produce a result of the specified type.
     *
     * @param key the unique recipe key
     * @param result the transmuted result material
     * @param input the input ingredient
     * @param material the additional ingredient
     */
    public TransmuteRecipe(@NotNull NamespacedKey key, @NotNull Material result, @NotNull RecipeChoice input, @NotNull RecipeChoice material) {
        super(key, checkResult(new ItemStack(result)));
        this.input = input.validate(false).clone(); // Paper
        this.material = material.validate(false).clone(); // Paper
    }

    /**
     * Gets the input material, which will be transmuted.
     *
     * @return the input from transmutation
     */
    @NotNull
    public RecipeChoice getInput() {
        return input.clone();
    }

    /**
     * Gets the additional material required to cause the transmutation.
     *
     * @return the ingredient material
     */
    @NotNull
    public RecipeChoice getMaterial() {
        return material.clone();
    }
}
