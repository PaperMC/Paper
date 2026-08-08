package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * @hidden
 */
@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface InlinedRegistryBuilderProvider {

    static InlinedRegistryBuilderProvider instance() {
        final class Holder {
            static final Optional<InlinedRegistryBuilderProvider> INSTANCE = ServiceLoader.load(InlinedRegistryBuilderProvider.class, InlinedRegistryBuilderProvider.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    static <T extends RegistryElement.Buildable<T, E, ?>, E> RegistryHolder<T, E> transientHolder(final TypedKey<T> key) {
        return instance().createTransientReferenceHolder(key);
    }

    <T extends RegistryElement.Inlineable<T, ?, B> & Keyed, B extends RegistryBuilder<T>> T create(final RegistryKey<T> key, final Consumer<RegistryBuilderFactory<T, ? extends B>> value);

    <T extends RegistryElement.Buildable<T, E, ?>, E> RegistryHolder.Reference<T, E> createTransientReferenceHolder(final TypedKey<T> key);
}
