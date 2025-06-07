package io.papermc.paper.registry.holder;

record InlinedRegistryHolderImpl<API, ENTRY>(ENTRY entry) implements RegistryHolder.Inlined<API, ENTRY> {
}
