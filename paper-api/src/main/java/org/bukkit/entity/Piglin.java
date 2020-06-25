package org.bukkit.entity;

/**
 * Represents a Piglin.
 */
public interface Piglin extends Monster {

    /**
     * Gets whether the piglin is a baby
     *
     * @return Whether the piglin is a baby
     */
    public boolean isBaby();

    /**
     * Sets whether the piglin is a baby
     *
     * @param flag Whether the piglin is a baby
     */
    public void setBaby(boolean flag);
}
