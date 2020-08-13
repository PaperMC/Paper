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
     * Sets the amount of ticks until this entity will be converted to a
     * Zombified Piglin.
     *
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
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
    @Deprecated
    public boolean isBaby();

    /**
     * Sets whether the piglin is a baby
     *
     * @param flag Whether the piglin is a baby
     * @deprecated see {@link Ageable#setBaby()} and {@link Ageable#setAdult()}
     */
    @Deprecated
    public void setBaby(boolean flag);
}
