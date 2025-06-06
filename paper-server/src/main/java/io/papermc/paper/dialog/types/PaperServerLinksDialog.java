package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.ButtonElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.actions.ButtonElementConversion;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.ServerLinksDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Optional;

public class PaperServerLinksDialog extends PaperDialogBase<PaperServerLinksDialog> implements Dialog.ServerLinks<PaperServerLinksDialog>, DialogElementConversion<PaperServerLinksDialog> {
    ButtonElement exitAction;
    int columns = 2;
    int buttonWidth = 150;

    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new ServerLinksDialog(
            this.commonDialogData(),
            Optional.ofNullable(this.exitAction).map(x -> ((ButtonElementConversion) x).button()),
            this.columns,
            this.buttonWidth
        );
    }

    @Override
    public ButtonElement exitAction() {
        return this.exitAction;
    }

    @Override
    public PaperServerLinksDialog exitAction(final ButtonElement buttonElement) {
        this.exitAction = buttonElement;
        return this;
    }

    @Override
    public int columns() {
        return this.columns;
    }

    @Override
    public PaperServerLinksDialog columns(final int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public int buttonWidth() {
        return this.buttonWidth;
    }

    @Override
    public PaperServerLinksDialog buttonWidth(final int width) {
        this.buttonWidth = width;
        return this;
    }
}
