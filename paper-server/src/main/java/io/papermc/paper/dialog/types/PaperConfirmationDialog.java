package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.Dialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.NoticeDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Optional;

public class PaperConfirmationDialog extends PaperDialogBase<PaperConfirmationDialog> implements Dialog.Confirmation<PaperConfirmationDialog> {
    @Override
    public void openFor(final Player player) {
        var dialog = new ConfirmationDialog(
            this.commonDialogData(),
            new ActionButton(
                new CommonButtonData(
                    net.minecraft.network.chat.Component.literal("Yes"),
                    150
                ),
                Optional.empty()
            ),
            new ActionButton(
                new CommonButtonData(
                    net.minecraft.network.chat.Component.literal("No"),
                    150
                ),
                Optional.empty()
            )
        );
        ((CraftPlayer) player).getHandle().openDialog(Holder.direct(dialog));
    }
}
