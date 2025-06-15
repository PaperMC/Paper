package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public sealed interface DialogInputType permits BooleanDialogInputType, NumberRangeDialogInputType, SingleOptionDialogInputType, TextDialogInputType {

    static BooleanDialogInputType bool(final Component label, final boolean initial, final String onTrue, final String onFalse) {
        return new BooleanDialogInputTypeImpl(label, initial, onTrue, onFalse);
    }

    static NumberRangeDialogInputType numberRange(final int width, final Component label, final String labelFormat, final float start, final float end, final @Nullable Float initial, final @Nullable Float step) {
        return new NumberRangeDialogInputTypeImpl(width, label, labelFormat, start, end, initial, step);
    }

    static SingleOptionDialogInputType singleOption(final int width, final List<SingleOptionDialogInputType.OptionEntry> entries, final Component label, final boolean labelVisible) {
        return new SingleOptionDialogInputTypeImpl(width, entries, label, labelVisible);
    }

    static TextDialogInputType text(final int width, final Component label, final boolean labelVisible, final String initial, final int maxLength, final TextDialogInputType.@Nullable MultilineOptions multiline) {
        return new TextDialogInputTypeImpl(width, label, labelVisible, initial, maxLength, multiline);
    }
}
