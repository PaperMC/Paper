package io.papermc.paper.registry.data.util;

import com.google.common.base.Preconditions;
import com.mojang.serialization.JavaOps;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.adventure.WrapperAwareSerializer;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.PaperRegistryBuilderFactory;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public class Conversions {

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
    private final WrapperAwareSerializer serializer;

    public Conversions(final RegistryOps.RegistryInfoLookup lookup) {
        this.lookup = lookup;
        this.serializer = new WrapperAwareSerializer(() -> RegistryOps.create(JavaOps.INSTANCE, lookup));
    }

    public RegistryOps.RegistryInfoLookup lookup() {
        return this.lookup;
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

    public ClientTextureAsset asBukkit(final net.minecraft.core.@Nullable ClientAsset clientTextureAsset) {
        return clientTextureAsset == null ? null : ClientTextureAsset.clientTextureAsset(
            PaperAdventure.asAdventure(clientTextureAsset.id()),
            PaperAdventure.asAdventure(clientTextureAsset.texturePath())
        );
    }

    public net.minecraft.core.ClientAsset asVanilla(final @Nullable ClientTextureAsset clientTextureAsset) {
        return clientTextureAsset == null ? null : new net.minecraft.core.ClientAsset(
            PaperAdventure.asVanilla(clientTextureAsset.identifier()),
            PaperAdventure.asVanilla(clientTextureAsset.texturePath())
        );
    }

    private static <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> RegistryEntryMeta.Buildable<M, A, B> getDirectHolderBuildableMeta(final ResourceKey<? extends Registry<M>> registryKey) {
        final RegistryEntryMeta.Buildable<M, A, B> buildableMeta = PaperRegistries.getBuildableMeta(registryKey);
        Preconditions.checkArgument(buildableMeta.registryTypeMapper().supportsDirectHolders(), "Registry type mapper must support direct holders");
        return buildableMeta;
    }

    public <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> A createApiInstanceFromBuilder(final ResourceKey<? extends Registry<M>> registryKey, final Consumer<? super PaperRegistryBuilderFactory<M, A, B>> value) {
        final RegistryEntryMeta.Buildable<M, A, B> meta = getDirectHolderBuildableMeta(registryKey);
        final PaperRegistryBuilderFactory<M, A, B> builderFactory = this.createRegistryBuilderFactory(registryKey, meta);
        value.accept(builderFactory);
        return meta.registryTypeMapper().createBukkit(Holder.direct(builderFactory.requireBuilder().build()));
    }

    public <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> Holder<M> createHolderFromBuilder(final ResourceKey<? extends Registry<M>> registryKey, final Consumer<? super PaperRegistryBuilderFactory<M, A, B>> value) {
        final RegistryEntryMeta.Buildable<M, A, B> meta = getDirectHolderBuildableMeta(registryKey);
        final PaperRegistryBuilderFactory<M, A, B> builderFactory = this.createRegistryBuilderFactory(registryKey, meta);
        value.accept(builderFactory);
        return Holder.direct(builderFactory.requireBuilder().build());
    }

    private <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> PaperRegistryBuilderFactory<M, A, B> createRegistryBuilderFactory(
        final ResourceKey<? extends Registry<M>> registryKey,
        final RegistryEntryMeta.Buildable<M, A, B> buildableMeta
    ) {
        final HolderLookup.RegistryLookup<M> lookupForBuilders = this.lookup.lookupForValueCopyViaBuilders().lookupOrThrow(registryKey);
        return new PaperRegistryBuilderFactory<>(registryKey, this, buildableMeta.builderFiller(), lookupForBuilders::getValueForCopying);
    }

}
