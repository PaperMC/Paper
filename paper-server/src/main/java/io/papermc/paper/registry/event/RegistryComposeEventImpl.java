package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import org.bukkit.Keyed;

public record RegistryComposeEventImpl<T, B extends RegistryBuilder<T>>(
    RegistryKey<T> registryKey,
    WritableRegistry<T, B> registry,
    Conversions conversions
) implements RegistryComposeEvent<T, B>, PaperLifecycleEvent {

    @Override
    public <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        final HolderGetter<Object> registryInfo = this.conversions.lookup().lookup(PaperRegistries.registryToNms(tagKey.registryKey())).orElseThrow();
        final HolderSet.Named<?> tagSet = registryInfo.getOrThrow(PaperRegistries.toNms(tagKey));
        return new NamedRegistryKeySetImpl<>(tagKey, tagSet);
    }
}
