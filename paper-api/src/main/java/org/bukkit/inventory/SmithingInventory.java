package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Smithing table.
 */
public interface SmithingInventory extends Inventory {

    /**
     * Check what item is in the result slot of this smithing table.
     *
     * @return the result item
     */
    @Nullable
    ItemStack getResult();

    /**
     * Set the item in the result slot of the smithing table
     *
     * @param newResult the new result item
     */
    void setResult(@Nullable ItemStack newResult);

    /**
     * Get the current recipe formed on the smithing table, if any.
     *
     * @return the recipe, or null if the current contents don't match any
     * recipe
     */
    @Nullable
    Recipe getRecipe();
}
