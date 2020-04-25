package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable; // Paper

/**
 * Interface to the inventory of a Grindstone.
 */
public interface GrindstoneInventory extends Inventory {

    // Paper start
    /**
     * Gets the upper input item.
     *
     * @return upper input item
     */
    @Nullable
    default ItemStack getUpperItem() {
        return getItem(0);
    }

    /**
     * Sets the upper input item.
     *
     * @param upperItem item to set
     */
    default void setUpperItem(@Nullable ItemStack upperItem) {
        setItem(0, upperItem);
    }

    /**
     * Gets the lower input item.
     *
     * @return lower input item
     */
    @Nullable
    default ItemStack getLowerItem() {
        return getItem(1);
    }

    /**
     * Sets the lower input item.
     *
     * @param lowerItem item to set
     */
    default void setLowerItem(@Nullable ItemStack lowerItem) {
        setItem(1, lowerItem);
    }

    /**
     * Gets the result.
     *
     * @return result
     */
    @Nullable
    default ItemStack getResult() {
        return getItem(2);
    }

    /**
     * Sets the result.
     *
     * @param result item to set
     */
    default void setResult(@Nullable ItemStack result) {
        setItem(2, result);
    }
    // Paper end
}
