package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryHolder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;

public interface RegistryEntry<M, A extends Keyed> { // TODO remove Keyed

    RegistryHolder<A> createRegistryHolder(Registry<M> nmsRegistry);

    RegistryEntryMeta<M, A> meta();

    default RegistryKey<A> apiKey() {
        return this.meta().apiKey();
    }

    default ResourceKey<? extends Registry<M>> mcKey() {
        return this.meta().mcKey();
    }

    /**
     * This should only be used if the registry instance needs to exist early due to the need
     * to populate a field in {@link org.bukkit.Registry}. Data-driven registries shouldn't exist
     * as fields, but instead be obtained via {@link io.papermc.paper.registry.RegistryAccess#getRegistry(RegistryKey)}
     */
    @Deprecated
    default RegistryEntry<M, A> delayed() {
        return new DelayedRegistryEntry<>(this);
    }
}
