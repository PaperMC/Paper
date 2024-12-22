package io.papermc.paper.registry.event.type;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.event.RegistryEntryAddEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Lifecycle event type for {@link RegistryEntryAddEvent}s.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 * @since 1.21
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface RegistryEntryAddEventType<T, B extends RegistryBuilder<T>> extends LifecycleEventType<BootstrapContext, RegistryEntryAddEvent<T, B>, RegistryEntryAddConfiguration<T>> {
}
