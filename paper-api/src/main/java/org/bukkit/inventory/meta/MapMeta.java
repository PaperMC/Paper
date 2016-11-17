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
     * Sets the location name.
     *
     * @param name the name to set
     */
    void setLocationName(String name);

    MapMeta clone();
}
