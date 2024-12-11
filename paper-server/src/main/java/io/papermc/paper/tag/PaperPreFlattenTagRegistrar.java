package io.papermc.paper.tag;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@SuppressWarnings("BoundedWildcard")
@DefaultQualifier(NonNull.class)
public class PaperPreFlattenTagRegistrar<T> implements PaperRegistrar<BootstrapContext>, PreFlattenTagRegistrar<T> {

    public final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags;
    private final RegistryKey<T> registryKey;

    private @Nullable BootstrapContext owner;

    public PaperPreFlattenTagRegistrar(
        final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags,
        final TagEventConfig<?, T> config
    ) {
        this.tags = new HashMap<>(tags);
        this.registryKey = config.apiRegistryKey();
    }

    @Override
    public void setCurrentContext(final @Nullable BootstrapContext owner) {
        this.owner = owner;
    }

    @Override
    public RegistryKey<T> registryKey() {
        return this.registryKey;
    }

    @Override
    public Map<TagKey<T>, Collection<TagEntry<T>>> getAllTags() {
        final ImmutableMap.Builder<TagKey<T>, Collection<io.papermc.paper.tag.TagEntry<T>>> builder = ImmutableMap.builderWithExpectedSize(this.tags.size());
        for (final Map.Entry<ResourceLocation, List<TagLoader.EntryWithSource>> entry : this.tags.entrySet()) {
            final TagKey<T> key = TagKey.create(this.registryKey, CraftNamespacedKey.fromMinecraft(entry.getKey()));
            builder.put(key, convert(entry.getValue()));
        }
        return builder.build();
    }

    private static <T> List<io.papermc.paper.tag.TagEntry<T>> convert(final List<TagLoader.EntryWithSource> nmsEntries) {
        return Collections.unmodifiableList(Lists.transform(nmsEntries, PaperPreFlattenTagRegistrar::convert));
    }

    private static <T> io.papermc.paper.tag.TagEntry<T> convert(final TagLoader.EntryWithSource nmsEntry) {
        return new TagEntryImpl<>(CraftNamespacedKey.fromMinecraft(nmsEntry.entry().id), nmsEntry.entry().tag, nmsEntry.entry().required);
    }

    private TagLoader.EntryWithSource convert(final TagEntry<T> entry) {
        Preconditions.checkState(this.owner != null, "Owner is not set");
        final ResourceLocation vanilla = PaperAdventure.asVanilla(entry.key());
        final net.minecraft.tags.TagEntry nmsEntry;
        if (entry.isTag()) {
            if (entry.isRequired()) {
                nmsEntry = net.minecraft.tags.TagEntry.tag(vanilla);
            } else {
                nmsEntry = net.minecraft.tags.TagEntry.optionalTag(vanilla);
            }
        } else {
            if (entry.isRequired()) {
                nmsEntry = net.minecraft.tags.TagEntry.element(vanilla);
            } else {
                nmsEntry = net.minecraft.tags.TagEntry.optionalElement(vanilla);
            }
        }
        return new TagLoader.EntryWithSource(nmsEntry, this.owner.getPluginMeta().getDisplayName());
    }

    @Override
    public boolean hasTag(final TagKey<T> tagKey) {
        return this.tags.containsKey(PaperAdventure.asVanilla(tagKey.key()));
    }

    private List<TagLoader.EntryWithSource> getNmsTag(final TagKey<T> tagKey, boolean create) {
        final ResourceLocation vanillaKey = PaperAdventure.asVanilla(tagKey.key());
        List<TagLoader.EntryWithSource> tag = this.tags.get(vanillaKey);
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
    public List<TagEntry<T>> getTag(final TagKey<T> tagKey) {
        return convert(this.getNmsTag(tagKey, false));
    }

    @Override
    public void addToTag(final TagKey<T> tagKey, final Collection<TagEntry<T>> entries) {
        final List<TagLoader.EntryWithSource> nmsTag = new ArrayList<>(this.getNmsTag(tagKey, true));
        for (final TagEntry<T> tagEntry : entries) {
            nmsTag.add(this.convert(tagEntry));
        }
        this.tags.put(PaperAdventure.asVanilla(tagKey.key()), nmsTag);
    }

    @Override
    public void setTag(final TagKey<T> tagKey, final Collection<TagEntry<T>> entries) {
        final List<TagLoader.EntryWithSource> newList = List.copyOf(Collections2.transform(entries, this::convert));
        this.tags.put(PaperAdventure.asVanilla(tagKey.key()), newList);
    }
}
