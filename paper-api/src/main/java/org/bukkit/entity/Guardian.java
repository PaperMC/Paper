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
}
