package org.bukkit.entity;

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
}
