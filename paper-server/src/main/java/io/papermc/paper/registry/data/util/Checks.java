package io.papermc.paper.registry.data.util;

import java.util.OptionalInt;
import org.jspecify.annotations.Nullable;

/**
 * Utility class providing validation methods for configuration values and function arguments.
 * Contains methods to check for null values, value ranges, and minimum values.
 */
public final class Checks {

    /**
     * Ensures a configuration value is not null.
     *
     * @param <T> The type of the value
     * @param value The value to check
     * @param field The field name for error reporting
     * @return The value if not null
     * @throws IllegalStateException if the value is null
     */
    public static <T> T asConfigured(final @Nullable T value, final String field) {
        if (value == null) {
            throw new IllegalStateException(String.format("%s has not been configured", field));
        }
        return value;
    }

    /**
     * Ensures an optional integer configuration value is present.
     *
     * @param value The optional value to check
     * @param field The field name for error reporting
     * @return The integer value if present
     * @throws IllegalStateException if the value is not present
     */
    public static int asConfigured(final OptionalInt value, final String field) {
        if (value.isEmpty()) {
            throw new IllegalStateException(String.format("%s has not been configured", field));
        }
        return value.getAsInt();
    }

    /**
     * Ensures an argument value is not null.
     *
     * @param <T> The type of the value
     * @param value The value to check
     * @param field The field name for error reporting
     * @return The value if not null
     * @throws IllegalArgumentException if the value is null
     */
    public static <T> T asArgument(final @Nullable T value, final String field) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("argument %s cannot be null", field));
        }
        return value;
    }

    /**
     * Ensures an integer argument value is within a specified range.
     *
     * @param value The value to check
     * @param field The field name for error reporting
     * @param min The minimum allowed value (inclusive)
     * @param max The maximum allowed value (inclusive)
     * @return The value if within range
     * @throws IllegalArgumentException if the value is outside the allowed range
     */
    public static int asArgumentRange(final int value, final String field, final int min, final int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                String.format("argument %s must be [%d, %d]", field, min, max)
            );
        }
        return value;
    }

    /**
     * Ensures an integer argument value is at least a specified minimum.
     *
     * @param value The value to check
     * @param field The field name for error reporting
     * @param min The minimum allowed value (inclusive)
     * @return The value if at least the minimum
     * @throws IllegalArgumentException if the value is less than the minimum
     */
    public static int asArgumentMin(final int value, final String field, final int min) {
        if (value < min) {
            throw new IllegalArgumentException(
                String.format("argument %s must be [%d,+inf)", field, min)
            );
        }
        return value;
    }

    /**
     * Specialized version for long values to ensure a minimum value.
     *
     * @param value The value to check
     * @param field The field name for error reporting
     * @param min The minimum allowed value (inclusive)
     * @return The value if at least the minimum
     * @throws IllegalArgumentException if the value is less than the minimum
     */
    public static long asArgumentMin(final long value, final String field, final long min) {
        if (value < min) {
            throw new IllegalArgumentException(
                String.format("argument %s must be [%d,+inf)", field, min)
            );
        }
        return value;
    }

    /**
     * Specialized version for double values to ensure a minimum value.
     *
     * @param value The value to check
     * @param field The field name for error reporting
     * @param min The minimum allowed value (inclusive)
     * @return The value if at least the minimum
     * @throws IllegalArgumentException if the value is less than the minimum
     */
    public static double asArgumentMin(final double value, final String field, final double min) {
        if (value < min) {
            throw new IllegalArgumentException(
                String.format("argument %s must be [%f,+inf)", field, min)
            );
        }
        return value;
    }

    private Checks() {
        // Prevent instantiation of utility class
    }
}
