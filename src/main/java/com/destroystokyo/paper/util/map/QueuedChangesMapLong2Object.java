package com.destroystokyo.paper.util.map;

import com.destroystokyo.paper.util.concurrent.WeakSeqLock;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Spottedleaf
 */
public class QueuedChangesMapLong2Object<V> {

    protected static final Object REMOVED = new Object();

    protected final Long2ObjectLinkedOpenHashMap<V> updatingMap;
    protected final Long2ObjectLinkedOpenHashMap<V> visibleMap;
    protected final Long2ObjectLinkedOpenHashMap<Object> queuedChanges;

    // we use a seqlock as writes are not common.
    protected final WeakSeqLock updatingMapSeqLock = new WeakSeqLock();

    public QueuedChangesMapLong2Object() {
        this(16, 0.75f); // dfl for fastutil
    }

    public QueuedChangesMapLong2Object(final int capacity, final float loadFactor) {
        this.updatingMap = new Long2ObjectLinkedOpenHashMap<>(capacity, loadFactor);
        this.visibleMap = new Long2ObjectLinkedOpenHashMap<>(capacity, loadFactor);
        this.queuedChanges = new Long2ObjectLinkedOpenHashMap<>();
    }

    public V queueUpdate(final long k, final V value) {
        this.queuedChanges.put(k, value);
        return this.updatingMap.put(k, value);
    }

    public V queueRemove(final long k) {
        this.queuedChanges.put(k, REMOVED);
        return this.updatingMap.remove(k);
    }

    public V getUpdating(final long k) {
        return this.updatingMap.get(k);
    }

    public boolean updatingContainsKey(final long k) {
        return this.updatingMap.containsKey(k);
    }

    public V getVisible(final long k) {
        return this.visibleMap.get(k);
    }

    public boolean visibleContainsKey(final long k) {
        return this.visibleMap.containsKey(k);
    }

    public V getVisibleAsync(final long k) {
        long readlock;
        V ret = null;

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

    public boolean visibleContainsKeyAsync(final long k) {
        long readlock;
        boolean ret = false;

        do {
            readlock = this.updatingMapSeqLock.acquireRead();

            try {
                ret = this.visibleMap.containsKey(k);
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

    public Long2ObjectLinkedOpenHashMap<V> getVisibleMap() {
        return this.visibleMap;
    }

    public Long2ObjectLinkedOpenHashMap<V> getUpdatingMap() {
        return this.updatingMap;
    }

    public int getVisibleSize() {
        return this.visibleMap.size();
    }

    public int getVisibleSizeAsync() {
        long readlock;
        int ret;

        do {
            readlock = this.updatingMapSeqLock.acquireRead();
            ret = this.visibleMap.size();
        } while (!this.updatingMapSeqLock.tryReleaseRead(readlock));

        return ret;
    }

    // unlike mojang's impl this cannot be used async since it's not a view of an immutable map
    public Collection<V> getUpdatingValues() {
        return this.updatingMap.values();
    }

    public List<V> getUpdatingValuesCopy() {
        return new ArrayList<>(this.updatingMap.values());
    }

    // unlike mojang's impl this cannot be used async since it's not a view of an immutable map
    public Collection<V> getVisibleValues() {
        return this.visibleMap.values();
    }

    public List<V> getVisibleValuesCopy() {
        return new ArrayList<>(this.visibleMap.values());
    }

    public boolean performUpdates() {
        if (this.queuedChanges.isEmpty()) {
            return false;
        }

        final ObjectBidirectionalIterator<Long2ObjectMap.Entry<Object>> iterator = this.queuedChanges.long2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            final Long2ObjectMap.Entry<Object> entry = iterator.next();
            final long key = entry.getLongKey();
            final Object val = entry.getValue();

            this.updatingMapSeqLock.acquireWrite();
            try {
                if (val == REMOVED) {
                    this.visibleMap.remove(key);
                } else {
                    this.visibleMap.put(key, (V)val);
                }
            } finally {
                this.updatingMapSeqLock.releaseWrite();
            }
        }

        this.queuedChanges.clear();
        return true;
    }

    public boolean performUpdatesLockMap() {
        if (this.queuedChanges.isEmpty()) {
            return false;
        }

        final ObjectBidirectionalIterator<Long2ObjectMap.Entry<Object>> iterator = this.queuedChanges.long2ObjectEntrySet().fastIterator();

        try {
            this.updatingMapSeqLock.acquireWrite();

            while (iterator.hasNext()) {
                final Long2ObjectMap.Entry<Object> entry = iterator.next();
                final long key = entry.getLongKey();
                final Object val = entry.getValue();

                if (val == REMOVED) {
                    this.visibleMap.remove(key);
                } else {
                    this.visibleMap.put(key, (V)val);
                }
            }
        } finally {
            this.updatingMapSeqLock.releaseWrite();
        }

        this.queuedChanges.clear();
        return true;
    }
}
