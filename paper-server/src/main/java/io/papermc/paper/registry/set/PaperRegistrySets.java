package io.papermc.paper.registry.set;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.util.Holderable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

public final class PaperRegistrySets {

    public static <A extends Keyed, M> HolderSet<M> convertToNms(final ResourceKey<? extends Registry<M>> resourceKey, final RegistryOps.RegistryInfoLookup lookup, final RegistryKeySet<A> registryKeySet) { // TODO remove Keyed
        if (registryKeySet instanceof NamedRegistryKeySetImpl<A, ?>) {
            return ((NamedRegistryKeySetImpl<A, M>) registryKeySet).namedSet();
        } else {
            final RegistryOps.RegistryInfo<M> registryInfo = lookup.lookup(resourceKey).orElseThrow();
            return HolderSet.direct(key -> {
                return registryInfo.getter().getOrThrow(PaperRegistries.toNms(key));
            }, registryKeySet.values());
        }
    }

    public static <A extends Keyed, M> HolderSet<M> convertToNmsWithDirects(final ResourceKey<? extends Registry<M>> resourceKey, final RegistryOps.RegistryInfoLookup lookup, final RegistrySet<A> registrySet) { // TODO remove Keyed
        if (registrySet instanceof NamedRegistryKeySetImpl<A, ?>) {
            return ((NamedRegistryKeySetImpl<A, M>) registrySet).namedSet();
        } else if (registrySet.isEmpty()) {
            return HolderSet.empty();
        } else if (registrySet instanceof final RegistryValueSet<A> valueSet) {
            final List<Holder<M>> directs = new ArrayList<>(valueSet.values().size());
            for (final A value : valueSet) {
                if (!(value instanceof final Holderable<?> holderable)) {
                    throw new UnsupportedOperationException("Cannot convert a registry set containing non-holderable values");
                }
                directs.add(((Holderable<M>) holderable).getHolder());
            }
            return HolderSet.direct(directs);
        } else if (registrySet instanceof final RegistryKeySet<A> keySet) {
            final RegistryOps.RegistryInfo<M> registryInfo = lookup.lookup(resourceKey).orElseThrow();
            return HolderSet.direct(key -> {
                return registryInfo.getter().getOrThrow(PaperRegistries.toNms(key));
            }, keySet.values());
        } else {
            throw new UnsupportedOperationException("Cannot convert a registry set of type " + registrySet.getClass().getName());
        }
    }

    public static <A extends Keyed, M> RegistryKeySet<A> convertToApi(final RegistryKey<A> registryKey, final HolderSet<M> holders) { // TODO remove Keyed
        if (holders instanceof final HolderSet.Named<M> named) {
            return new NamedRegistryKeySetImpl<>(PaperRegistries.fromNms(named.key()), named);
        } else {
            final List<TypedKey<A>> keys = new ArrayList<>();
            for (final Holder<M> holder : holders) {
                if (!(holder instanceof final Holder.Reference<M> reference)) {
                    throw new UnsupportedOperationException("Cannot convert a holder set containing direct holders");
                }
                keys.add(PaperRegistries.fromNms(reference.key()));
            }
            return RegistrySet.keySet(registryKey, keys);
        }
    }

    public static <A extends Keyed, M> RegistrySet<A> convertToApiWithDirects(final RegistryKey<A> registryKey, final HolderSet<M> holders) { // TODO remove Keyed
        if (holders instanceof final HolderSet.Named<M> named) {
            return new NamedRegistryKeySetImpl<>(PaperRegistries.fromNms(named.key()), named);
        } else {
            if (holders.size() == 0) {
                return RegistrySet.keySet(registryKey);
            }
            if (holders.get(0) instanceof Holder.Direct<M>) {
                final List<A> directs = new ArrayList<>(holders.size());
                final ResourceKey<? extends Registry<M>> nmsRegistryKey = PaperRegistries.registryToNms(registryKey);
                for (final Holder<M> holder : holders) {
                    directs.add(CraftRegistry.minecraftHolderToBukkit(holder, nmsRegistryKey));
                }
                return RegistrySet.valueSet(registryKey, directs);
            } else {
                final List<TypedKey<A>> keys = new ArrayList<>(holders.size());
                for (final Holder<M> holder : holders) {
                    keys.add(PaperRegistries.fromNms(((Holder.Reference<M>) holder).key()));
                }
                return RegistrySet.keySet(registryKey, keys);
            }
        }
    }

    private PaperRegistrySets() {
    }
}
