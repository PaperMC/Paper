package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'layers' represents the amount of layers of snow which are present in this
 * block.
 * <br>
 * May not be lower than {@link #getMinimumLayers()} or higher than
 * {@link #getMaximumLayers()}.
 */
public interface Snow extends BlockData {

    /**
     * Gets the value of the 'layers' property.
     *
     * @return the 'layers' value
     */
    int getLayers();

    /**
     * Sets the value of the 'layers' property.
     *
     * @param layers the new 'layers' value
     */
    void setLayers(int layers);

    /**
     * Gets the minimum allowed value of the 'layers' property.
     *
     * @return the minimum 'layers' value
     */
    int getMinimumLayers();

    /**
     * Gets the maximum allowed value of the 'layers' property.
     *
     * @return the maximum 'layers' value
     */
    int getMaximumLayers();
}
