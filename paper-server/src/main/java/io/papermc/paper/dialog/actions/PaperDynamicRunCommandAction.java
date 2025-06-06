package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.CommandTemplate;
import net.minecraft.server.dialog.action.ParsedTemplate;
import net.minecraft.server.dialog.action.StaticAction;
import java.text.ParseException;

public class PaperDynamicRunCommandAction implements ActionElementConversion, ActionElement.DynamicRunCommand {
    String command;

    @Override
    public Action action() {
        return new CommandTemplate(ParsedTemplate.parse(this.command).getOrThrow(RuntimeException::new));
    }

    @Override
    public String command() {
        return this.command;
    }

    @Override
    public DynamicRunCommand command(final String command) {
        this.command = command;
        return this;
    }
}
