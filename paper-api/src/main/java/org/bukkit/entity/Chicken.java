package org.bukkit.entity;

/**
 * Represents a Chicken.
 */
// Paper start
public interface Chicken extends Animals {

    /**
     * Gets if this chicken was spawned as a chicken jockey.
     *
     * @return is chicken jockey
     */
    boolean isChickenJockey();

    /**
     * Sets if this chicken was spawned as a chicken jockey.
     *
     * @param isChickenJockey is chicken jockey
     */
    void setIsChickenJockey(boolean isChickenJockey);

    /**
     * Gets the number of ticks till this chicken lays an egg.
     *
     * @return ticks till the chicken lays an egg
     */
    int getEggLayTime();

    /**
     * Sets the number of ticks till this chicken lays an egg.
     *
     * @param eggLayTime ticks till the chicken lays an egg
     */
    void setEggLayTime(int eggLayTime);
}
// Paper end
