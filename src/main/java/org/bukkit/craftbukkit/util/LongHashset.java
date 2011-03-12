package org.bukkit.craftbukkit.util;

import static org.bukkit.craftbukkit.util.Java15Compat.Arrays_copyOf;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class LongHashset extends LongHash {
    long[][][] values = new long[256][][];
    int count = 0;
    ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    ReadLock rl = rwl.readLock();
    WriteLock wl = rwl.writeLock();

    public boolean isEmpty() {
        rl.lock();
        try {
            return this.count == 0;
        } finally {
            rl.unlock();
        }
    }

    public void add(int msw, int lsw) {
        add(toLong(msw, lsw));
    }

    public void add(long key) {
        wl.lock();
        try {
            int mainIdx = (int) (key & 255);
            long outer[][] = this.values[mainIdx];
            if (outer == null) this.values[mainIdx] = outer = new long[256][];

            int outerIdx = (int) ((key >> 32) & 255);
            long inner[] = outer[outerIdx];

            if (inner == null) {
                synchronized (this) {
                    outer[outerIdx] = inner = new long[1];
                    inner[0] = key;
                    this.count++;
                }
            } else {
                int i;
                for (i = 0; i < inner.length; i++) {
                    if (inner[i] == key) {
                        return;
                    }
                }
                inner = Arrays_copyOf(inner, i + 1);
                outer[outerIdx] = inner;
                inner[i] = key;
                this.count++;
            }
        } finally {
            wl.unlock();
        }
    }

    public boolean containsKey(long key) {
        rl.lock();
        try {
            long[][] outer = this.values[(int) (key & 255)];
            if (outer == null) return false;

            long[] inner = outer[(int) ((key >> 32) & 255)];
            if (inner == null) return false;

            for (long entry: inner) {
                if (entry == key) return true;
            }
            return false;
        } finally {
            rl.unlock();
        }
    }

    public void remove(long key) {
        wl.lock();
        try {
            long[][] outer = this.values[(int) (key & 255)];
            if (outer == null) return;

            long[] inner = outer[(int) ((key >> 32) & 255)];
            if (inner == null) return;

            int max = inner.length - 1;
            for (int i = 0; i <= max; i++) {
                if (inner[i] == key) {
                    this.count--;
                    if (i != max) {
                        inner[i] = inner[max];
                    }

                    outer[(int) ((key >> 32) & 255)] = (max == 0 ? null : Arrays_copyOf(inner, max));
                    return;
                }
            }
        } finally {
            wl.unlock();
        }
    }

    public long popFirst() {
        wl.lock();
        try {
            for (long[][] outer: this.values) {
                if (outer == null) continue;

                for (int i = 0; i < outer.length; i++) {
                    long[] inner = outer[i];
                    if (inner == null || inner.length == 0) continue;

                    this.count--;
                    long ret = inner[inner.length - 1];
                    outer[i] = Arrays_copyOf(inner, inner.length - 1);

                    return ret;
                }
            }
        } finally {
            wl.unlock();
        }
        return 0;
    }

    public long[] keys() {
        int index = 0;
        rl.lock();
        try {
            long[] ret = new long[this.count];
            for (long[][] outer: this.values) {
                if (outer == null) continue;

                for (long[] inner: outer) {
                    if (inner == null) continue;

                    for (long entry: inner) {
                        ret[index++] = entry;
                    }
                }
            }
            return ret;
        } finally {
            rl.unlock();
        }
    }
}
