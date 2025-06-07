package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record NumberRangeDialogInputTypeImpl(int width, Component label, String labelFormat, float start, float end, @Nullable Float initial, @Nullable Float step) implements NumberRangeDialogInputType {
}
