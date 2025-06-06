package io.papermc.paper.dialog.actions;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.ActionElement;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.CustomAll;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperDynamicCustomAction implements ActionElementConversion, ActionElement.DynamicCustom {
    Key id;

    @Override
    public Action action() {
        return new CustomAll(
            PaperAdventure.asVanilla(this.id),
            Optional.empty()
        );
    }

    @Override
    public Key id() {
        return this.id;
    }

    @Override
    public DynamicCustom id(final Key key) {
        this.id = key;
        return this;
    }
}
