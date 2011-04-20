package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import net.minecraft.server.Chunk;
import net.minecraft.server.MinecraftServer;
import static org.bukkit.craftbukkit.util.Java15Compat.Arrays_copyOf;

public class LongHashtable<V> extends LongHash {
    Object[][][] values = new Object[256][][];
    Entry cache = null;

    public void put(int msw, int lsw, V value) {
        put(toLong(msw, lsw), value);
        if (value instanceof Chunk) {
            Chunk c = (Chunk) value;
            if (msw != c.x || lsw != c.z) {
                MinecraftServer.log.info("Chunk (" + c.x + ", " + c.z +") stored at  (" + msw + ", " + lsw + ")");
                Throwable x = new Throwable();
                x.fillInStackTrace();
                x.printStackTrace();
            }
        }
    }

    public V get(int msw, int lsw) {
        V value = get(toLong(msw, lsw));
        if (value instanceof Chunk) {
            Chunk c = (Chunk) value;
            if (msw != c.x || lsw != c.z) {
                MinecraftServer.log.info("Chunk (" + c.x + ", " + c.z +") stored at  (" + msw + ", " + lsw + ")");
                Throwable x = new Throwable();
                x.fillInStackTrace();
                x.printStackTrace();
            }
        }
        return value;
    }

    public synchronized void put(long key, V value) {
        int mainIdx = (int) (key & 255);
        Object[][] outer = this.values[mainIdx];
        if (outer == null) this.values[mainIdx] = outer = new Object[256][];

        int outerIdx = (int) ((key >> 32) & 255);
        Object[] inner = outer[outerIdx];

        if (inner == null) {
            outer[outerIdx] = inner = new Object[5];
            inner[0] = this.cache = new Entry(key, value);
        } else {
            int i;
            for (i = 0; i < inner.length; i++) {
                if (inner[i] == null || ((Entry) inner[i]).key == key) {
                    inner[i] = this.cache = new Entry(key, value);
                    return;
                }
            }

            outer[outerIdx] = inner = Arrays_copyOf(inner, i + i);
            inner[i] = new Entry(key, value);
        }
    }

    public synchronized V get(long key) {
        return containsKey(key) ? (V) cache.value : null;
    }

    public synchronized boolean containsKey(long key) {
        if (this.cache != null && cache.key == key) return true;

        int outerIdx = (int) ((key >> 32) & 255);
        Object[][] outer = this.values[(int) (key & 255)];
        if (outer == null) return false;

        Object[] inner = outer[outerIdx];
        if (inner == null) return false;

        for (int i = 0; i < inner.length; i++) {
            Entry e = (Entry) inner[i];
            if (e == null) {
                return false;
            } else if (e.key == key) {
                this.cache = e;
                return true;
            }
        }
        return false;
    }

    public synchronized void remove(long key) {
        Object[][] outer = this.values[(int) (key & 255)];
        if (outer == null) return;

        Object[] inner = outer[(int) ((key >> 32) & 255)];
        if (inner == null) return;

        for (int i = 0; i < inner.length; i++) {
            if (inner[i] == null) continue;

            if (((Entry) inner[i]).key == key) {
                for (i++; i < inner.length; i++) {
                    if (inner[i] == null) break;
                    inner[i-1] = inner[i];
                }

                inner[i-1] = null;
                this.cache = null;
                return;
            }
        }
    }

    public synchronized ArrayList<V> values() {
        ArrayList<V> ret = new ArrayList<V>();

        for (Object[][] outer: this.values) {
            if (outer == null) continue;

            for (Object[] inner: outer) {
                if (inner == null) continue;

                for (Object entry: inner) {
                    if (entry == null) break;

                    ret.add((V) ((Entry) entry).value);
                }
            }
        }
        return ret;
    }

    private class Entry {
        long key;
        Object value;

        Entry(long k, Object v) {
            this.key = k;
            this.value = v;
        }
    }
}
