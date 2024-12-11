package io.papermc.paper.registry.set;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;

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

    private PaperRegistrySets() {
    }
}
