package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a fishing hook.
 *
 * @since 1.7.10
 */
public interface FishHook extends Projectile {

    /**
     * Get the minimum number of ticks one has to wait for a fish appearing.
     * <p>
     * The default is 100 ticks (5 seconds).<br>
     * Note that this is before applying lure.
     *
     * @return Minimum number of ticks one has to wait for a fish appearing
     * @since 1.16.4
     */
    public int getMinWaitTime();

    /**
     * Set the minimum number of ticks one has to wait for a fish appearing.
     * <p>
     * The default is 100 ticks (5 seconds).<br>
     * Note that this is before applying lure.
     *
     * @param minWaitTime Minimum number of ticks one has to wait for a fish
     * appearing
     * @since 1.16.4
     */
    public void setMinWaitTime(int minWaitTime);

    /**
     * Get the maximum number of ticks one has to wait for a fish appearing.
     * <p>
     * The default is 600 ticks (30 seconds).<br>
     * Note that this is before applying lure.
     *
     * @return Maximum number of ticks one has to wait for a fish appearing
     * @since 1.16.4
     */
    public int getMaxWaitTime();

    /**
     * Set the maximum number of ticks one has to wait for a fish appearing.
     * <p>
     * The default is 600 ticks (30 seconds).<br>
     * Note that this is before applying lure.
     *
     * @param maxWaitTime Maximum number of ticks one has to wait for a fish
     * appearing
     * @since 1.16.4
     */
    public void setMaxWaitTime(int maxWaitTime);

    /**
     * Set both the minimum (default 100) and maximum (default 600) amount
     * of ticks one has to wait for a fish appearing.
     *
     * @param min minimum ticks for a fish to appear
     * @param max maximum ticks for a fish to appear
     * @since 1.19.4
     */
    public void setWaitTime(int min, int max);

    /**
     * Get the minimum number of ticks one has to wait for a fish to bite
     * after appearing.
     * <p>
     * The default is 20 ticks (1 second).<br>
     * Lure does not affect this value.
     * This will also effect the radius (0.1 * lureTime) of where
     * the fish will appear.
     *
     * @return Minimum number of ticks one has to wait for a fish to bite
     * @since 1.19.4
     */
    public int getMinLureTime();

    /**
     * Set the minimum number of ticks one has to wait for a fish to bite
     * after appearing.
     * <p>
     * The default is 20 ticks (1 second).<br>
     * Lure does not affect this value.
     * This will also effect the radius (0.1 * lureTime) of where
     * the fish will appear.
     *
     * @param minLureTime Minimum number of ticks one has to wait for a fish
     * to bite
     * @since 1.19.4
     */
    public void setMinLureTime(int minLureTime);

    /**
     * Get the maximum number of ticks one has to wait for a fish to bite
     * after appearing.
     * <p>
     * The default is 80 ticks (4 second).<br>
     * Lure does not affect this value.
     * This will also effect the radius (0.1 * lureTime) of where
     * the fish will appear.
     *
     * @return Maximum number of ticks one has to wait for a fish to bite
     * @since 1.19.4
     */
    public int getMaxLureTime();

    /**
     * Set the maximum number of ticks one has to wait for a fish to bite
     * after appearing.
     * <p>
     * The default is 80 ticks (4 second).<br>
     * Lure does not affect this value.
     * This will also effect the radius (0.1 * lureTime) of where
     * the fish will appear.
     *
     * @param maxLureTime Maximum number of ticks one has to wait for a fish
     * to bite
     * @since 1.19.4
     */
    public void setMaxLureTime(int maxLureTime);

    /**
     * Set both the minimum (default 20) and maximum (default 80) amount
     * of ticks one has to wait for a fish to bite after appearing.
     *
     * @param min minimum ticks to wait for a bite
     * @param max maximum ticks to wait for a bite
     * @since 1.19.4
     */
    public void setLureTime(int min, int max);

    /**
     * Get the minimum angle (in degrees, 0 being positive Z 90 being negative
     * X) of where a fish will appear after the wait time.
     * <p>
     * The default is 0 degrees.
     *
     * @return Minimum angle of where a fish will appear
     * @since 1.19.4
     */
    public float getMinLureAngle();

    /**
     * Set the minimum angle (in degrees, 0 being positive Z 90 being negative
     * X) of where a fish will appear after the wait time.
     * <p>
     * The default is 0 degrees.
     *
     * @param minLureAngle Minimum angle of where a fish may appear
     * @since 1.19.4
     */
    public void setMinLureAngle(float minLureAngle);

    /**
     * Get the maximum angle (in degrees, 0 being positive Z 90 being negative
     * X) of where a fish will appear after the wait time.
     * <p>
     * The default is 360 degrees.
     *
     * @return Maximum angle of where a fish will appear
     * @since 1.19.4
     */
    public float getMaxLureAngle();

    /**
     * Set the maximum angle (in degrees, 0 being positive Z 90 being negative
     * X) of where a fish will appear after the wait time.
     * <p>
     * The default is 360 degrees.
     *
     * @param maxLureAngle Maximum angle of where a fish may appear
     * @since 1.19.4
     */
    public void setMaxLureAngle(float maxLureAngle);

    /**
     * Set both the minimum (default 0) and maximum (default 360) angle of where
     * a fish will appear after the wait time.
     *
     * 0 degrees is positive Z, 90 degrees is negative X.
     *
     * @param min minimum angle in degrees
     * @param max maximum angle in degrees
     * @since 1.19.4
     */
    public void setLureAngle(float min, float max);

