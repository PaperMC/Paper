package org.bukkit.entity;

/**
 * An animal that can sit still.
 *
 * @since 1.12
 */
public interface Sittable {

    /**
     * Checks if this animal is sitting
     *
     * @return true if sitting
     */
    boolean isSitting();

    /**
     * Sets if this animal is sitting. Will remove any path that the animal
     * was following beforehand.
     *
     * @param sitting true if sitting
     */
    void setSitting(boolean sitting);

}
