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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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

    public static int asArgumentNonNegative(final int value, final String field) {
        if (value < 0) {
            throw new IllegalArgumentException("argument " + field + " must be non-negative");
        }
        return value;
    }

    public static int asArgumentPositive(final int value, final String field) {
        if (value < 1) {
            throw new IllegalArgumentException("argument " + field + " must be positive");
        }
        return value;
    }

    public static float asArgumentNonNegative(final float value, final String field) {
        if (Float.compare(value, 0.0F) > 0 && Float.compare(value, Float.MAX_VALUE) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be non-negative");
    }

    public static float asArgumentPositive(final float value, final String field) {
        if (Float.compare(value, 0.0F) >= 0 && Float.compare(value, Float.MAX_VALUE) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be positive");
    }

    public static float asArgumentRange(final float value, final String field, final float min, final float max) {
        return asArgumentMinExclusive(value, field, min, max);
    }

    public static float asArgumentMinExclusive(final float value, final String field, final float min, final float max) {
        if (Float.compare(value, min) > 0 && Float.compare(value, max) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be (" + min + "," + max +"]");
    }

    public static float asArgumentMinInclusive(final float value, final String field, final float min, final float max) {
        if (Float.compare(value, min) >= 0 && Float.compare(value, max) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be (" + min + "," + max +"]");
    }

    private Checks() {
    }
}
