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
     * @deprecated This API duplicates existing API which uses the more
     * preferable name due to mirroring internals better
     */
    @Deprecated
    int getLifeTicks();

    /**
     * Sets the remaining lifespan of this entity.
     *
     * @param lifeTicks life in ticks, or negative for unlimited lifepan
     * @deprecated This API duplicates existing API which uses the more
     * preferable name due to mirroring internals better
     */
    @Deprecated
    void setLifeTicks(int lifeTicks);

    /**
     * Gets if the entity has a limited life.
     *
     * @return true if the entity has limited life
     * @deprecated This API duplicates existing API which uses the more
     * preferable name due to mirroring internals better
     */
    @Deprecated
    boolean hasLimitedLife();
    // Paper start

    /**
     * Get the Mob that summoned this vex
     *
     * @return Mob that summoned this vex
     */
    @Nullable
    Mob getSummoner();

    /**
     * Set the summoner of this vex
     *
     * @param summoner New summoner
     */
    void setSummoner(@Nullable Mob summoner);

    /**
     * Gets if this vex should start to take damage
     * once {@link Vex#getLimitedLifetimeTicks()} is less than or equal to 0.
     * 
     * @return will take damage
     */
    boolean hasLimitedLifetime();

    /**
     * Sets if this vex should start to take damage
     * once {@link Vex#getLimitedLifetimeTicks()} is less than or equal to 0.
     *      
     * @param hasLimitedLifetime should take damage
     */
    void setLimitedLifetime(boolean hasLimitedLifetime);

    /**
     * Gets the number of ticks remaining until the vex will start
     * to take damage.
     * 
     * @return ticks until the vex will start to take damage
     */
    int getLimitedLifetimeTicks();

    /**
     * Sets the number of ticks remaining until the vex takes damage.
     * This number is ticked down only if {@link Vex#hasLimitedLifetime()} is true.
     * 
     * @param ticks ticks remaining
     */
    void setLimitedLifetimeTicks(int ticks);
    // Paper end
}
