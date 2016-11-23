package com.destroystokyo.paper.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
public class CachedSizeConcurrentLinkedQueue<E> extends ConcurrentLinkedQueue<E> {

    private final LongAdder cachedSize = new LongAdder();

    @Override
    public boolean add(final E e) {
        final boolean result = super.add(e);
        if (result) {
            this.cachedSize.increment();
        }
        return result;
    }

    @Override
    public @Nullable E poll() {
        final E result = super.poll();
        if (result != null) {
            this.cachedSize.decrement();
        }
        return result;
    }

    @Override
    public int size() {
        return this.cachedSize.intValue();
    }
}
