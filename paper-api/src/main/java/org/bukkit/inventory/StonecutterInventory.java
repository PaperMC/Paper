package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable; // Paper

/**
 * Interface to the inventory of a Stonecutter.
 *
 * @since 1.14
 */
public interface StonecutterInventory extends Inventory {

    // Paper start
    /**
     * Gets the input item.
     *
     * @return input item
     * @since 1.15.2
     */
    @Nullable
    default ItemStack getInputItem() {
        return getItem(0);
    }

    /**
     * Sets the input item.
     *
     * @param itemStack item to set
     * @since 1.15.2
     */
    default void setInputItem(@Nullable ItemStack itemStack) {
        setItem(0, itemStack);
    }

    /**
     * Gets the result item.
     *
     * @return result
     * @since 1.15.2
     */
    @Nullable
    default ItemStack getResult() {
        return getItem(1);
    }

    /**
     * Sets the result item.
     *
     * @param itemStack item to set
     * @since 1.15.2
     */
    default void setResult(@Nullable ItemStack itemStack) {
        setItem(1, itemStack);
    }
    // Paper end
}
