package io.papermc.paper.registry.data.dialog.action;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.event.ClickEvent;

public record StaticActionImpl(ClickEvent value) implements DialogAction.StaticAction {

    public StaticActionImpl {
        Preconditions.checkArgument(value.action().readable() || value.action() == ClickEvent.Action.SHOW_DIALOG, "action must be readable or show_dialog");
    }
}
