package io.papermc.paper.dialog.actions;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.ActionElement;
import net.kyori.adventure.key.Key;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;
import java.util.Optional;

public class PaperCustomAction implements ActionElementConversion, ActionElement.Custom {
    Key id;
    String payload;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.Custom(
            PaperAdventure.asVanilla(this.id),
            Optional.ofNullable(this.payload).map(StringTag::new)
        ));
    }

    @Override
    public Key id() {
        return this.id;
    }

    @Override
    public Custom id(final Key key) {
        this.id = key;
        return this;
    }

    @Override
    public String payload() {
        return this.payload;
    }

    @Override
    public Custom payload(final String payload) {
        this.payload = payload;
        return this;
    }
}
