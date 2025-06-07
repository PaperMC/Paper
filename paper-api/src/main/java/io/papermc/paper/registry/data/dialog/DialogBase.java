package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public sealed interface DialogBase permits DialogBaseImpl {

    static DialogBase create(
        final Component title,
        final @Nullable Component externalTitle,
        final boolean canCloseWithEscape,
        final boolean pause,
        final DialogAfterAction afterAction,
        final List<DialogBody> body,
        final List<DialogInput> inputs
    ) {
        return new DialogBaseImpl(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs);
    }

    Component title();

    @Nullable
    Component externalTitle();

    boolean canCloseWithEscape();

    boolean pause();

    DialogAfterAction afterAction();

    @Unmodifiable List<DialogBody> body();

    @Unmodifiable List<DialogInput> inputs();

    enum DialogAfterAction {
        CLOSE("close"), NONE("none"), WAIT_FOR_RESPONSE("wait_for_response");

        public static final Index<String, DialogAfterAction> NAMES = Index.create(DialogAfterAction.class, DialogAfterAction::name);

        private final String name;

        DialogAfterAction(final String name) {
            this.name = name;
        }
    }
}
