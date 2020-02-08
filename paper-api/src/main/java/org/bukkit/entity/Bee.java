package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Bee.
 */
public interface Bee extends Animals {

    /**
     * Get the bee's hive location.
     *
     * @return hive location or null
     */
    @Nullable
    Location getHive();

    /**
     * Set the bee's hive location.
     *
     * @param location or null
     */
    void setHive(@Nullable Location location);

    /**
     * Get the bee's flower location.
     *
     * @return flower location or null
     */
    @Nullable
    Location getFlower();

    /**
     * Set the bee's flower location.
     *
     * @param location or null
     */
    void setFlower(@Nullable Location location);

    /**
     * Get if the bee has nectar.
     *
     * @return nectar
     */
    boolean hasNectar();

    /**
     * Set if the bee has nectar.
     *
     * @param nectar whether the entity has nectar
     */
    void setHasNectar(boolean nectar);

    /**
     * Get if the bee has stung.
     *
     * @return has stung
     */
    boolean hasStung();

    /**
     * Set if the bee has stung.
     *
     * @param stung has stung
     */
    void setHasStung(boolean stung);

    /**
     * Get the bee's anger level.
     *
     * @return anger level
     */
    int getAnger();

    /**
     * Set the bee's new anger level.
     *
     * @param anger new anger
     */
    void setAnger(int anger);

    /**
     * Get the amount of ticks the bee cannot enter the hive for.
     *
     * @return Ticks the bee cannot enter a hive for
     */
    int getCannotEnterHiveTicks();

    /**
     * Set the amount of ticks the bee cannot enter a hive for.
     *
     * @param ticks Ticks the bee cannot enter a hive for
     */
    void setCannotEnterHiveTicks(int ticks);
}
