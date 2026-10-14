package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.PaperRegistryListenerManager;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;

public class RegistryEventTypeProviderImpl implements RegistryEventTypeProvider {

    public static RegistryEventTypeProviderImpl instance() {
        return (RegistryEventTypeProviderImpl) RegistryEventTypeProvider.provider();
    }

    @Override
    public <T, B extends RegistryBuilder<T>> RegistryEntryAddEventType<T, B> registryEntryAdd(final RegistryEventProvider<T, B> type) {
        return PaperRegistryListenerManager.INSTANCE.getRegistryValueAddEventType(type);
    }

    @Override
    public <T, B extends RegistryBuilder<T>> LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<T, B>> registryCompose(final RegistryEventProvider<T, B> type) {
        return PaperRegistryListenerManager.INSTANCE.getRegistryComposeEventType(type);
    }
}
