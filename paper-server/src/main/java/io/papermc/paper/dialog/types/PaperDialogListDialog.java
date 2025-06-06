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

public class PaperDialogListDialog extends PaperDialogBase<PaperDialogListDialog> implements Dialog.DialogList<PaperDialogListDialog>, DialogElementConversion<PaperDialogListDialog> {
    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new DialogListDialog(
            this.commonDialogData(),
            HolderSet.empty(),
            Optional.empty(),
            2,
            150
        );
    }
}
