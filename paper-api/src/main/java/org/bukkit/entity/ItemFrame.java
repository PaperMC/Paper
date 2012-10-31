package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an Item Frame
 */
public interface ItemFrame extends Hanging {

    /**
     * Get the item in this frame
     *
     * @return a defensive copy the item in this item frame
     */
    public ItemStack getItem();

    /**
     * Set the item in this frame
     *
     * @param item the new item
     */
    public void setItem(ItemStack item);

    /**
     * Get the rotation of the frame's item
     *
     * @return the direction
     */
    public Rotation getRotation();

    /**
     * Set the rotation of the frame's item
     *
     * @param rotation the new rotation
     * @throws IllegalArgumentException if rotation is null
     */
    public void setRotation(Rotation rotation) throws IllegalArgumentException;
}
