package io.papermc.paper.registry.set;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import org.bukkit.Keyed;
import org.jspecify.annotations.Nullable;

final class RegistryHolderSetImpl<T extends Keyed & RegistryElement.Buildable<T, E, ?>, E, M> implements RegistryHolderSet<T, E> { // TODO remove Keyed

    private final RegistryKey<T> registryKey;
    private final Function<M, E> entryCreator;
    private final int size;
    private HolderSet.@Nullable Direct<M> holders;
    private @Nullable List<RegistryHolder<T, E>> apiHolders;

    RegistryHolderSetImpl(final RegistryKey<T> registryKey, final HolderSet.Direct<M> holders, final Function<M, E> entryCreator) {
        this.registryKey = registryKey;
        this.entryCreator = entryCreator;
        this.holders = holders;
        this.size = this.holders.size();
    }

    RegistryHolderSetImpl(final RegistryKey<T> registryKey, final List<RegistryHolder<T, E>> apiHolders, final Function<M, E> entryCreator) {
        this.registryKey = registryKey;
        this.entryCreator = entryCreator;
        this.apiHolders = List.copyOf(apiHolders);
        this.size = this.apiHolders.size();
    }

    public HolderSet.Direct<M> nmsHolders() {
        if (this.holders == null) {
            final List<Holder<M>> newHolders = new ArrayList<>();
            for (final RegistryHolder<T, E> holder : this.holders()) {
                newHolders.add(PaperRegistryHolders.convert(holder));
            }
            this.holders = HolderSet.direct(newHolders);
        }
        return this.holders;
    }

    @Override
    public Collection<RegistryHolder<T, E>> holders() {
        if (this.apiHolders == null) {
            final List<RegistryHolder<T, E>> newHolders = new ArrayList<>();
            for (final Holder<M> holder : this.nmsHolders()) {
                newHolders.add(PaperRegistryHolders.create(this.registryKey, holder, this.entryCreator));
            }
            this.apiHolders = List.copyOf(newHolders);
        }
        return this.apiHolders;
    }

    @Override
    public RegistryKey<T> registryKey() {
        return this.registryKey;
    }

    @Override
    public int size() {
        return this.size;
    }
}
