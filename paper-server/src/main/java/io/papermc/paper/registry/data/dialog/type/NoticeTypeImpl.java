package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;

import static net.kyori.adventure.text.Component.translatable;

public record NoticeTypeImpl(ActionButton action) implements NoticeType {

    public static final ActionButton DEFAULT_ACTION = ActionButton.builder(translatable("gui.ok")).build();

    public NoticeTypeImpl() {
        this(DEFAULT_ACTION);
    }
}
