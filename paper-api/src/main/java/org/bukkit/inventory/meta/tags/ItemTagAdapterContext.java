package org.bukkit.inventory.meta.tags;

import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the context in which the {@link ItemTagType} can
 * serialize and deserialize the passed values.
 */
public interface ItemTagAdapterContext {

    /**
     * Creates a new and empty tag container instance.
     *
     * @return the fresh container instance
     */
    @NotNull
    CustomItemTagContainer newTagContainer();
}
