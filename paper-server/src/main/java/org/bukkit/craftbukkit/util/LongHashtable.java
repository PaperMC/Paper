package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import net.minecraft.server.Chunk;
import net.minecraft.server.MinecraftServer;
import static org.bukkit.craftbukkit.util.Java15Compat.Arrays_copyOf;

public class LongHashtable<V> extends LongAbstractHashtable {

    public void put(int msw, int lsw, V value) {
        put(toLong(msw, lsw), value);
    }

    public V get(int msw, int lsw) {
        return get(toLong(msw, lsw));
    }

    public synchronized void put(long key, V value) {
        put(new Entry(key, value));
    }

    public synchronized V get(long key) {
        Entry entry = ((Entry)getEntry(key));
        return entry != null ? entry.value : null;
    }

    public synchronized ArrayList<V> values() {
        ArrayList<V> ret = new ArrayList<V>();

        ArrayList<EntryBase> entries = entries();

        for(EntryBase entry : entries) {
            ret.add(((Entry)entry).value);
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
