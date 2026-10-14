package io.papermc.paper.registry;

import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import org.bukkit.NamespacedKey;

public abstract class HolderableBase<M> implements Holderable<M> {

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
        return Holderable.super.getHandle();
    }

    @Override
    public final int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }

    @Override
    public final NamespacedKey getKey() {
        return Holderable.super.getKey();
    }
}
