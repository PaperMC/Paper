package org.bukkit.entity;

import org.bukkit.Location;
import org.checkerframework.checker.index.qual.NonNegative;
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

    // Paper start
    /**
     * Sets the override for if the bee is currently rolling.
     *
     * @param rolling is rolling, or unset for vanilla behavior
     */
    void setRollingOverride(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState rolling);

    /**
     * Gets the plugin set override for if the bee is currently rolling.
     *
     * @return plugin set rolling override
     */
    @org.jetbrains.annotations.NotNull
    net.kyori.adventure.util.TriState getRollingOverride();

    /**
     * Gets if the bee is currently rolling.
     *
     * @return is rolling
     */
    boolean isRolling();

    /**
     * Sets how many crops this bee has grown since it last
     * pollinated.
     * @param crops number of crops
     */
    void setCropsGrownSincePollination(int crops);

    /**
     * Gets how many crops this bee has grown since it last
     * pollinated.
     * @return number of crops
     */
    int getCropsGrownSincePollination();

    /**
     * Sets how many ticks this bee has gone without pollinating.
     *
     * @param ticks number of ticks
     */
    void setTicksSincePollination(int ticks);

    /**
     * Gets how many ticks this bee has gone without pollinating
     *
     * @return number of ticks
     */
    int getTicksSincePollination();

    /**
     * Sets how many ticks have passed since this bee last stung.
     * This value is used to determine when the bee should die after stinging.
     * <p>
     * Note that bees don’t die at a fixed time. Instead, every few ticks, they
     * have a random chance of dying, and that chance increases with this value.
     *
     * @param time number of ticks since last sting
     */
    void setTimeSinceSting(@NonNegative int time);

    /**
     * Gets how many ticks have passed since this bee last stung.
     * This value increases each tick after the bee stings and is used
     * to determine when the bee should die.
     * <p>
     * Note that bees don’t die at a fixed time. Instead, every few ticks, they
     * have a random chance of dying, and that chance increases with this value.
     *
     * @return number of ticks since last sting
     */
    int getTimeSinceSting();
    // Paper end
}
