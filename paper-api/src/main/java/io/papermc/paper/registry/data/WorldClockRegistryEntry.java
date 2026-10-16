package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.world.WorldClock;
import org.jetbrains.annotations.ApiStatus;

/**
 * A data-centric version-specific registry entry for the {@link WorldClock} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WorldClockRegistryEntry {

    /**
     * A mutable builder for the {@link WorldClockRegistryEntry} plugins may change in applicable registry events.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends WorldClockRegistryEntry, RegistryBuilder<WorldClock> {
    }
}