    /**
     * Get whether the lure enchantment should be applied to reduce the wait
     * time.
     * <p>
     * The default is true.<br>
     * Lure reduces the wait time by 100 ticks (5 seconds) for each level of the
     * enchantment.
     *
     * @return Whether the lure enchantment should be applied to reduce the wait
     * time
     * @since 1.16.4
     */
    public boolean getApplyLure();

    /**
     * Set whether the lure enchantment should be applied to reduce the wait
     * time.
     * <p>
     * The default is true.<br>
     * Lure reduces the wait time by 100 ticks (5 seconds) for each level of the
     * enchantment.
     *
     * @param applyLure Whether the lure enchantment should be applied to reduce
     * the wait time
     * @since 1.16.4
     */
    public void setApplyLure(boolean applyLure);

    /**
     * Gets the chance of a fish biting.
     * <p>
     * 0.0 = No Chance.<br>
     * 1.0 = Instant catch.
     *
     * @return chance the bite chance
     * @deprecated has no effect in newer Minecraft versions
     */
    @Deprecated(since = "1.9.2")
    public double getBiteChance();

    /**
     * Sets the chance of a fish biting.
     * <p>
     * 0.0 = No Chance.<br>
     * 1.0 = Instant catch.
     *
     * @param chance the bite chance
     * @throws IllegalArgumentException if the bite chance is not between 0
     *     and 1
     * @deprecated has no effect in newer Minecraft versions
     */
    @Deprecated(since = "1.9.2")
    public void setBiteChance(double chance) throws IllegalArgumentException;

    /**
     * Check whether or not this fish hook is in open water.
     * <p>
     * Open water is defined by a 5x4x5 area of water, air and lily pads. If in
     * open water, treasure items may be caught.
     *
     * @return true if in open water, false otherwise
     * @since 1.16.5
     */
    public boolean isInOpenWater();

    /**
     * Get the entity hooked by this fish hook.
     *
     * @return the hooked entity. null if none
     * @since 1.16.5
     */
    @Nullable
    public Entity getHookedEntity();

    /**
     * Set the entity hooked by this fish hook.
     *
     * @param entity the entity to set, or null to unhook
     * @since 1.16.5
     */
    public void setHookedEntity(@Nullable Entity entity);

    /**
     * Pull the hooked entity to the caster of this fish hook. If no entity is
     * hooked, this method has no effect.
     *
     * @return true if pulled, false if no entity is hooked
     * @since 1.16.5
     */
    public boolean pullHookedEntity();

    /**
     * Whether or not wait and lure time will be impacted by direct sky access.
     *
     * True by default, causes a 50% time increase on average.
     *
     * @return skylight access influences catch rate
     * @since 1.19.4
     */
    public boolean isSkyInfluenced();

    /**
     * Set whether or not wait and lure time will be impacted by direct sky
     * access.
     *
     * True by default, causes a 50% time increase on average.
     *
     * @param skyInfluenced if this hook is influenced by skylight access
     * @since 1.19.4
     */
    public void setSkyInfluenced(boolean skyInfluenced);

    /**
     * Whether or not wait and lure time will be impacted by rain.
     *
     * True by default, causes a 25% time decrease on average.
     *
     * @return rain influences catch rate
     * @since 1.19.4
     */
    public boolean isRainInfluenced();

    /**
     * Set whether or not wait and lure time will be impacted by rain.
     *
     * True by default, causes a 25% time decrease on average.
     *
     * @param rainInfluenced if this hook is influenced by rain
     * @since 1.19.4
     */
    public void setRainInfluenced(boolean rainInfluenced);

    /**
     * Get the current state of this fish hook.
     *
     * @return the fish hook state
     * @since 1.16.5
     */
    @NotNull
    public HookState getState();

    /**
     * Represents a state in which a fishing hook may be.
     */
    public enum HookState {

        /**
         * The fishing hook has been cast and is either in the air or resting
         * against a block on the ground.
         */
        UNHOOKED,
        /**
         * The fishing hook has hooked an entity.
         */
        HOOKED_ENTITY,
        /**
         * The fishing hook is bobbing in the water, waiting for a bite.
         */
        BOBBING;
    }

    // Paper start - More FishHook API
    /**
     * Get the number of ticks the hook needs to wait for a fish to bite.
     *
     * @return Number of ticks
     * @since 1.18.2
     */
    int getWaitTime();

    /**
     * Sets the number of ticks the hook needs to wait for a fish to bite.
     *
     * @param ticks Number of ticks
     * @since 1.18.2
     */
    void setWaitTime(int ticks);

    /**
     * Get the number of ticks the fish has to swim until biting the hook.
     * The {@link #getWaitTime()} has to be zero or below for the fish to start the time until bite timer.
     *
     * @return number of ticks.
     *         A value of one indicates that the fish bites the very next time the fish hook is ticked
     *         while a value of zero represents a fish that has already bitten the hook.
     * @see #getWaitTime()
     * @since 1.20.6
     */
    @org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE)
    int getTimeUntilBite();

    /**
     * Sets the number of ticks the fish has to swim until biting the hook.
     *
     * @param ticks number of ticks.
     *              One is the minimum that can be passed to this method and instructs the fish to bite the very next tick.
     * @throws IllegalArgumentException if the passed tick value is less than one.
     * @since 1.20.6
     */
    void setTimeUntilBite(@org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) int ticks) throws IllegalArgumentException;

    /**
     * Completely resets this fishing hook's fishing state, re-randomizing the time needed until a fish is lured and
     * bites the hook.
     * <p>
     * This method takes all properties of the fishing hook into account when resetting said values, such as a lure
     * enchantment.
     *
     * @since 1.20.6
     */
    void resetFishingState();
    // Paper end
}
