package org.bukkit.inventory;

public interface EnchantingInventory extends Inventory {
    /**
     * Set the item being enchanted.
     * @param item The new item
     */
    void setItem(ItemStack item);
    /**
     * Get the item being enchanted.
     * @return The current item.
     */
    ItemStack getItem();
}
