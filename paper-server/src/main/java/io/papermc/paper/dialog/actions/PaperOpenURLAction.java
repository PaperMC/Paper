package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;
import java.net.URI;

public class PaperOpenURLAction implements ActionElementConversion, ActionElement.OpenURL {
    URI url;

    @Override
    public URI url() {
        return this.url;
    }

    @Override
    public OpenURL url(final URI url) {
        this.url = url;
        return this;
    }

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.OpenUrl(this.url));
    }
}
