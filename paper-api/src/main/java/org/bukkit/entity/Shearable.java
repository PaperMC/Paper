package org.bukkit.entity;

/**
 * Represents an entity which can be shorn with shears.
 */
public interface Shearable {

    /**
     * Gets whether the entity is in its sheared state.
     *
     * @return Whether the entity is sheared.
     */
    boolean isSheared();

    /**
     * Sets whether the entity is in its sheared state.
     *
     * @param flag Whether to shear the entity
     */
    void setSheared(boolean flag);
}
