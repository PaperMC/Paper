package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;

public sealed interface BooleanDialogInputType extends DialogInputType permits BooleanDialogInputTypeImpl {

    Component label();

    boolean initial();

    String onTrue();

    String onFalse();
}
