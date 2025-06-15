package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.input.type.DialogInputType;

public sealed interface DialogInput permits DialogInputImpl {

    static DialogInput create(final String key, final DialogInputType inputType) {
        return new DialogInputImpl(key, inputType);
    }

    String key();

    DialogInputType inputType();
}
