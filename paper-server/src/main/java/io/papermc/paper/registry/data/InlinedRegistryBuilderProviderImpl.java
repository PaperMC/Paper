package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.RegistryHolder;
import io.papermc.paper.registry.holder.TransientReferenceRegistryHolder;
import java.util.function.Consumer;
import org.bukkit.Keyed;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public <T extends RegistryElement.Inlineable<T, ?, B> & Keyed, B extends RegistryBuilder<T>> T create(
        final RegistryKey<T> key,
        final Consumer<RegistryBuilderFactory<T, ? extends B>> value
    ) {
        return Conversions.global().createApiInstanceFromBuilder(key, value);
    }

    @Override
    public <T extends RegistryElement.Buildable<T, E, ?>, E> RegistryHolder.Reference<T, E> createTransientReferenceHolder(final TypedKey<T> key) {
        return new TransientReferenceRegistryHolder<>(key);
    }
}
