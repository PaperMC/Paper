package io.papermc.paper.registry.data.dialog.input;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * A number range dialog input.
 * <p>Created via {@link DialogInput#numberRange(String, int, Component, String, float, float, Float, Float)}</p>
 */
public sealed interface NumberRangeDialogInput extends DialogInput permits NumberRangeDialogInputImpl {

    /**
     * The width of the input.
     *
     * @return the width
     */
    @Contract(pure = true)
    @Range(from = 1, to = 1024) int width();

    /**
     * The label for the input.
     *
     * @return the label component
     */
    @Contract(pure = true)
    Component label();

    /**
     * The format for the label, which can be a translation key or a format string.
     * <p>Example: {@code "%s: %s"} or {@code "options.generic_value"}</p>
     *
     * @return the label format
     */
    @Contract(pure = true)
    String labelFormat();

    /**
     * The start of the range.
     *
     * @return the start value
     */
    @Contract(pure = true)
    float start();

    /**
     * The end of the range.
     *
     * @return the end value
     */
    @Contract(pure = true)
    float end();

    /**
     * The initial value of the input, or null if not set.
     *
     * @return the initial value, or null
     */
    @Contract(pure = true)
    @Nullable Float initial();

    /**
     * The step size for the input, or null if not set.
     *
     * @return the step size, or null
     */
    @Contract(pure = true)
    @Positive @Nullable Float step();

    /**
     * A builder for creating instances of {@link NumberRangeDialogInput}.
     * <p>Created via {@link DialogInput#numberRange(String, Component, float, float)}</p>
     */
    sealed interface Builder permits NumberRangeDialogInputImpl.BuilderImpl {

        /**
         * Sets the width of the input.
         *
         * @param width the width
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(@Range(from = 1, to = 1024) int width);

        /**
         * Sets the format for the label.
         * <p>Example: {@code "%s: %s"} or {@code "options.generic_value"}</p>
         *
         * @param labelFormat the label format
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder labelFormat(String labelFormat);

        /**
         * Sets the initial value for the input.
         *
         * @param initial the initial value, or null if not set
         * @return this builder
         */
        @Contract(value = "_ -> this", pure = true)
        Builder initial(@Nullable Float initial);

        /**
         * Sets the step of the range.
         *
         * @param step the step size, or null if not set
         * @return this builder
         */
        @Contract(value = "_ -> this", pure = true)
        Builder step(@Positive @Nullable Float step);

        /**
         * Builds the instance with the configured values.
         *
         * @return a new instance
         */
        @Contract(pure = true, value = "-> new")
        NumberRangeDialogInput build();
    }
}
