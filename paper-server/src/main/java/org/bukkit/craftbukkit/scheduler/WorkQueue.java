package org.bukkit.craftbukkit.scheduler;

import java.util.function.Consumer;

interface WorkQueue extends Iterable<CraftTask> {
    boolean tryPush(CraftTask task);

    boolean remove(CraftTask task);

    void dropAll(Consumer<CraftTask> action);

    default void push(final CraftTask task) {
        while (!this.tryPush(task));
    }
}
