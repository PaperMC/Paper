package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;

/**
 * Event object for {@link RegistryEventProvider#freeze()}. This
 * event is fired right before a registry is frozen disallowing further changes.
 * It provides a way for plugins to add new objects to the registry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 * @deprecated renamed to {@link RegistryComposeEvent}
 */
@ApiStatus.ScheduledForRemoval(inVersion = "1.21.7 or 1.22, whichever comes first")
@Deprecated(since = "1.21.6", forRemoval = true)
@ApiStatus.NonExtendable
public interface RegistryFreezeEvent<T, B extends RegistryBuilder<T>> extends RegistryComposeEvent<T, B> {
}
