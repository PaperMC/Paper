package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.Dialog;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.NoticeDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Optional;

public class PaperNoticeDialog extends PaperDialogBase<PaperNoticeDialog> implements Dialog.Notice {
    @Override
    public void openFor(final Player player) {
        var noticeDialog = new NoticeDialog(
            this.commonDialogData(),
            new ActionButton(
                new CommonButtonData(
                    net.minecraft.network.chat.Component.literal("Ok"),
                    150
                ),
                Optional.empty()
            )
        );
        ((CraftPlayer) player).getHandle().openDialog(Holder.direct(noticeDialog));
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    public Notice title(final Component component) {
        this.title = component;
        return this;
    }
}
