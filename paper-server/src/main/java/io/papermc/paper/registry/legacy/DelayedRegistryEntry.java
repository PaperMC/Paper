package io.papermc.paper.registry.legacy;

import io.papermc.paper.registry.RegistryHolder;
import io.papermc.paper.registry.entry.RegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import net.minecraft.core.Registry;
import org.bukkit.Keyed;

public record DelayedRegistryEntry<M, A extends Keyed>(RegistryEntry<M, A> delegate) implements RegistryEntry<M, A> {

    @Override
    public RegistryEntryMeta<M, A> meta() {
        return this.delegate.meta();
    }

    @Override
    public RegistryHolder<A> createRegistryHolder(final Registry<M> nmsRegistry) {
        return this.delegate.createRegistryHolder(nmsRegistry);
    }
}
