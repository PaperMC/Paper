package org.bukkit.entity;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface EntityFactory {

    /**
     * Create a new EntitySnapshot with the supplied input.<br>
     * Accepts strings in the format output by {@link EntitySnapshot#getAsString()}.
     *
     * @param input the input string
     * @return the created EntitySnapshot
     * @throws IllegalArgumentException if the input string was provided in an invalid or unsupported format
     */
    @NotNull
    EntitySnapshot createEntitySnapshot(@NotNull String input) throws IllegalArgumentException;
}
