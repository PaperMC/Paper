package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.registry.RegistryKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base type for all registry events.
 *
 * @param <T> registry entry type
 */
@ApiStatus.NonExtendable
public interface RegistryEvent<T> extends LifecycleEvent {

    /**
     * Get the key for the registry this event pertains to.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();
}
