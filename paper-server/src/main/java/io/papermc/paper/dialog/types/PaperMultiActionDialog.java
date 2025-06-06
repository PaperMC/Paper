package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.ButtonElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.actions.ButtonElementConversion;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.MultiActionDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Optional;

public class PaperMultiActionDialog extends PaperDialogBase<PaperMultiActionDialog> implements Dialog.MultiAction<PaperMultiActionDialog>, DialogElementConversion<PaperMultiActionDialog> {
    List<ButtonElement> buttons = List.of();
    int columns = 2;
    ButtonElement exitButton;

    @Override
    public List<ButtonElement> buttons() {
        return this.buttons;
    }

    @Override
    public PaperMultiActionDialog buttons(final List<ButtonElement> buttonElements) {
        this.buttons = buttonElements;
        return this;
    }

    @Override
    public int columns() {
        return this.columns;
    }

    @Override
    public PaperMultiActionDialog columns(final int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public ButtonElement exitButton() {
        return this.exitButton;
    }

    @Override
    public PaperMultiActionDialog exitButton(final ButtonElement buttonElement) {
        this.exitButton = buttonElement;
        return this;
    }

    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new MultiActionDialog(
            this.commonDialogData(),
            this.buttons.stream().map(x -> ((ButtonElementConversion) x).button()).toList(),
            Optional.ofNullable(this.exitButton).map(x -> ((ButtonElementConversion) x).button()),
            this.columns
        );
    }
}
