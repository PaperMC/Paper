package org.bukkit.entity;

/**
 * A Glow Squid.
 */
public interface GlowSquid extends Squid {

    /**
     * Get the number of dark ticks remaining for this squid.
     *
     * Bravo Six will go dark for 100 ticks (5 seconds) if damaged.
     *
     * @return dark ticks remaining
     */
    int getDarkTicksRemaining();

    /**
     * Sets the number of dark ticks remaining for this squid.
     *
     * Bravo Six will go dark for 100 ticks (5 seconds) if damaged.
     *
     * @param darkTicksRemaining dark ticks remaining
     */
    void setDarkTicksRemaining(int darkTicksRemaining);
}
