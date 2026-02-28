package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryElement;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.craftbukkit.CraftRegistry;

record ReferenceRegistryHolderImpl<API extends RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M>(
    RegistryKey<API> registryKey,
    Holder.Reference<M> holder,
    Function<M, ? extends ENTRY> entryCreator
) implements RegistryHolder.Reference<API, ENTRY>, PaperRegistryElement<M, API> {

    ReferenceRegistryHolderImpl(final Holder.Reference<M> holder, final Function<M, ? extends ENTRY> entryCreator) {
        this(PaperRegistries.registryFromNms(holder.key().registryKey()), holder, entryCreator);
    }

    @Override
    public TypedKey<API> key() {
        return PaperRegistries.fromNms(this.holder.key());
    }

    @Override
    public API value() {
        //noinspection unchecked
        return CraftRegistry.minecraftHolderToBukkit(this.holder);
    }

    @Override
    public ENTRY entry() {
        return this.entryCreator.apply(this.holder.value());
    }

    @Override
    public Holder<M> getHolder() {
        return this.holder;
    }
}
