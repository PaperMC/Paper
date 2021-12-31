package org.bukkit.entity;

/**
 * Represents a SkeletonHorse - variant of {@link AbstractHorse}.
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
     */
    boolean isTrapped();

    /**
     * Sets if this skeleton horse is trapped.
     *
     * @param trapped new trapped state
     */
    void setTrapped(boolean trapped);

    /**
     * Returns the horse's current trap time in ticks.
     *
     * Trap time is incremented every tick when {@link #isTrapped()} is true.
     * The horse automatically despawns when it reaches 18000 ticks.
     *
     * @return current trap time
     */
    int getTrapTime();

    /**
     * Sets the trap time for the horse.
     *
     * Values greater than 18000 will cause the horse to despawn on the next
     * tick.
     *
     * @param trapTime new trap time
     */
    void setTrapTime(int trapTime);
}
