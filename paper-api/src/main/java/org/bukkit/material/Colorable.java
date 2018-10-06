package org.bukkit.material;

import org.bukkit.DyeColor;

/**
 * An object that can be colored.
 */
public interface Colorable {

    /**
     * Gets the color of this object.
     * <br>
     * This may be null to represent the default color of an object.
     *
     * @return The DyeColor of this object.
     */
    public DyeColor getColor();

    /**
     * Sets the color of this object to the specified DyeColor.
     * <br>
     * This may be null to represent the default color of an object.
     *
     * @param color The color of the object, as a DyeColor.
     */
    public void setColor(DyeColor color);

}
