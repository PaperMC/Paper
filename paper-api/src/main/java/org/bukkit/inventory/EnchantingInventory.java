package org.bukkit.inventory;

/**
 * Interface to the inventory of an Enchantment Table.
 */
public interface EnchantingInventory extends Inventory {

    /**
     * Set the item being enchanted.
     *
     * @param item The new item
     */
    void setItem(ItemStack item);

    /**
     * Get the item being enchanted.
     *
     * @return The current item.
     */
    ItemStack getItem();

    /**
     * Set the secondary item being used for the enchant.
     *
     * @param item The new item
     */
    void setSecondary(ItemStack item);

    /**
     * Get the secondary item being used for the enchant.
     *
     * @return The second item
     */
    ItemStack getSecondary();
}
