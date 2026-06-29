package io.papermc.paper.registry;

import io.papermc.paper.registry.entry.RegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.legacy.DelayedRegistry;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import io.papermc.paper.registry.legacy.LegacyRegistryIdentifiers;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;

public class PaperRegistryAccess implements RegistryAccess {

    // We store the API registries in a memoized supplier, so they can be created on-demand.
    // These suppliers are added to this map right after the instance of nms.Registry is created before it is loaded.
    // We want to do registration there, so we have access to the nms.Registry instance in order to wrap it in a CraftRegistry instance.
    // The memoized Supplier is needed because we *can't* instantiate any CraftRegistry class until **all** the BuiltInRegistries have been added
    // to this map because that would class-load org.bukkit.Registry which would query this map.
    private final Map<RegistryKey<?>, RegistryHolder<?>> registries = new ConcurrentHashMap<>(); // is "identity" because RegistryKey overrides equals and hashCode

    public static PaperRegistryAccess instance() {
        return (PaperRegistryAccess) RegistryAccessHolder.INSTANCE.orElseThrow(() -> new IllegalStateException("No RegistryAccess implementation found"));
    }

    @VisibleForTesting
    public Set<RegistryKey<?>> getLoadedServerBackedRegistries() {
        return this.registries.keySet().stream().filter(registryHolder -> {
            final RegistryEntry<?, ?> entry = PaperRegistries.getEntry(registryHolder);
            return entry != null && !(entry.meta() instanceof RegistryEntryMeta.ApiOnly<?,?>);
        }).collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true)
    @Override
    public <T extends Keyed> @Nullable Registry<T> getRegistry(final Class<T> type) {
        final RegistryKey<T> registryKey = byType(type);
        // If our mapping from Class -> RegistryKey did not contain the passed type it was either a completely invalid type or a registry
        // that merely exists as a SimpleRegistry in the org.bukkit.Registry type. We cannot return a registry for these, return null
        // as per method contract in Bukkit#getRegistry.
        if (registryKey == null) return null;

        final RegistryEntry<?, T> entry = PaperRegistries.getEntry(registryKey);
        final RegistryHolder<T> registry = (RegistryHolder<T>) this.registries.get(registryKey);
        if (registry != null) {
            // if the registry exists, return right away. Since this is the "legacy" method, we return DelayedRegistry
            // for the non-builtin Registry instances stored as fields in Registry.
            return registry.get();
        } else if (entry instanceof DelayedRegistryEntry<?, T>) {
            // if the registry doesn't exist and the entry is marked as "delayed", we create a registry holder that is empty
            // which will later be filled with the actual registry. This is so the fields on org.bukkit.Registry can be populated with
            // registries that don't exist at the time org.bukkit.Registry is statically initialized.
            final RegistryHolder<T> delayedHolder = new RegistryHolder.Delayed<>();
            this.registries.put(registryKey, delayedHolder);
            return delayedHolder.get();
        } else {
            // if the registry doesn't exist yet or doesn't have a delayed entry, just return null
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Keyed> Registry<T> getRegistry(final RegistryKey<T> key) {
        if (PaperRegistries.getEntry(key) == null) {
            throw new NoSuchElementException(key + " is not a valid registry key");
        }
        final RegistryHolder<T> registryHolder = (RegistryHolder<T>) this.registries.get(key);
        if (registryHolder == null) {
            throw new IllegalArgumentException(key + " points to a registry that is not available yet");
        }
        // since this is the getRegistry method that uses the modern RegistryKey, we unwrap any DelayedRegistry instances
        // that might be returned here. I don't think reference equality is required when doing getRegistry(RegistryKey.WOLF_VARIANT) == Registry.WOLF_VARIANT
        return possiblyUnwrap(registryHolder.get());
    }

    public <M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> WritableCraftRegistry<M, T, B> getWritableRegistry(final RegistryKey<T> key) {
        final Registry<T> registry = this.getRegistry(key);
        if (registry instanceof WritableCraftRegistry<?, T, ?>) {
            return (WritableCraftRegistry<M, T, B>) registry;
        }
        throw new IllegalArgumentException(key + " does not point to a writable registry");
    }

    private static <T extends Keyed> Registry<T> possiblyUnwrap(final Registry<T> registry) {
        if (registry instanceof final DelayedRegistry<T, ?> delayedRegistry) { // if not coming from legacy, unwrap the delayed registry
            return delayedRegistry.delegate();
        }
        return registry;
    }

    public <M> void registerReloadableRegistry(final net.minecraft.core.Registry<M> registry) {
        this.registerRegistry(registry, true);
    }

    public <M> void registerRegistry(final net.minecraft.core.Registry<M> registry) {
        this.registerRegistry(registry, false);
    }

    public <M> void lockReferenceHolders(final ResourceKey<? extends net.minecraft.core.Registry<M>> resourceKey) {
        final RegistryEntry<M, Keyed> entry = PaperRegistries.getEntry(resourceKey);
        if (entry == null || !(entry.meta() instanceof final RegistryEntryMeta.ServerSide<M, Keyed> serverSide) || !serverSide.registryTypeMapper().constructorUsesHolder()) {
            return;
        }
        final CraftRegistry<?, M> registry = (CraftRegistry<?, M>) this.getRegistry(entry.apiKey());
        registry.lockReferenceHolders();
    }

    @SuppressWarnings("unchecked") // this method should be called right after any new MappedRegistry instances are created to later be used by the server.
    private <M, B extends Keyed, R extends Registry<B>> void registerRegistry(final net.minecraft.core.Registry<M> registry, final boolean replace) {
        final RegistryEntry<M, B> entry = PaperRegistries.getEntry(registry.key());
        if (entry == null) { // skip registries that don't have API entries
            return;
        }
        final RegistryHolder<B> registryHolder = (RegistryHolder<B>) this.registries.get(entry.apiKey());
        if (registryHolder == null || replace) {
            // if the holder doesn't exist yet, or is marked as "replaceable", put it in the map.
            this.registries.put(entry.apiKey(), entry.createRegistryHolder(registry));
        } else {
            if (registryHolder instanceof RegistryHolder.Delayed<?, ?> && entry instanceof final DelayedRegistryEntry<M, B> delayedEntry) {
                // if the registry holder is delayed, and the entry is marked as "delayed", then load the holder with the CraftRegistry instance that wraps the actual nms Registry.
                ((RegistryHolder.Delayed<B, R>) registryHolder).loadFrom(delayedEntry, registry);
            } else {
                throw new IllegalArgumentException(registry.key() + " has already been created");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    @VisibleForTesting
    public static <T extends Keyed> @Nullable RegistryKey<T> byType(final Class<T> type) {
        return (RegistryKey<T>) LegacyRegistryIdentifiers.CLASS_TO_KEY_MAP.get(type);
    }
}
