package io.papermc.paper.registry.holder;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record InlinedRegistryHolderImpl<API, ENTRY, M>(ENTRY entry, Holder.Direct<M> holder) implements RegistryHolder.Inlined<API, ENTRY> {
}
