package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryHolder;
import net.minecraft.core.Registry;
import org.bukkit.Keyed;

record RegistryEntryImpl<M, A extends Keyed>(RegistryEntryMeta<M, A> meta) implements RegistryEntry<M, A> {

    @Override
    public RegistryHolder<A> createRegistryHolder(final Registry<M> nmsRegistry) {
        return new RegistryHolder.Memoized<>(() -> this.meta().createApiRegistry(nmsRegistry));
    }
}
