package io.papermc.paper.registry.data.util;

import java.util.OptionalInt;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class Checks {

    public static <T> T asConfigured(final @Nullable T value, final String field) {
        if (value == null) {
            throw new IllegalStateException(field + " has not been configured");
        }
        return value;
    }

    public static int asConfigured(final OptionalInt value, final String field) {
        if (value.isEmpty()) {
            throw new IllegalStateException(field + " has not been configured");
        }
        return value.getAsInt();
    }

    public static <T> T asArgument(final @Nullable T value, final String field) {
        if (value == null) {
            throw new IllegalArgumentException("argument " + field + " cannot be null");
        }
        return value;
    }

    private Checks() {
    }
}
