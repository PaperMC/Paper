package org.bukkit.block.data.type;

import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Waterlogged;

/**
 * 'candles' represents the number of candles which are present.
 *
 * @since 1.17
 */
public interface Candle extends Lightable, Waterlogged {

    /**
     * Gets the value of the 'candles' property.
     *
     * @return the 'candles' value
     */
    int getCandles();

    /**
     * Sets the value of the 'candles' property.
     *
     * @param candles the new 'candles' value
     */
    void setCandles(int candles);

    /**
     * Gets the maximum allowed value of the 'candles' property.
     *
     * @return the maximum 'candles' value
     */
    int getMaximumCandles();

    // Paper start
    /**
     * Gets the minimum allowed value of the 'candles' property.
     *
     * @return the minimum 'candles' value
     * @since 1.18.1
     */
    int getMinimumCandles();
    // Paper end
}
