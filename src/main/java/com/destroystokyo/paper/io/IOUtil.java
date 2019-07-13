package com.destroystokyo.paper.io;

import org.bukkit.Bukkit;

public final class IOUtil {

    /* Copied from concrete or concurrentutil */

    public static long getCoordinateKey(final int x, final int z) {
        return ((long)z << 32) | (x & 0xFFFFFFFFL);
    }

    public static int getCoordinateX(final long key) {
        return (int)key;
    }

    public static int getCoordinateZ(final long key) {
        return (int)(key >>> 32);
    }

    public static int getRegionCoordinate(final int chunkCoordinate) {
        return chunkCoordinate >> 5;
    }

    public static int getChunkInRegion(final int chunkCoordinate) {
        return chunkCoordinate & 31;
    }

    public static String genericToString(final Object object) {
        return object == null ? "null" : object.getClass().getName() + ":" + object.toString();
    }

    public static <T> T notNull(final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T notNull(final T obj, final String msgIfNull) {
        if (obj == null) {
            throw new NullPointerException(msgIfNull);
        }
        return obj;
    }

    public static void arrayBounds(final int off, final int len, final int arrayLength, final String msgPrefix) {
        if (off < 0 || len < 0 || (arrayLength - off) < len) {
            throw new ArrayIndexOutOfBoundsException(msgPrefix + ": off: " + off + ", len: " + len + ", array length: " + arrayLength);
        }
    }

    public static int getPriorityForCurrentThread() {
        return Bukkit.isPrimaryThread() ? PrioritizedTaskQueue.HIGHEST_PRIORITY : PrioritizedTaskQueue.NORMAL_PRIORITY;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void rethrow(final Throwable throwable) throws T {
        throw (T)throwable;
    }

}
