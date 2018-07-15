package org.bukkit.entity;

/**
 * Represents a puffer fish.
 */
public interface PufferFish extends Fish {

    /**
     * Returns the current puff state of this fish (i.e. how inflated it is).
     *
     * @return current puff state
     */
    int getPuffState();

    /**
     * Sets the current puff state of this fish (i.e. how inflated it is).
     *
     * @param state new puff state
     */
    void setPuffState(int state);
}
