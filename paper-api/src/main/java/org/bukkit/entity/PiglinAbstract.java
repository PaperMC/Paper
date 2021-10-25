package org.bukkit.entity;

/**
 * Piglin / Piglin Brute.
 */
public interface PiglinAbstract extends Monster, Ageable {

    /**
     * Gets whether the piglin is immune to zombification.
     *
     * @return Whether the piglin is immune to zombification
     */
    public boolean isImmuneToZombification();

    /**
     * Sets whether the piglin is immune to zombification.
     *
     * @param flag Whether the piglin is immune to zombification
     */
    public void setImmuneToZombification(boolean flag);

    /**
     * Gets the amount of ticks until this entity will be converted to a
     * Zombified Piglin.
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
     * this value will not start the conversion if the {@link PiglinAbstract} is
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
     * Get if this entity is in the process of converting to a Zombified Piglin.
     *
     * @return conversion status
     */
    boolean isConverting();

    /**
     * Gets whether the piglin is a baby
     *
     * @return Whether the piglin is a baby
     * @deprecated see {@link Ageable#isAdult()}
     */
    @Deprecated(since = "1.16.2")
    public boolean isBaby();

    /**
     * Sets whether the piglin is a baby
     *
     * @param flag Whether the piglin is a baby
     * @deprecated see {@link Ageable#setBaby()} and {@link Ageable#setAdult()}
     */
    @Deprecated(since = "1.16.2")
    public void setBaby(boolean flag);
}
