package org.bukkit.craftbukkit.util;

import java.util.ArrayList;

public class LongHashtable<V> extends LongBaseHashtable {

    public void put(int msw, int lsw, V value) {
        put(toLong(msw, lsw), value);
    }

    public V get(int msw, int lsw) {
        return get(toLong(msw, lsw));
    }

    public synchronized void put(long key, V value) {
        put(new Entry(key, value));
    }

    @SuppressWarnings("unchecked")
    public synchronized V get(long key) {
        Entry entry = ((Entry) getEntry(key));
        return entry != null ? entry.value : null;
    }

    @SuppressWarnings("unchecked")
    public synchronized ArrayList<V> values() {
        ArrayList<V> ret = new ArrayList<V>();

        ArrayList<EntryBase> entries = entries();

        for (EntryBase entry : entries) {
            ret.add(((Entry) entry).value);
        }
        return ret;
    }

    private class Entry extends EntryBase {
        V value;

        Entry(long k, V v) {
            super(k);
            this.value = v;
        }
    }
}
