package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;
import org.jetbrains.annotations.ApiStatus;

/// Provider for registry events for a specific registry.
///
/// Supported events are:
///
///   - [RegistryEntryAddEvent] (via [#entryAdd()])
///   - [RegistryComposeEvent] (via [#compose()])
///
/// @param <T> registry entry type
/// @param <B> registry entry builder type
@ApiStatus.NonExtendable
public interface RegistryEventProvider<T, B extends RegistryBuilder<T>> {

    /// Gets the event type for [RegistryEntryAddEvent] which is fired just before
    /// an object is added to a registry.
    ///
    /// Can be used in [io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, LifecycleEventHandler)]
    /// to register a handler for [RegistryEntryAddEvent].
    ///
    /// @return the registry entry add event type
    RegistryEntryAddEventType<T, B> entryAdd();

    /// Gets the event type for [RegistryComposeEvent] which is fired after
    /// a registry is loaded of expected elements. It allows for the registration of new objects.
    ///
    /// Can be used in [io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, LifecycleEventHandler)]
    /// to register a handler for [RegistryComposeEvent].
    ///
    /// @return the registry compose event type
    LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<T, B>> compose();

    /// Gets the registry key associated with this event type provider.
    ///
    /// @return the registry key
    RegistryKey<T> registryKey();
}
