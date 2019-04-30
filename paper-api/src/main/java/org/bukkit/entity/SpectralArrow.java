package org.bukkit.entity;

/**
 * Represents a spectral arrow.
 */
public interface SpectralArrow extends AbstractArrow {

    /**
     * Returns the amount of time that this arrow will apply
     * the glowing effect for.
     *
     * @return the glowing effect ticks
     */
    int getGlowingTicks();

    /**
     * Sets the amount of time to apply the glowing effect for.
     *
     * @param duration the glowing effect ticks
     */
    void setGlowingTicks(int duration);
}
