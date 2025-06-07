package io.papermc.paper.registry.holder;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record InlinedRegistryHolderImpl<API, ENTRY>(ENTRY entry) implements RegistryHolder.Inlined<API, ENTRY> {
}
