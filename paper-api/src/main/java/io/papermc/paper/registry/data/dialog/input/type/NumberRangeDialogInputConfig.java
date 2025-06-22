package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * A configuration for a number range dialog input.
 */
public sealed interface NumberRangeDialogInputConfig extends DialogInputConfig permits NumberRangeDialogInputConfigImpl {

    /**
     * Creates a new builder for a number range dialog input configuration.
     *
     * @param label the label for the input
     * @param start the start of the range
     * @param end   the end of the range
     * @return a new builder instance
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static Builder builder(final Component label, final float start, final float end) {
        return new NumberRangeDialogInputConfigImpl.BuilderImpl(label, start, end);
    }

    /**
     * The width of the input.
     *
     * @return the width
     */
    @Contract(pure = true)
    int width();

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
    @Nullable Float step();

    /**
     * A builder for creating instances of {@link NumberRangeDialogInputConfig}.
     */
    sealed interface Builder permits NumberRangeDialogInputConfigImpl.BuilderImpl {

        /**
         * Sets the width of the input.
         *
         * @param width the width
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(int width);

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
        Builder step(@Nullable Float step);

        /**
         * Builds the instance with the configured values.
         *
         * @return a new instance
         */
        @Contract(pure = true, value = "-> new")
        NumberRangeDialogInputConfig build();
    }
}
