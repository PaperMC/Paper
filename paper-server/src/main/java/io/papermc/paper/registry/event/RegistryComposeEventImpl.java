package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;

public record RegistryComposeEventImpl<T, B extends RegistryBuilder<T>>(
    RegistryKey<T> registryKey,
    WritableRegistry<T, B> registry,
    Conversions conversions,
    RegistryRetriever retriever
) implements RegistryComposeEvent<T, B>, PaperLifecycleEvent {
}
