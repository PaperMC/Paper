package org.bukkit.entity;

import java.util.UUID;

/**
 * Represents an Animal.
 */
public interface Animals extends Ageable {

    /**
     * Get the UUID of the entity that caused this entity to enter the
     * {@link #canBreed()} state.
     *
     * @return uuid if set, or null
     */
    UUID getBreedCause();

    /**
     * Set the UUID of the entity that caused this entity to enter the
     * {@link #canBreed()} state.
     *
     * @param uuid new uuid, or null
     */
    void setBreedCause(UUID uuid);
}
