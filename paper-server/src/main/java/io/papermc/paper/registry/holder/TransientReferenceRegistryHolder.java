package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.Holder;

/**
 * This is useful for api-only types and implementations that need to represent a typed key as a holder
 * during the bootstrap phase. Should never be a return value from any type that has a server-side implementation.
 */
public record TransientReferenceRegistryHolder<API extends RegistryElement.Buildable<API, E, ?>, E, M>(TypedKey<API> key) implements PaperReferenceHolder<API, E, M> {

    @Override
    public Holder.Reference<M> getHolder(final Conversions conversions) {
        return conversions.getReferenceHolder(this.key);
    }

    @Override
    public RegistryKey<API> registryKey() {
        return this.key.registryKey();
    }

    @Override
    public API value() {
        throw new IllegalStateException("Holder for " + this.key + " is not bound");
    }

    @Override
    public E entry() {
        throw new IllegalStateException("Holder for " + this.key + " is not bound");
    }
}
