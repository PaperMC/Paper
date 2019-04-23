package org.bukkit.block.data.type;

import org.bukkit.block.data.Waterlogged;

/**
 * 'bottom' indicates whether the scaffolding is floating or not.
 * <br>
 * 'distance' indicates the distance from a scaffolding block placed above a
 * 'bottom' scaffold.
 * <br>
 * When 'distance' reaches {@link #getMaximumDistance()} the block will drop.
 */
public interface Scaffolding extends Waterlogged {

    /**
     * Gets the value of the 'bottom' property.
     *
     * @return the 'bottom' value
     */
    boolean isBottom();

    /**
     * Sets the value of the 'bottom' property.
     *
     * @param bottom the new 'bottom' value
     */
    void setBottom(boolean bottom);

    /**
     * Gets the value of the 'distance' property.
     *
     * @return the 'distance' value
     */
    int getDistance();

    /**
     * Sets the value of the 'distance' property.
     *
     * @param distance the new 'distance' value
     */
    void setDistance(int distance);

    /**
     * Gets the maximum allowed value of the 'distance' property.
     *
     * @return the maximum 'distance' value
     */
    int getMaximumDistance();
}
