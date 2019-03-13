package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Item Frame
 */
public interface ItemFrame extends Hanging {

    /**
     * Get the item in this frame
     *
     * @return a defensive copy the item in this item frame
     */
    @NotNull
    public ItemStack getItem();

    /**
     * Set the item in this frame
     *
     * @param item the new item
     */
    public void setItem(@Nullable ItemStack item);

    /**
     * Set the item in this frame
     *
     * @param item the new item
     * @param playSound whether or not to play the item placement sound
     */
    public void setItem(@Nullable ItemStack item, boolean playSound);

    /**
     * Get the rotation of the frame's item
     *
     * @return the direction
     */
    @NotNull
    public Rotation getRotation();

    /**
     * Set the rotation of the frame's item
     *
     * @param rotation the new rotation
     * @throws IllegalArgumentException if rotation is null
     */
    public void setRotation(@NotNull Rotation rotation) throws IllegalArgumentException;
}
