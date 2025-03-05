package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

final class StripedQueue implements WorkQueue {
    private final List<CraftTask> internal = new LinkedList<>();
    private final HdTlWorkQueue external = new HdTlWorkQueue();
    private final CraftScheduler scheduler;
    private final Thread leader;

    public StripedQueue(final CraftScheduler scheduler, final Thread leader) {
        this.scheduler = scheduler;
        this.leader = leader;
    }

    private boolean internal(final CraftTask task) {
        return task.getPeriod() > 0 &&
            Thread.currentThread() == leader &&
            // because of the methods isQueued getPendingTasks...
            scheduler.runners.containsKey(task.getTaskId());
    }
    @Override
    public boolean tryPush(final CraftTask task) {
        if (internal(task)) {
            return internal.add(task);
        } else {
            return external.tryPush(task);
        }
    }

    @Override
    public boolean remove(final CraftTask task) {
        if (internal(task)) {
            return internal.remove(task);
        } else {
            return external.remove(task);
        }
    }

    @Override
    public void dropAll(final Consumer<CraftTask> action) {
        assert Thread.currentThread() == leader;
        external.dropAll(action);
        internal.forEach(action);
        internal.clear();
    }

    @Override
    public Iterator<CraftTask> iterator() {
        return external.iterator();
    }
}
