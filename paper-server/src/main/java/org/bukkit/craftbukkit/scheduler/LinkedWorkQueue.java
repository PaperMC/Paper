package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

final class LinkedWorkQueue implements WorkQueue {
    private final AtomicReference<Node> head = new AtomicReference<>();

    @Override
    public boolean tryPush(final CraftTask task) {
        final Node newNode = new Node(task);
        head.updateAndGet(h -> {
            newNode.link(h);
            return newNode;
        });
        return true;
    }

    @Override
    public boolean remove(final CraftTask task) {
        return false;
    }

    @Override
    public void dropAll(final Consumer<CraftTask> action) {
        while (this.head.get() != null) {
            for (Node x = this.head.getAndSet(null); x != null; x = x.next) {
                action.accept(x.task);
            }
        }
    }

    @Override
    public Iterator<CraftTask> iterator() {
        final Node start = head.get();
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
        private final CraftTask task;
        private Node next;

        public Node(CraftTask task) {
            this.task = task;
        }

        public void link(final Node next) {
            this.next = next;
        }

        public void unlink() {
            this.next = null;
        }
    }
}
