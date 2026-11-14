package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Keyed;

public record RegistryComposeEventImpl<T, B extends RegistryBuilder<T>>(
    RegistryKey<T> registryKey,
    WritableRegistry<T, B> registry,
    RegistryFactory factory
) implements RegistryComposeEvent<T, B>, PaperLifecycleEvent {

    @Override
    public <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        return this.factory.getOrCreateTag(tagKey);
    }
}
