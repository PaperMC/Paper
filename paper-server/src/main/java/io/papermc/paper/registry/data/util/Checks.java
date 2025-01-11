package io.papermc.paper.registry.data.util;

import java.util.OptionalInt;
import org.jspecify.annotations.Nullable;

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

    public static int asArgumentRange(final int value, final String field, final int min, final int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("argument " + field + " must be [" + min + ", " + max + "]");
        }
        return value;
    }

    public static int asArgumentMin(final int value, final String field, final int min) {
        if (value < min) {
            throw new IllegalArgumentException("argument " + field + " must be [" + min + ",+inf)");
        }
        return value;
    }

    public static float asArgumentMinExclusive(final float value, final String field, final float min) {
        if (value <= min) {
            throw new IllegalArgumentException("argument " + field + " must be (" + min + ",+inf)");
        }
        return value;
    }

    private Checks() {
    }
}
