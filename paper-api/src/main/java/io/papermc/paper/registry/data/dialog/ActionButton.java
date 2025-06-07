package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public sealed interface ActionButton permits ActionButtonImpl {

    static ActionButton create(final Component label, final @Nullable Component tooltip, final int width, final @Nullable DialogAction action) {
        return new ActionButtonImpl(label, tooltip, width, action);
    }

    Component label();

    @Nullable Component tooltip();

    int width();

    @Nullable DialogAction action();
}
