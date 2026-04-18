package org.bukkit.entity;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a Breeze. Whoosh!
 */
@NullMarked
public interface Breeze extends Monster {

    /**
     * Gets the number of ticks the breeze has been inhaling for.
     *
     * @return the inhale ticks
     */
    int getInhaleTicks();

    /**
     * Sets the number of ticks the breeze has been inhaling for.
     *
     * @param ticks the inhale ticks
     */
    void setInhaleTicks(int ticks);

    /**
     * Gets the number of ticks remaining on the breeze's jump cooldown.
     *
     * @return the jump cooldown ticks
     */
    int getJumpCooldown();

    /**
     * Sets the number of ticks remaining on the breeze's jump cooldown.
     *
     * @param ticks the jump cooldown ticks
     */
    void setJumpCooldown(int ticks);

    /**
     * Returns whether the breeze is currently jumping.
     *
     * @return true if jumping
     */
    boolean isJumping();
}
