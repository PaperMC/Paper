package org.bukkit.craftbukkit.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Handleable<M> {

    M getHandle();
}
