package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.Material;

/**
 * Represents leather armor ({@link Material#LEATHER_BOOTS}, {@link Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_HELMET}, or {@link Material#LEATHER_LEGGINGS}) that can be colored.
 */
public interface LeatherArmorMeta extends ItemMeta {

    /**
     * Gets the color of the armor
     *
     * @return the color of the armor, never null
     */
    Color getColor();

    /**
     * Sets the color of the armor
     *
     * @param color the color to set, null makes it the default leather color
     */
    void setColor(Color color);

    LeatherArmorMeta clone();
}
