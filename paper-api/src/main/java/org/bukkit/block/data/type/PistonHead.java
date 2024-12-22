package org.bukkit.block.data.type;

/**
 * 'short' denotes this piston head is shorter than the usual amount because it
 * is currently retracting.
 *
 * @since 1.13
 */
public interface PistonHead extends TechnicalPiston {

    /**
     * Gets the value of the 'short' property.
     *
     * @return the 'short' value
     */
    boolean isShort();

    /**
     * Sets the value of the 'short' property.
     *
     * @param _short the new 'short' value
     */
    void setShort(boolean _short);
}
