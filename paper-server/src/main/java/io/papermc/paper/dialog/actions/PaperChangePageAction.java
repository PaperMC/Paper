package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperChangePageAction implements ActionElementConversion, ActionElement.ChangePage {
    int page;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.ChangePage(this.page));
    }

    @Override
    public int page() {
        return this.page;
    }

    @Override
    public ChangePage page(final int page) {
        this.page = page;
        return this;
    }
}
