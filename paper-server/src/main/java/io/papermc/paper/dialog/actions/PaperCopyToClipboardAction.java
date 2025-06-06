package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperCopyToClipboardAction implements ActionElementConversion, ActionElement.CopyToClipboard {
    String value;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.CopyToClipboard(this.value));
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public CopyToClipboard value(final String value) {
        this.value = value;
        return this;
    }
}
