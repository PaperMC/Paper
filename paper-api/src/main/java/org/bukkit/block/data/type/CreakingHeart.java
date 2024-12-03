package org.bukkit.block.data.type;

import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.ApiStatus;

/**
 * 'active' is whether the block is active.
 * <br>
 * 'natural' is whether this is a naturally generated block.
 */
@ApiStatus.Experimental
public interface CreakingHeart extends Orientable {

    /**
     * Gets the value of the 'active' property.
     *
     * @return the 'active' value
     */
    boolean isActive();

    /**
     * Sets the value of the 'active' property.
     *
     * @param active the new 'active' value
     */
    void setActive(boolean active);

    /**
     * Gets the value of the 'natural' property.
     *
     * @return the 'natural' value
     */
    boolean isNatural();

    /**
     * Sets the value of the 'natural' property.
     *
     * @param natural the new 'natural' value
     */
    void setNatural(boolean natural);
}
