package io.papermc.paper.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class MorePreconditions {
    private MorePreconditions() {
    }

    public static void validateFloatRange(final float value, final float min, final float max, final String valueName) {
        if (Float.compare(value, min) < 0 || Float.compare(value, max) > 0) {
            throw new IllegalArgumentException(
                String.format("%s value %.2f is out of range [%.2f, %.2f]", valueName, value, min, max)
            );
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
