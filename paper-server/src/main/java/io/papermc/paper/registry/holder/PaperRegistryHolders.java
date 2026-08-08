package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.Keyed;

public final class PaperRegistryHolders {

    public static <API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M> RegistryHolder<API, ENTRY> create(final RegistryKey<API> registryKey, final Holder<M> holder, final Conversions conversions) { // TODO remove Keyed
        final Function<M, ENTRY> entryCreator = conversions.getEntryCreator(registryKey);
        return create(registryKey, holder, entryCreator);
    }

    public static <API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M> RegistryHolder<API, ENTRY> create(final RegistryKey<API> registryKey, final Holder<M> holder, final Function<M, ENTRY> entryCreator) { // TODO remove Keyed
        return switch (holder) {
            case final Holder.Direct<M> direct -> createInlined(registryKey, direct, entryCreator);
            case final Holder.Reference<M> reference -> createReference(reference, entryCreator);
        };
    }

    public static <API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M> Holder<M> convert(final RegistryHolder<API, ENTRY> holder, final Conversions conversions) { // TODO remove Keyed
        return switch (holder) {
            case final RegistryHolder.Reference<API, ENTRY> ref -> ((PaperReferenceHolder<API, ENTRY, M>) ref).getHolder(conversions);
            case final RegistryHolder.Inlined<API, ENTRY> inlined -> ((InlinedRegistryHolderImpl<API, ENTRY, M>) inlined).holder();
        };
    }

    public static <API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M> RegistryHolder.Reference<API, ENTRY> createReference(final Holder.Reference<M> holder, final Function<M, ENTRY> entryCreator) { // TODO remove Keyed
        return new ReferenceRegistryHolderImpl<>(holder, entryCreator);
    }

    public static <API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M> RegistryHolder.Inlined<API, ENTRY> createInlined(final RegistryKey<API> registryKey, final Holder.Direct<M> holder, final Function<M, ENTRY> entryCreator) { // TODO remove Keyed
        return new InlinedRegistryHolderImpl<>(registryKey, entryCreator.apply(holder.value()), holder);
    }
    private PaperRegistryHolders() {}
}
