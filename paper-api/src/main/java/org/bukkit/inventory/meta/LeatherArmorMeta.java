package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents leather armor ({@link Material#LEATHER_BOOTS}, {@link
 * Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_HELMET}, or {@link
 * Material#LEATHER_LEGGINGS}) that can be colored.
 */
public interface LeatherArmorMeta extends ItemMeta {

    /**
     * Gets the color of the armor. If it has not been set otherwise, it will
     * be {@link ItemFactory#getDefaultLeatherColor()}.
     *
     * @return the color of the armor, never null
     */
    @NotNull
    Color getColor();

    /**
     * Sets the color of the armor.
     *
     * @param color the color to set. Setting it to null is equivalent to
     *     setting it to {@link ItemFactory#getDefaultLeatherColor()}.
     */
    void setColor(@Nullable Color color);

    @Override
    @NotNull
    LeatherArmorMeta clone();
}
