package org.bukkit.entity;

/**
 * Represents a Ghast.
 *
 * @since 1.0.0 R1
 */
public interface Ghast extends Flying, Enemy {

    /**
     * Gets whether the Ghast is charging
     *
     * @return Whether the Ghast is charging
     * @since 1.18.2
     */
    boolean isCharging();

    /**
     * Sets whether the Ghast is charging
     *
     * @param flag Whether the Ghast is charging
     * @since 1.18.2
     */
    void setCharging(boolean flag);

    // Paper start
    /**
     * Returns the explosion power of shot fireballs.
     *
     * @return explosion power of shot fireballs
     * @since 1.18.2
     */
    int getExplosionPower();

    /**
     * Sets the explosion power of shot fireballs.
     *
     * @param explosionPower explosion power of shot fireballs
     * @throws IllegalArgumentException if the explosion power is less than 0 or greater than 127
     * @since 1.18.2
     */
    void setExplosionPower(int explosionPower);
    // Paper end
}
