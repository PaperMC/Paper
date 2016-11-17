package org.bukkit.inventory.meta;

import org.bukkit.Color;

/**
 * Represents a map that can be scalable.
 */
public interface MapMeta extends ItemMeta {

    /**
     * Checks to see if this map is scaling.
     *
     * @return true if this map is scaling
     */
    boolean isScaling();

    /**
     * Sets if this map is scaling or not.
     *
     * @param value true to scale
     */
    void setScaling(boolean value);

    /**
     * Checks for existence of a location name.
     *
     * @return true if this has a location name
     */
    boolean hasLocationName();

    /**
     * Gets the location name that is set.
     * <p>
     * Plugins should check that hasLocationName() returns <code>true</code>
     * before calling this method.
     *
     * @return the location name that is set
     */
    String getLocationName();

    /**
     * Sets the location name. A custom map color will alter the display of the
     * map in an inventory slot.
     *
     * @param name the name to set
     */
    void setLocationName(String name);

    /**
     * Checks for existence of a map color.
     *
     * @return true if this has a custom map color
     */
    boolean hasColor();

    /**
     * Gets the map color that is set. A custom map color will alter the display
     * of the map in an inventory slot.
     * <p>
     * Plugins should check that hasColor() returns <code>true</code> before
     * calling this method.
     *
     * @return the map color that is set
     */
    Color getColor();

    /**
     * Sets the map color. A custom map color will alter the display of the map
     * in an inventory slot.
     *
     * @param color the color to set
     */
    void setColor(Color color);

    MapMeta clone();
}
