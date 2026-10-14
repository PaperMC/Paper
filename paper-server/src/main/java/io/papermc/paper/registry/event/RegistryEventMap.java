package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public final class RegistryEventMap {

    private final Map<RegistryKey<?>, LifecycleEventType<BootstrapContext, ? extends LifecycleEvent, ?>> eventTypes = new HashMap<>();
    private final String name;

    public RegistryEventMap(final String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public <T, E extends LifecycleEvent, ET extends LifecycleEventType<BootstrapContext, E, ?>> ET getOrCreate(final RegistryKey<T> registryKey, final BiFunction<? super RegistryKey<T>, ? super String, ET> eventTypeCreator) {
        final ET eventType;
        if (this.eventTypes.containsKey(registryKey)) {
            eventType = (ET) this.eventTypes.get(registryKey);
        } else {
            eventType = eventTypeCreator.apply(registryKey, this.name);
            this.eventTypes.put(registryKey, eventType);
        }
        return eventType;
    }

    @SuppressWarnings("unchecked")
    public <T, E extends LifecycleEvent> LifecycleEventType<BootstrapContext, E, ?> getEventType(final RegistryKey<T> registryKey) {
        return (LifecycleEventType<BootstrapContext, E, ?>) Objects.requireNonNull(this.eventTypes.get(registryKey), () -> "No hook for " + registryKey);
    }

    public boolean hasHandlers(final RegistryKey<?> registryKey) {
        final AbstractLifecycleEventType<?, ?, ?> type = ((AbstractLifecycleEventType<?, ?, ?>) this.eventTypes.get(registryKey));
        return type != null && type.hasHandlers();
    }

}
