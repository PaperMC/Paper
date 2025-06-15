package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

record DialogBaseImpl(
    Component title,
    @Nullable Component externalTitle,
    boolean canCloseWithEscape,
    boolean pause,
    DialogAfterAction afterAction,
    @Unmodifiable List<DialogBody> body,
    @Unmodifiable List<DialogInput> inputs
) implements DialogBase {

    DialogBaseImpl {
        body = List.copyOf(body);
        inputs = List.copyOf(inputs);
    }
}
