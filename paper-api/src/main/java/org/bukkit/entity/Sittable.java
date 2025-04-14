package org.bukkit.entity;

/**
 * An entity that can sit still.
 */
public interface Sittable extends Entity {

    /**
     * Checks if this entity is sitting
     *
     * @return true if sitting
     */
    boolean isSitting();

    /**
     * Sets if this entity is sitting. Will remove any path that the entity
     * was following beforehand.
     *
     * @param sitting true if sitting
     */
    void setSitting(boolean sitting);

}
