package com.destroystokyo.paper.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @hidden
 */
@ApiStatus.Internal
@NullMarked
public final class SneakyThrow {

    public static void sneaky(final Throwable exception) {
        SneakyThrow.throwSneaky(exception);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwSneaky(final Throwable exception) throws T {
        throw (T) exception;
    }

}
