package org.bukkit.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the context in which the {@link PersistentDataType} can
 * serialize and deserialize the passed values.
 */
public interface PersistentDataAdapterContext {

    /**
     * Creates a new and empty meta container instance.
     *
     * @return the fresh container instance
     */
    @NotNull
    PersistentDataContainer newPersistentDataContainer();
}
