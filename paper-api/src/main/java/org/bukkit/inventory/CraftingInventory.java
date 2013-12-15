package org.bukkit.inventory;

/**
 * Interface to the crafting inventories
 */
public interface CraftingInventory extends Inventory {

    /**
     * Check what item is in the result slot of this crafting inventory.
     *
     * @return The result item.
     */
    ItemStack getResult();

    /**
     * Get the contents of the crafting matrix.
     *
     * @return The contents.
     */
    ItemStack[] getMatrix();

    /**
     * Set the item in the result slot of the crafting inventory.
     *
     * @param newResult The new result item.
     */
    void setResult(ItemStack newResult);

    /**
     * Replace the contents of the crafting matrix
     *
     * @param contents The new contents.
     * @throws IllegalArgumentException if the length of contents is greater
     *     than the size of the crafting matrix.
     */
    void setMatrix(ItemStack[] contents);

    /**
     * Get the current recipe formed on the crafting inventory, if any.
     *
     * @return The recipe, or null if the current contents don't match any
     *     recipe.
     */
    Recipe getRecipe();
}