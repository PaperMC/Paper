package io.papermc.generator.utils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record MergedRegistryProvider(
    HolderLookup.Provider registryAccess,
    HolderLookup.Provider reloadableProvider,
    Set<ResourceKey<? extends Registry<?>>> reloadableRegistries
) implements HolderLookup.Provider {

    @Override
    public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
        return Stream.concat(this.registryAccess.listRegistryKeys(), this.reloadableRegistries.stream());
    }

    @Override
    public <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
        return this.reloadableRegistries.contains(registryKey) ? this.reloadableProvider.lookup(registryKey) : this.registryAccess.lookup(registryKey);
    }
}
