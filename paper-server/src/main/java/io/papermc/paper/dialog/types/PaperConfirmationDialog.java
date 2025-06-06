package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.ButtonElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.actions.ButtonElementConversion;
import io.papermc.paper.dialog.actions.PaperButtonElement;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.NoticeDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Optional;

public class PaperConfirmationDialog extends PaperDialogBase<PaperConfirmationDialog> implements Dialog.Confirmation<PaperConfirmationDialog>, DialogElementConversion<PaperConfirmationDialog> {
    ButtonElementConversion yes = (PaperButtonElement)new PaperButtonElement()
        .label(Component.text("Yes"));
    ButtonElementConversion no = (PaperButtonElement) new PaperButtonElement()
        .label(Component.text("No"));

    @Override
    public ButtonElement yes() {
        return this.yes;
    }

    @Override
    public PaperConfirmationDialog yes(final ButtonElement button) {
        this.yes = (ButtonElementConversion) button;
        return this;
    }

    @Override
    public ButtonElement no() {
        return this.no;
    }

    @Override
    public PaperConfirmationDialog no(final ButtonElement button) {
        this.no = (ButtonElementConversion) button;
        return this;
    }

    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new ConfirmationDialog(
            this.commonDialogData(),
            this.yes.button(),
            this.no.button()
        );
    }
}
