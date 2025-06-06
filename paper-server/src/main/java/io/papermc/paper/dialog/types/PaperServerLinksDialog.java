package io.papermc.paper.dialog.types;

import io.papermc.paper.dialog.Dialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.MultiActionDialog;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Optional;

public class PaperServerLinksDialog extends PaperDialogBase<PaperServerLinksDialog> implements Dialog.ServerLinks<PaperServerLinksDialog>, DialogElementConversion<PaperServerLinksDialog> {
    @Override
    public net.minecraft.server.dialog.Dialog getMcDialog() {
        return new MultiActionDialog(
            this.commonDialogData(),
            List.of(),
            Optional.empty(),
            2
        );
    }
}
