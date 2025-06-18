package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Function;
import net.minecraft.core.Holder;

public final class PaperRegistryHolders {

    public static <API, ENTRY, M> RegistryHolder<API, ENTRY> create(final Holder<M> holder, final Function<M, ENTRY> entryCreator) {
        return switch (holder) {
            case final Holder.Direct<M> direct -> new InlinedRegistryHolderImpl<>(entryCreator.apply(direct.value()), direct);
            case final Holder.Reference<M> reference -> new ReferenceRegistryHolderImpl<>(PaperRegistries.fromNms(reference.key()));
            default -> throw new IllegalArgumentException("Unsupported holder type: " + holder.getClass().getName());
        };
    }

    public static <API, ENTRY, M> Holder<M> convert(final RegistryHolder<API, ENTRY> holder, final Conversions conversions) {
        return switch (holder) {
            case final RegistryHolder.Reference<API, ENTRY> ref -> conversions.getReferenceHolder(PaperRegistries.toNms(ref.key()));
            case final RegistryHolder.Inlined<API, ENTRY> inlined -> ((InlinedRegistryHolderImpl<API, ENTRY, M>) inlined).holder();
        };
    }

    private PaperRegistryHolders() {}
}
