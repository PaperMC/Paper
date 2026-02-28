package io.papermc.paper.registry;

import io.papermc.paper.util.HolderableElement;
import net.minecraft.core.Holder;
import org.bukkit.NamespacedKey;

public abstract class HolderableBase<M, A> implements HolderableElement<M, A> {

    protected final Holder<M> holder;

    protected HolderableBase(final Holder<M> holder) {
        this.holder = holder;
    }

    // methods below are overridden to make final
    @Override
    public final Holder<M> getHolder() {
        return this.holder;
    }

    @Override
    public final M getHandle() {
        return HolderableElement.super.getHandle();
    }

    @Override
    public final int hashCode() {
        return HolderableElement.super.implHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return HolderableElement.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return HolderableElement.super.implToString();
    }

    @Override
    public final NamespacedKey getKey() {
        return HolderableElement.super.getKey();
    }
}
