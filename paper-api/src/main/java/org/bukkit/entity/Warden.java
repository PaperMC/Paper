package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * A Warden.
 */
public interface Warden extends Monster {

    /**
     * Gets the anger level of this warden.
     *
     * Anger is an integer from 0 to 150. Once a Warden reaches 80 anger at a
     * target it will actively pursue it.
     *
     * @param entity target
     * @return anger level
     */
    int getAnger(@NotNull Entity entity);

    /**
     * Increases the anger level of this warden.
     *
     * @param entity target
     * @param increase number to increase by
     * @see #getAnger(org.bukkit.entity.Entity)
     */
    void increaseAnger(@NotNull Entity entity, int increase);

    /**
     * Sets the anger level of this warden.
     *
     * @param entity target
     * @param anger new anger level
     * @see #getAnger(org.bukkit.entity.Entity)
     */
    void setAnger(@NotNull Entity entity, int anger);
}
