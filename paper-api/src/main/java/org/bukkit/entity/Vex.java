package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Vex.
 */
public interface Vex extends Monster {

    /**
     * Gets the charging state of this entity.
     *
     * When this entity is charging it will having a glowing red texture.
     *
     * @return charging state
     */
    boolean isCharging();

    /**
     * Sets the charging state of this entity.
     *
     * When this entity is charging it will having a glowing red texture.
     *
     * @param charging new state
     */
    void setCharging(boolean charging);

    /**
     * Gets the bound of this entity.
     *
     * An idle vex will navigate a 15x11x15 area centered around its bound
     * location.
     *
     * When summoned by an Evoker, this location will be set to that of the
     * summoner.
     *
     * @return {@link Location} of the bound or null if not set
     */
    @Nullable
    Location getBound();

    /**
     * Sets the bound of this entity.
     *
     * An idle vex will navigate a 15x11x15 area centered around its bound
     * location.
     *
     * When summoned by an Evoker, this location will be set to that of the
     * summoner.
     *
     * @param location {@link Location} of the bound or null to clear
     */
    void setBound(@Nullable Location location);

    /**
     * Gets the remaining lifespan of this entity.
     *
     * @return life in ticks
     */
    int getLifeTicks();

    /**
     * Sets the remaining lifespan of this entity.
     *
     * @param lifeTicks life in ticks, or negative for unlimited lifepan
     */
    void setLifeTicks(int lifeTicks);

    /**
     * Gets if the entity has a limited life.
     *
     * @return true if the entity has limited life
     */
    boolean hasLimitedLife();
}
