package io.papermc.paper.dialog.actions;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.ActionElement;
import io.papermc.paper.dialog.ButtonElement;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import java.util.Optional;

public class PaperButtonElement implements ButtonElementConversion {
    Component label = Component.empty();
    Component tooltip;
    int width = 150;
    ActionElement action;

    @Override
    public Component label() {
        return this.label;
    }

    @Override
    public ButtonElement label(final Component text) {
        this.label = text;
        return this;
    }

    @Override
    public Component tooltip() {
        return this.tooltip;
    }

    @Override
    public ButtonElement tooltip(final Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public ButtonElement width(final int width) {
        this.width = width;
        return this;
    }

    @Override
    public ActionElement action() {
        return this.action;
    }

    @Override
    public ButtonElement action(final ActionElement action) {
        this.action = action;
        return this;
    }

    @Override
    public ActionButton button() {
        return new ActionButton(
            new CommonButtonData(
                PaperAdventure.asVanilla(this.label),
                Optional.ofNullable(this.tooltip).map(PaperAdventure::asVanilla),
                this.width
            ),
            Optional.ofNullable(this.action).map(x -> ((ActionElementConversion) x).action())
        );
    }
}
