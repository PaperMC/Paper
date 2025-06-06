package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import java.net.URI;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperRunCommandAction implements ActionElementConversion, ActionElement.RunCommand {
    String command;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.RunCommand(this.command));
    }

    @Override
    public String command() {
        return this.command;
    }

    @Override
    public RunCommand command(final String command) {
        this.command = command;
        return this;
    }
}
