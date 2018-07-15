package org.bukkit.block.data;

/**
 * 'power' represents the redstone power level currently being emitted or
 * transmitted via this block.
 * <br>
 * May not be over 9000 or {@link #getMaximumPower()} (usually 15).
 */
public interface AnaloguePowerable extends BlockData {

    /**
     * Gets the value of the 'power' property.
     *
     * @return the 'power' value
     */
    int getPower();

    /**
     * Sets the value of the 'power' property.
     *
     * @param power the new 'power' value
     */
    void setPower(int power);

    /**
     * Gets the maximum allowed value of the 'power' property.
     *
     * @return the maximum 'power' value
     */
    int getMaximumPower();
}
