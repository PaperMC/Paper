package org.bukkit.inventory.meta;

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

    MapMeta clone();
}
