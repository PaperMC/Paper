package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.ActionElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.types.DialogElementConversion;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

public class PaperShowDialogAction implements ActionElementConversion, ActionElement.ShowDialog {
    Dialog<?> dialog;

    @Override
    public Action action() {
        return new StaticAction(new ClickEvent.ShowDialog(
            Holder.direct(
                ((DialogElementConversion<?>) this.dialog).getMcDialog())
            )
        );
    }

    @Override
    public Dialog<?> dialog() {
        return this.dialog;
    }

    @Override
    public ShowDialog dialog(final Dialog<?> dialog) {
        this.dialog = dialog;
        return this;
    }
}
