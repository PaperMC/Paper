package org.bukkit.entity;

/**
 * Represents a Ghast.
 */
public interface Ghast extends Flying, Enemy {

    /**
     * Gets whether the Ghast is charging
     *
     * @return Whether the Ghast is charging
     */
    boolean isCharging();

    /**
     * Sets whether the Ghast is charging
     *
     * @param flag Whether the Ghast is charging
     */
    void setCharging(boolean flag);

    /**
     * Returns the explosion power of shot fireballs.
     *
     * @return explosion power of shot fireballs
     */
    int getExplosionPower();

    /**
     * Sets the explosion power of shot fireballs.
     *
     * @param explosionPower explosion power of shot fireballs
     * @throws IllegalArgumentException if the explosion power is less than 0 or greater than 127
     */
    void setExplosionPower(int explosionPower);
}
