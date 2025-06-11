package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;

public final class PaperRegistryHolders {

    public static <API, ENTRY> RegistryHolder<API, ENTRY> create(final TypedKey<API> key) {
        return new ReferenceRegistryHolderImpl<>(key);
    }

    public static <API, ENTRY> RegistryHolder<API, ENTRY> create(final ENTRY entry) {
        return new InlinedRegistryHolderImpl<>(entry);
    }

    private PaperRegistryHolders() {}
}
