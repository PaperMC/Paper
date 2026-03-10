package io.papermc.paper.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BoundChecker {

    public static int requireRange(final int value, final String field, final int min, final int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("argument " + field + " must be in [" + min + ", " + max + "]: " + value);
        }
        return value;
    }

    public static int requireNonNegative(final int value, final String field) {
        if (value < 0) {
            throw new IllegalArgumentException("argument " + field + " must be non-negative: " + value);
        }
        return value;
    }

    public static int requirePositive(final int value, final String field) {
        if (value < 1) {
            throw new IllegalArgumentException("argument " + field + " must be positive: " + value);
        }
        return value;
    }

    public static float requireRange(final float value, final String field, final float min, final float max) {
        if (Float.compare(value, min) >= 0 && Float.compare(value, max) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be in [" + min + "," + max +"]: " + value);
    }

    public static float requireNonNegative(final float value, final String field) {
        if (Float.compare(value, 0.0F) >= 0 && Float.compare(value, Float.MAX_VALUE) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be non-negative: " + value);
    }

    public static float requirePositive(final float value, final String field) {
        if (Float.compare(value, 0.0F) > 0 && Float.compare(value, Float.MAX_VALUE) <= 0) {
            return value;
        }
        throw new IllegalArgumentException("argument " + field + " must be positive: " + value);
    }

    private BoundChecker() {
    }
}
