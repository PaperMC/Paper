package com.destroystokyo.paper.util.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * copied from https://github.com/Spottedleaf/ConcurrentUtil/blob/master/src/main/java/ca/spottedleaf/concurrentutil/lock/WeakSeqLock.java
 * @author Spottedleaf
 */
public final class WeakSeqLock {
    // TODO when the switch to J11 is made, nuke this class from orbit

    protected final AtomicLong lock = new AtomicLong();

    public WeakSeqLock() {
        //VarHandle.storeStoreFence(); // warn: usages must be checked to ensure this behaviour isn't needed
    }

    public void acquireWrite() {
        // must be release-type write
        this.lock.lazySet(this.lock.get() + 1);
    }

    public boolean canRead(final long read) {
        return (read & 1) == 0;
    }

    public boolean tryAcquireWrite() {
        this.acquireWrite();
        return true;
    }

    public void releaseWrite() {
        // must be acquire-type write
        final long lock = this.lock.get(); // volatile here acts as store-store
        this.lock.lazySet(lock + 1);
    }

    public void abortWrite() {
        // must be acquire-type write
        final long lock = this.lock.get(); // volatile here acts as store-store
        this.lock.lazySet(lock ^ 1);
    }

    public long acquireRead() {
        int failures = 0;
        long curr;

        for (curr = this.lock.get(); !this.canRead(curr); curr = this.lock.get()) {
            // without j11, our only backoff is the yield() call...

            if (++failures > 5_000) { /* TODO determine a threshold */
                Thread.yield();
            }
            /* Better waiting is beyond the scope of this lock; if it is needed the lock is being misused */
        }

        //VarHandle.loadLoadFence(); // volatile acts as the load-load barrier
        return curr;
    }

    public boolean tryReleaseRead(final long read) {
        return this.lock.get() == read; // volatile acts as the load-load barrier
    }

    public long getSequentialCounter() {
        return this.lock.get();
    }
}
