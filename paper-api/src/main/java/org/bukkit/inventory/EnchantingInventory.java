package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of an Enchantment Table.
 *
 * @since 1.1.0
 */
public interface EnchantingInventory extends Inventory {

    /**
     * Set the item being enchanted.
     *
     * @param item The new item
     */
    void setItem(@Nullable ItemStack item);

    /**
     * Get the item being enchanted.
     *
     * @return The current item.
     */
    @Nullable
    ItemStack getItem();

    /**
     * Set the secondary item being used for the enchant.
     *
     * @param item The new item
     * @since 1.8
     */
    void setSecondary(@Nullable ItemStack item);

    /**
     * Get the secondary item being used for the enchant.
     *
     * @return The second item
     * @since 1.8
     */
    @Nullable
    ItemStack getSecondary();
}
