package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;

public record RegistryEntryAddEventImpl<T, B extends RegistryBuilder<T>>(
    TypedKey<T> key,
    B builder,
    RegistryKey<T> registryKey,
    Conversions conversions,
    RegistryRetriever retriever
) implements RegistryEntryAddEvent<T, B>, PaperLifecycleEvent {
}
