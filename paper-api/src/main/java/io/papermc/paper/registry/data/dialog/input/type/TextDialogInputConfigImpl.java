package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public record TextDialogInputConfigImpl(int width, Component label, boolean labelVisible, String initial, int maxLength, TextDialogInputConfig.@Nullable MultilineOptions multiline) implements TextDialogInputConfig {
}
