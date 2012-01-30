package org.bukkit.craftbukkit.util;

import static org.bukkit.craftbukkit.util.Java15Compat.Arrays_copyOf;

import java.util.ArrayList;

public class LongBaseHashtable extends LongHash {

    EntryBase[][][] values = new EntryBase[256][][];
    EntryBase cache = null;

    public void put(int msw, int lsw, EntryBase entry) {
        put(entry);
    }

    public EntryBase getEntry(int msw, int lsw) {
        return getEntry(toLong(msw, lsw));
    }

    public synchronized void put(EntryBase entry) {
        int mainIdx = (int) (entry.key & 255);
        EntryBase[][] outer = this.values[mainIdx];
        if (outer == null) this.values[mainIdx] = outer = new EntryBase[256][];

        int outerIdx = (int) ((entry.key >> 32) & 255);
        EntryBase[] inner = outer[outerIdx];

        if (inner == null) {
            outer[outerIdx] = inner = new EntryBase[5];
            inner[0] = this.cache = entry;
        } else {
            int i;
            for (i = 0; i < inner.length; i++) {
                if (inner[i] == null || inner[i].key == entry.key) {
                    inner[i] = this.cache = entry;
                    return;
                }
            }

            outer[outerIdx] = inner = Arrays_copyOf(inner, i + i);
            inner[i] = entry;
        }
    }

    public synchronized EntryBase getEntry(long key) {
        return containsKey(key) ? cache : null;
    }

    public synchronized boolean containsKey(long key) {
        if (this.cache != null && cache.key == key) return true;

        int outerIdx = (int) ((key >> 32) & 255);
        EntryBase[][] outer = this.values[(int) (key & 255)];
        if (outer == null) return false;

        EntryBase[] inner = outer[outerIdx];
        if (inner == null) return false;

        for (int i = 0; i < inner.length; i++) {
            EntryBase e = inner[i];
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
        EntryBase[][] outer = this.values[(int) (key & 255)];
        if (outer == null) return;

        EntryBase[] inner = outer[(int) ((key >> 32) & 255)];
        if (inner == null) return;

        for (int i = 0; i < inner.length; i++) {
            if (inner[i] == null) continue;

            if (inner[i].key == key) {
                for (i++; i < inner.length; i++) {
                    if (inner[i] == null) break;
                    inner[i - 1] = inner[i];
                }

                inner[i-1] = null;
                this.cache = null;
                return;
            }
        }
    }

    public synchronized ArrayList<EntryBase> entries() {
        ArrayList<EntryBase> ret = new ArrayList<EntryBase>();

        for (EntryBase[][] outer : this.values) {
            if (outer == null) continue;

            for (EntryBase[] inner : outer) {
                if (inner == null) continue;

                for (EntryBase entry : inner) {
                    if (entry == null) break;

                    ret.add(entry);
                }
            }
        }
        return ret;
    }
}
