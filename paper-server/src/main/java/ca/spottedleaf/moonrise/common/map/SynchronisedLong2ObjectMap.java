package ca.spottedleaf.moonrise.common.map;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import java.util.function.BiFunction;

public final class SynchronisedLong2ObjectMap<V> {
    private final Long2ObjectLinkedOpenHashMap<V> map = new Long2ObjectLinkedOpenHashMap<>();
    private final int limit;

    public SynchronisedLong2ObjectMap(final int limit) {
        this.limit = limit;
    }

    // must hold lock on map
    private void purgeEntries() {
        while (this.map.size() > this.limit) {
            this.map.removeLast();
        }
    }

    public V get(final long key) {
        synchronized (this.map) {
            return this.map.getAndMoveToFirst(key);
        }
    }

    public V put(final long key, final V value) {
        synchronized (this.map) {
            final V ret = this.map.putAndMoveToFirst(key, value);
            this.purgeEntries();
            return ret;
        }
    }

    public V compute(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        synchronized (this.map) {
            // first, compute the value - if one is added, it will be at the last entry
            this.map.compute(key, remappingFunction);
            // move the entry to first, just in case it was added at last
            final V ret = this.map.getAndMoveToFirst(key);
            // now purge the last entries
            this.purgeEntries();

            return ret;
        }
    }
}