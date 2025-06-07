package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public record TextDialogInputTypeImpl(int width, Component label, boolean labelVisible, String initial, int maxLength, TextDialogInputType.@Nullable MultilineOptions multiline) implements TextDialogInputType {
}
