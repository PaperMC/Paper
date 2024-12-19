package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Bee.
 *
 * @since 1.15
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
     * @since 1.15.2
     */
    int getCannotEnterHiveTicks();

    /**
     * Set the amount of ticks the bee cannot enter a hive for.
     *
     * @param ticks Ticks the bee cannot enter a hive for
     * @since 1.15.2
     */
    void setCannotEnterHiveTicks(int ticks);

    // Paper start
    /**
     * Sets the override for if the bee is currently rolling.
     *
     * @param rolling is rolling, or unset for vanilla behavior
     * @since 1.18.2
     */
    void setRollingOverride(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState rolling);

    /**
     * Gets the plugin set override for if the bee is currently rolling.
     *
     * @return plugin set rolling override
     * @since 1.18.2
     */
    @org.jetbrains.annotations.NotNull
    net.kyori.adventure.util.TriState getRollingOverride();

    /**
     * Gets if the bee is currently rolling.
     *
     * @return is rolling
     * @since 1.18.2
     */
    boolean isRolling();

    /**
     * Sets how many crops this bee has grown since it last
     * pollinated.
     * @param crops number of crops
     * @since 1.19.2
     */
    void setCropsGrownSincePollination(int crops);

    /**
     * Gets how many crops this bee has grown since it last
     * pollinated.
     * @return number of crops
     * @since 1.19.2
     */
    int getCropsGrownSincePollination();

    /**
     * Sets how many ticks this bee has gone without pollinating.
     *
     * @param ticks number of ticks
     * @since 1.19.2
     */
    void setTicksSincePollination(int ticks);

    /**
     * Gets how many ticks this bee has gone without pollinating
     *
     * @return number of ticks
     * @since 1.19.2
     */
    int getTicksSincePollination();
    // Paper end
}
