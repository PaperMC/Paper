package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a fishing hook.
 */
public interface FishHook extends Projectile {

    /**
     * Get the minimum number of ticks one has to wait for a fish biting.
     * <p>
     * The default is 100 ticks (5 seconds).<br>
     * Note that this is before applying lure.
     *
     * @return Minimum number of ticks one has to wait for a fish biting
     */
    public int getMinWaitTime();

    /**
     * Set the minimum number of ticks one has to wait for a fish biting.
     * <p>
     * The default is 100 ticks (5 seconds).<br>
     * Note that this is before applying lure.
     *
     * @param minWaitTime Minimum number of ticks one has to wait for a fish
     * biting
     */
    public void setMinWaitTime(int minWaitTime);

    /**
     * Get the maximum number of ticks one has to wait for a fish biting.
     * <p>
     * The default is 600 ticks (30 seconds).<br>
     * Note that this is before applying lure.
     *
     * @return Maximum number of ticks one has to wait for a fish biting
     */
    public int getMaxWaitTime();

    /**
     * Set the maximum number of ticks one has to wait for a fish biting.
     * <p>
     * The default is 600 ticks (30 seconds).<br>
     * Note that this is before applying lure.
     *
     * @param maxWaitTime Maximum number of ticks one has to wait for a fish
     * biting
     */
    public void setMaxWaitTime(int maxWaitTime);

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
    @Deprecated
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
    @Deprecated
    public void setBiteChance(double chance) throws IllegalArgumentException;

    /**
     * Check whether or not this fish hook is in open water.
     * <p>
     * Open water is defined by a 5x4x5 area of water, air and lily pads. If in
     * open water, treasure items may be caught.
     *
     * @return true if in open water, false otherwise
     */
    public boolean isInOpenWater();

    /**
     * Get the entity hooked by this fish hook.
     *
     * @return the hooked entity. null if none
     */
    @Nullable
    public Entity getHookedEntity();

    /**
     * Set the entity hooked by this fish hook.
     *
     * @param entity the entity to set, or null to unhook
     */
    public void setHookedEntity(@Nullable Entity entity);

    /**
     * Pull the hooked entity to the caster of this fish hook. If no entity is
     * hooked, this method has no effect.
     *
     * @return true if pulled, false if no entity is hooked
     */
    public boolean pullHookedEntity();

    /**
     * Get the current state of this fish hook.
     *
     * @return the fish hook state
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
}
