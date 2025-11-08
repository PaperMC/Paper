package io.papermc.paper.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.function.Function;

@NullMarked
@ApiStatus.Internal
public final class MorePreconditions {
    private MorePreconditions() {
    }

    public static void validateFloatNonNegative(final float value, final String valueName) {
        validateFloatRangeMinInclusive(value, 0.0F, Float.MAX_VALUE, _float -> String.format("%s value %.2f must be non-negative", valueName, value));
    }

    public static void validateFloatPositive(final float value, final String valueName) {
        validateFloatRangeMinExclusive(value, 0.0F, Float.MAX_VALUE, _float -> String.format("%s value %.2f must be positive", valueName, value));
    }

    public static void validateFloatRangeMinInclusive(final float value, final float min, final float max, final String valueName) {
        validateFloatRangeMinInclusive(value, min, max, _float -> String.format("%s value %.2f is out of range [%.2f, %.2f]", valueName, value, min, max));
    }

    private static void validateFloatRangeMinInclusive(final float value, final float min, final float max, Function<Float, String> errorMessage) {
        if (!(Float.compare(value, min) >= 0 && Float.compare(value, max) <= 0)) {
            throw new IllegalArgumentException(errorMessage.apply(value));
        }
    }

    public static void validateFloatRangeMinExclusive(final float value, final float min, final float max, final String valueName) {
        validateFloatRangeMinExclusive(value, min, max, _float -> String.format("%s value %.2f is out of range [%.2f, %.2f)", valueName, value, min, max));
    }

    private static void validateFloatRangeMinExclusive(final float value, final float min, final float max, Function<Float, String> errorMessage) {
        if (!(Float.compare(value, min) > 0 && Float.compare(value, max) <= 0)) {
            throw new IllegalArgumentException(errorMessage.apply(value));
        }
    }

    public static void validateDoubleRange(final double value, final double min, final double max, final String valueName) {
        if (Double.compare(value, min) < 0 || Double.compare(value, max) > 0) {
            throw new IllegalArgumentException(
                String.format("%s value %.2f is out of range [%.2f, %.2f]", valueName, value, min, max)
            );
        }
    }
}
