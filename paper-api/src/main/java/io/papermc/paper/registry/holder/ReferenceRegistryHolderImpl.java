package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record ReferenceRegistryHolderImpl<API, ENTRY>(TypedKey<API> key) implements RegistryHolder.Reference<API, ENTRY> {
}
