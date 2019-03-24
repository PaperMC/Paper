package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the crafting inventories
 */
public interface CraftingInventory extends Inventory {

    /**
     * Check what item is in the result slot of this crafting inventory.
     *
     * @return The result item.
     */
    @Nullable
    ItemStack getResult();

    /**
     * Get the contents of the crafting matrix.
     *
     * @return The contents. Individual entries may be null.
     */
    @Nullable ItemStack @NotNull [] getMatrix(); // Paper - make array elements nullable instead array

    /**
     * Set the item in the result slot of the crafting inventory.
     *
     * @param newResult The new result item.
     */
    void setResult(@Nullable ItemStack newResult);

    /**
     * Replace the contents of the crafting matrix
     *
     * @param contents The new contents. Individual entries may be null.
     * @throws IllegalArgumentException if the length of contents is greater
     *     than the size of the crafting matrix.
     */
    void setMatrix(@Nullable ItemStack @NotNull [] contents); // Paper - make array elements nullable instead array

    /**
     * Get the current recipe formed on the crafting inventory, if any.
     *
     * @return The recipe, or null if the current contents don't match any
     *     recipe.
     */
    @Nullable
    Recipe getRecipe();
}
