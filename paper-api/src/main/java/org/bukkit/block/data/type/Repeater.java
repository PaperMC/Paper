package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;

/**
 * 'delay' is the propagation delay of a repeater, i.e. how many ticks before it
 * will be activated from a current change and propagate it to the next block.
 * <br>
 * Delay may not be lower than {@link #getMinimumDelay()} or higher than
 * {@link #getMaximumDelay()}.
 * <br>
 * 'locked' denotes whether the repeater is in the locked state or not.
 * <br>
 * A locked repeater will not change its output until it is unlocked. In game, a
 * locked repeater is created by having a constant current perpendicularly
 * entering the block.
 */
public interface Repeater extends Directional, Powerable {

    /**
     * Gets the value of the 'delay' property.
     *
     * @return the 'delay' value
     */
    int getDelay();

    /**
     * Sets the value of the 'delay' property.
     *
     * @param delay the new 'delay' value
     */
    void setDelay(int delay);

    /**
     * Gets the minimum allowed value of the 'delay' property.
     *
     * @return the minimum 'delay' value
     */
    int getMinimumDelay();

    /**
     * Gets the maximum allowed value of the 'delay' property.
     *
     * @return the maximum 'delay' value
     */
    int getMaximumDelay();

    /**
     * Gets the value of the 'locked' property.
     *
     * @return the 'locked' value
     */
    boolean isLocked();

    /**
     * Sets the value of the 'locked' property.
     *
     * @param locked the new 'locked' value
     */
    void setLocked(boolean locked);
}
