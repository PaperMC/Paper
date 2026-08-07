package io.papermc.paper.registry.set;

import io.papermc.paper.registry.Registered;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

public class RegistryHolderSetBuilderImpl<T extends Registered.Inlineable<T, E, B> & Keyed, E, B extends RegistryBuilder<T>, M> implements RegistryHolderSetBuilder<T, E, B> { // TODO remove Keyed

    private final RegistryKey<T> registryKey;
    final List<Holder<M>> holders = new ArrayList<>();
    final Conversions conversions;

    public RegistryHolderSetBuilderImpl(final RegistryKey<T> registryKey, final Conversions conversions) {
        this.registryKey = registryKey;
        this.conversions = conversions;
    }

    @Override
    public RegistryHolderSetBuilder<T, E, B> add(final T value) {
        this.holders.add(CraftRegistry.bukkitToMinecraftHolder(value));
        return this;
    }

    @Override
    public RegistryHolderSetBuilder<T, E, B> add(final TypedKey<T> key) {
        this.holders.add(this.conversions.getReferenceHolder(key));
        return this;
    }

    @Override
    public RegistryHolderSetBuilder<T, E, B> add(final Consumer<RegistryBuilderFactory<T, ? extends B>> value) {
        this.holders.add(this.conversions.createHolderFromBuilder(this.registryKey, value));
        return this;
    }

    @Override
    public RegistryHolderSet<T, E> build() {
        return new RegistryHolderSetImpl<>(this.registryKey, HolderSet.direct(this.holders), this.conversions.getEntryCreator(this.registryKey));
    }
}
