package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * The 'moisture' level of farmland indicates how close it is to a water source
 * (if any).
 * <br>
 * A higher moisture level leads, to faster growth of crops on this block, but
 * cannot be higher than {@link #getMaximumMoisture()}.
 */
public interface Farmland extends BlockData {

    /**
     * Gets the value of the 'moisture' property.
     *
     * @return the 'moisture' value
     */
    int getMoisture();

    /**
     * Sets the value of the 'moisture' property.
     *
     * @param moisture the new 'moisture' value
     */
    void setMoisture(int moisture);

    /**
     * Gets the maximum allowed value of the 'moisture' property.
     *
     * @return the maximum 'moisture' value
     */
    int getMaximumMoisture();
}
