package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record RegistryEventProviderImpl<T, B extends RegistryBuilder<T>>(RegistryKey<T> registryKey) implements RegistryEventProvider<T, B> {

    static <T, B extends RegistryBuilder<T>> RegistryEventProvider<T, B> create(final RegistryKey<T> registryKey) {
        return new RegistryEventProviderImpl<>(registryKey);
    }

    @Override
    public RegistryEntryAddEventType<T, B> entryAdd() {
        return RegistryEventTypeProvider.provider().registryEntryAdd(this);
    }

    @Override
    public LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<T, B>> compose() {
        return RegistryEventTypeProvider.provider().registryCompose(this);
    }
}
