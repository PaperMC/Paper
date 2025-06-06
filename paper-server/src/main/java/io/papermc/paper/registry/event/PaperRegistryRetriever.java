package io.papermc.paper.registry.event;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryAccess;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;

public final class PaperRegistryRetriever implements RegistryRetriever {

    private final Conversions conversions;

    public PaperRegistryRetriever(final Conversions conversions) {
        this.conversions = conversions;
    }

    @Override
    public <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        final RegistryOps.RegistryInfo<Object> registryInfo = this.conversions.lookup().lookup(PaperRegistries.registryToNms(tagKey.registryKey())).orElseThrow();
        final HolderSet.Named<?> tagSet = registryInfo.getter().getOrThrow(PaperRegistries.toNms(tagKey));
        return new NamedRegistryKeySetImpl<>(tagKey, tagSet);
    }

    @Override
    public <V extends Keyed> V getOrCreate(final TypedKey<V> key) {
        final Registry<V> registry = PaperRegistryAccess.instance().getRegistry(key.registryKey());
        final V value = registry.get(key);
        if (value != null) {
            return value;
        } else if (!(registry instanceof final CraftRegistry<V, ?> craftRegistry)) {
            throw new IllegalStateException("Cannot create instance for key " + key + " as the registry is not a CraftRegistry");
        } else if (!craftRegistry.constructorUsesHolder()) {
            throw new IllegalStateException("Cannot create instance for key " + key + " as the registry does not yet support creating instances with Holder references");
        } else {
            final ResourceKey<? extends net.minecraft.core.Registry<Object>> resourceKey = PaperRegistries.registryToNms(key.registryKey());
            final RegistryOps.RegistryInfo<?> registryInfo = this.conversions.lookup().lookup(resourceKey).orElseThrow(() -> new IllegalStateException("Cannot create instance for key " + key + " as the registry is not registered in the lookup"));
            registryInfo.getter().get(PaperRegistries.toNms(key)).orElseThrow(() -> new IllegalStateException("Cannot create instance for key " + key + " as it does not exist in the registry " + resourceKey));
            return craftRegistry.getOrThrow(key);
        }
    }
}
