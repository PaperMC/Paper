package io.papermc.paper.registry.set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collection;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.Unmodifiable;

public record NamedRegistryKeySetImpl<T extends Keyed, M>( // TODO remove Keyed
    TagKey<T> tagKey,
    HolderSet.Named<M> namedSet
) implements Tag<T>, org.bukkit.Tag<T> {

    public NamedRegistryKeySetImpl(final HolderSet.Named<M> namedSet) {
        this(PaperRegistries.fromNms(namedSet.key()), namedSet);
    }

    @Override
    public @Unmodifiable Collection<TypedKey<T>> values() {
        final ImmutableList.Builder<TypedKey<T>> builder = ImmutableList.builder();
        for (final Holder<M> holder : this.namedSet) {
            builder.add(TypedKey.create(this.tagKey.registryKey(), CraftNamespacedKey.fromMinecraft(((Holder.Reference<?>) holder).key().location())));
        }
        return builder.build();
    }

    @Override
    public RegistryKey<T> registryKey() {
        return this.tagKey.registryKey();
    }

    @Override
    public boolean contains(final TypedKey<T> valueKey) {
        return Iterables.any(this.namedSet, h -> {
            return PaperRegistries.fromNms(((Holder.Reference<?>) h).key()).equals(valueKey);
        });
    }

    @Override
    public @Unmodifiable Collection<T> resolve(final Registry<T> registry) {
        final ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (final Holder<M> holder : this.namedSet) {
            builder.add(registry.getOrThrow(CraftNamespacedKey.fromMinecraft(((Holder.Reference<?>) holder).key().location())));
        }
        return builder.build();
    }

    @Override
    public boolean isTagged(final T item) {
        return this.getValues().contains(item);
    }

    @Override
    public Set<T> getValues() {
        return Set.copyOf(this.resolve(RegistryAccess.registryAccess().getRegistry(this.registryKey())));
    }

    @Override
    public NamespacedKey getKey() {
        final Key key = this.tagKey().key();
        return new NamespacedKey(key.namespace(), key.value());
    }
}
