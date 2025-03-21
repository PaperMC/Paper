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
     * If the armadillo's state is currently frozen, this operation will unfreeze it.
     */
    void rollUp();

    /**
     * Attempt to roll out if the armadillo is not {@link State#IDLE}
     * If the armadillo's state is currently frozen, this operation will unfreeze it.
     */
    void rollOut();

    /**
     * Freeze the current state of the armadillo and prevents the AI from changing the states.
     * {@link #rollUp()} and {@link #rollOut()} can still change the state.
     */
    void freezeState();

    /**
     * Unfreezes the armadillo, allowing the AI to change states.
     */
    void unFreezeState();

    /**
     * Checks if the armadillo is frozen.
     * @return {@code true} if the state is frozen; {@code false} otherwise.
     */
    boolean isFrozen();

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
