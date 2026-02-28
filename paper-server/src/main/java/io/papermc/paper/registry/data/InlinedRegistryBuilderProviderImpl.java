package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
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
}
