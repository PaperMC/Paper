package org.bukkit.entity;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an Armadillo.
 */
@NullMarked
public interface Armadillo extends Animals {

    /**
     * Get the current state of the armadillo.
     *
     * @return the state of the armadillo
     */
    State getState();

    /**
     * Attempt to roll up if the armadillo is {@link State#IDLE}
     */
    void rollUp();

    /**
     * Attempt to roll out if the armadillo is not {@link State#IDLE}
     */
    void rollOut();

    /**
     * Freeze the current state of the armadillo.
     */
    void freezeState();

    /**
     * Unfreezes the armadillo, allowing the AI to change states.
     */
    void unFreezeState();

    /**
     * Checks if the armadillo is frozen.
     * @return {@code true} if the paper is frozen; {@code false} otherwise.
     */
    boolean isFrozen();

    /**
     * Freezes the state of the armadillo after rolling up.
     */
    void freezeRollUp();

    /**
     * Freezes the state of the armadillo after rolling out.
     */
    void freezeRollOut();

    /**
     * Represents the current state of the armadillo.
     */
    enum State {
        IDLE,
        ROLLING,
        SCARED,
        UNROLLING;
    }

}
