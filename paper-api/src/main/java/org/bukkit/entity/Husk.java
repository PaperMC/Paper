package org.bukkit.entity;

/**
 * Represents a Husk - variant of {@link Zombie}.
 *
 * @since 1.11
 */
public interface Husk extends Zombie {

    /**
     * Get if this entity is in the process of converting to a Zombie as a
     * result of being underwater.
     *
     * @return conversion status
     * @since 1.13.2
     */
    @Override
    boolean isConverting();

    /**
     * Gets the amount of ticks until this entity will be converted to a Zombie
     * as a result of being underwater.
     *
     * When this reaches 0, the entity will be converted.
     *
     * @return conversion time
     * @throws IllegalStateException if {@link #isConverting()} is false.
     * @since 1.13.2
     */
    @Override
    int getConversionTime();

    /**
     * Sets the amount of ticks until this entity will be converted to a Zombie
     * as a result of being underwater.
     *
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
     * @since 1.13.2
     */
    @Override
    void setConversionTime(int time);
}
