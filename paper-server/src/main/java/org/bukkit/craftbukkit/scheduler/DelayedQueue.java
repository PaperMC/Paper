package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

final class DelayedQueue implements WorkQueue {
    private final Queue<CraftTask> pending = new PriorityQueue<>();

    @Override
    public boolean tryPush(final CraftTask task) {
        return this.pending.add(task);
    }

    @Override
    public void dropAll(final Consumer<CraftTask> action) {
        final int currentTick = CraftScheduler.now();
        for (CraftTask h;
             (h = this.pending.peek()) != null && h.getNextRun() <= currentTick;
             action.accept(this.pending.poll())
        );
    }

    @Override
    public boolean remove(final CraftTask task) {
        // todo: heapIndex for fast remove
        return this.pending.remove(task);
    }

    @Override
    public Iterator<CraftTask> iterator() {
        return this.pending.iterator();
    }

    @Override
    public String toString() {
        return this.pending.toString();
    }
}
