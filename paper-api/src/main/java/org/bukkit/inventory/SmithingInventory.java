package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Smithing table.
 *
 * @since 1.16.1
 */
public interface SmithingInventory extends Inventory {

    /**
     * Check what item is in the result slot of this smithing table.
     *
     * @return the result item
     * @since 1.16.2
     */
    @Nullable
    ItemStack getResult();

    /**
     * Set the item in the result slot of the smithing table
     *
     * @param newResult the new result item
     * @since 1.16.2
     */
    void setResult(@Nullable ItemStack newResult);

    /**
     * Get the current recipe formed on the smithing table, if any.
     *
     * @return the recipe, or null if the current contents don't match any
     * recipe
     * @since 1.16.5
     */
    @Nullable
    Recipe getRecipe();

    // Paper start
    /**
     * Gets the input template (first slot).
     *
     * @return input template item
     * @since 1.20
     */
    default @Nullable ItemStack getInputTemplate() {
        return this.getItem(0);
    }

    /**
     * Sets the input template (first slot).
     *
     * @param itemStack item to set
     * @since 1.20
     */
    default void setInputTemplate(@Nullable ItemStack itemStack) {
        this.setItem(0, itemStack);
    }
    /**
     * Gets the input equipment (second slot).
     *
     * @return input equipment item
     * @since 1.16.2
     */
    default @Nullable ItemStack getInputEquipment() {
        return this.getItem(1);
    }

    /**
     * Sets the input equipment (second slot).
     *
     * @param itemStack item to set
     * @since 1.16.2
     */
    default void setInputEquipment(@Nullable ItemStack itemStack) {
        this.setItem(1, itemStack);
    }

    /**
     * Gets the input mineral (third slot).
     *
     * @return input mineral item
     * @since 1.16.2
     */
    default @Nullable ItemStack getInputMineral() {
        return this.getItem(2);
    }

    /**
     * Sets the input mineral (third slot).
     *
     * @param itemStack item to set
     * @since 1.16.2
     */
    default void setInputMineral(@Nullable ItemStack itemStack) {
        this.setItem(2, itemStack);
    }
    // Paper end
}
