package org.bukkit.entity;

import org.bukkit.ItemStack;

/**
 * Represents a dropped item.
 * 
 * @author sk89q
 */
public interface ItemDrop extends Entity {
    /**
     * Gets the item stack.
     * 
     * @return
     */
    public ItemStack getItemStack();
}
