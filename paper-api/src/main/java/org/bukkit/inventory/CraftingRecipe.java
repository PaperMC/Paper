package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a shaped or shapeless crafting recipe.
 *
 * @since 1.20.1
 */
public abstract class CraftingRecipe implements Recipe, Keyed {
    private final NamespacedKey key;
    private final ItemStack output;
    private String group = "";
    private CraftingBookCategory category = CraftingBookCategory.MISC;

    protected CraftingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        this.key = key;
        this.output = new ItemStack(result);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get the result of this recipe.
     *
     * @return The result stack.
     */
    @Override
    @NotNull
    public ItemStack getResult() {
        return output.clone();
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

    /**
     * Gets the category which this recipe will appear in the recipe book under.
     * <br>
     * Defaults to {@link CraftingBookCategory#MISC} if not set.
     *
     * @return recipe book category
     */
    @NotNull
    public CraftingBookCategory getCategory() {
        return category;
    }

    /**
     * Sets the category which this recipe will appear in the recipe book under.
     * <br>
     * Defaults to {@link CraftingBookCategory#MISC} if not set.
     *
     * @param category recipe book category
     */
    public void setCategory(@NotNull CraftingBookCategory category) {
        Preconditions.checkArgument(category != null, "category cannot be null");
        this.category = category;
    }

    /**
     * Checks an ItemStack to be used in constructors related to CraftingRecipe
     * is not empty.
     *
     * @param result an ItemStack
     * @return the same result ItemStack
     * @throws IllegalArgumentException if the {@code result} is an empty item
     * (AIR)
     */
    @ApiStatus.Internal
    @NotNull
    protected static ItemStack checkResult(@NotNull ItemStack result) {
        Preconditions.checkArgument(!result.isEmpty(), "Recipe cannot have an empty result."); // Paper
        return result;
    }
}
