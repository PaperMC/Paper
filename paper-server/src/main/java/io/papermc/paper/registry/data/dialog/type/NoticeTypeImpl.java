package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;

import static net.kyori.adventure.text.Component.translatable;

public record NoticeTypeImpl(ActionButton action) implements NoticeType {

    public static final ActionButton DEFAULT_ACTION = ActionButton.builder(translatable("gui.ok")).width(CommonButtonData.DEFAULT_WIDTH).build();

    public NoticeTypeImpl() {
        this(DEFAULT_ACTION);
    }
}
