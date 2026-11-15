package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import java.util.Optional;
import java.util.ServiceLoader;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * @hidden
 */
@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface RegistrySetProvider {

    static RegistrySetProvider instance() {
        final class Holder {
            static final Optional<RegistrySetProvider> INSTANCE = ServiceLoader.load(RegistrySetProvider.class, RegistrySetProvider.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    <T extends Keyed & RegistryElement.Inlineable<T, E, B>, E, B extends RegistryBuilder<T>> RegistryHolderSetBuilder<T, E, B> registryHolderSetBuilder(RegistryKey<T> registryKey); // TODO remove Keyed
}
