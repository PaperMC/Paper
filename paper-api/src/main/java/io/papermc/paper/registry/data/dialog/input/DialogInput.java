package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.DialogInstancesProvider;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * Represents an input for dialog.
 */
public sealed interface DialogInput permits BooleanDialogInput, NumberRangeDialogInput, SingleOptionDialogInput, TextDialogInput {

    /**
     * Creates a boolean dialog input.
     *
     * @param key the key identifier for the input
     * @param label the label for the input
     * @param initial the initial value of the input
     * @param onTrue the input's value in a template when the value is true
     * @param onFalse the input's value in a template when the value is false
     * @return a new boolean dialog input instance
     */
    @Contract(pure = true, value = "_, _, _, _, _ -> new")
    static BooleanDialogInput bool(final String key, final Component label, final boolean initial, final String onTrue, final String onFalse) {
        return bool(key, label)
            .initial(initial)
            .onTrue(onTrue)
            .onFalse(onFalse)
            .build();
    }

    /**
     * Creates a new builder for a boolean dialog input.
     *
     * @param key the key identifier for the input
     * @param label the label for the input
     * @return a new builder instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static BooleanDialogInput.Builder bool(final String key, final Component label) {
        return DialogInstancesProvider.instance().booleanBuilder(key, label);
    }

    /**
     * Creates a number range dialog input.
     *
     * @param key the key identifier for the input
     * @param width the width of the input
     * @param label the label for the input
     * @param labelFormat the format for the label (a translation key or format string)
     * @param start the start of the range
     * @param end the end of the range
     * @param initial the initial value, or null if not set
     * @param step the step size, or null if not set
     * @return a new number range dialog input instance
     */
    @Contract(pure = true, value = "_, _, _, _, _, _, _, _ -> new")
    static NumberRangeDialogInput numberRange(
        final String key,
        final @Range(from = 1, to = 1024) int width,
        final Component label,
        final String labelFormat,
        final float start,
        final float end,
        final @Nullable Float initial,
        final @Positive @Nullable Float step
    ) {
        return numberRange(key, label, start, end).width(width).labelFormat(labelFormat).initial(initial).step(step).build();
    }

    /**
     * Creates a new builder for a number range dialog input.
     *
     * @param key the key identifier for the input
     * @param label the label for the input
     * @param start the start of the range
     * @param end   the end of the range
     * @return a new builder instance
     */
    @Contract(pure = true, value = "_, _, _, _ -> new")
    static NumberRangeDialogInput.Builder numberRange(final String key, final Component label, final float start, final float end) {
        return DialogInstancesProvider.instance().numberRangeBuilder(key, label, start, end);
    }

    /**
     * Creates a single option dialog input (radio input).
     *
     * @param key the key identifier for the input
     * @param width the width of the input
     * @param entries the list of options for the input
     * @param label the label for the input
     * @param labelVisible whether the label should be visible
     * @return a new single option dialog input instance
     */
    @Contract(pure = true, value = "_, _, _, _, _ -> new")
    static SingleOptionDialogInput singleOption(
        final String key,
        final @Range(from = 1, to = 1024) int width,
        final List<SingleOptionDialogInput.OptionEntry> entries,
        final Component label,
        final boolean labelVisible
    ) {
        return singleOption(key, label, entries).width(width).labelVisible(labelVisible).build();
    }

    /**
     * Creates a new builder for a single option dialog input.
     *
     * @param key the key identifier for the input
     * @param label the label for the input
     * @param entries the list of options for the input
     * @return a new builder instance
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    static SingleOptionDialogInput.Builder singleOption(final String key, final Component label, final List<SingleOptionDialogInput.OptionEntry> entries) {
        return DialogInstancesProvider.instance().singleOptionBuilder(key, label, entries);
    }

    /**
     * Creates a text dialog input.
     *
     * @param key the key identifier for the input
     * @param width the width of the input
     * @param label the label for the input
     * @param labelVisible whether the label should be visible
     * @param initial the initial value of the input
     * @param maxLength the maximum length of the input
     * @param multilineOptions the multiline options, or null if not set
     * @return a new text dialog input instance
     */
    @Contract(pure = true, value = "_, _, _, _, _, _, _ -> new")
    static TextDialogInput text(
        final String key,
        final @Range(from = 1, to = 1024) int width,
        final Component label,
        final boolean labelVisible,
        final String initial,
        final @Positive int maxLength,
        final TextDialogInput.@Nullable MultilineOptions multilineOptions
    ) {
        return text(key, label).width(width).labelVisible(labelVisible).initial(initial).maxLength(maxLength).multiline(multilineOptions).build();
    }

    /**
     * Creates a new builder for a text dialog input.
     *
     * @param key the key identifier for the input
     * @param label the label for the input
     * @return a new builder instance
     */
    @Contract(value = "_, _ -> new", pure = true)
    static TextDialogInput.Builder text(final String key, final Component label) {
        return DialogInstancesProvider.instance().textBuilder(key, label);
    }

    /**
     * Gets the key for this input.
     * <p>Used in dialog actions to identify this dialog input's value</p>
     *
     * @return the key
     */
    @Contract(pure = true)
    String key();
}
