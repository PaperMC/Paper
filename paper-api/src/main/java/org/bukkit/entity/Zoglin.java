package org.bukkit.entity;

/**
 * Represents a Zoglin.
 */
public interface Zoglin extends Monster {

    /**
     * Gets whether the zoglin is a baby
     *
     * @return Whether the zoglin is a baby
     */
    public boolean isBaby();

    /**
     * Sets whether the zoglin is a baby
     *
     * @param flag Whether the zoglin is a baby
     */
    public void setBaby(boolean flag);
}
