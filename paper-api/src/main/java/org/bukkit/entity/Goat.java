package org.bukkit.entity;

/**
 * A Goat.
 */
public interface Goat extends Animals {

    /**
     * Gets if this goat has its left horn.
     *
     * @return left horn status
     */
    boolean hasLeftHorn();

    /**
     * Sets if this goat has its left horn.
     *
     * @param hasHorn left horn status
     */
    void setLeftHorn(boolean hasHorn);

    /**
     * Gets if this goat has its right horn.
     *
     * @return right horn status
     */
    boolean hasRightHorn();

    /**
     * Sets if this goat has its right horn.
     *
     * @param hasHorn right horn status
     */
    void setRightHorn(boolean hasHorn);

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

    // Paper start - Goat ram API
    /**
     * Makes the goat ram at the specified entity
     * @param entity the entity to ram at
     */
    void ram(@org.jetbrains.annotations.NotNull LivingEntity entity);
    // Paper end
}
