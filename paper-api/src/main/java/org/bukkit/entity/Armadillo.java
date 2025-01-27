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
    public State getState();

    /**
     * Set a new state for the armadillo.
     * <br>
     * This will also make the armadillo make the transition to the new state.
     *
     * @param state the new state
     */
    public void setState(final State state);

    /**
     * Attempt to roll up if it in {@link State#IDLE}
     */
    public void rollUp();

    /**
     * Attempt to roll out if it's not in {@link State#IDLE}
     */
    public void rollOut();

    /**
     * Represents the current state of the Armadillo.
     */
    public enum State {
        IDLE,
        ROLLING,
        SCARED,
        UNROLLING;
    }

}
