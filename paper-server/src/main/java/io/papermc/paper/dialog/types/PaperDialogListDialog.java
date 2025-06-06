package io.papermc.paper.dialog.types;

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

public class PaperDialogListDialog extends PaperDialogBase<PaperDialogListDialog> implements Dialog.DialogList<PaperDialogListDialog> {
    @Override
    public void openFor(final Player player) {
        var dialog = new DialogListDialog(
            this.commonDialogData(),
            HolderSet.empty(),
            Optional.empty(),
            2,
            150
        );
        ((CraftPlayer) player).getHandle().openDialog(Holder.direct(dialog));
    }
}
