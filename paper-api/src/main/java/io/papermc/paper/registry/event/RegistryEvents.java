package io.papermc.paper.registry.event;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Holds providers for {@link RegistryEntryAddEvent} and {@link RegistryFreezeEvent}
 * handlers for each applicable registry.
 */
@ApiStatus.Experimental
@NullMarked
public final class RegistryEvents {

    private RegistryEvents() {
    }
}
