package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface AnimalTamer {

    /**
     * This is the name of the specified AnimalTamer.
     *
     * @return The name to reference on tamed animals or null if a name cannot be obtained
     */
    @Nullable
    public String getName();

    /**
     * This is the UUID of the specified AnimalTamer.
     *
     * @return The UUID to reference on tamed animals
     */
    @NotNull
    public UUID getUniqueId();
}
