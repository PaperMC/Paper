package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.ButtonElement;
import io.papermc.paper.dialog.Dialog;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.dialog.DialogListDialog;
import net.minecraft.server.dialog.MultiActionDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PaperDialogListDialog extends PaperDialogBase<PaperDialogListDialog> implements Dialog.DialogList<PaperDialogListDialog>, DialogElementConversion<PaperDialogListDialog> {
    List<Dialog<?>> dialogs;
    ButtonElement exitAction;
    int columns = 2;
    int buttonWidth = 150;

    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new DialogListDialog(
            this.commonDialogData(),
            HolderSet.direct(
                this.dialogs.stream()
                    .map(x -> ((DialogElementConversion<?>) x).getMcDialog())
                    .map(Holder::direct)
                    .toList()
            ),
            Optional.empty(),
            2,
            150
        );
    }

    @Override
    public List<Dialog<?>> dialogs() {
        return this.dialogs;
    }

    @Override
    public PaperDialogListDialog dialogs(final List<Dialog<?>> dialogs) {
        this.dialogs = dialogs;
        return this;
    }

    @Override
    public ButtonElement exitAction() {
        return this.exitAction;
    }

    @Override
    public PaperDialogListDialog exitAction(final ButtonElement buttonElement) {
        this.exitAction = buttonElement;
        return this;
    }

    @Override
    public int columns() {
        return this.columns;
    }

    @Override
    public PaperDialogListDialog columns(final int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public int buttonWidth() {
        return this.buttonWidth;
    }

    @Override
    public PaperDialogListDialog buttonWidth(final int width) {
        this.buttonWidth = width;
        return this;
    }
}
