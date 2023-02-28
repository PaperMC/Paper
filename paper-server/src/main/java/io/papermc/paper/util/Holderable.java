package io.papermc.paper.util;

import net.minecraft.core.Holder;
import org.bukkit.craftbukkit.util.Handleable;

public interface Holderable<M> extends Handleable<M> {

    Holder<M> getHolder();

    @Override
    default M getHandle() {
        return this.getHolder().value();
    }
}
