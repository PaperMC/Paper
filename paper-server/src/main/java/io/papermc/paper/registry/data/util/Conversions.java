package io.papermc.paper.registry.data.util;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;
import io.papermc.paper.adventure.WrapperAwareSerializer;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.PaperRegistryBuilderFactory;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.event.RegistryFactory;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.set.RegistryHolderSetBuilder;
import io.papermc.paper.registry.set.RegistryHolderSetBuilderImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public class Conversions implements RegistryFactory {

    private static @Nullable Conversions globalInstance;
    public static Conversions global() {
        if (globalInstance == null) {
            final RegistryAccess globalAccess = CraftRegistry.getMinecraftRegistry();
            Preconditions.checkState(globalAccess != null, "Global registry access is not available");
            globalInstance = new Conversions(new RegistryOps.HolderLookupAdapter(globalAccess));
        }
        return globalInstance;
    }

    private final RegistryOps.RegistryInfoLookup lookup;
    private final RegistryOps<Object> javaOps;
    private final WrapperAwareSerializer serializer;

    public Conversions(final RegistryOps.RegistryInfoLookup lookup) {
        this.lookup = lookup;
        this.javaOps = RegistryOps.create(JavaOps.INSTANCE, lookup);
        this.serializer = new WrapperAwareSerializer(() -> this.javaOps);
    }

    public <OUT, IN> OUT convert(final IN in, final Codec<OUT> outCodec, final Codec<IN> inCodec) {
        final Object obj = inCodec.encodeStart(this.javaOps, in)
            .getOrThrow(s -> new RuntimeException("Failed to encode input: " + in + "; " + s));
        return outCodec.decode(this.javaOps, obj)
            .getOrThrow(s -> new RuntimeException("Failed to decode to output: " + obj + "; " + s))
            .getFirst();
    }

    public RegistryOps.RegistryInfoLookup lookup() {
        return this.lookup;
    }

    public <M, T> Holder.Reference<M> getReferenceHolder(final TypedKey<T> key) {
        final ResourceKey<M> nms = PaperRegistries.toNms(key);
        return this.lookup.lookup(nms.registryKey()).orElseThrow().getter().getOrThrow(nms);
    }

    public <M> Holder.Reference<M> getReferenceHolder(final ResourceKey<M> key) {
        return this.lookup.lookup(key.registryKey()).orElseThrow().getter().getOrThrow(key);
    }

    @Contract("null -> null; !null -> !null")
    public net.minecraft.network.chat.@Nullable Component asVanilla(final @Nullable Component adventure) {
        if (adventure == null) return null;
        return this.serializer.serialize(adventure);
    }

    public Component asAdventure(final net.minecraft.network.chat.@Nullable Component vanilla) {
        return vanilla == null ? Component.empty() : this.serializer.deserialize(vanilla);
    }

    private static <M, A extends Keyed, E, B extends PaperRegistryBuilder<M, A>> RegistryEntryMeta.Buildable<M, A, E, B> getDirectHolderBuildableMeta(final RegistryKey<A> registryKey) {
        final RegistryEntryMeta.Buildable<M, A, E, B> buildableMeta = PaperRegistries.getBuildableMeta(registryKey);
        Preconditions.checkArgument(buildableMeta.registryTypeMapper().supportsDirectHolders(), "Registry type mapper must support direct holders");
        return buildableMeta;
    }

    public <M, A extends Keyed & RegistryElement.Buildable<A, E, ?>, E> Function<M, E> getEntryCreator(final RegistryKey<A> registryKey) {
        final RegistryEntryMeta.Buildable<M, A, E, ?> directHolderBuildableMeta = getDirectHolderBuildableMeta(registryKey);
        return m -> directHolderBuildableMeta.entryFactory().create(this, m);
    }

    public <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> A createApiInstanceFromBuilder(final RegistryKey<A> registryKey, final Consumer<? super PaperRegistryBuilderFactory<M, A, B>> value) {
        final RegistryEntryMeta.Buildable<M, A, ?, B> meta = getDirectHolderBuildableMeta(registryKey);
        final PaperRegistryBuilderFactory<M, A, B> builderFactory = this.createRegistryBuilderFactory(registryKey, meta);
        value.accept(builderFactory);
        return meta.registryTypeMapper().createBukkit(Holder.direct(builderFactory.requireBuilder().build()));
    }

    public <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> Holder.Direct<M> createHolderFromBuilder(final RegistryKey<A> registryKey, final Consumer<? super PaperRegistryBuilderFactory<M, A, B>> value) {
        final RegistryEntryMeta.Buildable<M, A, ?, B> meta = getDirectHolderBuildableMeta(registryKey);
        final PaperRegistryBuilderFactory<M, A, B> builderFactory = this.createRegistryBuilderFactory(registryKey, meta);
        value.accept(builderFactory);
        return (Holder.Direct<M>) Holder.direct(builderFactory.requireBuilder().build());
    }

    private <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> PaperRegistryBuilderFactory<M, A, B> createRegistryBuilderFactory(
        final RegistryKey<A> registryKey,
        final RegistryEntryMeta.Buildable<M, A, ?, B> buildableMeta
    ) {
        final ResourceKey<? extends Registry<M>> resourceRegistryKey = PaperRegistries.registryToNms(registryKey);
        final HolderLookup.RegistryLookup<M> lookupForBuilders = this.lookup.lookupForValueCopyViaBuilders().lookupOrThrow(resourceRegistryKey);
        return new PaperRegistryBuilderFactory<>(this, buildableMeta.builderFiller(), lookupForBuilders::getValueForCopying);
    }


    @Override
    public <V extends Keyed> Tag<V> getOrCreateTag(final TagKey<V> tagKey) {
        final RegistryOps.RegistryInfo<Object> registryInfo = this.lookup().lookup(PaperRegistries.registryToNms(tagKey.registryKey())).orElseThrow();
        final HolderSet.Named<?> tagSet = registryInfo.getter().getOrThrow(PaperRegistries.toNms(tagKey));
        return new NamedRegistryKeySetImpl<>(tagKey, tagSet);
    }

    @Override
    public <V extends Keyed & RegistryElement.Buildable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolder.Reference<V, E> getOrCreateReferenceHolder(final TypedKey<V> key) {
        final Holder.Reference<Object> reference = this.getReferenceHolder(key);
        return PaperRegistryHolders.createReference(reference, this.getEntryCreator(key.registryKey()));
    }

    @Override
    public <V extends Keyed & RegistryElement.Inlineable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolder.Inlined<V, E> getOrCreateInlinedHolder(final RegistryKey<V> registryKey, final Consumer<RegistryBuilderFactory<V, ? extends B>> value) {
        final Holder.Direct<Object> directHolder = this.createHolderFromBuilder(registryKey, value);
        return PaperRegistryHolders.createInlined(registryKey, directHolder, this.getEntryCreator(registryKey));
    }

    @Override
    public <V extends Keyed & RegistryElement.Inlineable<V, E, B>, E, B extends RegistryBuilder<V>> RegistryHolderSetBuilder<V, E, B> createSetBuilder(final RegistryKey<V> registryKey) {
        return new RegistryHolderSetBuilderImpl<>(registryKey, this);
    }
}
