package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import org.bukkit.GameEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * A data-centric version-specific registry entry for the {@link GameEvent} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface GameEventRegistryEntry {

    /**
     * Provides the range in which this game event will notify its listeners.
     *
     * @return the range of blocks, represented as an int.
     * @see GameEvent#getRange()
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int range();

    /**
     * A mutable builder for the {@link GameEventRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #range(int)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends GameEventRegistryEntry, RegistryBuilder<GameEvent> {

        /**
         * Sets the range in which this game event should notify its listeners.
         *
         * @param range the range of blocks.
         * @return this builder instance.
         * @see GameEventRegistryEntry#range()
         * @see GameEvent#getRange()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder range(@Range(from = 0, to = Integer.MAX_VALUE) int range);
    }
}
