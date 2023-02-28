package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;

public abstract class BaseRegistryEntry<M, B extends Keyed> implements RegistryEntry<M, B> { // TODO remove Keyed

    private final ResourceKey<? extends Registry<M>> minecraftRegistryKey;
    private final RegistryKey<B> apiRegistryKey;

    protected BaseRegistryEntry(final ResourceKey<? extends Registry<M>> minecraftRegistryKey, final RegistryKey<B> apiRegistryKey) {
        this.minecraftRegistryKey = minecraftRegistryKey;
        this.apiRegistryKey = apiRegistryKey;
    }

    @Override
    public final ResourceKey<? extends Registry<M>> mcKey() {
        return this.minecraftRegistryKey;
    }

    @Override
    public final RegistryKey<B> apiKey() {
        return this.apiRegistryKey;
    }
}
