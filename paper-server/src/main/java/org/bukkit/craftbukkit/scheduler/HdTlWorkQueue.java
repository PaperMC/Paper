package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

final class HdTlWorkQueue implements WorkQueue {
    private volatile Node head = new Node(null);
    private final AtomicReference<Node> tail = new AtomicReference<>(this.head);

    @Override
    public boolean tryPush(final CraftTask task) {
        final Node node = new Node(task);
        Node last = tail.getAndSet(node);
        last.link(node);
        return true;
    }

    @Override
    public boolean remove(final CraftTask task) {
        return false;
    }

    @Override
    public void dropAll(final Consumer<CraftTask> action) {
        Node head = this.head;
        Node task = head.next;
        Node lastTask = head;
        for (; task != null; task = (lastTask = task).next) {
            action.accept(task.task);
        }
        // We split this because of the way things are ordered for all of the async calls in CraftScheduler
        // (it prevents race-conditions)
        for (task = head; task != lastTask; task = head) {
            head = task.next;
            task.unlink();
        }
        this.head = lastTask;
    }

    @Override
    public Iterator<CraftTask> iterator() {
        final Node start = head.next;
        return new Iterator<>() {
            private Node current = start;
            private CraftTask item = start == null ? null : start.task;

            @Override
            public boolean hasNext() {
                return item != null;
            }

            @Override
            public CraftTask next() {
                try {
                    return item;
                } finally {
                    Node node = current.next;
                    if (node == null) {
                        item = null;
                    } else {
                        item = node.task;
                        current = node;
                    }
                }
            }
        };
    }

    private static final class Node {
        private CraftTask task;
        private volatile Node next;

        public Node(CraftTask task) {
            this.task = task;
        }

        public void link(final Node next) {
            this.next = next;
        }

        public void unlink() {
            this.task = null;
            this.next = null; // plain write
        }
    }
}
