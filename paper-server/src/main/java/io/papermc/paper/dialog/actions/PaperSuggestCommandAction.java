package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperSuggestCommandAction implements ActionElementConversion, ActionElement.SuggestCommand {
    String command;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.SuggestCommand(this.command));
    }

    @Override
    public String command() {
        return this.command;
    }

    @Override
    public SuggestCommand command(final String command) {
        this.command = command;
        return this;
    }
}
