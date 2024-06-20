package io.papermc.paper.tag;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.TagKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@SuppressWarnings("BoundedWildcard")
@DefaultQualifier(NonNull.class)
public class PaperPostFlattenTagRegistrar<M, T> implements PaperRegistrar<BootstrapContext>, PostFlattenTagRegistrar<T> {

    public final Map<ResourceLocation, List<M>> tags;
    private final Function<ResourceLocation, Optional<? extends M>> fromIdConverter;
    private final Function<M, ResourceLocation> toIdConverter;
    private final RegistryKey<T> registryKey;

    public PaperPostFlattenTagRegistrar(
        final Map<ResourceLocation, List<M>> tags,
        final TagEventConfig<M, T> config
    ) {
        this.tags = tags;
        this.fromIdConverter = config.fromIdConverter();
        this.toIdConverter = config.toIdConverter();
        this.registryKey = config.apiRegistryKey();
    }

    @Override
    public void setCurrentContext(final @Nullable BootstrapContext owner) {
    }

    @Override
    public RegistryKey<T> registryKey() {
        return this.registryKey;
    }

    @Override
    public Map<TagKey<T>, Collection<TypedKey<T>>> getAllTags() {
        final ImmutableMap.Builder<TagKey<T>, Collection<TypedKey<T>>> tags = ImmutableMap.builderWithExpectedSize(this.tags.size());
        for (final Map.Entry<ResourceLocation, List<M>> entry : this.tags.entrySet()) {
            final TagKey<T> key = TagKey.create(this.registryKey, CraftNamespacedKey.fromMinecraft(entry.getKey()));
            tags.put(key, this.convert(entry.getValue()));
        }
        return tags.build();
    }

    private List<TypedKey<T>> convert(final List<M> nms) {
        return Collections.unmodifiableList(
            Lists.transform(nms, m -> this.convert(this.toIdConverter.apply(m)))
        );
    }

    private TypedKey<T> convert(final ResourceLocation location) {
        return TypedKey.create(this.registryKey, CraftNamespacedKey.fromMinecraft(location));
    }

    private M convert(final TypedKey<T> key) {
        final Optional<? extends M> optional = this.fromIdConverter.apply(PaperAdventure.asVanilla(key.key()));
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(key + " doesn't exist");
        }
        return optional.get();
    }

    @Override
    public boolean hasTag(final TagKey<T> tagKey) {
        return this.tags.containsKey(PaperAdventure.asVanilla(tagKey.key()));
    }

    private List<M> getNmsTag(final TagKey<T> tagKey, final boolean create) {
        final ResourceLocation vanillaKey = PaperAdventure.asVanilla(tagKey.key());
        List<M> tag = this.tags.get(vanillaKey);
        if (tag == null) {
            if (create) {
                tag = this.tags.computeIfAbsent(vanillaKey, k -> new ArrayList<>());
            } else {
                throw new NoSuchElementException("Tag " + tagKey + " is not present");
            }
        }
        return tag;
    }

    @Override
    public Collection<TypedKey<T>> getTag(final TagKey<T> tagKey) {
        return this.convert(this.getNmsTag(tagKey, false));
    }

    @Override
    public void addToTag(final TagKey<T> tagKey, final Collection<TypedKey<T>> values) {
        final List<M> nmsTag = new ArrayList<>(this.getNmsTag(tagKey, true));
        for (final TypedKey<T> key : values) {
            nmsTag.add(this.convert(key));
        }
        this.tags.put(PaperAdventure.asVanilla(tagKey.key()), nmsTag);
    }

    @Override
    public void setTag(final TagKey<T> tagKey, final Collection<TypedKey<T>> values) {
        final List<M> newList = List.copyOf(Collections2.transform(values, this::convert));
        this.tags.put(PaperAdventure.asVanilla(tagKey.key()), newList);
    }
}
