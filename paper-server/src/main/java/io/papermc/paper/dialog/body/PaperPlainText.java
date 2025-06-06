package io.papermc.paper.dialog.body;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.BodyElement;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;

public class PaperPlainText implements BodyElementConversion, BodyElement.PlainText {
    Component text;
    int width = 200;

    @Override
    public DialogBody dialogBody() {
        return new PlainMessage(
            PaperAdventure.asVanilla(this.text),
            this.width
        );
    }

    @Override
    public Component component() {
        return this.text;
    }

    @Override
    public PlainText component(final Component component) {
        this.text = component;
        return this;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public PlainText width(final int width) {
        this.width = width;
        return this;
    }
}
