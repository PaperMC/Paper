package io.papermc.paper.registry.set;

import io.papermc.paper.registry.Registered;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import org.bukkit.Keyed;

public class RegistrySetProviderImpl implements RegistrySetProvider {

    @Override
    public <T extends Keyed & Registered.Inlineable<T, E, B>, E, B extends RegistryBuilder<T>> RegistryHolderSetBuilder<T, E, B> registryHolderSetBuilder(final RegistryKey<T> registryKey) { // TODO remove Keyed
        return new RegistryHolderSetBuilderImpl<>(registryKey, Conversions.global());
    }
}
