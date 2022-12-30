package org.bukkit.entity;

/**
 * Represents a Hoglin.
 */
public interface Hoglin extends Animals, Enemy {

    /**
     * Gets whether the hoglin is immune to zombification.
     *
     * @return Whether the hoglin is immune to zombification
     */
    public boolean isImmuneToZombification();

    /**
     * Sets whether the hoglin is immune to zombification.
     *
     * @param flag Whether the hoglin is immune to zombification
     */
    public void setImmuneToZombification(boolean flag);

    /**
     * Get whether the hoglin is able to be hunted by piglins.
     *
     * @return Whether the hoglin is able to be hunted by piglins
     */
    public boolean isAbleToBeHunted();

    /**
     * Sets whether the hoglin is able to be hunted by piglins.
     *
     * @param flag Whether the hoglin is able to be hunted by piglins.
     */
    public void setIsAbleToBeHunted(boolean flag);

    /**
     * Gets the amount of ticks until this entity will be converted to a Zoglin.
     *
     * When this reaches 300, the entity will be converted.
     *
     * @return conversion time
     * @throws IllegalStateException if {@link #isConverting()} is false.
     */
    public int getConversionTime();

    /**
     * Sets the amount of ticks until this entity will be converted to a Zoglin.
     *
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
     */
    public void setConversionTime(int time);

    /**
     * Get if this entity is in the process of converting to a Zoglin.
     *
     * @return conversion status
     */
    boolean isConverting();
}
