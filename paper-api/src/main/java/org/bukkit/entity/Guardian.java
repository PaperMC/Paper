package org.bukkit.entity;

public interface Guardian extends Monster {

    /**
     * Sets whether the guardian laser should show or not.
     *
     * A target must be present. If no target is present the laser will not show
     * and the method will return false.
     *
     * @param activated whether the laser is active
     * @return true if the laser was activated otherwise false
     * @see #getTarget()
     * @see #setTarget(LivingEntity)
     */
    boolean setLaser(boolean activated);

    /**
     * Gets whether the guardian laser is active or not.
     *
     * @return true if the laser is active otherwise false
     */
    boolean hasLaser();

    /**
     * Get the duration (in ticks) that a laser attack takes.
     *
     * @return the laser duration in ticks
     */
    public int getLaserDuration();

    /**
     * Set the amount of ticks that have elapsed since this guardian has initiated
     * a laser attack. If set to {@link #getLaserDuration()} or greater, the guardian
     * will inflict damage upon its target and the laser attack will complete.
     * <p>
     * For this value to have any effect, the guardian must have an active target
     * (see {@link #setTarget(LivingEntity)}) and be charging a laser attack (where
     * {@link #hasLaser()} is true). The client may display a different animation of
     * the guardian laser than the set ticks.
     *
     * @param ticks the ticks to set. Must be at least -10
     */
    public void setLaserTicks(int ticks);

    /**
     * Get the amount of ticks that have elapsed since this guardian has initiated
     * a laser attack.
     * <p>
     * This value may or may not be significant depending on whether or not the guardian
     * has an active target ({@link #getTarget()}) and is charging a laser attack
     * ({@link #hasLaser()}). This value is not reset after a successful attack nor used
     * in the next and will be reset to the minimum value when the guardian initiates a
     * new one.
     *
     * @return the laser ticks ranging from -10 to {@link #getLaserDuration()}
     */
    public int getLaserTicks();

    /**
     * Check if the Guardian is an elder Guardian
     *
     * @return true if the Guardian is an Elder Guardian, false if not
     * @deprecated should check if instance of {@link ElderGuardian}.
     */
    @Deprecated
    public boolean isElder();

    /**
     * @param shouldBeElder shouldBeElder
     * @deprecated Must spawn a new {@link ElderGuardian}.
     */
    @Deprecated
    public void setElder(boolean shouldBeElder);

    /**
     * Check whether or not this guardian is moving.
     * <p>
     * While moving, the guardian's spikes are retracted and will not inflict thorns
     * damage upon entities that attack it. Additionally, a moving guardian cannot
     * attack another entity. If stationary (i.e. this method returns {@code false}),
     * thorns damage is guaranteed and the guardian may initiate laser attacks.
     *
     * @return true if moving, false if stationary
     */
    public boolean isMoving();
}
