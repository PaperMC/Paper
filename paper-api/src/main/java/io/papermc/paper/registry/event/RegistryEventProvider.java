package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Provider for registry events for a specific registry.
 * <p>
 * Supported events are:
 * <ul>
 *     <li>{@link RegistryEntryAddEvent} (via {@link #entryAdd()})</li>
 *     <li>{@link RegistryComposeEvent} (via {@link #compose()})</li>
 * </ul>
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.NonExtendable
public interface RegistryEventProvider<T, B extends RegistryBuilder<T>> {

    /**
     * Gets the event type for {@link RegistryEntryAddEvent} which is fired just before
     * an object is added to a registry.
     * <p>
     * Can be used in {@link io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, LifecycleEventHandler)}
     * to register a handler for {@link RegistryEntryAddEvent}.
     *
     * @return the registry entry add event type
     */
    RegistryEntryAddEventType<T, B> entryAdd();

    /**
     * Gets the event type for {@link RegistryFreezeEvent} which is fired just before
     * a registry is frozen. It allows for the registration of new objects.
     * <p>
     * Can be used in {@link io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, LifecycleEventHandler)}
     * to register a handler for {@link RegistryFreezeEvent}.
     *
     * @return the registry freeze event type
     * @deprecated use {@link #compose()} instead.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21.7 or 1.22, whichever comes first")
    @Deprecated(since = "1.21.6", forRemoval = true)
    default LifecycleEventType.Prioritizable<BootstrapContext, RegistryFreezeEvent<T, B>> freeze() {
        return (LifecycleEventType.Prioritizable<BootstrapContext, RegistryFreezeEvent<T,B>>) (LifecycleEventType.Prioritizable) this.compose();
    }

    /**
     * Gets the event type for {@link RegistryComposeEvent} which is fired after
     * a registry is loaded of expected elements. It allows for the registration of new objects.
     * <p>
     * Can be used in {@link io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager#registerEventHandler(LifecycleEventType, LifecycleEventHandler)}
     * to register a handler for {@link RegistryComposeEvent}.
     *
     * @return the registry freeze event type
     */
    LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<T, B>> compose();

    /**
     * Gets the registry key associated with this event type provider.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();
}
