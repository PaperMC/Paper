package org.bukkit.entity;

/**
 * Represents a SkeletonHorse - variant of {@link AbstractHorse}.
 *
 * @since 1.11
 */
public interface SkeletonHorse extends AbstractHorse {

    /**
     * Returns whether this skeleton horse is trapped.
     * <p>
     * When a horse is trapped and a player comes within 10 blocks of a trapped
     * horse, lightning will strike the horse. When struck, the skeleton trap
     * will activate, turning the horse into a skeleton horseman as well as
     * spawning three additional horsemen nearby.
     *
     * @return true if trapped
     * @since 1.18.1
     */
    boolean isTrapped();

    /**
     * Sets if this skeleton horse is trapped.
     *
     * @param trapped new trapped state
     * @since 1.18.1
     */
    void setTrapped(boolean trapped);

    /**
     * Returns the horse's current trap time in ticks.
     *
     * Trap time is incremented every tick when {@link #isTrapped()} is true.
     * The horse automatically despawns when it reaches 18000 ticks.
     *
     * @return current trap time
     * @since 1.13
     */
    int getTrapTime();

    /**
     * Sets the trap time for the horse.
     *
     * Values greater than 18000 will cause the horse to despawn on the next
     * tick.
     *
     * @param trapTime new trap time
     * @since 1.18.1
     */
    void setTrapTime(int trapTime);

    // Paper start
    /**
     * @deprecated use {@link #isTrapped()}
     * @since 1.13
     */
    @Deprecated
    boolean isTrap();

    /**
     * @deprecated use {@link #setTrapped(boolean)}
     * @since 1.13
     */
    @Deprecated
    void setTrap(boolean trap);
    // Paper end
}
