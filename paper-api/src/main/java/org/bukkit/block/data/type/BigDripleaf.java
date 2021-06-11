package org.bukkit.block.data.type;

import org.jetbrains.annotations.NotNull;

/**
 * 'tilt' indicates how far the leaf is tilted.
 */
public interface BigDripleaf extends Dripleaf {

    /**
     * Gets the value of the 'tilt' property.
     *
     * @return the 'tilt' value
     */
    @NotNull
    Tilt getTilt();

    /**
     * Sets the value of the 'tilt' property.
     *
     * @param tilt the new 'tilt' value
     */
    void setTilt(@NotNull Tilt tilt);

    /**
     * The tilt of a leaf.
     */
    public enum Tilt {
        /**
         * No tilt.
         */
        NONE,
        /**
         * Unstable tilt.
         */
        UNSTABLE,
        /**
         * Partial tilt.
         */
        PARTIAL,
        /**
         * Pinball.
         */
        FULL;
    }
}
