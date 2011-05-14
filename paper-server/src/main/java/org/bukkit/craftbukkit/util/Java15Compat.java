package org.bukkit.craftbukkit.util;

import java.lang.reflect.Array;

public class Java15Compat {
    public static <T> T[] Arrays_copyOf(T[] original, int newLength) {
        if (0 <= newLength) {
            return org.bukkit.util.Java15Compat.Arrays_copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static long[] Arrays_copyOf(long[] original, int newLength) {
        if (0 <= newLength) {
            return Arrays_copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    private static long[] Arrays_copyOfRange(long[] original, int start, int end) {
        if (original.length >= start && 0 <= start) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                long[] copy = (long[]) Array.newInstance(original.getClass().getComponentType(), length);
                System.arraycopy(original, start, copy, 0, copyLength);
                return copy;
            }
            throw new IllegalArgumentException();
        }
        throw new ArrayIndexOutOfBoundsException();
    }

}
