package org.bukkit.entity;

/**
 * Represents a Creeper
 */
public interface Creeper extends Monster {

    /**
     * Checks if this Creeper is powered (Electrocuted)
     *
     * @return true if this creeper is powered
     */
    public boolean isPowered();

    /**
     * Sets the Powered status of this Creeper
     *
     * @param value New Powered status
     */
    public void setPowered(boolean value);

    /**
     * Set the maximum fuse ticks for this Creeper, where the maximum ticks
     * is the amount of time in which a creeper is allowed to be in the
     * primed state before exploding.
     *
     * @param ticks the new maximum fuse ticks
     */
    public void setMaxFuseTicks(int ticks);

    /**
     * Get the maximum fuse ticks for this Creeper, where the maximum ticks
     * is the amount of time in which a creeper is allowed to be in the
     * primed state before exploding.
     *
     * @return the maximum fuse ticks
     */
    public int getMaxFuseTicks();

    /**
     * Set the fuse ticks for this Creeper, where the ticks is the amount of
     * time in which a creeper has been in the primed state.
     *
     * @param ticks the new fuse ticks
     */
    public void setFuseTicks(int ticks);

    /**
     * Get the maximum fuse ticks for this Creeper, where the ticks is the
     * amount of time in which a creeper has been in the primed state.
     *
     * @return the fuse ticks
     */
    public int getFuseTicks();

    /**
     * Set the explosion radius in which this Creeper's explosion will affect.
     *
     * @param radius the new explosion radius
     */
    public void setExplosionRadius(int radius);

    /**
     * Get the explosion radius in which this Creeper's explosion will affect.
     *
     * @return the explosion radius
     */
    public int getExplosionRadius();

    /**
     * Makes this Creeper explode instantly.
     *
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     */
    public void explode();

    /**
     * Ignites this Creeper, beginning its fuse.
     *
     * The amount of time the Creeper takes to explode will depend on what
     * {@link #setMaxFuseTicks} is set as.
     *
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     */
    public void ignite();
}
