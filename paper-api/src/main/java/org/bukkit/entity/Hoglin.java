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
     * Sets the conversion counter value. The counter is incremented
     * every tick the method {@link #isConverting()} returns true. Setting
     * this value will not start the conversion if the {@link Hoglin} is
     * not in a valid environment ({@link org.bukkit.World#isPiglinSafe})
     * to convert, is immune to zombification ({@link #isImmuneToZombification()})
     * or has no AI ({@link #hasAI}).
     *
     * When this reaches 300, the entity will be converted. To stop the
     * conversion use {@link #setImmuneToZombification(boolean)}.
     *
     * @param time new conversion counter
     */
    public void setConversionTime(int time);

    /**
     * Get if this entity is in the process of converting to a Zoglin.
     *
     * @return conversion status
     */
    boolean isConverting();
}
