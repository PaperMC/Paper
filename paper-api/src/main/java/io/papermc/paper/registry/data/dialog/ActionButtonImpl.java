package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record ActionButtonImpl(Component label, @Nullable Component tooltip, int width, @Nullable DialogAction action) implements ActionButton {
}
