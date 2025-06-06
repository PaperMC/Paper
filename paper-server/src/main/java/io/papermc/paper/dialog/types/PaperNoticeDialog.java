package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.ButtonElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.actions.ButtonElementConversion;
import io.papermc.paper.dialog.actions.PaperButtonElement;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.NoticeDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PaperNoticeDialog extends PaperDialogBase<PaperNoticeDialog> implements Dialog.Notice<PaperNoticeDialog>, DialogElementConversion<PaperNoticeDialog> {
    ButtonElementConversion button = (PaperButtonElement) new PaperButtonElement()
        .label(Component.translatable("gui.ok"));

    @Override
    public ButtonElement button() {
        return this.button;
    }

    @Override
    public PaperNoticeDialog button(final ButtonElement button) {
        this.button = (ButtonElementConversion) button;
        return this;
    }

    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new NoticeDialog(
            this.commonDialogData(),
            this.button.button()
        );
    }
}
