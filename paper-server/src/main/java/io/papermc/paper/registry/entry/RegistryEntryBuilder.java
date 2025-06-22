package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.ApiVersion;

import static io.papermc.paper.registry.entry.RegistryEntryMeta.RegistryModificationApiSupport.ADDABLE;
import static io.papermc.paper.registry.entry.RegistryEntryMeta.RegistryModificationApiSupport.MODIFIABLE;
import static io.papermc.paper.registry.entry.RegistryEntryMeta.RegistryModificationApiSupport.WRITABLE;

public class RegistryEntryBuilder<M, A extends Keyed> { // TODO remove Keyed

    public static <M, A extends Keyed> RegistryEntryBuilder<M, A> start( // TODO remove Keyed
        final ResourceKey<? extends Registry<M>> mcKey,
        final RegistryKey<A> apiKey
    ) {
        return new RegistryEntryBuilder<>(mcKey, apiKey);
    }

    protected final ResourceKey<? extends Registry<M>> mcKey;
    protected final RegistryKey<A> apiKey;

    private RegistryEntryBuilder(final ResourceKey<? extends Registry<M>> mcKey, RegistryKey<A> apiKey) {
        this.mcKey = mcKey;
        this.apiKey = apiKey;
    }

    public RegistryEntry<M, A> apiOnly(final Supplier<org.bukkit.Registry<A>> apiRegistrySupplier) {
        return new RegistryEntryImpl<>(new RegistryEntryMeta.ApiOnly<>(this.mcKey, this.apiKey, apiRegistrySupplier));
    }

    public CraftStage<M, A> craft(final Class<?> classToPreload, final Function<Holder<M>, ? extends A> minecraftToBukkit) {
        return this.craft(classToPreload, minecraftToBukkit, false);
    }

    public CraftStage<M, A> craft(final Class<?> classToPreload, final Function<Holder<M>, ? extends A> minecraftToBukkit, final boolean allowDirect) {
        return new CraftStage<>(this.mcKey, this.apiKey, classToPreload, new RegistryTypeMapper<>(minecraftToBukkit, allowDirect));
    }

    public static final class CraftStage<M, A extends Keyed> extends RegistryEntryBuilder<M, A> { // TODO remove Keyed

        private static final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> EMPTY = (namespacedKey, apiVersion) -> namespacedKey;

        private final Class<?> classToPreload;
        private final RegistryTypeMapper<M, A> minecraftToBukkit;
        private BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater = EMPTY;

        private CraftStage(
            final ResourceKey<? extends Registry<M>> mcKey,
            final RegistryKey<A> apiKey,
            final Class<?> classToPreload,
            final RegistryTypeMapper<M, A> minecraftToBukkit
        ) {
            super(mcKey, apiKey);
            this.classToPreload = classToPreload;
            this.minecraftToBukkit = minecraftToBukkit;
        }

        public CraftStage<M, A> serializationUpdater(final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater) {
            this.serializationUpdater = serializationUpdater;
            return this;
        }

        public RegistryEntry<M, A> build() {
            return new RegistryEntryImpl<>(new RegistryEntryMeta.Craft<>(this.mcKey, this.apiKey, this.classToPreload, this.minecraftToBukkit, this.serializationUpdater));
        }

        public <B extends PaperRegistryBuilder<M, A>> RegistryEntry<M, A> modifiable(final PaperRegistryBuilder.Filler<M, A, B> filler) {
            return this.create(filler, MODIFIABLE);
        }

        public <B extends PaperRegistryBuilder<M, A>> RegistryEntry<M, A> addable(final PaperRegistryBuilder.Filler<M, A, B> filler) {
            return this.create(filler, ADDABLE);
        }

        public <B extends PaperRegistryBuilder<M, A>> RegistryEntry<M, A> writable(final PaperRegistryBuilder.Filler<M, A, B> filler) {
            return this.create(filler, WRITABLE);
        }

        public <B extends PaperRegistryBuilder<M, A>> RegistryEntry<M, A> create(final PaperRegistryBuilder.Filler<M, A, B> filler, final RegistryEntryMeta.RegistryModificationApiSupport support) {
            return new RegistryEntryImpl<>(new RegistryEntryMeta.Buildable<>(this.mcKey, this.apiKey, this.classToPreload, this.minecraftToBukkit, this.serializationUpdater, filler, support));
        }
    }
}
