package org.bukkit.entity;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.0.0 R1
 */
public interface AnimalTamer {

    /**
     * This is the name of the specified AnimalTamer.
     *
     * @return The name to reference on tamed animals or null if a name cannot be obtained
     * @since 1.3.1 R1.0
     */
    @Nullable
    public String getName();

    /**
     * This is the UUID of the specified AnimalTamer.
     *
     * @return The UUID to reference on tamed animals
     * @since 1.7.10
     */
    @NotNull
    public UUID getUniqueId();
}
