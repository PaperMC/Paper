package org.bukkit.entity;

/**
 * Represents a polar bear.
 */
public interface PolarBear extends Animals {

    /**
     * Returns whether the polar bear is standing.
     *
     * @return whether the polar bear is standing
     */
    boolean isStanding();

    /**
     * Sets whether the polar bear is standing.
     *
     * @param standing whether the polar bear should be standing
     */
    void setStanding(boolean standing);

}
