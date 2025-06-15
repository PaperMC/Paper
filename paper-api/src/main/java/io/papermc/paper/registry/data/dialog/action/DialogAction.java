package io.papermc.paper.registry.data.dialog.action;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.jspecify.annotations.Nullable;

public sealed interface DialogAction permits DialogAction.CommandTemplateAction, DialogAction.CustomAllAction, DialogAction.StaticAction {

    static CommandTemplateAction commandTemplate(final String template) {
        return new CommandTemplateActionImpl(template);
    }

    static StaticAction staticAction(final ClickEvent value) {
        return new StaticActionImpl(value);
    }

    static CustomAllAction customAll(final Key id, final @Nullable BinaryTagHolder additions) {
        return new CustomAllActionImpl(id, additions);
    }

    sealed interface CommandTemplateAction extends DialogAction permits CommandTemplateActionImpl {

        String template();
    }

    sealed interface StaticAction extends DialogAction permits StaticActionImpl {

        ClickEvent value();
    }

    sealed interface CustomAllAction extends DialogAction permits CustomAllActionImpl {

        Key id();

        @Nullable BinaryTagHolder additions();
    }
}
