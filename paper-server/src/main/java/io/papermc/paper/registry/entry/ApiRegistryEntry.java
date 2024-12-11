package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryHolder;
import io.papermc.paper.registry.RegistryKey;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;

public class ApiRegistryEntry<M, B extends Keyed> extends BaseRegistryEntry<M, B> {

    private final Supplier<org.bukkit.Registry<B>> registrySupplier;

    protected ApiRegistryEntry(
        final ResourceKey<? extends Registry<M>> mcKey,
        final RegistryKey<B> apiKey,
        final Supplier<org.bukkit.Registry<B>> registrySupplier
    ) {
        super(mcKey, apiKey);
        this.registrySupplier = registrySupplier;
    }

    @Override
    public RegistryHolder<B> createRegistryHolder(final Registry<M> nmsRegistry) {
        return new RegistryHolder.Memoized<>(this.registrySupplier);
    }
}
