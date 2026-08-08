package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.Holder;

public interface PaperReferenceHolder<API extends RegistryElement.Buildable<API, E, ?>, E, M> extends RegistryHolder.Reference<API, E> {

    Holder.Reference<M> getHolder(final Conversions conversions);
}
