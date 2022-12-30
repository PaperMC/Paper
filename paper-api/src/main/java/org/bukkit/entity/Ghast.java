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
}
