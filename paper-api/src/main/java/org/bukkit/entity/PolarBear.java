package org.bukkit.entity;

/**
 * Represents a polar bear.
 *
 * @since 1.10.2
 */
// Paper start
public interface PolarBear extends Animals {

    /**
     * Returns whether the polar bear is standing.
     *
     * @return whether the polar bear is standing
     * @since 1.18.2
     */
    boolean isStanding();

    /**
     * Sets whether the polar bear is standing.
     *
     * @param standing whether the polar bear should be standing
     * @since 1.18.2
     */
    void setStanding(boolean standing);

}
// Paper end
