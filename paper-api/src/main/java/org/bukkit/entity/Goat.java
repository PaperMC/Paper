package org.bukkit.entity;

/**
 * A Goat.
 */
public interface Goat extends Animals {

    /**
     * Gets if this is a screaming goat.
     *
     * A screaming goat makes screaming sounds and rams more often. They do not
     * offer home loans.
     *
     * @return screaming status
     */
    boolean isScreaming();

    /**
     * Sets if this is a screaming goat.
     *
     * A screaming goat makes screaming sounds and rams more often. They do not
     * offer home loans.
     *
     * @param screaming screaming status
     */
    void setScreaming(boolean screaming);
}
