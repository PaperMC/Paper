package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

record ReferenceRegistryHolderImpl<API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M>( // TODO remove Keyed
    RegistryKey<API> registryKey,
    Holder.Reference<M> holder,
    Function<M, ? extends ENTRY> entryCreator
) implements PaperReferenceHolder<API, ENTRY, M> {

    ReferenceRegistryHolderImpl(final Holder.Reference<M> holder, final Function<M, ? extends ENTRY> entryCreator) {
        this(PaperRegistries.registryFromNms(holder.key().registryKey()), holder, entryCreator);
    }

    @Override
    public Holder.Reference<M> getHolder(final Conversions conversions) {
        return this.holder;
    }

    @Override
    public TypedKey<API> key() {
        return PaperRegistries.fromNms(this.holder.key());
    }

    private void checkBound() {
        if (!this.holder.isBound()) {
            throw new IllegalStateException("Holder for " + this.key() + " is not bound");
        }
    }

    @Override
    public API value() {
        this.checkBound();
        return CraftRegistry.minecraftHolderToBukkit(this.holder);
    }

    @Override
    public ENTRY entry() {
        this.checkBound();
        return this.entryCreator.apply(this.holder.value());
    }
}
