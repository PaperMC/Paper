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

    @Contract(pure = true)
    Key location();

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

        @Contract(value = "_ -> this", mutates = "this")
        Builder location(Key location);

        @Contract(value = "_ -> this", mutates = "this")
        Builder fixedRange(@Nullable Float fixedRange);
    }
}
