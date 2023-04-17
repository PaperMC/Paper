package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.UndefinedNullability;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a map that can be scalable.
 */
public interface MapMeta extends ItemMeta {

    /**
     * Checks for existence of a map ID number.
     *
     * @return true if this has a map ID number.
     * @see #hasMapView()
     * @deprecated These methods are poor API: They rely on the caller to pass
     * in an only an integer property, and have poorly defined implementation
     * behavior if that integer is not a valid map (the current implementation
     * for example will generate a new map with a different ID). The xxxMapView
     * family of methods should be used instead.
     */
    @Deprecated
    boolean hasMapId();

    /**
     * Gets the map ID that is set. This is used to determine what map is
     * displayed.
     * <p>
     * Plugins should check that hasMapId() returns <code>true</code> before
     * calling this method.
     *
     * @return the map ID that is set
     * @see #getMapView()
     * @deprecated These methods are poor API: They rely on the caller to pass
     * in an only an integer property, and have poorly defined implementation
     * behavior if that integer is not a valid map (the current implementation
     * for example will generate a new map with a different ID). The xxxMapView
     * family of methods should be used instead.
     */
    @Deprecated
    int getMapId();

    /**
     * Sets the map ID. This is used to determine what map is displayed.
     *
     * @param id the map id to set
     * @see #setMapView(org.bukkit.map.MapView)
     * @deprecated These methods are poor API: They rely on the caller to pass
     * in an only an integer property, and have poorly defined implementation
     * behavior if that integer is not a valid map (the current implementation
     * for example will generate a new map with a different ID). The xxxMapView
     * family of methods should be used instead.
     */
    @Deprecated
    void setMapId(int id);

    /**
     * Checks for existence of an associated map.
     *
     * @return true if this item has an associated map
     */
    boolean hasMapView();

    /**
     * Gets the map view that is associated with this map item.
     *
     * <p>
     * Plugins should check that hasMapView() returns <code>true</code> before
     * calling this method.
     *
     * @return the map view, or null if the item hasMapView(), but this map does
     * not exist on the server
     */
    @Nullable
    MapView getMapView();

    /**
     * Sets the associated map. This is used to determine what map is displayed.
     *
     * <p>
     * The implementation <b>may</b> allow null to clear the associated map, but
     * this is not required and is liable to generate a new (undefined) map when
     * the item is first used.
     *
     * @param map the map to set
     */
    void setMapView(@UndefinedNullability("implementation defined") MapView map);

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
     * @deprecated This method does not have the expected effect and is
     * actually an alias for {@link ItemMeta#hasLocalizedName()}.
     */
    @Deprecated
    boolean hasLocationName();

    /**
     * Gets the location name that is set.
     * <p>
     * Plugins should check that hasLocationName() returns <code>true</code>
     * before calling this method.
     *
     * @return the location name that is set
     * @deprecated This method does not have the expected effect and is
     * actually an alias for {@link ItemMeta#getLocalizedName()}.
     */
    @Deprecated
    @Nullable
    String getLocationName();

    /**
     * Sets the location name.
     *
     * @param name the name to set
     * @deprecated This method does not have the expected effect and is
     * actually an alias for {@link ItemMeta#setLocalizedName(String)}.
     */
    @Deprecated
    void setLocationName(@Nullable String name);

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
    @Nullable
    Color getColor();

    /**
     * Sets the map color. A custom map color will alter the display of the map
     * in an inventory slot.
     *
     * @param color the color to set
     */
    void setColor(@Nullable Color color);

    @Override
    @NotNull
    MapMeta clone();
}
