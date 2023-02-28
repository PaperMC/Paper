package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryHolder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import java.util.function.BiFunction;
import net.minecraft.core.Registry;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.ApiVersion;

public interface RegistryEntry<M, B extends Keyed> extends RegistryEntryInfo<M, B> { // TODO remove Keyed

    RegistryHolder<B> createRegistryHolder(Registry<M> nmsRegistry);

    default RegistryEntry<M, B> withSerializationUpdater(final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> updater) {
        return this;
    }

    /**
     * This should only be used if the registry instance needs to exist early due to the need
     * to populate a field in {@link org.bukkit.Registry}. Data-driven registries shouldn't exist
     * as fields, but instead be obtained via {@link io.papermc.paper.registry.RegistryAccess#getRegistry(RegistryKey)}
     */
    @Deprecated
    default RegistryEntry<M, B> delayed() {
        return new DelayedRegistryEntry<>(this);
    }
}
