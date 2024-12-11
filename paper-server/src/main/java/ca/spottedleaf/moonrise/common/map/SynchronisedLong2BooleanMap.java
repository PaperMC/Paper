package ca.spottedleaf.moonrise.common.map;

import it.unimi.dsi.fastutil.longs.Long2BooleanFunction;
import it.unimi.dsi.fastutil.longs.Long2BooleanLinkedOpenHashMap;

public final class SynchronisedLong2BooleanMap {
    private final Long2BooleanLinkedOpenHashMap map = new Long2BooleanLinkedOpenHashMap();
    private final int limit;

    public SynchronisedLong2BooleanMap(final int limit) {
        this.limit = limit;
    }

    // must hold lock on map
    private void purgeEntries() {
        while (this.map.size() > this.limit) {
            this.map.removeLastBoolean();
        }
    }

    public boolean remove(final long key) {
        synchronized (this.map) {
            return this.map.remove(key);
        }
    }

    // note:
    public boolean getOrCompute(final long key, final Long2BooleanFunction ifAbsent) {
        synchronized (this.map) {
            if (this.map.containsKey(key)) {
                return this.map.getAndMoveToFirst(key);
            }
        }

        final boolean put = ifAbsent.get(key);

        synchronized (this.map) {
            if (this.map.containsKey(key)) {
                return this.map.getAndMoveToFirst(key);
            }
            this.map.putAndMoveToFirst(key, put);

            this.purgeEntries();

            return put;
        }
    }
}