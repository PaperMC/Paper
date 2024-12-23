package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Creeper
 *
 * @since 1.0.0
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
     * @since 1.12.2
     */
    public void setMaxFuseTicks(int ticks);

    /**
     * Get the maximum fuse ticks for this Creeper, where the maximum ticks
     * is the amount of time in which a creeper is allowed to be in the
     * primed state before exploding.
     *
     * @return the maximum fuse ticks
     * @since 1.12.2
     */
    public int getMaxFuseTicks();

    /**
     * Set the fuse ticks for this Creeper, where the ticks is the amount of
     * time in which a creeper has been in the primed state.
     *
     * @param ticks the new fuse ticks
     * @since 1.16.5
     */
    public void setFuseTicks(int ticks);

    /**
     * Get the maximum fuse ticks for this Creeper, where the ticks is the
     * amount of time in which a creeper has been in the primed state.
     *
     * @return the fuse ticks
     * @since 1.13.1
     */
    public int getFuseTicks();

    /**
     * Set the explosion radius in which this Creeper's explosion will affect.
     *
     * @param radius the new explosion radius
     * @since 1.12.2
     */
    public void setExplosionRadius(int radius);

    /**
     * Get the explosion radius in which this Creeper's explosion will affect.
     *
     * @return the explosion radius
     * @since 1.12.2
     */
    public int getExplosionRadius();

    /**
     * Makes this Creeper explode instantly.
     * <br>
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @since 1.13.1
     */
    public void explode();

    /**
     * Ignites this Creeper, beginning its fuse.
     * <br>
     * The amount of time the Creeper takes to explode will depend on what
     * {@link #setMaxFuseTicks} is set as.
     * <br>
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @param entity the entity which ignited the creeper
     * @since 1.21
     */
    public void ignite(@NotNull Entity entity);

    /**
     * Ignites this Creeper, beginning its fuse.
     * <br>
     * The amount of time the Creeper takes to explode will depend on what
     * {@link #setMaxFuseTicks} is set as.
     * <br>
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @since 1.14.2
     */
    public void ignite();

    /**
     * Gets the entity which ignited the creeper, if available.
     *
     * @return the entity which ignited the creeper (if available) or null
     * @since 1.21
     */
    @Nullable
    public Entity getIgniter();
    // Paper start

    /**
     * Set whether creeper is ignited or not (armed to explode)
     *
     * @param ignited New ignited state
     * @since 1.13.1
     */
    public void setIgnited(boolean ignited);

    /**
     * Check if creeper is ignited or not (armed to explode)
     *
     * @return Ignited state
     * @since 1.13.1
     */
    public boolean isIgnited();
    // Paper end
}
