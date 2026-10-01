package io.papermc.paper.registry.event;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import org.bukkit.Keyed;

public record RegistryEntryAddEventImpl<T, B extends RegistryBuilder<T>>(
    TypedKey<T> key,
    B builder,
    RegistryKey<T> registryKey,
    Conversions conversions
) implements RegistryEntryAddEvent<T, B>, PaperLifecycleEvent {

    @Override
    public <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        final HolderGetter<Object> registryInfo = this.conversions.lookup().lookup(PaperRegistries.registryToNms(tagKey.registryKey())).orElseThrow();
        final HolderSet.Named<?> tagSet = registryInfo.getOrThrow(PaperRegistries.toNms(tagKey));
        return new NamedRegistryKeySetImpl<>(tagKey, tagSet);
    }
}
