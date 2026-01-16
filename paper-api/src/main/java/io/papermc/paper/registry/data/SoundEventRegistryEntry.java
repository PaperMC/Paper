package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Sound} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface SoundEventRegistryEntry {

    /**
     * Gets the resource pack location for this sound event.
     *
     * @return the location
     */
    @Contract(pure = true)
    Key location();

    /**
     * Gets the fixed range for this sound event, if present.
     *
     * @return the fixed range, or {@code null} if not present
     */
    @Contract(pure = true)
    @Nullable Float fixedRange();

    /**
     * A mutable builder for the {@link SoundEventRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #location(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends SoundEventRegistryEntry, RegistryBuilder<Sound> {

        /**
         * Sets the resource pack location for this sound event.
         *
         * @param location the location
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder location(Key location);

        /**
         * Sets the fixed range for this sound event.
         *
         * @param fixedRange the fixed range
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder fixedRange(@Nullable Float fixedRange);
    }
}
