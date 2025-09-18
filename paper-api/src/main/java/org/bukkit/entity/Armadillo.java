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
     * Represents the current state of the armadillo.
     */
    enum State {
        // Start generate - ArmadilloState
        IDLE,
        ROLLING,
        SCARED,
        UNROLLING;
        // End generate - ArmadilloState
    }

}
