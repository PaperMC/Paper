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
     * Sets the frozen state of the armadillo.
     */
    void setFrozenState(Armadillo.State frozenState);

    /**
     * Gets the frozen state of the armadillo.
     */
    Armadillo.State getFrozenState();

    /**
     * Clears the frozen state of the armadillo.
     */
    void clearFrozenState();

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
