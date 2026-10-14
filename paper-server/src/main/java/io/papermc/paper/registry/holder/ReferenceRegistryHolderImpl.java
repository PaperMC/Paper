package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.TypedKey;

record ReferenceRegistryHolderImpl<API, ENTRY>(TypedKey<API> key) implements RegistryHolder.Reference<API, ENTRY> {
}
