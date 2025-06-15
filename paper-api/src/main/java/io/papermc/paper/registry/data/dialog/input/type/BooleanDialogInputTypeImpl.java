package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;

record BooleanDialogInputTypeImpl(Component label, boolean initial, String onTrue, String onFalse) implements BooleanDialogInputType {
}
