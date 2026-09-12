package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.PaperRegistryListenerManager;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;

public class RegistryEventTypeProviderImpl implements RegistryEventTypeProvider {

    public static RegistryEventTypeProviderImpl instance() {
        return (RegistryEventTypeProviderImpl) RegistryEventTypeProvider.provider();
    }

    @Override
    public <API extends RegistryElement.Buildable<API, E, B>, E, B extends RegistryBuilder<API>> RegistryEntryAddEventType<API, B> registryEntryAdd(final RegistryKey<API> key) {
        return PaperRegistryListenerManager.INSTANCE.getRegistryValueAddEventType(key);
    }

    @Override
    public <API extends RegistryElement.Buildable<API, E, B>, E, B extends RegistryBuilder<API>> LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<API, B>> registryCompose(final RegistryKey<API> key) {
        return PaperRegistryListenerManager.INSTANCE.getRegistryComposeEventType(key);
    }
}
