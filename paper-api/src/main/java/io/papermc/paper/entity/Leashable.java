package io.papermc.paper.entity;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents an entity that can be leashed.
 */
@NullMarked
public interface Leashable extends Entity {

    /**
     * Returns whether the entity is currently leashed.
     *
     * @return whether the entity is leashed
     */
    boolean isLeashed();

    /**
     * Gets the entity that is currently leading this entity.
     *
     * @return the entity holding the leash
     * @throws IllegalStateException if not currently leashed
     */
    Entity getLeashHolder() throws IllegalStateException;

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p>
     * This method has no effect on players.
     *
     * @param holder the entity to leash this entity to, or {@code null} to unleash
     * @return whether the operation was successful
     */
    boolean setLeashHolder(@Nullable Entity holder);
}
