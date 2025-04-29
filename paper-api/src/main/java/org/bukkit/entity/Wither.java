package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Wither boss
 */
public interface Wither extends Monster, Boss, com.destroystokyo.paper.entity.RangedEntity { // Paper

    /**
     * {@inheritDoc}
     * <p>
     * This method will set the target of the {@link Head#CENTER center head} of
     * the wither.
     *
     * @see #setTarget(org.bukkit.entity.Wither.Head, org.bukkit.entity.LivingEntity)
     */
    @Override
    void setTarget(@Nullable LivingEntity target);

    /**
     * This method will set the target of individual heads {@link Head} of the
     * wither.
     *
     * @param head the individual head
     * @param target the entity that should be targeted
     */
    void setTarget(@NotNull Head head, @Nullable LivingEntity target);

    /**
     * This method will get the target of individual heads {@link Head} of the
     * wither.
     *
     * @param head the individual head
     * @return the entity targeted by the given head, or null if none is
     * targeted
     */
    @Nullable
    LivingEntity getTarget(@NotNull Head head);

    /**
     * Returns the wither's current invulnerability ticks.
     *
     * @return amount of invulnerability ticks
     * @deprecated Duplicate api, use {@link #getInvulnerableTicks()}
     */
    @Deprecated(forRemoval = true) // Paper
    int getInvulnerabilityTicks();

    /**
     * Sets the wither's current invulnerability ticks.
     *
     * When invulnerability ticks reach 0, the wither will trigger an explosion.
     *
     * @param ticks amount of invulnerability ticks
     * @deprecated Duplicate api, use {@link #setInvulnerableTicks(int)}
     */
    @Deprecated(forRemoval = true) // Paper
    void setInvulnerabilityTicks(int ticks);

    /**
     * Represents one of the Wither's heads.
     */
    enum Head {

        CENTER,
        LEFT,
        RIGHT
    }

    /**
     * @return whether the wither is charged
     */
    boolean isCharged();

    /**
     * @return ticks the wither is invulnerable for
     */
    int getInvulnerableTicks();

    /**
     * Sets for how long in the future, the wither should be invulnerable.
     *
     * @param ticks ticks the wither is invulnerable for
     */
    void setInvulnerableTicks(int ticks);

    /**
     * @return whether the wither can travel through portals
     */
    boolean canTravelThroughPortals();

    /**
     * Sets whether the wither can travel through portals.
     *
     * @param value whether the wither can travel through portals
     */
    void setCanTravelThroughPortals(boolean value);

    /**
     * Makes the wither invulnerable for 11 seconds and
     * sets the health to one third of the max health.
     * <br>
     * This is called in vanilla directly after spawning the wither.
     */
    void enterInvulnerabilityPhase();
}
