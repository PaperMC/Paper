package com.destroystokyo.paper.util;

import org.jetbrains.annotations.ApiStatus;

/**
 * Rethrows checked exceptions without declaring them.
 *
 * @hidden
 */
@ApiStatus.Internal
public final class SneakyThrow {

    private SneakyThrow() {
    }

    public static void sneaky(final Throwable exception) {
        SneakyThrow.throwSneaky(exception);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwSneaky(final Throwable exception) throws T {
        throw (T) exception;
    }

}
