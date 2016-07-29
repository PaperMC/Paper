package org.bukkit.util;

import java.lang.reflect.Array;

/**
 * @deprecated Bukkit targets Java 1.6. This class will be removed in a
 * subsequent release.
 */
@Deprecated
public class Java15Compat {
    @SuppressWarnings("unchecked")
    public static <T> T[] Arrays_copyOfRange(T[] original, int start, int end) {
        if (original.length >= start && 0 <= start) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                T[] copy = (T[]) Array.newInstance(original.getClass().getComponentType(), length);

                System.arraycopy(original, start, copy, 0, copyLength);
                return copy;
            }
            throw new IllegalArgumentException();
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
