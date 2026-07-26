package io.papermc.paper.registry.entry;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.WritableCraftRegistry;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.event.RegistryEntryAddEventImpl;
import io.papermc.paper.registry.event.RegistryComposeEventImpl;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.ApiVersion;

public sealed interface RegistryEntryMeta<M, A extends Keyed> permits RegistryEntryMeta.ApiOnly, RegistryEntryMeta.ServerSide { // TODO remove Keyed

    ResourceKey<? extends Registry<M>> mcKey();

    RegistryKey<A> apiKey();

    org.bukkit.Registry<A> createApiRegistry(final Registry<M> nmsRegistry);

    default RegistryModificationApiSupport modificationApiSupport() {
        return RegistryModificationApiSupport.NONE;
    }

    record ApiOnly<M, A extends Keyed>(ResourceKey<? extends Registry<M>> mcKey, RegistryKey<A> apiKey, Supplier<org.bukkit.Registry<A>> registrySupplier) implements RegistryEntryMeta<M, A> { // TODO remove Keyed

        @Override
        public org.bukkit.Registry<A> createApiRegistry(final Registry<M> nmsRegistry) {
            return this.registrySupplier.get();
        }
    }

    sealed interface ServerSide<M, A extends Keyed> extends RegistryEntryMeta<M, A> permits RegistryEntryMeta.Craft, RegistryEntryMeta.Buildable { // TODO remove Keyed

        Class<?> classToPreload();

        RegistryTypeMapper<M, A> registryTypeMapper();

        BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater();

        default org.bukkit.Registry<A> createApiRegistry(final Registry<M> nmsRegistry) {
            return new CraftRegistry<>(this, nmsRegistry);
        }
    }

    record Craft<M, A extends Keyed>(
        ResourceKey<? extends Registry<M>> mcKey,
        RegistryKey<A> apiKey,
        Class<?> classToPreload,
        RegistryTypeMapper<M, A> registryTypeMapper,
        BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater
    ) implements ServerSide<M, A> { // TODO remove Keyed

        public Craft {
            Preconditions.checkArgument(!classToPreload.getPackageName().startsWith("net.minecraft"), "%s should not be in the net.minecraft package as the class-to-preload", classToPreload);
        }
    }

    enum RegistryModificationApiSupport {
        /**
         * Cannot add or modify values in the registry.
         */
        NONE,
        /**
         * Can only add new values to the registry, not modify any values.
         */
        ADDABLE,
        /**
         * Can mutate values being added to the registry
         */
        MODIFIABLE,
        /**
         * Can mutate values and add new values.
         */
        WRITABLE,
        ;

        public boolean canAdd() {
            return this != MODIFIABLE && this != NONE;
        }

        public boolean canModify() {
            return this != ADDABLE && this != NONE;
        }
    }

    record Buildable<M, A extends Keyed, B extends PaperRegistryBuilder<M, A>>( // TODO remove Keyed
        ResourceKey<? extends Registry<M>> mcKey,
        RegistryKey<A> apiKey,
        Class<?> classToPreload,
        RegistryTypeMapper<M, A> registryTypeMapper,
        BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater,
        PaperRegistryBuilder.Filler<M, A, B> builderFiller,
        RegistryModificationApiSupport modificationApiSupport
    ) implements ServerSide<M, A> {

        public RegistryEntryAddEventImpl<A, B> createEntryAddEvent(final TypedKey<A> key, final B initialBuilder, final Conversions conversions) {
            return new RegistryEntryAddEventImpl<>(key, initialBuilder, this.apiKey(), conversions);
        }

        public RegistryComposeEventImpl<A, B> createPostLoadEvent(final WritableCraftRegistry<M, A, B> writableRegistry, final Conversions conversions) {
            return new RegistryComposeEventImpl<>(this.apiKey(), writableRegistry.createApiWritableRegistry(conversions), conversions);
        }

        @Override
        public org.bukkit.Registry<A> createApiRegistry(final Registry<M> nmsRegistry) {
            if (this.modificationApiSupport.canAdd()) {
                return new WritableCraftRegistry<>((MappedRegistry<M>) nmsRegistry, this);
            } else {
                return ServerSide.super.createApiRegistry(nmsRegistry);
            }
        }
    }
}
