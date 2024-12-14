package org.bukkit.entity;

/**
 * Represents a Chicken.
 *
 * @since 1.0.0 R1
 */
// Paper start
public interface Chicken extends Animals {

    /**
     * Gets if this chicken was spawned as a chicken jockey.
     *
     * @return is chicken jockey
     * @since 1.19.2
     */
    boolean isChickenJockey();

    /**
     * Sets if this chicken was spawned as a chicken jockey.
     *
     * @param isChickenJockey is chicken jockey
     * @since 1.19.2
     */
    void setIsChickenJockey(boolean isChickenJockey);

    /**
     * Gets the number of ticks till this chicken lays an egg.
     *
     * @return ticks till the chicken lays an egg
     * @since 1.19.2
     */
    int getEggLayTime();

    /**
     * Sets the number of ticks till this chicken lays an egg.
     *
     * @param eggLayTime ticks till the chicken lays an egg
     * @since 1.19.2
     */
    void setEggLayTime(int eggLayTime);
}
// Paper end
