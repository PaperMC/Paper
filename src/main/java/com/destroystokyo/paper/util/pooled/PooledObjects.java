package com.destroystokyo.paper.util.pooled;

import net.minecraft.server.MCUtil;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayDeque;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class PooledObjects<E> {

    /**
     * Wrapper for an object that will be have a cleaner registered for it, and may be automatically returned to pool.
     */
    public class AutoReleased {
        private final E object;
        private final Runnable cleaner;

        public AutoReleased(E object, Runnable cleaner) {
            this.object = object;
            this.cleaner = cleaner;
        }

        public final E getObject() {
            return object;
        }

        public final Runnable getCleaner() {
            return cleaner;
        }
    }

    public static final PooledObjects<MutableInt> POOLED_MUTABLE_INTEGERS = new PooledObjects<>(MutableInt::new, 1024);

    private final Supplier<E> creator;
    private final Consumer<E> releaser;
    private final int maxPoolSize;
    private final ArrayDeque<E> queue;

    public PooledObjects(final Supplier<E> creator, int maxPoolSize) {
        this(creator, maxPoolSize, null);
    }
    public PooledObjects(final Supplier<E> creator, int maxPoolSize, Consumer<E> releaser) {
        if (creator == null) {
            throw new NullPointerException("Creator must not be null");
        }
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("Max pool size must be greater-than 0");
        }

        this.queue = new ArrayDeque<>(maxPoolSize);
        this.maxPoolSize = maxPoolSize;
        this.creator = creator;
        this.releaser = releaser;
    }

    public AutoReleased acquireCleaner(Object holder) {
        return acquireCleaner(holder, this::release);
    }

    public AutoReleased acquireCleaner(Object holder, Consumer<E> releaser) {
        E resource = acquire();
        Runnable cleaner = MCUtil.registerCleaner(holder, resource, releaser);
        return new AutoReleased(resource, cleaner);
    }

    public final E acquire() {
        E value;
        synchronized (queue) {
            value = this.queue.pollLast();
        }
        return value != null ? value : this.creator.get();
    }

    public final void release(final E value) {
        if (this.releaser != null) {
            this.releaser.accept(value);
        }
        synchronized (this.queue) {
            if (queue.size() < this.maxPoolSize) {
                this.queue.addLast(value);
            }
        }
    }
}
