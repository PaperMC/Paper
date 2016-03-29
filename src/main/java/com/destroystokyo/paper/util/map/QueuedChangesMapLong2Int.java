package com.destroystokyo.paper.util.map;

import com.destroystokyo.paper.util.concurrent.WeakSeqLock;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

/**
 * @author Spottedleaf
 */
public class QueuedChangesMapLong2Int {

    protected final Long2IntOpenHashMap updatingMap;
    protected final Long2IntOpenHashMap visibleMap;
    protected final Long2IntOpenHashMap queuedPuts;
    protected final LongOpenHashSet queuedRemove;

    protected int queuedDefaultReturnValue;

    // we use a seqlock as writes are not common.
    protected final WeakSeqLock updatingMapSeqLock = new WeakSeqLock();

    public QueuedChangesMapLong2Int() {
        this(16, 0.75f);
    }

    public QueuedChangesMapLong2Int(final int capacity, final float loadFactor) {
        this.updatingMap = new Long2IntOpenHashMap(capacity, loadFactor);
        this.visibleMap = new Long2IntOpenHashMap(capacity, loadFactor);
        this.queuedPuts = new Long2IntOpenHashMap();
        this.queuedRemove = new LongOpenHashSet();
    }

    public void queueDefaultReturnValue(final int dfl) {
        this.queuedDefaultReturnValue = dfl;
        this.updatingMap.defaultReturnValue(dfl);
    }

    public int queueUpdate(final long k, final int v) {
        this.queuedRemove.remove(k);
        this.queuedPuts.put(k, v);

        return this.updatingMap.put(k, v);
    }

    public int queueRemove(final long k) {
        this.queuedPuts.remove(k);
        this.queuedRemove.add(k);

        return this.updatingMap.remove(k);
    }

    public int getUpdating(final long k) {
        return this.updatingMap.get(k);
    }

    public int getVisible(final long k) {
        return this.visibleMap.get(k);
    }

    public int getVisibleAsync(final long k) {
        long readlock;
        int ret = 0;

        do {
            readlock = this.updatingMapSeqLock.acquireRead();
            try {
                ret = this.visibleMap.get(k);
            } catch (final Throwable thr) {
                if (thr instanceof ThreadDeath) {
                    throw (ThreadDeath)thr;
                }
                // ignore...
                continue;
            }

        } while (!this.updatingMapSeqLock.tryReleaseRead(readlock));

        return ret;
    }

    public boolean performUpdates() {
        this.updatingMapSeqLock.acquireWrite();
        this.visibleMap.defaultReturnValue(this.queuedDefaultReturnValue);
        this.updatingMapSeqLock.releaseWrite();

        if (this.queuedPuts.isEmpty() && this.queuedRemove.isEmpty()) {
            return false;
        }

        // update puts
        final ObjectIterator<Long2IntMap.Entry> iterator0 = this.queuedPuts.long2IntEntrySet().fastIterator();
        while (iterator0.hasNext()) {
            final Long2IntMap.Entry entry = iterator0.next();
            final long key = entry.getLongKey();
            final int val = entry.getIntValue();

            this.updatingMapSeqLock.acquireWrite();
            try {
                this.visibleMap.put(key, val);
            } finally {
                this.updatingMapSeqLock.releaseWrite();
            }
        }

        this.queuedPuts.clear();

        final LongIterator iterator1 = this.queuedRemove.iterator();
        while (iterator1.hasNext()) {
            final long key = iterator1.nextLong();

            this.updatingMapSeqLock.acquireWrite();
            try {
                this.visibleMap.remove(key);
            } finally {
                this.updatingMapSeqLock.releaseWrite();
            }
        }

        this.queuedRemove.clear();

        return true;
    }

    public boolean performUpdatesLockMap() {
        this.updatingMapSeqLock.acquireWrite();
        try {
            this.visibleMap.defaultReturnValue(this.queuedDefaultReturnValue);

            if (this.queuedPuts.isEmpty() && this.queuedRemove.isEmpty()) {
                return false;
            }

            // update puts
            final ObjectIterator<Long2IntMap.Entry> iterator0 = this.queuedPuts.long2IntEntrySet().fastIterator();
            while (iterator0.hasNext()) {
                final Long2IntMap.Entry entry = iterator0.next();
                final long key = entry.getLongKey();
                final int val = entry.getIntValue();

                this.visibleMap.put(key, val);
            }

            this.queuedPuts.clear();

            final LongIterator iterator1 = this.queuedRemove.iterator();
            while (iterator1.hasNext()) {
                final long key = iterator1.nextLong();

                this.visibleMap.remove(key);
            }

            this.queuedRemove.clear();

            return true;
        } finally {
            this.updatingMapSeqLock.releaseWrite();
        }
    }
}
