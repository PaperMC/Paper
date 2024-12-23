package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Item Frame
 *
 * @since 1.4.5
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
     * @since 1.13.1
     */
    public void setItem(@Nullable ItemStack item, boolean playSound);

    /**
     * Gets the chance of the item being dropped upon this frame's destruction.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return chance of the off hand item being dropped
     * @since 1.16.4
     */
    float getItemDropChance();

    /**
     * Sets the chance of the off hand item being dropped upon this frame's
     * destruction.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance the chance of off hand item being dropped
     * @since 1.16.4
     */
    void setItemDropChance(float chance);

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

    /**
     * Returns whether the item frame is visible or not.
     *
     * @return whether the item frame is visible or not
     * @since 1.16.1
     */
    boolean isVisible();

    /**
     * Sets whether the item frame should be visible or not.
     *
     * @param visible whether the item frame is visible or not
     * @since 1.16.1
     */
    void setVisible(boolean visible);

    /**
     * Returns whether the item frame is "fixed" or not.
     *
     * When true it's not possible to destroy/move the frame (e.g. by damage,
     * interaction, pistons, or missing supporting blocks), rotate the item or
     * place/remove items.
     *
     * @return whether the item frame is fixed or not
     * @since 1.16.1
     */
    boolean isFixed();

    /**
     * Sets whether the item frame should be fixed or not.
     *
     * When set to true it's not possible to destroy/move the frame (e.g. by
     * damage, interaction, pistons, or missing supporting blocks), rotate the
     * item or place/remove items.
     *
     * @param fixed whether the item frame is fixed or not
     * @since 1.16.1
     */
    void setFixed(boolean fixed);
}
