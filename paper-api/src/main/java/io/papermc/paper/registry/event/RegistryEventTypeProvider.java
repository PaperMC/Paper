package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.event.type.RegistryEntryAddEventType;
import java.util.Optional;
import java.util.ServiceLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
interface RegistryEventTypeProvider {

    Optional<RegistryEventTypeProvider> PROVIDER = ServiceLoader.load(RegistryEventTypeProvider.class, RegistryEventTypeProvider.class.getClassLoader()).findFirst();

    static RegistryEventTypeProvider provider() {
        return PROVIDER.orElseThrow(() -> new IllegalStateException("Could not find a %s service implementation".formatted(RegistryEventTypeProvider.class.getSimpleName())));
    }

    <T, B extends RegistryBuilder<T>> RegistryEntryAddEventType<T, B> registryEntryAdd(RegistryEventProvider<T, B> type);

    <T, B extends RegistryBuilder<T>> LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<T, B>> registryCompose(RegistryEventProvider<T, B> type);
}
