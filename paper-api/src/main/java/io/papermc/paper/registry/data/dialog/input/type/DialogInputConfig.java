package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Represents a configuration of an input for dialog.
 */
public sealed interface DialogInputConfig permits BooleanDialogInputConfig, NumberRangeDialogInputConfig, SingleOptionDialogInputConfig, TextDialogInputConfig {

    /**
     * Creates a boolean dialog input config.
     *
     * @param label the label for the input
     * @param initial the initial value of the input
     * @param onTrue the input's value in a template when the value is true
     * @param onFalse the input's value in a template when the value is false
     * @return a new boolean dialog input config instance
     */
    @Contract(pure = true, value = "_, _, _, _ -> new")
    static BooleanDialogInputConfig bool(final Component label, final boolean initial, final String onTrue, final String onFalse) {
        return new BooleanDialogInputConfigImpl(label, initial, onTrue, onFalse);
    }

    /**
     * Creates a number range dialog input config.
     *
     * @param width the width of the input
     * @param label the label for the input
     * @param labelFormat the format for the label (a translation key or format string)
     * @param start the start of the range
     * @param end the end of the range
     * @param initial the initial value, or null if not set
     * @param step the step size, or null if not set
     * @return a new number range dialog input config instance
     */
    @Contract(pure = true, value = "_, _, _, _, _, _, _ -> new")
    static NumberRangeDialogInputConfig numberRange(final int width, final Component label, final String labelFormat, final float start, final float end, final @Nullable Float initial, final @Nullable Float step) {
        return new NumberRangeDialogInputConfigImpl(width, label, labelFormat, start, end, initial, step);
    }

    /**
     * Creates a single option dialog input config (radio input).
     *
     * @param width the width of the input
     * @param entries the list of options for the input
     * @param label the label for the input
     * @param labelVisible whether the label should be visible
     * @return a new single option dialog input config instance
     */
    @Contract(pure = true, value = "_, _, _, _ -> new")
    static SingleOptionDialogInputConfig singleOption(final int width, final List<SingleOptionDialogInputConfig.OptionEntry> entries, final Component label, final boolean labelVisible) {
        return new SingleOptionDialogInputConfigImpl(width, entries, label, labelVisible);
    }

    /**
     * Creates a text dialog input type.
     *
     * @param width the width of the input
     * @param label the label for the input
     * @param labelVisible whether the label should be visible
     * @param initial the initial text, or null if not set
     * @param maxLength the maximum length of the text input
     * @param multiline options for multiline text input, or null if not applicable
     * @return a new text dialog input type instance
     */
    @Contract(pure = true, value = "_, _, _, _, _, _ -> new")
    static TextDialogInputConfig text(final int width, final Component label, final boolean labelVisible, final String initial, final int maxLength, final TextDialogInputConfig.@Nullable MultilineOptions multiline) {
        return new TextDialogInputConfigImpl(width, label, labelVisible, initial, maxLength, multiline);
    }
}
