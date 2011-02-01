package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a dropped item.
 * 
 * @author sk89q
 */
public interface ItemDrop extends Entity {
    /**
     * Gets the item stack contained in this ItemDrop
     *
     * @return ItemStack of the contents of this drop
     */
    public ItemStack getItemStack();


    /**
     * sets the item stack contained in this ItemDrop
     * 
     * @param items New contents of this drop
     */
    public void setItemStack(ItemStack items);
}
