package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

final class DelayedQueue implements WorkQueue {
    private final Queue<CraftTask> pq = new PriorityQueue<>();

    @Override
    public boolean tryPush(final CraftTask task) {
        return this.pq.add(task);
    }

    @Override
    public void dropAll(final Consumer<CraftTask> action) {
        final int currentTick = CraftScheduler.now();
        for (CraftTask h;
             (h = this.pq.peek()) != null && h.getNextRun() <= currentTick;
             action.accept(this.pq.poll())
        );
    }

    @Override
    public boolean remove(CraftTask task) {
        // todo: heapIndex for fast remove
        return this.pq.remove(task);
    }

    @Override
    public Iterator<CraftTask> iterator() {
        return this.pq.iterator();
    }
    @Override
    public String toString() {
        return this.pq.toString();
    }
}
