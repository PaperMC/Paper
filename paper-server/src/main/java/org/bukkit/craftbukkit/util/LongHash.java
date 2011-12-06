package org.bukkit.craftbukkit.util;

public abstract class LongHash {
    public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - Integer.MIN_VALUE;
    }

    public static int msw(long l) {
        return (int) (l >> 32);
    }

    public static int lsw(long l) {
        return (int) (l & 0xFFFFFFFF) + Integer.MIN_VALUE;
    }

    public boolean containsKey(int msw, int lsw) {
        return containsKey(toLong(msw, lsw));
    }

    public void remove(int msw, int lsw) {
        remove(toLong(msw, lsw));
    }

    public abstract boolean containsKey(long key);

    public abstract void remove(long key);
}
