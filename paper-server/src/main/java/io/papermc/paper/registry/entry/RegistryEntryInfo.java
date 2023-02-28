package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryEntryInfo<M, B> {

    ResourceKey<? extends Registry<M>> mcKey();

    RegistryKey<B> apiKey();
}
