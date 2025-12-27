package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.DialogInstancesProvider;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * A text dialog input.
 * <p>Created via {@link DialogInput#text(String, int, Component, boolean, String, int, MultilineOptions)}</p>
 */
@ApiStatus.NonExtendable
public non-sealed interface TextDialogInput extends DialogInput {

    /**
     * The width of the input.
     *
     * @return the width of the input
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
     * Whether the label should be visible.
     *
     * @return true if the label is visible, false otherwise
     */
    @Contract(pure = true)
    boolean labelVisible();

    /**
     * The initial value of the input.
     *
     * @return the initial text
     */
    @Contract(pure = true)
    String initial();

    /**
     * The format for the label (a translation key or format string).
     *
     * @return the label format
     */
    @Contract(pure = true)
    @Positive int maxLength();

    /**
     * The multiline options for the input, or null if not set.
     *
     * @return the multiline options
     */
    @Contract(pure = true)
    @Nullable MultilineOptions multiline();

    /**
     * Represents the multiline options for a text dialog input.
     */
    @ApiStatus.NonExtendable
    interface MultilineOptions {

        /**
         * Creates a new multiline options instance.
         *
         * @param maxLines the maximum number of lines, or null if not set
         * @param height the height of the input, or null if not set
         * @return a new MultilineOptions instance
         */
        static MultilineOptions create(
            final @Positive @Nullable Integer maxLines,
            final @Range(from = 1, to = 512) @Nullable Integer height
        ) {
            return DialogInstancesProvider.instance().multilineOptions(maxLines, height);
        }

        /**
         * Gets the maximum number of lines.
         *
         * @return the maximum number of lines, or null if not set
         */
        @Contract(pure = true)
        @Positive @Nullable Integer maxLines();

        /**
         * Gets the height of the input.
         *
         * @return the height of the input, or null if not set
         */
        @Contract(pure = true)
        @Range(from = 1, to = 512) @Nullable Integer height();
    }

    /**
     * A builder for a text dialog input.
     * <p>Created via {@link DialogInput#text(String, Component)}</p>
     */
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the width of the input.
         *
         * @param width the width of the input
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(final @Range(from = 1, to = 1024) int width);

        /**
         * Sets whether the label should be visible.
         *
         * @param labelVisible true if the label should be visible, false otherwise
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder labelVisible(final boolean labelVisible);

        /**
         * Sets the initial value of the input.
         *
         * @param initial the initial value of the input
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder initial(final String initial);

        /**
         * Sets the maximum length of the input.
         *
         * @param maxLength the maximum length of the input
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maxLength(final @Positive int maxLength);

        /**
         * Sets the multiline options for the input.
         *
         * @param multiline the multiline options, or null if not set
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder multiline(final @Nullable MultilineOptions multiline);

        /**
         * Builds the text dialog input.
         *
         * @return the text dialog input
         */
        @Contract(value = "-> new", pure = true)
        TextDialogInput build();
    }
}
